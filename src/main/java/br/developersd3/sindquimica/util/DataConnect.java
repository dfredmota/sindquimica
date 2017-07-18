package br.developersd3.sindquimica.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



//"jdbc:postgresql://localhost:5432/fabricam_sindquimica", "fabricam_developer", "Valente@3873"
//"jdbc:postgresql://localhost:5432/sindquimica", "postgres", "postgres2530");
public class DataConnect {

	public static Connection getConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager
					.getConnection("jdbc:postgresql://localhost:5432/sindquimica", "postgres", "Root@3873");
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
	
	public static void salvaImagemEvento(Integer id,byte[] imagem){
		
		 PreparedStatement ps = null;
		 Connection con = null;
		 ResultSet rs = null;
		 
		 try {
		 
		 String sql = "update evento set imagem=? where id="+id;
		 
		 con = getConnection();
		 
		 ps = con.prepareStatement(sql);
		 
		 ps.setBytes(1, imagem);
		 
		 ps.executeUpdate();
		
		 } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            if (rs != null)
		                rs.close();
		            if (ps != null)
		                ps.close();
		            if (con != null)
		                con.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		
		    }
		
	}
	
	
	public static byte[] carregaImagem(Integer id){
		
		Connection con = null;
		PreparedStatement ps = null;
		byte[] imagem = null;

		try {
			con = DataConnect.getConnection();

			ps = con.prepareStatement("Select imagem from evento where id ="+id);

			ResultSet rs = ps.executeQuery();
			
			System.out.println(ps);

			if (rs.next()) {
				
				imagem = rs.getBytes("imagem");

			}
	
			
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return imagem;
		} finally {
			DataConnect.close(con);
		}
		return imagem;
		
	}
}