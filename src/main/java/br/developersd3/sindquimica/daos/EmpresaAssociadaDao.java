package br.developersd3.sindquimica.daos;

import java.util.List;

import javax.inject.Named;

import org.hibernate.Query;

import br.developersd3.sindquimica.models.EmpresaAssociada;

@Named("empresaAssociadaDao")
public class EmpresaAssociadaDao extends GenericDao<EmpresaAssociada, Integer> {
	
	public List<EmpresaAssociada> findAllBySegmento(Integer idSegmento) {
		
	      String sql = "select emp from EmpresaAssociada emp  where segmento.id =" +idSegmento+" and status = true";

	      List<EmpresaAssociada> lista = getSession().createQuery(sql).list();
	      
	      return lista;
	}
	
	
	public List<EmpresaAssociada> findAllByCnae(Integer idCnae) {
		
	      String sql = "select emp from EmpresaAssociada emp join emp.cnaes cnae where cnae.id =" +idCnae;

	      List<EmpresaAssociada> lista = getSession().createQuery(sql).list();
	      
	      return lista;
	}
	
	public List<EmpresaAssociada> searchByFilters(EmpresaAssociada emp,String field) {
		
		StringBuilder sql = new StringBuilder("from EmpresaAssociada empresa where 1=1 ");
	     
		Query query = null;
		
		 List<EmpresaAssociada> lista;
		
		
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
