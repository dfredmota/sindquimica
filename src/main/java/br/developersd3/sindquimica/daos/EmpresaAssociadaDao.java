package br.developersd3.sindquimica.daos;

import java.util.List;

import javax.inject.Named;

import org.hibernate.Query;

import br.developersd3.sindquimica.models.EmpresaAssociada;

@Named("empresaAssociadaDao")
public class EmpresaAssociadaDao extends GenericDao<EmpresaAssociada, Integer> {
	
	
	public List<EmpresaAssociada> findAllByCnae(Integer idCnae) {
		
	      String sql = "select emp from EmpresaAssociada emp join emp.cnaes cnae where cnae.id =" +idCnae;

	      List<EmpresaAssociada> lista = getSession().createQuery(sql).list();
	      
	      return lista;
	}
	
	public List<EmpresaAssociada> searchByFilters(EmpresaAssociada emp) {
		
		StringBuilder sql = new StringBuilder("from EmpresaAssociada emp where 1=1 ");
	     
		if(emp.getNomeFantasia() != null && !emp.getNomeFantasia().isEmpty()){
	    	 
			sql.append(" and lower(emp.nomeFantasia) LIKE lower(:nomeFantasia) "); 
		}
		     
		 Query query = getSession().createQuery(sql.toString());

		 if(emp.getNomeFantasia() != null && !emp.getNomeFantasia().isEmpty()){
			 query.setParameter("nomeFantasia", emp.getNomeFantasia());
		 }
		 	 
		 List<EmpresaAssociada> lista = query.list();	
			
		return lista;
	
	
	}


}
