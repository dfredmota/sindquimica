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

import br.developersd3.sindquimica.datatable.LazyTipoDocumentoDataModel;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.TipoDocumento;
import br.developersd3.sindquimica.service.TipoDocumentoService;

@ManagedBean(name = "tipoDocumentoMB")
@SessionScoped
public class TipoDocumentoMB implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private LazyDataModel<TipoDocumento> lazyModel;

	@ManagedProperty(value = "#{tipoDocumento}")
	private TipoDocumento tipoDocumento;


	@ManagedProperty(name = "tipoDocumentoService", value = "#{tipoDocumentoService}")
	private TipoDocumentoService tipoDocumentoService;

	@PostConstruct
	public void init() {
		lazyModel = new LazyTipoDocumentoDataModel(tipoDocumentoService.all());

	}

	public String prepareUpdate() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idSindicato = params.get("idTipoDocumento");

		this.tipoDocumento = tipoDocumentoService.getById(Integer.parseInt(idSindicato));

		return "prepareUpdate";
	}
	
	public String prepareInsert() {

		this.tipoDocumento = new TipoDocumento();
		
		return "prepareInsert";
	}

	public String create() {

		String str = "insert";
		
		try {
			tipoDocumentoService.create(tipoDocumento);
		} catch (GenericException e1) {

		}
		
		FacesMessage msg = new FacesMessage("Tipo de Documento Criado com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazyTipoDocumentoDataModel(tipoDocumentoService.all());

		try {

		} catch (Exception e) {
			str = "insertError";

		}

		return str;
	}

	public String update() {

		String str = "update";

		try {

			tipoDocumentoService.update(tipoDocumento);
			
			FacesMessage msg = new FacesMessage("Tipo de Documento Atualizado com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			lazyModel = new LazyTipoDocumentoDataModel(tipoDocumentoService.all());

		} catch (Exception e) {
			str = "updateError";
		}

		return str;
	}

	public String delete() {

		String str = "delete";

		try {
			
		tipoDocumentoService.delete(tipoDocumento);	
		
		FacesMessage msg = new FacesMessage("Tipo de Documento excluído com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazyTipoDocumentoDataModel(tipoDocumentoService.all());

		} catch (Exception e) {
			str = "deleteError";

		}

		return str;
	}
	
	public void onRowSelect(SelectEvent event) {

		if (event.getObject() != null) {
			FacesMessage msg = new FacesMessage("Tipo Documento Selected:" + ((TipoDocumento) event.getObject()).getId());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.tipoDocumento = ((TipoDocumento) event.getObject());		

		}
	}	
	
	public LazyDataModel<TipoDocumento> getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyDataModel<TipoDocumento> lazyModel) {
		this.lazyModel = lazyModel;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public TipoDocumentoService getTipoDocumentoService() {
		return tipoDocumentoService;
	}

	public void setTipoDocumentoService(TipoDocumentoService tipoDocumentoService) {
		this.tipoDocumentoService = tipoDocumentoService;
	}
	
}
