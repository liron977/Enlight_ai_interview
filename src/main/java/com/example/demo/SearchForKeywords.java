package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchForKeywords {

    @GetMapping("/searchKeywords")
    public String searchKeywords(@RequestParam String query, Model model) {
        List<String> filesName = new ArrayList<>();
        try {
            filesName=getQueriesFromDB(query);
            model.addAttribute("filesName", filesName);
            return "index";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private List<String> getQueriesFromDB(String query) throws SQLException {
        List<String> filesName = new ArrayList<>();
        String fileName,url,username,password,sql;
        ResultSet result;
        Connection conn;

        url = "jdbc:postgresql://localhost:5432/postgres";
        username = "postgres";
        password = "mysecretpassword";
        conn = DriverManager.getConnection(url, username, password);

        sql = "SELECT file_name FROM queriesTable WHERE query=?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, query);
         result = statement.executeQuery();

        while (result.next()) {
            fileName = result.getString("file_name");
            filesName.add(fileName);
        }
        conn.close();

        return filesName;

    }

}
