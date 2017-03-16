package br.developersd3.sindquimica.daos;

import java.util.List;

import javax.inject.Named;

import org.hibernate.Query;

import br.developersd3.sindquimica.models.Empresa;

@Named("empresaDao")
public class EmpresaDao extends GenericDao<Empresa, Integer> {
	
	public List<Empresa> searchByFilters(Empresa emp) {
		
		StringBuilder sql = new StringBuilder("from Empresa emp where 1=1 ");
	     
		if(emp.getNomeFantasia() != null && !emp.getNomeFantasia().isEmpty()){
	    	 
			sql.append(" and lower(emp.nomeFantasia) LIKE lower(:nomeFantasia) "); 
		}
		     
		 Query query = getSession().createQuery(sql.toString());

		 if(emp.getNomeFantasia() != null && !emp.getNomeFantasia().isEmpty()){
			 query.setParameter("nomeFantasia", emp.getNomeFantasia());
		 }
		 	 
		 List<Empresa> lista = query.list();	
			
		return lista;	
	
	}


}
