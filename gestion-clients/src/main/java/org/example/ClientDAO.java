package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    private static final String URL =
            "jdbc:mysql://localhost:3306/gestion_clients?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public List<String> getAllClients() {
        List<String> clients = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(
                    "SELECT nom, prenom FROM client"
            );

            while (rs.next()) {
                clients.add(rs.getString("prenom") + " " + rs.getString("nom"));
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            clients.clear();
            clients.add("ERREUR SQL : " + e.getMessage());
        }

        return clients;
    }
}
