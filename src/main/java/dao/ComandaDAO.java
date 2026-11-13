/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import model.Comanda;
import model.LiniaComanda;
import util.Connexio;

public class ComandaDAO {

    public void crearComanda(Comanda comanda) throws SQLException {
        String sqlComprovaEstoc = "SELECT estoc, preu FROM Productes WHERE id = ?";
        String sqlActualitzaEstoc = "UPDATE Productes SET estoc = estoc - ? WHERE id = ?";
        String sqlInserirComanda = "INSERT INTO Comandes (client_id, total) VALUES (?, 0)";
        String sqlInserirLinia = "INSERT INTO LiniesComanda (comanda_id, producte_id, quantitat, preuUnitari) VALUES (?, ?, ?, ?)";
        String sqlActualitzarTotalComanda = "UPDATE Comandes SET total = ? WHERE id = ?";
        String sqlBuscarDescompte = "SELECT tipus, valor FROM Descomptes WHERE producte_id = ?";
        String sqlActualitzarPreuLinia = "UPDATE LiniesComanda SET preuUnitari = ? WHERE comanda_id = ? AND producte_id = ?";

        Connection conn = null;
        try {
            conn = Connexio.getConnection();
            conn.setAutoCommit(false); 

            int comandaId;
            try (PreparedStatement psComanda = conn.prepareStatement(sqlInserirComanda, Statement.RETURN_GENERATED_KEYS)) {
                psComanda.setInt(1, comanda.getClientId());
                psComanda.executeUpdate();
                try (ResultSet rsKeys = psComanda.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        comandaId = rsKeys.getInt(1);
                        comanda.setId(comandaId);
                    } else {
                        throw new SQLException("No s'ha pogut obtenir l'ID de la comanda.");
                    }
                }
            }

            double totalSenseDescompte = 0.0;

            for (LiniaComanda linia : comanda.getLinies()) {
                int estocActual;
                double preuProducte;

                try (PreparedStatement psEstoc = conn.prepareStatement(sqlComprovaEstoc)) {
                    psEstoc.setInt(1, linia.getProducteId());
                    try (ResultSet rs = psEstoc.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("El producte amb ID " + linia.getProducteId() + " no existeix.");
                        }
                        estocActual = rs.getInt("estoc");
                        preuProducte = rs.getDouble("preu");
                    }
                }

                if (estocActual < linia.getQuantitat()) {
                    throw new SQLException("Estoc insuficient per al producte ID " + linia.getProducteId()
                            + " (Disponible: " + estocActual + ", Demanat: " + linia.getQuantitat() + ")");
                }

                try (PreparedStatement psUpdEstoc = conn.prepareStatement(sqlActualitzaEstoc)) {
                    psUpdEstoc.setInt(1, linia.getQuantitat());
                    psUpdEstoc.setInt(2, linia.getProducteId());
                    psUpdEstoc.executeUpdate();
                }

                linia.setPreuUnitari(preuProducte);
                try (PreparedStatement psLinia = conn.prepareStatement(sqlInserirLinia)) {
                    psLinia.setInt(1, comandaId);
                    psLinia.setInt(2, linia.getProducteId());
                    psLinia.setInt(3, linia.getQuantitat());
                    psLinia.setDouble(4, preuProducte);
                    psLinia.executeUpdate();
                }

                totalSenseDescompte += preuProducte * linia.getQuantitat();
            }

            try (PreparedStatement psTotal = conn.prepareStatement(sqlActualitzarTotalComanda)) {
                psTotal.setDouble(1, totalSenseDescompte);
                psTotal.setInt(2, comandaId);
                psTotal.executeUpdate();
            }
            comanda.setTotal(totalSenseDescompte);

            Savepoint spDespresLinies = conn.setSavepoint("DespresLinies");

            try {
                double totalAmbDescompte = 0.0;

                for (LiniaComanda linia : comanda.getLinies()) {
                    double preuFinalUnitari = linia.getPreuUnitari();

                    try (PreparedStatement psDesc = conn.prepareStatement(sqlBuscarDescompte)) {
                        psDesc.setInt(1, linia.getProducteId());
                        try (ResultSet rsDesc = psDesc.executeQuery()) {
                            if (rsDesc.next()) {
                                String tipus = rsDesc.getString("tipus");
                                double valor = rsDesc.getDouble("valor");

                                if ("%".equals(tipus)) {
                                    preuFinalUnitari = preuFinalUnitari - (preuFinalUnitari * (valor / 100.0));
                                } else if ("€".equals(tipus)) {
                                    preuFinalUnitari = preuFinalUnitari - valor;
                                }

                                if (preuFinalUnitari < 0) {
                                    throw new SQLException("Error en aplicar descompte: el preu resultant és negatiu.");
                                }

                                try (PreparedStatement psUpdLinia = conn.prepareStatement(sqlActualitzarPreuLinia)) {
                                    psUpdLinia.setDouble(1, preuFinalUnitari);
                                    psUpdLinia.setInt(2, comandaId);
                                    psUpdLinia.setInt(3, linia.getProducteId());
                                    psUpdLinia.executeUpdate();
                                }
                            }
                        }
                    }
                    totalAmbDescompte += preuFinalUnitari * linia.getQuantitat();
                }

                try (PreparedStatement psTotalFinal = conn.prepareStatement(sqlActualitzarTotalComanda)) {
                    psTotalFinal.setDouble(1, totalAmbDescompte);
                    psTotalFinal.setInt(2, comandaId);
                    psTotalFinal.executeUpdate();
                }
                comanda.setTotal(totalAmbDescompte);
                System.out.println("Descomptes aplicats correctament.");

            } catch (SQLException eDesc) {
                System.out.println("Avís: No s'han pogut aplicar els descomptes (" + eDesc.getMessage() + "). Es desa el tiquet sense descompte.");
                conn.rollback(spDespresLinies);
            }

            conn.commit();
            System.out.printf("Comanda #%d creada correctament. Total final: %.2f €%n", comandaId, comanda.getTotal());

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transacció cancel·lada (ROLLBACK): " + e.getMessage());
                } catch (SQLException exRollback) {
                    exRollback.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
