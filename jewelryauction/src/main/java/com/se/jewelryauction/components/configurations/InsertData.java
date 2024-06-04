package com.se.jewelryauction.components.configurations;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
@Component
@RequiredArgsConstructor
public class InsertData {

//    private final JdbcTemplate jdbcTemplate;
//    private static final Logger LOGGER = LoggerFactory.getLogger(InsertData.class);
//
//    @PostConstruct
//    public void insertDataFromSqlFile() {
//        try (BufferedReader reader = new BufferedReader(new FileReader("data_sql"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
//                    jdbcTemplate.execute(line);
//                }
//            }
//            LOGGER.info("Data insert successfully");
//        } catch (IOException e) {
//            LOGGER.error("Insert data fail: {}", e.getMessage());
//        }
//    }
}

