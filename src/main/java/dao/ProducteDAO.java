/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author vboxuser
 */
import java.sql.*;
import util.Connexio;
import model.Producte;
import java.util.ArrayList;
import java.util.List;

public class ProducteDAO {

    public void llistar() throws SQLException {

        String sql = "SELECT * FROM productes";
        try (Connection conn = Connexio.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("%d - %s (%.2f €) Estoc: %d%n",
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("preu"),
                        rs.getInt("estoc"));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }
}
