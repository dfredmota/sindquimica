package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.MensagemDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Grupo;
import br.developersd3.sindquimica.models.Mensagem;

@Named("mensagemService")
public class MensagemService implements GenericService<Mensagem> {

	@Inject
	@Named("mensagemDao")
	private MensagemDao dao;

	@Override
	public List<Mensagem> all(Integer empresaSistema) {
		return dao.findAll(empresaSistema);
	}

	@Override
	public Mensagem getById(Integer id,Integer empresaSistema) {
		return dao.getById(Mensagem.class,id,empresaSistema);
	}

	@Override
	public Mensagem create(Mensagem entity,Integer empresaSistema) throws GenericException {
		entity.setEmpresaSistema(empresaSistema);
		return dao.save(entity);
	}

	@Override
	public Mensagem update(Mensagem entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(Mensagem entity) throws GenericException {
		dao.delete(entity);
	}

	public List<Mensagem> findAllByUsuario(Integer idEmpresaSistema,Integer idUsuario) throws GenericException {
		return dao.findAllByUsuario(idEmpresaSistema,idUsuario);
	}
	
	public List<Mensagem> findAllByGrupo(Integer idEmpresaSistema,Integer grupoId) {
		return dao.findAllByGrupo(idEmpresaSistema,grupoId);		
	}

	@Override
	public List<Mensagem> searchByFilters(Mensagem entity,String field) {
		// TODO Auto-generated method stub
		return null;
	}

}