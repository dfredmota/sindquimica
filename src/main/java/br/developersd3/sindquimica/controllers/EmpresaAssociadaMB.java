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
import br.developersd3.sindquimica.models.Empresa;
import br.developersd3.sindquimica.models.EmpresaAssociada;
import br.developersd3.sindquimica.models.Endereco;
import br.developersd3.sindquimica.models.Segmento;
import br.developersd3.sindquimica.service.CnaeService;
import br.developersd3.sindquimica.service.EmpresaAssociadaService;
import br.developersd3.sindquimica.service.EmpresaService;
import br.developersd3.sindquimica.service.SegmentoService;
import br.developersd3.sindquimica.util.SessionUtils;
import br.developersd3.sindquimica.util.Validations;

@ManagedBean(name = "empresaAssociadaMB")
@SessionScoped
public class EmpresaAssociadaMB implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private List<EmpresaAssociada> lista;

	@ManagedProperty(value = "#{empresaAssociada}")
	private EmpresaAssociada empresaAssociada;
	
	private Integer empresaId;
	
	private String  telefone;
	
	private List<String> telefones;
	
	private List<String> emails;
	
	private String email;

	@ManagedProperty(name = "empresaAssociadaService", value = "#{empresaAssociadaService}")
	private EmpresaAssociadaService empresaAssociadaService;
	
	@ManagedProperty(name = "empresaService", value = "#{empresaService}")
	private EmpresaService empresaService;
	
	@ManagedProperty(name = "cnaeService", value = "#{cnaeService}")
	private CnaeService cnaeService;
	
	@ManagedProperty(name = "segmentoService", value = "#{segmentoService}")
	private SegmentoService segmentoService;
		
	private List<Empresa> empresas;
	
	private Integer     cnaeId;
	
	private List<Cnae>  cnaes;
	
	private List<Segmento> segmentos;
	
	private Integer idSegmento;
	
	private String tipoFiltro;
	
	private String filtro;	

	@PostConstruct
	public void init() {
		lista = empresaAssociadaService.all(getEmpresaSistema());
		empresas = empresaService.all(getEmpresaSistema());
		telefones = new ArrayList<String>();
		emails = new ArrayList<String>();
	}
	
	public String searchByFilters(){
		
		this.empresaAssociada = new EmpresaAssociada();
		
		if(this.tipoFiltro.equals("todos")){
			
			lista = empresaAssociadaService.all(getEmpresaSistema());
			return null;
		}
		
		if(this.tipoFiltro.equals("nomeFantasia") && (this.filtro != null && !this.filtro.isEmpty())){
			this.empresaAssociada.setNomeFantasia(filtro);
		}
		
		if(this.tipoFiltro.equals("razaoSocial") && (this.filtro != null && !this.filtro.isEmpty())){
			this.empresaAssociada.setRazaoSocial(filtro);
		}
		
		if(this.tipoFiltro.equals("email") && (this.filtro != null && !this.filtro.isEmpty())){
			this.empresaAssociada.setEmail(filtro);
		}
		
		if(this.tipoFiltro.equals("site") && (this.filtro != null && !this.filtro.isEmpty())){
			this.empresaAssociada.setSite(filtro);
		}
		
		if(this.tipoFiltro.equals("cnpj") && (this.filtro != null && !this.filtro.isEmpty())){
			this.empresaAssociada.setCnpj(filtro);
		}
		
		if(this.tipoFiltro.equals("responsavel") && (this.filtro != null && !this.filtro.isEmpty())){
			this.empresaAssociada.setResponsavel(filtro);
		}
				
		lista = empresaAssociadaService.searchByFilters(this.empresaAssociada,this.tipoFiltro);	
		
		return null;		
		
	}
	
	public void setListaCnaes(){
		
		this.cnaes = cnaeService.all(getEmpresaSistema());	
		
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
		
		if(telefone.trim().isEmpty()){
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Telefone Inválido!","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;
			
		}
		
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
	
	public String addEmail(){
		
		boolean isValidEmail = Validations.isValidEmailAddress(email.trim());
		
		if(!isValidEmail){
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Email Inválido!","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;
			
		}
		
		emails.add(email);
		email = "";		
		
		return null;
	}
	
	public String deleteEmail(){
		
		if(emails.contains(email)){
			emails.remove(email);
			email = "";
		}
		
		FacesMessage msg = new FacesMessage("Email excluído com sucesso!");
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
		
		emails = new ArrayList<String>();
		
		if(this.empresaAssociada.getEmail() != null && !this.empresaAssociada.getEmail().isEmpty()){
			
			String[] emailsArr = this.empresaAssociada.getEmail().split(";");
			
			for(int x =0;x< emailsArr.length;x++){
				emails.add(emailsArr[x]);
			}
						
		}
		
		this.cnaes	= cnaeService.all(getEmpresaSistema());		
		
		segmentos = segmentoService.all(getEmpresaSistema());

		return "prepareUpdate";
	}
	
	public String prepareInsert() {

		this.empresaAssociada = new EmpresaAssociada();
		
		this.empresaAssociada.setEndereco(new Endereco());
		
		this.empresaAssociada.setCnaes(new ArrayList<Cnae>());
				
		telefones = new ArrayList<String>();
		
		emails = new ArrayList<String>();
		
		empresas = empresaService.all(getEmpresaSistema());
		
		segmentos = segmentoService.all(getEmpresaSistema());
		
		System.out.println("EMPRESA:"+empresas.size());
		
		this.empresaAssociada.getEndereco().setEmpresaSistema(getEmpresaSistema());
			
		this.cnaes	= cnaeService.all(getEmpresaSistema());		

		return "prepareInsert";
	}

	public String create() {
		

		String str = "insert";
		
		Empresa empresa = empresaService.getById(empresaId,getEmpresaSistema());
		
		empresaAssociada.setEmpresa(empresa);
		
		Segmento segmento = segmentoService.getById(idSegmento, getEmpresaSistema());
		
		empresaAssociada.setSegmento(segmento);		
		
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
		
		if(emails != null && !emails.isEmpty()){
			
			String emailsUsuario = "";
			
			for(String tel : emails){
				
				emailsUsuario = emailsUsuario + tel;
				emailsUsuario = emailsUsuario + ";";
				
			}	
			
			empresaAssociada.setEmail(emailsUsuario);	
			
		}else{
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Adicione ao menos um email","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;
			
		}
		
		try {
			empresaAssociadaService.create(empresaAssociada,getEmpresaSistema());
		} catch (GenericException e1) {

		}
		
		FacesMessage msg = new FacesMessage("Empresa Associada Criada com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lista = empresaAssociadaService.all(getEmpresaSistema());

		try {

		} catch (Exception e) {
			str = "insertError";

		}

		return str;
	}

	public String update() {
		
		boolean isValidEmail = Validations.isValidEmailAddress(empresaAssociada.getEmail().trim());
		
		if(!isValidEmail){
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Email Inválido!","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;
			
		}

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
		
		if(emails != null && !emails.isEmpty()){
			
			String emailsUsuario = "";
			
			for(String tel : emails){
				
				emailsUsuario = emailsUsuario + tel;
				emailsUsuario = emailsUsuario + ";";
				
			}	
			
			empresaAssociada.setEmail(emailsUsuario);	
			
		}else{
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Adicione ao menos um email","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;
			
		}

		try {
			
			Segmento segmento = segmentoService.getById(idSegmento, getEmpresaSistema());
			
			empresaAssociada.setSegmento(segmento);	

			empresaAssociadaService.update(empresaAssociada);
			
			FacesMessage msg = new FacesMessage("Empresa Associada Atualizada com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			lista = empresaAssociadaService.all(getEmpresaSistema());

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
		
		lista = empresaAssociadaService.all(getEmpresaSistema());

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
		

	public List<EmpresaAssociada> getLista() {
		return lista;
	}

	public void setLista(List<EmpresaAssociada> lista) {
		this.lista = lista;
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

	public List<Segmento> getSegmentos() {
		return segmentos;
	}

	public void setSegmentos(List<Segmento> segmentos) {
		this.segmentos = segmentos;
	}

	public Integer getIdSegmento() {
		return idSegmento;
	}

	public void setIdSegmento(Integer idSegmento) {
		this.idSegmento = idSegmento;
	}

	public SegmentoService getSegmentoService() {
		return segmentoService;
	}

	public void setSegmentoService(SegmentoService segmentoService) {
		this.segmentoService = segmentoService;
	}

	public String getTipoFiltro() {
		return tipoFiltro;
	}

	public void setTipoFiltro(String tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
