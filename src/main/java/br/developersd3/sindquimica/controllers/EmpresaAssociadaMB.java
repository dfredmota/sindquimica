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
import org.primefaces.model.LazyDataModel;

import br.developersd3.sindquimica.datatable.LazyEmpresaAssociadaDataModel;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Cnae;
import br.developersd3.sindquimica.models.Empresa;
import br.developersd3.sindquimica.models.EmpresaAssociada;
import br.developersd3.sindquimica.models.Endereco;
import br.developersd3.sindquimica.service.CnaeService;
import br.developersd3.sindquimica.service.EmpresaAssociadaService;
import br.developersd3.sindquimica.service.EmpresaService;
import br.developersd3.sindquimica.util.SessionUtils;

@ManagedBean(name = "empresaAssociadaMB")
@SessionScoped
public class EmpresaAssociadaMB implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private LazyDataModel<EmpresaAssociada> lazyModel;

	@ManagedProperty(value = "#{empresaAssociada}")
	private EmpresaAssociada empresaAssociada;
	
	private Integer empresaId;
	
	private String  telefone;
	
	private List<String> telefones;

	@ManagedProperty(name = "empresaAssociadaService", value = "#{empresaAssociadaService}")
	private EmpresaAssociadaService empresaAssociadaService;
	
	@ManagedProperty(name = "empresaService", value = "#{empresaService}")
	private EmpresaService empresaService;
	
	@ManagedProperty(name = "cnaeService", value = "#{cnaeService}")
	private CnaeService cnaeService;
		
	private List<Empresa> empresas;
	
	private Integer     cnaeId;
	
	private List<Cnae>  cnaes;

	@PostConstruct
	public void init() {
		lazyModel = new LazyEmpresaAssociadaDataModel(empresaAssociadaService.all(getEmpresaSistema()));
		empresas = empresaService.all(getEmpresaSistema());
		telefones = new ArrayList<String>();
	}
	
	public void setListaCnaes(){
		
		Empresa empresa = empresaService.getById(empresaId,getEmpresaSistema());
		
		this.cnaes = empresa.getCnaes();	
		
		this.empresaAssociada = new EmpresaAssociada();
		
		this.empresaAssociada.setEndereco(new Endereco());
		
		this.empresaAssociada.setCnaes(new ArrayList<Cnae>());
		
	}
	
	public String addCnae(){
		
		Cnae cnae = cnaeService.getById(cnaeId,getEmpresaSistema());
		
		if(!this.empresaAssociada.getCnaes().contains(cnae)){
			this.empresaAssociada.getCnaes().add(cnae);
		}else{
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Esse Cnae já existe na lista!", "Esse Cnae já existe na lista!"));
		}
		
		return null;
	}
	
	public String deleteCnae(){
		
		Cnae cnae = cnaeService.getById(cnaeId,getEmpresaSistema());
		
		for(Cnae cn : this.empresaAssociada.getCnaes()){
			
			if(cn.getId().equals(cnae.getId()));
			this.empresaAssociada.getCnaes().remove(cn);
			break;
		}
				
		return null;
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
		String idEmpresaAssociada = params.get("idEmpresaAssociada");

		this.empresaAssociada = empresaAssociadaService.getById(Integer.parseInt(idEmpresaAssociada),getEmpresaSistema());
		
		telefones = new ArrayList<String>();
		
		if(this.empresaAssociada.getTelefones() != null && !this.empresaAssociada.getTelefones().isEmpty()){
			
			String[] telefonesArr = this.empresaAssociada.getTelefones().split(";");
			
			for(int x =0;x< telefonesArr.length;x++){
				telefones.add(telefonesArr[x]);
			}
						
		}
		
		if(this.empresas != null && !empresas.isEmpty()){
			
		Empresa emp = empresaService.getById(this.empresas.get(0).getId(), getEmpresaSistema());	
			
		this.cnaes	= emp.getCnaes();		
			
		}		

		return "prepareUpdate";
	}
	
	public String prepareInsert() {

		this.empresaAssociada = new EmpresaAssociada();
		
		this.empresaAssociada.setEndereco(new Endereco());
		
		this.empresaAssociada.setCnaes(new ArrayList<Cnae>());
				
		telefones = new ArrayList<String>();
		
		empresas = empresaService.all(getEmpresaSistema());
		
		System.out.println("EMPRESA:"+empresas.size());
		
		this.empresaAssociada.getEndereco().setEmpresaSistema(getEmpresaSistema());
		
		if(this.empresas != null && !empresas.isEmpty()){
			
		Empresa emp = empresaService.getById(this.empresas.get(0).getId(), getEmpresaSistema());	
			
		this.cnaes	= emp.getCnaes();		
			
		}

		return "prepareInsert";
	}

	public String create() {

		String str = "insert";
		
		Empresa empresa = empresaService.getById(empresaId,getEmpresaSistema());
		
		empresaAssociada.setEmpresa(empresa);
		
		
		if(telefones != null && !telefones.isEmpty()){
			
			String telefonesUsuario = "";
			
			for(String tel : telefones){
				
				telefonesUsuario = telefonesUsuario + tel;
				telefonesUsuario = telefonesUsuario + ";";
				
			}	
			
			empresaAssociada.setTelefones(telefonesUsuario);			
		}else{
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Adicione ao menos um telefone","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;
			
		}
		
		try {
			empresaAssociadaService.create(empresaAssociada,getEmpresaSistema());
		} catch (GenericException e1) {

		}
		
		FacesMessage msg = new FacesMessage("Empresa Associada Criada com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazyEmpresaAssociadaDataModel(empresaAssociadaService.all(getEmpresaSistema()));

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
			
			empresaAssociada.setTelefones(telefonesUsuario);			
		}else{
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Adicione ao menos um telefone","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;
			
		}

		try {

			empresaAssociadaService.update(empresaAssociada);
			
			FacesMessage msg = new FacesMessage("Empresa Associada Atualizada com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			lazyModel = new LazyEmpresaAssociadaDataModel(empresaAssociadaService.all(getEmpresaSistema()));

		} catch (Exception e) {
			str = "updateError";
		}

		return str;
	}

	public String delete() {

		String str = "delete";

		try {
			
			empresaAssociadaService.delete(empresaAssociada);	
		
		FacesMessage msg = new FacesMessage("EmpresaAssociada excluído com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazyEmpresaAssociadaDataModel(empresaAssociadaService.all(getEmpresaSistema()));

		} catch (Exception e) {
			str = "deleteError";

		}

		return str;
	}
	
	public void onRowSelect(SelectEvent event) {

		if (event.getObject() != null) {
			FacesMessage msg = new FacesMessage("EmpresaAssociada Selected:" + ((EmpresaAssociada) event.getObject()).getId());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.empresaAssociada = ((EmpresaAssociada) event.getObject());		

		}
	}
		

	public LazyDataModel<EmpresaAssociada> getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyDataModel<EmpresaAssociada> lazyModel) {
		this.lazyModel = lazyModel;
	}

	public EmpresaAssociada getEmpresaAssociada() {
		return empresaAssociada;
	}

	public void setEmpresaAssociada(EmpresaAssociada empresaAssociada) {
		this.empresaAssociada = empresaAssociada;
	}

	public EmpresaAssociadaService getEmpresaAssociadaService() {
		return empresaAssociadaService;
	}

	public void setEmpresaAssociadaService(EmpresaAssociadaService empresaAssociadaService) {
		this.empresaAssociadaService = empresaAssociadaService;
	}

	public void setService(EmpresaAssociadaService service) {
		this.empresaAssociadaService = service;
	}

	public Integer getEmpresaId() {
		return empresaId;
	}

	public void setEmpresaId(Integer empresaId) {
		this.empresaId = empresaId;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
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

	public Integer getCnaeId() {
		return cnaeId;
	}

	public void setCnaeId(Integer cnaeId) {
		this.cnaeId = cnaeId;
	}

	public List<Cnae> getCnaes() {
		return cnaes;
	}

	public void setCnaes(List<Cnae> cnaes) {
		this.cnaes = cnaes;
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
