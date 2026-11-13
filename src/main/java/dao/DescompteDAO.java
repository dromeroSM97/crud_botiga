/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Descompte;
import util.Connexio;

public class DescompteDAO {

    public void inserir(Descompte d) throws SQLException {
        String sql = "INSERT INTO Descomptes (producte_id, tipus, valor) VALUES (?, ?, ?)";
        try (Connection conn = Connexio.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getProducteId());
            ps.setString(2, d.getTipus());
            ps.setDouble(3, d.getValor());
            ps.executeUpdate();
        }
    }

    public List<Descompte> llistar() throws SQLException {
        List<Descompte> llista = new ArrayList<>();
        String sql = "SELECT * FROM Descomptes";
        try (Connection conn = Connexio.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Descompte d = new Descompte(
                        rs.getInt("id"),
                        rs.getInt("producte_id"),
                        rs.getString("tipus"),
                        rs.getDouble("valor")
                );
                llista.add(d);
            }
        }
        return llista;
    }

    public void actualitzar(Descompte d) throws SQLException {
        String sql = "UPDATE Descomptes SET producte_id = ?, tipus = ?, valor = ? WHERE id = ?";
        try (Connection conn = Connexio.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getProducteId());
            ps.setString(2, d.getTipus());
            ps.setDouble(3, d.getValor());
            ps.setInt(4, d.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Descomptes WHERE id = ?";
        try (Connection conn = Connexio.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}