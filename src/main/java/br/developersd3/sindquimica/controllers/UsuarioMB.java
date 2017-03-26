package br.developersd3.sindquimica.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import javax.faces.event.PhaseId;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Documento;
import br.developersd3.sindquimica.models.EmpresaAssociada;
import br.developersd3.sindquimica.models.Endereco;
import br.developersd3.sindquimica.models.Perfil;
import br.developersd3.sindquimica.models.TipoDocumento;
import br.developersd3.sindquimica.models.Usuario;
import br.developersd3.sindquimica.service.DocumentoService;
import br.developersd3.sindquimica.service.EmpresaAssociadaService;
import br.developersd3.sindquimica.service.PerfilService;
import br.developersd3.sindquimica.service.TipoDocumentoService;
import br.developersd3.sindquimica.service.UsuarioService;
import br.developersd3.sindquimica.util.SessionUtils;
import br.developersd3.sindquimica.util.Validations;

@ManagedBean(name = "usuarioMB")
@SessionScoped
public class UsuarioMB implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private List<Usuario> lista;

	@ManagedProperty(value = "#{usuario}")
	private Usuario usuario;
	
	private Integer usuarioId;
	
	private Integer empresaAssociadaId;
	
	private String  telefone;
	
	private List<String> telefones;
	
	private Boolean  isAdm;
	
	@ManagedProperty(name = "usuarioService", value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	@ManagedProperty(name = "empresaAssociadaService", value = "#{empresaAssociadaService}")
	private EmpresaAssociadaService empresaAssociadaService;
	
	@ManagedProperty(name = "perfilService", value = "#{perfilService}")
	private PerfilService perfilService;
	
	private List<EmpresaAssociada> listaDeEmpresasAssociadas;
	
	private List<Documento> listDeDocumentos;
	
	private List<TipoDocumento> listaTipoDeDocumentos;
	
	private Documento documento;
	
	@ManagedProperty(name = "tipoDocumentoService", value = "#{tipoDocumentoService}")
	private TipoDocumentoService tipoDocumentoService;

	@ManagedProperty(name = "documentoService", value = "#{documentoService}")
	private DocumentoService documentoService;
	
	private UploadedFile file;
	
	private List<Perfil> listaPerfil;
	
	private String tipoFiltro;
	
	private String filtro;
	
	@PostConstruct
	public void init() {
		lista = usuarioService.all(getEmpresaSistema());
		listaDeEmpresasAssociadas = empresaAssociadaService.all(getEmpresaSistema());
		telefones = new ArrayList<String>();
	}
	
	public String searchByFilters(){
						
		this.usuario = new Usuario();
		
		if(this.tipoFiltro.equals("todos")){
			
			lista = usuarioService.all(getEmpresaSistema());
			return null;
		}
		
		if(this.tipoFiltro.equals("nome") && (this.filtro != null && !this.filtro.isEmpty())){
			this.usuario.setNome(filtro);
		}
		
		if(this.tipoFiltro.equals("email") && (this.filtro != null && !this.filtro.isEmpty())){
			this.usuario.setEmail(filtro);
		}
		
		if(this.tipoFiltro.equals("site") && (this.filtro != null && !this.filtro.isEmpty())){
			this.usuario.setSite(filtro);
		}
		
				
		lista = usuarioService.searchByFilters(this.usuario,this.tipoFiltro);	
		
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
	
	public String addDocumento(){
		
		TipoDocumento tipo = tipoDocumentoService.getById(documento.getTipo().getId(),getEmpresaSistema());
		
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

		this.usuario = usuarioService.getById(Integer.parseInt(idUsuario),getEmpresaSistema());
		
		listDeDocumentos = documentoService.findAllByUsuario(this.usuario.getId());
		
		documento = new Documento();
		documento.setTipo(new TipoDocumento());
		
		listaTipoDeDocumentos = tipoDocumentoService.all(getEmpresaSistema());
		
		telefones = new ArrayList<String>();
		
		if(this.usuario.getTelefones() != null && !this.usuario.getTelefones().isEmpty()){
			
			String[] telefonesArr = this.usuario.getTelefones().split(";");
			
			for(int x =0;x< telefonesArr.length;x++){
				telefones.add(telefonesArr[x]);
			}
						
		}
		
		this.listaPerfil = perfilService.all(getEmpresaSistema());
		
		if(getPerfilSindicato()){
			
			for(Perfil perfil : this.listaPerfil){
				
				if(perfil.getId() == 1){
					this.listaPerfil.remove(perfil);
					break;
				}
					
			}
			
			
		}

		return "prepareUpdate";
	}
	
	public String prepareInsert() {

		this.usuario = new Usuario();
		
		this.usuario.setEmpresa(new EmpresaAssociada());
		this.usuario.setEndereco(new Endereco());
		
		this.usuario.getEndereco().setEmpresaSistema(getEmpresaSistema());
		
		listaTipoDeDocumentos = tipoDocumentoService.all(getEmpresaSistema());
		
		listDeDocumentos = new ArrayList<Documento>();
		
		telefones = new ArrayList<String>();
		
		documento = new Documento();
		documento.setTipo(new TipoDocumento());
		
		listaDeEmpresasAssociadas = empresaAssociadaService.all(getEmpresaSistema());
		
		this.usuario.setPerfil(new Perfil());
		
		this.listaPerfil = perfilService.all(getEmpresaSistema());
		
		
		if(getPerfilSindicato()){
			
			for(Perfil perfil : this.listaPerfil){
				
				if(perfil.getId() == 1){
					this.listaPerfil.remove(perfil);
					break;
				}
					
			}
			
			
		}

		return "prepareInsert";
	}
	
	
	public String liberarUsuario(){
		
		this.usuario.setStatus(true);
		
		try {
			usuarioService.update(this.usuario);
			FacesMessage msg = new FacesMessage("Usuário Liberado com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (GenericException e) {
			e.printStackTrace();
		}	
		
		return null;
	}
	
	public boolean getPerfilAdm(){
		
	if(getPerfilUsuario() != null){	
	 if(getPerfilUsuario().equalsIgnoreCase("ADM"))
		 return true;
	 else
		 return false;
	}
	
	return false;
		
	}
	
	public boolean getPerfilSindicato(){
		
		 if(getPerfilUsuario().equalsIgnoreCase("ADMSINDICATO"))
			 return true;
		 else
			 return false;	
	}	
	
	
	public boolean getBtnLiberado(){
		
		 if(getPerfilUsuario().equalsIgnoreCase("ADMSINDICATO") && (this.usuario.getStatus() == null || 
				 this.usuario.getStatus() == false))
			 return true;
		 else
			 return false;	
	}
	
	public StreamedContent getImage() throws IOException {
	    FacesContext context = FacesContext.getCurrentInstance();

	    if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
	        // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
	        return new DefaultStreamedContent();
	    }
	    else {
	        // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
	    	
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	        String dir = servletContext.getRealPath("/")+"/images/";
	    	
	        return new DefaultStreamedContent(new FileInputStream(new File(dir, this.usuario.getImagemPath())));
	    }
	}

	public String create() {
		
		boolean isValidEmail = Validations.isValidEmailAddress(usuario.getEmail().trim());
		
		if(!isValidEmail){
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Email Inválido!","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;
			
		}

		String str = "insert";
		
		EmpresaAssociada empresa = empresaAssociadaService.getById(empresaAssociadaId,getEmpresaSistema());
		
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
		
		
		if (file != null && !file.getFileName().isEmpty()) {       	        	

			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	        String dir = servletContext.getRealPath("/");
			
			File file1 = new File(dir+"/images/", usuario.getNome() + "_" + file.getFileName());
			this.usuario.setImagemPath(usuario.getNome() + "_" + file.getFileName());
			try {
				FileOutputStream fos = new FileOutputStream(file1);
				fos.write(file.getContents());
				fos.close();

				FacesContext instance = FacesContext.getCurrentInstance();
				instance.addMessage("mensagens", new FacesMessage(FacesMessage.SEVERITY_INFO,
						file.getFileName() + " anexado com sucesso", null));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		try {
		
		usuario = usuarioService.create(usuario,getEmpresaSistema());
		
		// gravar lista de documentos
		if(listDeDocumentos != null && !listDeDocumentos.isEmpty()){
			
			for(Documento doc: listDeDocumentos){
				
				doc.setUsuario(usuario);
				
				documentoService.create(doc,getEmpresaSistema());
								
			}
				
		}
			
		
		} catch (GenericException e1) {

		}
		
		FacesMessage msg = new FacesMessage("Usuário Criado com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lista = usuarioService.all(getEmpresaSistema());

		try {

		} catch (Exception e) {
			str = "insertError";

		}

		return str;
	}

	public String update() {

		String str = "update";
		
		EmpresaAssociada empresa = empresaAssociadaService.getById(empresaAssociadaId,getEmpresaSistema());
		
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
				documentoService.create(doc,getEmpresaSistema());
								
			}
				
		}
			
		FacesMessage msg = new FacesMessage("Usuário Atualizado com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
			
		lista = usuarioService.all(getEmpresaSistema());

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
		
		lista = usuarioService.all(getEmpresaSistema());

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

	public List<Usuario> getLista() {
		return lista;
	}

	public void setLista(List<Usuario> lista) {
		this.lista = lista;
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

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	private Integer getEmpresaSistema(){
		HttpSession session = SessionUtils.getSession();
		Integer empresaSistemaId = (Integer)session.getAttribute("empresaSistemaId");
		
		return empresaSistemaId;
	}
	
	public Boolean getIsAdm() {
		return isAdm;
	}

	public void setIsAdm(Boolean isAdm) {
		this.isAdm = isAdm;
	}

	public List<Perfil> getListaPerfil() {
		return listaPerfil;
	}

	public void setListaPerfil(List<Perfil> listaPerfil) {
		this.listaPerfil = listaPerfil;
	}

	public PerfilService getPerfilService() {
		return perfilService;
	}

	public void setPerfilService(PerfilService perfilService) {
		this.perfilService = perfilService;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public String getTipoFiltro() {
		return tipoFiltro;
	}

	public void setTipoFiltro(String tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}

	private Integer getUsuarioSistema(){
		HttpSession session = SessionUtils.getSession();
		Integer empresaSistemaId = (Integer)session.getAttribute("userId");
		
		return empresaSistemaId;
	}
	
	public String getPerfilUsuario(){
		HttpSession session = SessionUtils.getSession();
		String perfil = (String)session.getAttribute("perfil");
		
		return perfil;
	}
	
}
