package br.developersd3.sindquimica.daos;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

import br.developersd3.sindquimica.models.Mensagem;

@Named("mensagemDao")
public class MensagemDao extends GenericDao<Mensagem, Integer> {


	public List<Mensagem> findAllByUsuario(Integer idEmpresaSistema,Integer idUsuario) {
		
//	      String sql = "select msg from Mensagem msg join msg.usuarios users where users.id = "+idUsuario+" and msg.empresaSistema="+
//	      idEmpresaSistema +" and msg.usuario.id="+idUsuario;
		
	      String sql = "select msg from Mensagem msg where msg.usuario.id = "+idUsuario+" and msg.empresaSistema="+
	      idEmpresaSistema +" order by createdAt desc";
	      
	      Query q = getEntityManagerFactory().createQuery(sql);

	      List<Mensagem> lista = getSession().createQuery(sql).list();
	      
	      return lista;
	}
	
	public List<Mensagem> findAllByGrupo(Integer idEmpresaSistema,Integer grupoId) {
		
	      String sql = "select msg from Mensagem msg join msg.grupos grupo where grupo.id = "+grupoId+"and msg.empresaSistema="+
	    	      idEmpresaSistema;
	      
	      Query q = getEntityManagerFactory().createQuery(sql);

	      List<Mensagem> lista = getSession().createQuery(sql).list();
	      
	      return lista;
	}
	
}
