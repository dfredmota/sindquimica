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

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import br.developersd3.sindquimica.datatable.LazyEmpresaDataModel;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Empresa;
import br.developersd3.sindquimica.models.Endereco;
import br.developersd3.sindquimica.service.EmpresaService;

@ManagedBean(name = "empresaController")
@SessionScoped
public class EmpresaController implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private LazyDataModel<Empresa> lazyModel;

	@ManagedProperty(value = "#{empresa}")
	private Empresa empresa;
	
	private String  telefone;
	
	private List<String> telefones;

	@ManagedProperty(name = "empresaService", value = "#{empresaService}")
	private EmpresaService empresaService;

	@PostConstruct
	public void init() {
		lazyModel = new LazyEmpresaDataModel(empresaService.all());
		telefones = new ArrayList<String>();

	}
	
	public String addTelefone(){
		
		telefones.add(telefone);
		telefone = "";		
		
		return null;
	}
	
	public String deleteTelefone(){
		
		if(telefones.contains(telefone)){
			telefones.remove(telefone);
			telefone = "";
		}
		
		FacesMessage msg = new FacesMessage("Telefone excluído com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		return null;
		
	}

	public String prepareUpdate() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idEmpresa = params.get("idEmpresa");

		this.empresa = empresaService.getById(Integer.parseInt(idEmpresa));
		
		// setando telefones
		
		telefones = new ArrayList<String>();
		
		if(this.empresa.getTelefones() != null && this.empresa.getTelefones().isEmpty()){
			
			String[] telefonesArr = this.empresa.getTelefones().split(";");
			
			for(int x =0;x< telefonesArr.length;x++){
				telefones.add(telefonesArr[x]);
			}
						
		}

		return "prepareUpdate";
	}
	
	public String prepareInsert() {

		this.empresa = new Empresa();
		
		this.empresa.setEndereco(new Endereco());
		
		telefones = new ArrayList<String>();

		return "prepareInsert";
	}

	public String create() {

		String str = "insert";
		
		if(telefones != null && !telefones.isEmpty()){
			
			String telefonesUsuario = "";
			
			for(String tel : telefones){
				
				telefonesUsuario = telefonesUsuario + tel;
				telefonesUsuario = telefonesUsuario + ";";
				
			}	
			
			empresa.setTelefones(telefonesUsuario);			
		}
		
		if(empresa.getStatus() == null)
			empresa.setStatus(false);
		
		try {
			empresaService.create(empresa);
		} catch (GenericException e1) {

		}
		
		FacesMessage msg = new FacesMessage("Empresa Criado com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazyEmpresaDataModel(empresaService.all());

		try {

		} catch (Exception e) {
			str = "insertError";

		}

		return str;
	}

	public String update() {

		String str = "update";
		
		if(telefones != null && !telefones.isEmpty()){
			
			String telefonesUsuario = "";
			
			for(String tel : telefones){
				
				telefonesUsuario = telefonesUsuario + tel;
				telefonesUsuario = telefonesUsuario + ";";
				
			}	
			
			empresa.setTelefones(telefonesUsuario);			
		}

		try {

			empresaService.update(empresa);
			
			FacesMessage msg = new FacesMessage("Empresa Atualizado com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			lazyModel = new LazyEmpresaDataModel(empresaService.all());

		} catch (Exception e) {
			str = "updateError";
		}

		return str;
	}

	public String delete() {

		String str = "delete";

		try {
			
		empresaService.delete(empresa);	
		
		FacesMessage msg = new FacesMessage("Empresa excluído com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazyEmpresaDataModel(empresaService.all());

		} catch (Exception e) {
			str = "deleteError";

		}

		return str;
	}
	
	public void onRowSelect(SelectEvent event) {

		if (event.getObject() != null) {
			FacesMessage msg = new FacesMessage("Empresa Selected:" + ((Empresa) event.getObject()).getId());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.empresa = ((Empresa) event.getObject());		

		}
	}
		

	public LazyDataModel<Empresa> getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyDataModel<Empresa> lazyModel) {
		this.lazyModel = lazyModel;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public void setService(EmpresaService service) {
		this.empresaService = service;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public List<String> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<String> telefones) {
		this.telefones = telefones;
	}	
	
}
