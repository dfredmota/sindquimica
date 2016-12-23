package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.MensagemDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Mensagem;

@Named("mensagemService")
public class MensagemService implements GenericService<Mensagem> {

	@Inject
	@Named("mensagemDao")
	private MensagemDao dao;

	@Override
	public List<Mensagem> all() {
		return dao.findAll();
	}

	@Override
	public Mensagem getById(Integer id) {
		return dao.getById(Mensagem.class,id);
	}

	@Override
	public Mensagem create(Mensagem entity) throws GenericException {
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

}