package br.developersd3.sindquimica.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.martinlabs.commons.OpResponse;
import br.developersd3.sindquimica.models.Usuario;
import br.developersd3.sindquimica.util.DataConnect;


public class LoginDAO {

	public static Integer[] validate(String user, String password) {
		Connection con = null;
		PreparedStatement ps = null;
		Integer retorno[] = null;

		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement("Select id,empresa_sistema_id from usuario where login = ? and password = ?");
			ps.setString(1, user);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				
				retorno = new Integer[]{rs.getInt("id"),rs.getInt("empresa_sistema_id")};

			}
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return retorno;
		} finally {
			DataConnect.close(con);
		}
		return retorno;
	}
	
	public static OpResponse<Usuario> loginApp(String user, String password) {
		Connection con = null;
		PreparedStatement ps = null;
		br.developersd3.sindquimica.ws.Usuario usuario = null;

		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement("Select id,empresa_sistema_id from usuario where login = ? and password = ?");
			ps.setString(1, user);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
								
				usuario = new br.developersd3.sindquimica.ws.Usuario();
				
				usuario.setEmail("dfredmota@gmail.com");

			}
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return new OpResponse(usuario);
		} finally {
			DataConnect.close(con);
		}
		return new OpResponse(usuario);
	}
}