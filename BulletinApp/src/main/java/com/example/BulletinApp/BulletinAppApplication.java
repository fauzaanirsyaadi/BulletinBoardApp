package com.example.BulletinApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class BulletinAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BulletinAppApplication.class, args);
		System.out.println("Application is running...");
		System.out.println("Swagger UI: http://localhost:8080/swagger-ui.html");
		System.out.println("Front end WEB: http://localhost:8080/");
	}

	@Bean
	public CommandLineRunner initDatabase(DataSource dataSource) {
//		check database
		return args -> {
            try {
                System.out.println("Checking database...");
				Connection connection = dataSource.getConnection();
                System.out.println("Database is ready.");
				// Read the content of check-data.sql
				String checkDataSql = new BufferedReader(new InputStreamReader(
						new ClassPathResource("check-data.sql").getInputStream())).readLine();

				// Execute check-data.sql and get the result
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(checkDataSql);
				int count = 0;
				if (rs.next()) {
					count = rs.getInt(1);
				}
				rs.close();
				stmt.close();

				if (count == 0) {
					ScriptUtils.executeSqlScript(connection, new ClassPathResource("data.sql"));
					System.out.println("Data Dummy is inserted.");
				} else {
					System.out.println("Data Dummy is already inserted.");
				}
            } catch (Exception e) {
                System.out.println("Database is not ready. Creating database...");
                try {
                    ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("schema.sql"));
                    System.out.println("Database is ready.");
					ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("data.sql"));
					System.out.println("Data Dummy is inserted.");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        };
	}
}
