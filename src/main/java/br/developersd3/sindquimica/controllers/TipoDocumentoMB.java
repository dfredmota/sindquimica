package br.developersd3.sindquimica.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.event.SelectEvent;

import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.TipoDocumento;
import br.developersd3.sindquimica.service.TipoDocumentoService;
import br.developersd3.sindquimica.util.SessionUtils;

@ManagedBean(name = "tipoDocumentoMB")
@SessionScoped
public class TipoDocumentoMB implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private List<TipoDocumento> lista;

	@ManagedProperty(value = "#{tipoDocumento}")
	private TipoDocumento tipoDocumento;

	private String descricaoFiltro;

	@ManagedProperty(name = "tipoDocumentoService", value = "#{tipoDocumentoService}")
	private TipoDocumentoService tipoDocumentoService;

	@PostConstruct
	public void init() {
			
		lista = new ArrayList<TipoDocumento>(tipoDocumentoService.all(getEmpresaSistema()));

	}
	
	public String searchByFilters(){
		
		this.tipoDocumento = new TipoDocumento();
		
		this.tipoDocumento.setDescricao(descricaoFiltro);
		
		lista = tipoDocumentoService.searchByFilters(this.tipoDocumento,null);		
		
		return null;
		
		
	}

	public String prepareUpdate() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idSindicato = params.get("idTipoDocumento");

		this.tipoDocumento = tipoDocumentoService.getById(Integer.parseInt(idSindicato),getEmpresaSistema());

		return "prepareUpdate";
	}
	
	public String prepareInsert() {

		this.tipoDocumento = new TipoDocumento();
		
		return "prepareInsert";
	}

	public String create() {

		String str = "insert";
		
		try {
			tipoDocumentoService.create(tipoDocumento,getEmpresaSistema());
		} catch (GenericException e1) {

		}
		
		FacesMessage msg = new FacesMessage("Tipo de Documento Criado com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lista = new ArrayList<TipoDocumento>(tipoDocumentoService.all(getEmpresaSistema()));

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
			
			lista = new ArrayList<TipoDocumento>(tipoDocumentoService.all(getEmpresaSistema()));

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
		
		lista = new ArrayList<TipoDocumento>(tipoDocumentoService.all(getEmpresaSistema()));

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

	public List<TipoDocumento> getLista() {
		return lista;
	}

	public void setLista(List<TipoDocumento> lista) {
		this.lista = lista;
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
	
	private Integer getEmpresaSistema(){
		HttpSession session = SessionUtils.getSession();
		Integer empresaSistemaId = (Integer)session.getAttribute("empresaSistemaId");
		
		return empresaSistemaId;
	}

	public String getDescricaoFiltro() {
		return descricaoFiltro;
	}

	public void setDescricaoFiltro(String descricaoFiltro) {
		this.descricaoFiltro = descricaoFiltro;
	}
	
}
