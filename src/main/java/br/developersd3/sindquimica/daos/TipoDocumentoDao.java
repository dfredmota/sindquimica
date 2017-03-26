package br.developersd3.sindquimica.daos;

import java.util.List;

import javax.inject.Named;

import org.hibernate.Query;

import br.developersd3.sindquimica.models.TipoDocumento;

@Named("tipoDocumentoDao")
public class TipoDocumentoDao extends GenericDao<TipoDocumento, Integer> {
	
public List<TipoDocumento> searchByFilters(TipoDocumento tpDoc) {
		
		StringBuilder sql = new StringBuilder("from TipoDocumento tpDoc where 1=1 ");
	     
		if(tpDoc.getDescricao() != null && !tpDoc.getDescricao().isEmpty()){
	    	 
			sql.append(" and lower(tpDoc.descricao) LIKE lower(:descFiltro) "); 
		}
	     
		 Query query = getSession().createQuery(sql.toString());

		 if(tpDoc.getDescricao() != null && !tpDoc.getDescricao().isEmpty()){
			 query.setParameter("descFiltro", "%"+tpDoc.getDescricao()+"%");
		 }
		 
		 List<TipoDocumento> lista = query.list();	
			
		return lista;
	
	
	}


}
