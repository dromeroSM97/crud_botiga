/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.Connexio;

public class ConsultesDAO {

    public void llistarComandesClient(int clientId) throws SQLException {
        String sql = "SELECT cl.nom AS nom_client, c.id AS comanda_id, c.data, c.total AS total_comanda, "
                   + "p.nom AS nom_producte, p.preu AS preu_original, lc.quantitat, lc.preuUnitari AS preu_aplicat, "
                   + "d.tipus AS desc_tipus, d.valor AS desc_valor "
                   + "FROM Clients cl "
                   + "INNER JOIN Comandes c ON cl.id = c.client_id "
                   + "INNER JOIN LiniesComanda lc ON c.id = lc.comanda_id "
                   + "INNER JOIN Productes p ON lc.producte_id = p.id "
                   + "LEFT JOIN Descomptes d ON p.id = d.producte_id "
                   + "WHERE cl.id = ? "
                   + "ORDER BY c.id DESC, lc.id ASC";

        try (Connection conn = Connexio.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clientId);

            try (ResultSet rs = ps.executeQuery()) {
                int comandaActual = -1;
                boolean teComandes = false;

                while (rs.next()) {
                    teComandes = true;
                    int comandaId = rs.getInt("comanda_id");

                    if (comandaId != comandaActual) {
                        if (comandaActual != -1) {
                            System.out.println("--------------------------------------------------");
                        }
                        comandaActual = comandaId;
                        System.out.printf("%n=== COMANDA #%d | Client: %s | Data: %s ===%n",
                                comandaId,
                                rs.getString("nom_client"),
                                rs.getTimestamp("data"));
                        System.out.printf("TOTAL COMANDA: %.2f €%n", rs.getDouble("total_comanda"));
                        System.out.println("Detall de productes:");
                    }

                    String nomProd = rs.getString("nom_producte");
                    int quantitat = rs.getInt("quantitat");
                    double preuOrig = rs.getDouble("preu_original");
                    double preuAplicat = rs.getDouble("preu_aplicat");
                    String descTipus = rs.getString("desc_tipus");
                    double descValor = rs.getDouble("desc_valor");

                    String infoDescompte = "Sense descompte";
                    if (descTipus != null && preuAplicat < preuOrig) {
                        infoDescompte = String.format("Descompte aplicat: -%.2f%s", descValor, descTipus);
                    }

                    System.out.printf("  - %s | Quantitat: %d | Preu base: %.2f € | Preu unitari final: %.2f € (%s) | Subtotal: %.2f €%n",
                            nomProd,
                            quantitat,
                            preuOrig,
                            preuAplicat,
                            infoDescompte,
                            (quantitat * preuAplicat));
                }

                if (!teComandes) {
                    System.out.println("Aquest client no té cap comanda registrada (o l'ID no existeix).");
                } else {
                    System.out.println("--------------------------------------------------");
                }
            }
        }
    }
}