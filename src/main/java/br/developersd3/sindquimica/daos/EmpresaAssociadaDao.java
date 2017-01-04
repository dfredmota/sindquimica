package br.developersd3.sindquimica.daos;

import java.util.List;

import javax.inject.Named;

import br.developersd3.sindquimica.models.EmpresaAssociada;

@Named("empresaAssociadaDao")
public class EmpresaAssociadaDao extends GenericDao<EmpresaAssociada, Integer> {
	
	
	public List<EmpresaAssociada> findAllByCnae(Integer idCnae) {
		
	      String sql = "select emp from EmpresaAssociada emp join emp.cnaes cnae where cnae.id =" +idCnae;

	      List<EmpresaAssociada> lista = getSession().createQuery(sql).list();
	      
	      return lista;
	}


}
