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

import br.developersd3.sindquimica.datatable.LazyUsuarioDataModel;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Documento;
import br.developersd3.sindquimica.models.EmpresaAssociada;
import br.developersd3.sindquimica.models.Endereco;
import br.developersd3.sindquimica.models.TipoDocumento;
import br.developersd3.sindquimica.models.Usuario;
import br.developersd3.sindquimica.service.DocumentoService;
import br.developersd3.sindquimica.service.EmpresaAssociadaService;
import br.developersd3.sindquimica.service.TipoDocumentoService;
import br.developersd3.sindquimica.service.UsuarioService;

@ManagedBean(name = "usuarioMB")
@SessionScoped
public class UsuarioMB implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private LazyDataModel<Usuario> lazyModel;

	@ManagedProperty(value = "#{usuario}")
	private Usuario usuario;
	
	private Integer usuarioId;
	
	private Integer empresaAssociadaId;
	
	private String  telefone;
	
	private List<String> telefones;

	@ManagedProperty(name = "usuarioService", value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	@ManagedProperty(name = "empresaAssociadaService", value = "#{empresaAssociadaService}")
	private EmpresaAssociadaService empresaAssociadaService;
	
	private List<EmpresaAssociada> listaDeEmpresasAssociadas;
	
	private List<Documento> listDeDocumentos;
	
	private List<TipoDocumento> listaTipoDeDocumentos;
	
	private Documento documento;
	
	@ManagedProperty(name = "tipoDocumentoService", value = "#{tipoDocumentoService}")
	private TipoDocumentoService tipoDocumentoService;

	@ManagedProperty(name = "documentoService", value = "#{documentoService}")
	private DocumentoService documentoService;
	
	@PostConstruct
	public void init() {
		lazyModel = new LazyUsuarioDataModel(usuarioService.all());
		listaDeEmpresasAssociadas = empresaAssociadaService.all();
		telefones = new ArrayList<String>();
	}
	
	public String addTelefone(){
		
		telefones.add(telefone);
		telefone = "";		
		
		return null;
	}
	
	public String addDocumento(){
		
		TipoDocumento tipo = tipoDocumentoService.getById(documento.getTipo().getId());
		
		documento.setTipo(tipo);
		
		listDeDocumentos.add(documento);
		documento = new Documento();
		documento.setTipo(new TipoDocumento());	
		
		return null;
	}
	
	public String deleteDocumento(){
		
		if(documento.getId() == null){
			listDeDocumentos.remove(documento);
			documento.setTipo(new TipoDocumento());
			documento.setValor("");
		}
		else{
			try {
				documentoService.delete(documento);
				listDeDocumentos.remove(documento);
			} catch (GenericException e) {
				e.printStackTrace();
			}
		}
		
		FacesMessage msg = new FacesMessage("Documento excluído com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
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
		String idUsuario = params.get("idUsuario");

		this.usuario = usuarioService.getById(Integer.parseInt(idUsuario));
		
		listDeDocumentos = documentoService.findAllByUsuario(this.usuario.getId());
		
		documento = new Documento();
		documento.setTipo(new TipoDocumento());
		
		listaTipoDeDocumentos = tipoDocumentoService.all();
		
		telefones = new ArrayList<String>();
		
		if(this.usuario.getTelefones() != null && !this.usuario.getTelefones().isEmpty()){
			
			String[] telefonesArr = this.usuario.getTelefones().split(";");
			
			for(int x =0;x< telefonesArr.length;x++){
				telefones.add(telefonesArr[x]);
			}
						
		}

		return "prepareUpdate";
	}
	
	public String prepareInsert() {

		this.usuario = new Usuario();
		
		this.usuario.setEmpresa(new EmpresaAssociada());
		this.usuario.setEndereco(new Endereco());
		
		listaTipoDeDocumentos = tipoDocumentoService.all();
		
		listDeDocumentos = new ArrayList<Documento>();
		
		telefones = new ArrayList<String>();
		
		documento = new Documento();
		documento.setTipo(new TipoDocumento());
		
		listaDeEmpresasAssociadas = empresaAssociadaService.all();

		return "prepareInsert";
	}

	public String create() {

		String str = "insert";
		
		EmpresaAssociada empresa = empresaAssociadaService.getById(empresaAssociadaId);
		
		usuario.setEmpresa(empresa);
				
		// recupera os documentos
		
		if(telefones != null && !telefones.isEmpty()){
			
			String telefonesUsuario = "";
			
			for(String tel : telefones){
				
				telefonesUsuario = telefonesUsuario + tel;
				telefonesUsuario = telefonesUsuario + ";";
				
			}	
			
			usuario.setTelefones(telefonesUsuario);			
		}else{
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Adicione ao menos um telefone","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;
			
		}
			
		if(usuario.getStatus() == null)
			usuario.setStatus(false);
		
		try {
		
		usuario = usuarioService.create(usuario);
		
		// gravar lista de documentos
		if(listDeDocumentos != null && !listDeDocumentos.isEmpty()){
			
			for(Documento doc: listDeDocumentos){
				
				doc.setUsuario(usuario);
				
				documentoService.create(doc);
								
			}
				
		}
			
		
		} catch (GenericException e1) {

		}
		
		FacesMessage msg = new FacesMessage("Usuário Criado com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazyUsuarioDataModel(usuarioService.all());

		try {

		} catch (Exception e) {
			str = "insertError";

		}

		return str;
	}

	public String update() {

		String str = "update";
		
		EmpresaAssociada empresa = empresaAssociadaService.getById(empresaAssociadaId);
		
		usuario.setEmpresa(empresa);
		
		if(telefones != null && !telefones.isEmpty()){
			
			String telefonesUsuario = "";
			
			for(String tel : telefones){
				
				telefonesUsuario = telefonesUsuario + tel;
				telefonesUsuario = telefonesUsuario + ";";
				
			}	
			
			usuario.setTelefones(telefonesUsuario);			
		}else{
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Adicione ao menos um telefone","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;
			
		}

		try {

		usuarioService.update(usuario);
		
		
		// gravar lista de documentos
		if(listDeDocumentos != null && !listDeDocumentos.isEmpty()){
			
			for(Documento doc: listDeDocumentos){
				
				doc.setUsuario(usuario);
				
				if(doc.getId() == null)
				documentoService.create(doc);
								
			}
				
		}
			
		FacesMessage msg = new FacesMessage("Usuário Atualizado com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
			
		lazyModel = new LazyUsuarioDataModel(usuarioService.all());

		} catch (Exception e) {
			str = "updateError";
		}

		return str;
	}

	public String delete() {

		String str = "delete";

		try {
			
		usuarioService.delete(usuario);	
		
		FacesMessage msg = new FacesMessage("Usuario excluído com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazyUsuarioDataModel(usuarioService.all());

		} catch (Exception e) {
			str = "deleteError";

		}

		return str;
	}
	
	public void onRowSelect(SelectEvent event) {

		if (event.getObject() != null) {
			FacesMessage msg = new FacesMessage("Usuario Selected:" + ((Usuario) event.getObject()).getId());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.usuario = ((Usuario) event.getObject());		

		}
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

	public LazyDataModel<Usuario> getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyDataModel<Usuario> lazyModel) {
		this.lazyModel = lazyModel;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Integer getEmpresaAssociadaId() {
		return empresaAssociadaId;
	}

	public void setEmpresaAssociadaId(Integer empresaAssociadaId) {
		this.empresaAssociadaId = empresaAssociadaId;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public List<EmpresaAssociada> getListaDeEmpresasAssociadas() {
		return listaDeEmpresasAssociadas;
	}

	public void setListaDeEmpresasAssociadas(List<EmpresaAssociada> listaDeEmpresasAssociadas) {
		this.listaDeEmpresasAssociadas = listaDeEmpresasAssociadas;
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

	public List<Documento> getListDeDocumentos() {
		return listDeDocumentos;
	}

	public void setListDeDocumentos(List<Documento> listDeDocumentos) {
		this.listDeDocumentos = listDeDocumentos;
	}

	public List<TipoDocumento> getListaTipoDeDocumentos() {
		return listaTipoDeDocumentos;
	}

	public void setListaTipoDeDocumentos(List<TipoDocumento> listaTipoDeDocumentos) {
		this.listaTipoDeDocumentos = listaTipoDeDocumentos;
	}

	public TipoDocumentoService getTipoDocumentoService() {
		return tipoDocumentoService;
	}

	public void setTipoDocumentoService(TipoDocumentoService tipoDocumentoService) {
		this.tipoDocumentoService = tipoDocumentoService;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public DocumentoService getDocumentoService() {
		return documentoService;
	}

	public void setDocumentoService(DocumentoService documentoService) {
		this.documentoService = documentoService;
	}	
		
}
