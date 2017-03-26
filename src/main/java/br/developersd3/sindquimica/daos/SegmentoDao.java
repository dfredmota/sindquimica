package br.developersd3.sindquimica.daos;

import java.util.List;

import javax.inject.Named;

import br.developersd3.sindquimica.models.Segmento;

import org.hibernate.Query;

@Named("segmentoDao")
public class SegmentoDao extends GenericDao<Segmento, Integer> {
	
	public List<Segmento> searchByFilters(Segmento seg) {
		
		StringBuilder sql = new StringBuilder("from Segmento seg where 1=1 ");
	     
		if(seg.getDescricao() != null && !seg.getDescricao().isEmpty()){
	    	 
			sql.append(" and lower(seg.descricao) LIKE lower(:descFiltro) "); 
		}
	     
		 Query query = getSession().createQuery(sql.toString());

		 if(seg.getDescricao() != null && !seg.getDescricao().isEmpty()){
			 query.setParameter("descFiltro", "%"+seg.getDescricao()+"%");
		 }
		 
		 List<Segmento> lista = query.list();	
			
		return lista;
	
	
	}


}
