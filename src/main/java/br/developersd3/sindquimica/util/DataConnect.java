package br.developersd3.sindquimica.util;

import java.sql.Connection;
import java.sql.DriverManager;

//"jdbc:postgresql://localhost:5432/fabricam_quimica", "fabricam_developer", "Valente@3873"
//"jdbc:postgresql://localhost:5432/sindquimica", "postgres", "123456");
public class DataConnect {

	public static Connection getConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sindquimica", "postgres", "123456");
			return con;
		} catch (Exception ex) {
			System.out.println("Database.getConnection() Error -->"
					+ ex.getMessage());
			return null;
		}
	}

	public static void close(Connection con) {
		try {
			con.close();
		} catch (Exception ex) {
		}
	}
}