package com.example.demo;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Controller
public class SearchController {

    @GetMapping("/search")
    public String search(@RequestParam String query, Model model) throws SQLException {
        // Perform search logic here
        model.addAttribute("query", query);
        insertQueryIntoDB(query);

        return "results";
    }
    private void insertQueryIntoDB(String query) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "mysecretpassword";
        Connection conn = DriverManager.getConnection(url, username, password);

        String sql = "INSERT INTO queriesTable (query) VALUES (?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, query);
        int rows = statement.executeUpdate();
        System.out.println(rows + " row(s) inserted");
        //conn.close();

    }
}