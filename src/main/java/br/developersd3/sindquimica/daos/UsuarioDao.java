package br.developersd3.sindquimica.daos;

import java.util.List;

import javax.inject.Named;

import org.hibernate.Query;

import br.developersd3.sindquimica.models.Segmento;
import br.developersd3.sindquimica.models.Usuario;

@Named("usuarioDao")
public class UsuarioDao extends GenericDao<Usuario, Integer> {
	
	public List<Usuario> searchByFilters(Usuario user) {
		
		StringBuilder sql = new StringBuilder("from Usuario user where 1=1 ");
	     
		if(user.getNome() != null && !user.getNome().isEmpty()){
	    	 
			sql.append(" and lower(user.nome) LIKE lower(:nomeUsuario) "); 
		}
		
		if(user.getEmail() != null && !user.getEmail().isEmpty()){
	    	 
			sql.append(" and lower(user.email) LIKE lower(:emailUsuario) "); 
		}
	     
		 Query query = getSession().createQuery(sql.toString());

		 if(user.getNome() != null && !user.getNome().isEmpty()){
			 query.setParameter("nomeUsuario", user.getNome());
		 }
		 
		 if(user.getEmail() != null && !user.getEmail().isEmpty()){
	    	 
			 query.setParameter("emailUsuario", user.getEmail());
		 }
		 
		 List<Usuario> lista = query.list();	
			
		return lista;
	
	
	}


}
