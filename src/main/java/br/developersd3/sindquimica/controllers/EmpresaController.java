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

import br.developersd3.sindquimica.datatable.LazyEmpresaDataModel;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Cnae;
import br.developersd3.sindquimica.models.Empresa;
import br.developersd3.sindquimica.models.Endereco;
import br.developersd3.sindquimica.service.CnaeService;
import br.developersd3.sindquimica.service.EmpresaService;
import br.developersd3.sindquimica.util.SessionUtils;

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
	
	@ManagedProperty(name = "cnaeService", value = "#{cnaeService}")
	private CnaeService cnaeService;
	
	private Cnae        cnae;
	
	private Integer     cnaeId;
	
	private List<Cnae>  cnaes;

	@PostConstruct
	public void init() {
		lazyModel = new LazyEmpresaDataModel(empresaService.all(getEmpresaSistema()));
		telefones = new ArrayList<String>();

	}
	
	public String addCnae(){
		
		Cnae cnae = cnaeService.getById(cnaeId,getEmpresaSistema());
		
		if(!this.empresa.getCnaes().contains(cnae)){
			this.empresa.getCnaes().add(cnae);
		}else{
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Esse Cnae já existe na lista!", "Esse Cnae já existe na lista!"));
		}
		
		return null;
	}
	
	public String deleteCnae(){
		
		Cnae cnae = cnaeService.getById(cnaeId,getEmpresaSistema());
		
		for(Cnae cn : this.empresa.getCnaes()){
			
			if(cn.getId().equals(cnae.getId()));
			this.empresa.getCnaes().remove(cn);
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
		String idEmpresa = params.get("idEmpresa");

		this.empresa = empresaService.getById(Integer.parseInt(idEmpresa),getEmpresaSistema());
		
		cnaes = cnaeService.all(getEmpresaSistema());
		
		// setando telefones
		
		telefones = new ArrayList<String>();
		
		if(this.empresa.getTelefones() != null && !this.empresa.getTelefones().isEmpty()){
			
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
		
		this.empresa.setCnaes(new ArrayList<Cnae>());
		
		telefones = new ArrayList<String>();
		
		this.cnaes = cnaeService.all(getEmpresaSistema());
		
		this.empresa.getEndereco().setEmpresaSistema(getEmpresaSistema());

		return "prepareInsert";
	}

	public String create() {

		String str = "insert";
		
		cnaes = cnaeService.all(getEmpresaSistema());
		
		if(telefones != null && !telefones.isEmpty()){
			
			String telefonesUsuario = "";
			
			for(String tel : telefones){
				
				telefonesUsuario = telefonesUsuario + tel;
				telefonesUsuario = telefonesUsuario + ";";
				
			}	
			
			empresa.setTelefones(telefonesUsuario);	
			
		}else{
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Adicione ao menos um telefone","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;
			
		}
		
		if(empresa.getStatus() == null)
			empresa.setStatus(false);
		
		try {
			empresaService.create(empresa,getEmpresaSistema());
		} catch (GenericException e1) {

		}
		
		FacesMessage msg = new FacesMessage("Empresa Criada com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazyEmpresaDataModel(empresaService.all(getEmpresaSistema()));

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
		}else{
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Adicione ao menos um telefone","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;
			
		}

		try {

			empresaService.update(empresa);
			
			FacesMessage msg = new FacesMessage("Empresa Atualizado com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			lazyModel = new LazyEmpresaDataModel(empresaService.all(getEmpresaSistema()));

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
		
		lazyModel = new LazyEmpresaDataModel(empresaService.all(getEmpresaSistema()));

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

	public CnaeService getCnaeService() {
		return cnaeService;
	}

	public void setCnaeService(CnaeService cnaeService) {
		this.cnaeService = cnaeService;
	}

	public Cnae getCnae() {
		return cnae;
	}

	public void setCnae(Cnae cnae) {
		this.cnae = cnae;
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
	
	private Integer getEmpresaSistema(){
		HttpSession session = SessionUtils.getSession();
		Integer empresaSistemaId = (Integer)session.getAttribute("empresaSistemaId");
		
		return empresaSistemaId;
	}
	
	
}
