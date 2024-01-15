package com.kernel360.kernelsquare.global.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class TechStackCrawling {
        public static void main(String[] args) throws IOException, SQLException, InterruptedException {
            String url = System.getenv("LOCAL_DB_URL") + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
            String username = System.getenv("LOCAL_DB_HOST");
            String password = System.getenv("LOCAL_DB_PASSWORD");

            Connection conn = DriverManager.getConnection(url, username, password);

            String selectQuery = "SELECT * FROM tech_stack";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            Set<String> teckStackSet = new HashSet<>();

            while (rs.next()) {
                String skill = rs.getString("skill");
                teckStackSet.add(skill);
            }

            rs.close();
            stmt.close();

            Document doc = Jsoup.connect("https://stackoverflow.com/tags?page=1&tab=popular").get();

            Integer totalPages = Integer.parseInt(doc.getElementsByClass("s-pagination--item js-pagination-item")
                .select("[rel='']").last().text());

            String insertQuery = "INSERT INTO tech_stack (skill, created_date, modified_date) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);

            for (int i=1; i<=totalPages; i++) {
                String stackOverFlowUrl = "https://stackoverflow.com/tags?page=" + i + "&tab=popular";
                doc = Jsoup.connect(stackOverFlowUrl).get();

                Elements tags = doc.getElementsByClass("post-tag");

                for (Element tag : tags) {
                    String techStack = tag.text();
                    if (!teckStackSet.contains(techStack)) {
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        pstmt.setString(1, techStack);
                        pstmt.setTimestamp(2, timestamp);
                        pstmt.setTimestamp(3, timestamp);

                        pstmt.executeUpdate();
                    }
                }
                /* 1초에 30번, 1일에 10000번 제한이 있습니다. */
                Thread.sleep(250);
            }

            pstmt.close();
            conn.close();
    }
}
