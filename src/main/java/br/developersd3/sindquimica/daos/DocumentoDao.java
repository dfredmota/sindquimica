package br.developersd3.sindquimica.daos;

import java.util.List;

import javax.inject.Named;

import br.developersd3.sindquimica.models.Documento;

@Named("documentoDao")
public class DocumentoDao extends GenericDao<Documento, Integer> {

	public List<Documento> findAllByUsuario(Integer idUsuario) {
		
	      String sql = "from Documento documento where documento.usuario.id = " +idUsuario;

	      List<Documento> lista = getSession().createQuery(sql).list();
	      
	      return lista;
	}


}
