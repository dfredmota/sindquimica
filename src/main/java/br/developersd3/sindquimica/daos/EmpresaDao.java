package br.developersd3.sindquimica.daos;

import java.util.List;

import javax.inject.Named;

import org.hibernate.Query;

import br.developersd3.sindquimica.models.Empresa;

@Named("empresaDao")
public class EmpresaDao extends GenericDao<Empresa, Integer> {
	
	public List<Empresa> searchByFilters(Empresa emp,String field) {
		

		StringBuilder sql = new StringBuilder("from Empresa empresa where 1=1 ");
	     
		Query query = null;
		
		 List<Empresa> lista;
		
		
		if(field.equals("razaoSocial")){
			
			if(emp.getRazaoSocial() != null && !emp.getRazaoSocial().isEmpty()){
		    	 
				sql.append(" and lower(empresa.razaoSocial) LIKE lower(:razaoSocial) "); 
			}
		     
			 query = getSession().createQuery(sql.toString());

			 if(emp.getRazaoSocial() != null && !emp.getRazaoSocial().isEmpty()){
				 query.setParameter("razaoSocial", "%"+emp.getRazaoSocial()+"%");
			 }
			
		}
		
		if(field.equals("email")){
			
			if(emp.getEmail() != null && !emp.getEmail().isEmpty()){
		    	 
				sql.append(" and lower(empresa.email ) LIKE lower(:nomeEmail) "); 
			}
		     
			query = getSession().createQuery(sql.toString());

			 if(emp.getEmail() != null && !emp.getEmail().isEmpty()){
				 query.setParameter("nomeEmail", "%"+emp.getEmail()+"%");
			 }
			
		}
		
		if(field.equals("site")){
			
			if(emp.getSite() != null && !emp.getSite().isEmpty()){
		    	 
				sql.append(" and lower(empresa.site ) LIKE lower(:nomeSite) "); 
			}		     
			 
			query = getSession().createQuery(sql.toString());

			 if(emp.getSite() != null && !emp.getSite().isEmpty()){
				 query.setParameter("nomeSite", "%"+emp.getSite()+"%");
			 }
			
		}
		
		if(field.equals("cnpj")){
			
			if(emp.getCnpj() != null && !emp.getCnpj().isEmpty()){
		    	 
				sql.append(" and lower(empresa.cnpj ) LIKE lower(:cnpj) "); 
			}		     
			 
			query = getSession().createQuery(sql.toString());

			 if(emp.getCnpj() != null && !emp.getCnpj().isEmpty()){
				 query.setParameter("cnpj", "%"+emp.getCnpj()+"%");
			 }
			
		}
		
		if(field.equals("responsavel")){
			
			if(emp.getResponsavel() != null && !emp.getResponsavel().isEmpty()){
		    	 
				sql.append(" and lower(empresa.responsavel ) LIKE lower(:responsavel) "); 
			}		     
			 
			query = getSession().createQuery(sql.toString());

			 if(emp.getResponsavel() != null && !emp.getResponsavel().isEmpty()){
				 query.setParameter("responsavel", "%"+emp.getResponsavel()+"%");
			 }
			
		}
		
		if(field.equals("nomeFantasia")){
			
			if(emp.getNomeFantasia() != null && !emp.getNomeFantasia().isEmpty()){
		    	 
				sql.append(" and lower(empresa.nomeFantasia ) LIKE lower(:nomeFantasia) "); 
			}		     
			 
			query = getSession().createQuery(sql.toString());

			 if(emp.getNomeFantasia() != null && !emp.getNomeFantasia().isEmpty()){
				 query.setParameter("nomeFantasia", "%"+emp.getNomeFantasia()+"%");
			 }
			
		}

		 lista = query.list();	
			
		return lista;	
	
	}


}
