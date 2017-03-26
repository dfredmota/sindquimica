package br.developersd3.sindquimica.controllers;

import java.io.Serializable;
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
import org.primefaces.model.LazyDataModel;


import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Segmento;
import br.developersd3.sindquimica.service.SegmentoService;
import br.developersd3.sindquimica.util.SessionUtils;

@ManagedBean(name = "segmentoMB")
@SessionScoped
public class SegmentoMB implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private List<Segmento> lista;

	@ManagedProperty(value = "#{segmento}")
	private Segmento segmento;


	@ManagedProperty(name = "segmentoService", value = "#{segmentoService}")
	private SegmentoService segmentoService;
	
	private String descricaoFiltro;

	@PostConstruct
	public void init() {
			
		lista = segmentoService.all(getEmpresaSistema());

	}
	
	public String searchByFilters(){
		
		this.segmento = new Segmento();
		
		this.segmento.setDescricao(descricaoFiltro);
		
		lista = segmentoService.searchByFilters(this.segmento,null);		
		
		return null;
		
		
	}

	public String prepareUpdate() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idSindicato = params.get("idSegmento");

		this.segmento = segmentoService.getById(Integer.parseInt(idSindicato),getEmpresaSistema());

		return "prepareUpdate";
	}
	
	public String prepareInsert() {

		this.segmento = new Segmento();
		
		return "prepareInsert";
	}

	public String create() {

		String str = "insert";
		
		try {
			segmentoService.create(segmento,getEmpresaSistema());
		} catch (GenericException e1) {

		}
		
		FacesMessage msg = new FacesMessage("Segmento Criado com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lista = segmentoService.all(getEmpresaSistema());

		try {

		} catch (Exception e) {
			str = "insertError";

		}

		return str;
	}

	public String update() {

		String str = "update";

		try {

			segmentoService.update(segmento);
			
			FacesMessage msg = new FacesMessage("Tipo de Documento Atualizado com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			lista = segmentoService.all(getEmpresaSistema());

		} catch (Exception e) {
			str = "updateError";
		}

		return str;
	}

	public String delete() {

		String str = "delete";

		try {
			
		segmentoService.delete(segmento);	
		
		FacesMessage msg = new FacesMessage("Tipo de Documento excluído com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lista = segmentoService.all(getEmpresaSistema());

		} catch (Exception e) {
			str = "deleteError";

		}

		return str;
	}
	
	public void onRowSelect(SelectEvent event) {

		if (event.getObject() != null) {
			FacesMessage msg = new FacesMessage("Tipo Documento Selected:" + ((Segmento) event.getObject()).getId());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.segmento = ((Segmento) event.getObject());		

		}
	}	

	public List<Segmento> getLista() {
		return lista;
	}

	public void setLista(List<Segmento> lista) {
		this.lista = lista;
	}

	public Segmento getSegmento() {
		return segmento;
	}

	public void setSegmento(Segmento segmento) {
		this.segmento = segmento;
	}

	public SegmentoService getSegmentoService() {
		return segmentoService;
	}

	public void setSegmentoService(SegmentoService segmentoService) {
		this.segmentoService = segmentoService;
	}
	
	public String getDescricaoFiltro() {
		return descricaoFiltro;
	}

	public void setDescricaoFiltro(String descricaoFiltro) {
		this.descricaoFiltro = descricaoFiltro;
	}

	private Integer getEmpresaSistema(){
		HttpSession session = SessionUtils.getSession();
		Integer empresaSistemaId = (Integer)session.getAttribute("empresaSistemaId");
		
		return empresaSistemaId;
	}
	
}
