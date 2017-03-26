package br.developersd3.sindquimica.daos;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;
import br.developersd3.sindquimica.models.Grupo;
import br.developersd3.sindquimica.models.Segmento;

@Named("grupoDao")
public class GrupoDao extends GenericDao<Grupo, Integer> {


	public List<Grupo> findAllByEmpresaAssociada(Integer idEmpresaAssociada) {
		
	      String sql = "select grupo from Grupo grupo join grupo.empresaAssociada emp where emp.id = "+idEmpresaAssociada;
	      
	      Query q = getEntityManagerFactory().createQuery(sql);

	      List<Grupo> lista = getSession().createQuery(sql).list();
	      
	      return lista;
	}
	
	public List<Grupo> findAllByUsuario(Integer usuarioId) {
		
	      String sql = "select grupo from Grupo grupo join grupo.usuarios user where user.id = "+usuarioId;
	      
	      Query q = getEntityManagerFactory().createQuery(sql);

	      List<Grupo> lista = getSession().createQuery(sql).list();
	      
	      return lista;
	}
	
	public List<Grupo> searchByFilters(Grupo grupo) {
		
		StringBuilder sql = new StringBuilder("from Grupo grupo where 1=1 ");
	     
		if(grupo.getNome() != null && !grupo.getNome().isEmpty()){
	    	 
			sql.append(" and lower(grupo.nome) LIKE lower(:nomeFiltro) "); 
		}
	     
		 org.hibernate.Query query = getSession().createQuery(sql.toString());

		 if(grupo.getNome() != null && !grupo.getNome().isEmpty()){
			 query.setParameter("nomeFiltro", "%"+grupo.getNome()+"%");
		 }
		 
		 List<Grupo> lista = query.list();	
			
		return lista;
	
	
	}

}
