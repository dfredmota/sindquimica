package br.developersd3.sindquimica.daos;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

import br.developersd3.sindquimica.models.Grupo;

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

}
