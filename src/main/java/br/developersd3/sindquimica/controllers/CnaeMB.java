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
import br.developersd3.sindquimica.models.Cnae;
import br.developersd3.sindquimica.service.CnaeService;
import br.developersd3.sindquimica.util.SessionUtils;

@ManagedBean(name = "cnaeMB")
@SessionScoped
public class CnaeMB implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private List<Cnae> lista;

	@ManagedProperty(value = "#{cnae}")
	private Cnae cnae;

	@ManagedProperty(name = "cnaeService", value = "#{cnaeService}")
	private CnaeService cnaeService;

	@PostConstruct
	public void init() {
		
		this.cnae = new Cnae();
			
		lista = new ArrayList<Cnae>(cnaeService.all(getEmpresaSistema()));

	}
	
	public String searchByFilters(){
				
		lista = cnaeService.all(getEmpresaSistema());		
		
		return null;		
		
	}

	public String prepareUpdate() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idSindicato = params.get("idCnae");

		this.cnae = cnaeService.getById(Integer.parseInt(idSindicato),getEmpresaSistema());

		return "prepareUpdate";
	}
	
	public String prepareInsert() {

		this.cnae = new Cnae();
		
		this.cnae.setCodigo("");
		this.cnae.setDescricao("");
		
		return "prepareInsert";
	}

	public String create() {

		String str = "insert";
		
		try {
			cnaeService.create(cnae,getEmpresaSistema());
		} catch (GenericException e1) {

		}
		
		FacesMessage msg = new FacesMessage("Cnae Criado com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lista = new ArrayList<Cnae>(cnaeService.all(getEmpresaSistema()));

		try {

		} catch (Exception e) {
			str = "insertError";

		}

		return str;
	}

	public String update() {

		String str = "update";

		try {

			cnaeService.update(cnae);
			
			FacesMessage msg = new FacesMessage("Cnae Atualizado com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			lista = new ArrayList<Cnae>(cnaeService.all(getEmpresaSistema()));

		} catch (Exception e) {
			str = "updateError";
		}

		return str;
	}

	public String delete() {

		String str = "delete";

		try {
			
		cnaeService.delete(cnae);	
		
		FacesMessage msg = new FacesMessage("Cnae excluído com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lista = new ArrayList<Cnae>(cnaeService.all(getEmpresaSistema()));

		} catch (Exception e) {
			str = "deleteError";

		}

		return str;
	}
	
	public void onRowSelect(SelectEvent event) {

		if (event.getObject() != null) {
			FacesMessage msg = new FacesMessage("Cnae Selected:" + ((Cnae) event.getObject()).getId());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.cnae = ((Cnae) event.getObject());		

		}
	}	

	public List<Cnae> getLista() {
		return lista;
	}

	public void setLista(List<Cnae> lista) {
		this.lista = lista;
	}
	
	public Cnae getCnae() {
		return cnae;
	}

	public void setCnae(Cnae cnae) {
		this.cnae = cnae;
	}

	public CnaeService getCnaeService() {
		return cnaeService;
	}

	public void setCnaeService(CnaeService cnaeService) {
		this.cnaeService = cnaeService;
	}

	private Integer getEmpresaSistema(){
		HttpSession session = SessionUtils.getSession();
		Integer empresaSistemaId = (Integer)session.getAttribute("empresaSistemaId");
		
		return empresaSistemaId;
	}

}
