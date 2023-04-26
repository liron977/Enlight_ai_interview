package com.example.demo;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.*;

@Controller
public class SearchController {

    @GetMapping("/search")
    public String search(@RequestParam String query, Model model)  {
        // Perform search logic here
       // model.addAttribute("query", query);
        try {
            insertQueryIntoDB(query);
            try {
                int  amountOfQueries = getAmountOfQueriesFromDB();
                model.addAttribute("queryCount", amountOfQueries);
                return "index";
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        conn.close();

    }
    public int getAmountOfQueriesFromDB() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "mysecretpassword";
        Connection conn = DriverManager.getConnection(url, username, password);

        String sql = "SELECT COUNT(*) FROM queriesTable";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        conn.close();

        return count;
    }
}