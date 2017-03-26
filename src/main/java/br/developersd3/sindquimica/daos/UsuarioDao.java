package br.developersd3.sindquimica.daos;

import java.util.List;

import javax.inject.Named;

import org.hibernate.Query;

import br.developersd3.sindquimica.models.Usuario;

@Named("usuarioDao")
public class UsuarioDao extends GenericDao<Usuario, Integer> {
	
	public List<Usuario> searchByFilters(Usuario user,String field) {
		
		StringBuilder sql = new StringBuilder("from Usuario user where 1=1 ");
	     
		Query query = null;
		
		 List<Usuario> lista;
		
		
		if(field.equals("nome")){
			
			if(user.getNome() != null && !user.getNome().isEmpty()){
		    	 
				sql.append(" and lower(user.nome) LIKE lower(:nomeUsuario) "); 
			}
		     
			 query = getSession().createQuery(sql.toString());

			 if(user.getNome() != null && !user.getNome().isEmpty()){
				 query.setParameter("nomeUsuario", "%"+user.getNome()+"%");
			 }
			
		}
		
		if(field.equals("email")){
			
			if(user.getEmail() != null && !user.getEmail().isEmpty()){
		    	 
				sql.append(" and lower(user.email ) LIKE lower(:nomeEmail) "); 
			}
		     
			query = getSession().createQuery(sql.toString());

			 if(user.getEmail() != null && !user.getEmail().isEmpty()){
				 query.setParameter("nomeEmail", "%"+user.getEmail()+"%");
			 }
			
		}
		
		if(field.equals("site")){
			
			if(user.getSite() != null && !user.getSite().isEmpty()){
		    	 
				sql.append(" and lower(user.site ) LIKE lower(:nomeSite) "); 
			}		     
			 
			query = getSession().createQuery(sql.toString());

			 if(user.getSite() != null && !user.getSite().isEmpty()){
				 query.setParameter("nomeSite", "%"+user.getSite()+"%");
			 }
			
		}

		 lista = query.list();	
			
		return lista;
	
	
	}


}
