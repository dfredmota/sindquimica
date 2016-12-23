package br.developersd3.sindquimica.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.developersd3.sindquimica.util.DataConnect;


public class LoginDAO {

	public static Integer validate(String user, String password) {
		Connection con = null;
		PreparedStatement ps = null;
		Integer retorno = 0;

		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement("Select id from usuario where login = ? and password = ?");
			ps.setString(1, user);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt("id");
			}
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return 0;
		} finally {
			DataConnect.close(con);
		}
		return retorno;
	}
}