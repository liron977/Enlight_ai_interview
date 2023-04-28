package com.example.demo;


import jakarta.annotation.PostConstruct;
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
        String url = "jdbc:postgresql://localhost:5432/mydb";
        String username = "postgres";
        String password = "mysecretpassword";
        Connection conn = DriverManager.getConnection(url, username, password);

        String sql = "INSERT INTO queriesTable (query) VALUES (?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, query);
        statement.executeUpdate();
        conn.close();

    }
    public int getAmountOfQueriesFromDB() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/mydb";
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
    @GetMapping("/")
    public String init(Model model) throws SQLException {
        int amountOfQueries = getAmountOfQueriesFromDB();
        model.addAttribute("queryCount", amountOfQueries);
        return "index";
    }
}