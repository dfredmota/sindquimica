package br.developersd3.sindquimica.controllers;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import br.developersd3.sindquimica.datatable.LazySindicatoDataModel;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Endereco;
import br.developersd3.sindquimica.models.Sindicato;
import br.developersd3.sindquimica.service.SindicatoService;

@ManagedBean(name = "sindicatoController")
@SessionScoped
public class SindicatoController implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private LazyDataModel<Sindicato> lazyModel;

	@ManagedProperty(value = "#{sindicato}")
	private Sindicato sindicato;


	@ManagedProperty(name = "sindicatoService", value = "#{sindicatoService}")
	private SindicatoService sindicatoService;

	@PostConstruct
	public void init() {
		lazyModel = new LazySindicatoDataModel(sindicatoService.all());

	}

	public String prepareUpdate() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idSindicato = params.get("idSindicato");

		this.sindicato = sindicatoService.getById(Integer.parseInt(idSindicato));

		return "prepareUpdate";
	}
	
	public String prepareInsert() {

		this.sindicato = new Sindicato();
		
		this.sindicato.setEndereco(new Endereco());

		return "prepareInsert";
	}

	public String create() {

		String str = "insert";
		
		try {
			sindicatoService.create(sindicato);
		} catch (GenericException e1) {

		}
		
		FacesMessage msg = new FacesMessage("Sindicato Criado com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazySindicatoDataModel(sindicatoService.all());

		try {

		} catch (Exception e) {
			str = "insertError";

		}

		return str;
	}

	public String update() {

		String str = "update";

		try {

			sindicatoService.update(sindicato);
			
			FacesMessage msg = new FacesMessage("Sindicato Atualizado com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			lazyModel = new LazySindicatoDataModel(sindicatoService.all());

		} catch (Exception e) {
			str = "updateError";
		}

		return str;
	}

	public String delete() {

		String str = "delete";

		try {
			
		sindicatoService.delete(sindicato);	
		
		FacesMessage msg = new FacesMessage("Sindicato excluído com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazySindicatoDataModel(sindicatoService.all());

		} catch (Exception e) {
			str = "deleteError";

		}

		return str;
	}

	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	public LazyDataModel<Sindicato> getLazyModel() {
		return lazyModel;
	}

	public void setService(SindicatoService service) {
		this.sindicatoService = service;
	}

	public SindicatoService getSindicatoService() {
		return sindicatoService;
	}

	public void setSindicatoService(SindicatoService sindicatoService) {
		this.sindicatoService = sindicatoService;
	}

	public void setLazyModel(LazyDataModel<Sindicato> lazyModel) {
		this.lazyModel = lazyModel;
	}
	
	public void onRowSelect(SelectEvent event) {

		if (event.getObject() != null) {
			FacesMessage msg = new FacesMessage("Sindicato Selected:" + ((Sindicato) event.getObject()).getId());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.sindicato = ((Sindicato) event.getObject());		

		}
	}
}
