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
import model.Producte;
import util.Connexio;

public class ProducteDAO {

    public void inserir(Producte p) throws SQLException {
        String sql = "INSERT INTO Productes (nom, preu, estoc) VALUES (?, ?, ?)";
        try (Connection conn = Connexio.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setDouble(2, p.getPreu());
            ps.setInt(3, p.getEstoc());
            ps.executeUpdate();
        }
    }

    public List<Producte> llistar() throws SQLException {
        List<Producte> llista = new ArrayList<>();
        String sql = "SELECT * FROM Productes";
        try (Connection conn = Connexio.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producte p = new Producte(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("preu"),
                        rs.getInt("estoc")
                );
                llista.add(p);
            }
        }
        return llista;
    }

    public Producte obtenirPerId(int id) throws SQLException {
        String sql = "SELECT * FROM Productes WHERE id = ?";
        try (Connection conn = Connexio.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producte(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getDouble("preu"),
                            rs.getInt("estoc")
                    );
                }
            }
        }
        return null;
    }

    public void actualitzar(Producte p) throws SQLException {
        String sql = "UPDATE Productes SET nom = ?, preu = ?, estoc = ? WHERE id = ?";
        try (Connection conn = Connexio.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setDouble(2, p.getPreu());
            ps.setInt(3, p.getEstoc());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Productes WHERE id = ?";
        try (Connection conn = Connexio.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
