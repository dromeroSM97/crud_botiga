/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package src;

import java.util.Scanner;
import java.sql.*;
import util.Connexio;

/**
 *
 * @author vboxuser
 */
public class App {

    public static void main(String[] args) {

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
