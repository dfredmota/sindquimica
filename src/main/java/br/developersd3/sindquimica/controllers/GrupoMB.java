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

import br.developersd3.sindquimica.datatable.LazyGrupoDataModel;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Grupo;
import br.developersd3.sindquimica.models.Usuario;
import br.developersd3.sindquimica.service.GrupoService;
import br.developersd3.sindquimica.service.UsuarioService;

@ManagedBean(name = "grupoMB")
@SessionScoped
public class GrupoMB implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private LazyDataModel<Grupo> lazyModel;

	@ManagedProperty(value = "#{grupo}")
	private Grupo grupo;

	@ManagedProperty(name = "grupoService", value = "#{grupoService}")
	private GrupoService grupoService;
	
	@ManagedProperty(name = "usuarioService", value = "#{usuarioService}")
	private UsuarioService usuarioService;

	private Integer usuarioId;
	
	private Usuario usuario;
	
	private List<Usuario> usuarios;
	
	@PostConstruct
	public void init() {
		lazyModel = new LazyGrupoDataModel(grupoService.all());

	}
	
	public String addUsuario(){
		
		Usuario user = usuarioService.getById(usuarioId);
		
		if(!this.grupo.getUsuarios().contains(user)){
			this.grupo.getUsuarios().add(user);
		}else{
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Esse Usuário já existe na lista!", "Esse Usuário já existe na lista!"));
		}
		
		return null;
	}
	
	public String deleteUsuario(){
		
		Usuario user = usuarioService.getById(usuarioId);
		
		for(Usuario us : this.grupo.getUsuarios()){
			
			if(us.getId().equals(user.getId()));
			this.grupo.getUsuarios().remove(us);
			break;
		}
				
		return null;
	}

	public String prepareUpdate() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idSindicato = params.get("idGrupo");

		this.grupo = grupoService.getById(Integer.parseInt(idSindicato));
		
		if(this.grupo.getUsuarios() == null)
			this.grupo.setUsuarios(new ArrayList<Usuario>());
		
		this.usuarios = usuarioService.all();

		return "prepareUpdate";
	}
	
	public String prepareInsert() {

		this.grupo = new Grupo();
		
		this.grupo.setUsuarios(new ArrayList<Usuario>());
		
		this.usuarios = usuarioService.all();
		
		return "prepareInsert";
	}

	public String create() {

		String str = "insert";
		
		try {
			grupoService.create(grupo);
		} catch (GenericException e1) {

		}
		
		FacesMessage msg = new FacesMessage("Tipo de Documento Criado com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazyGrupoDataModel(grupoService.all());

		try {

		} catch (Exception e) {
			str = "insertError";

		}

		return str;
	}

	public String update() {

		String str = "update";

		try {

			grupoService.update(grupo);
			
			FacesMessage msg = new FacesMessage("Tipo de Documento Atualizado com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			lazyModel = new LazyGrupoDataModel(grupoService.all());

		} catch (Exception e) {
			str = "updateError";
		}

		return str;
	}

	public String delete() {

		String str = "delete";

		try {
			
		grupoService.delete(grupo);	
		
		FacesMessage msg = new FacesMessage("Tipo de Documento excluído com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazyGrupoDataModel(grupoService.all());

		} catch (Exception e) {
			str = "deleteError";

		}

		return str;
	}
	
	public void onRowSelect(SelectEvent event) {

		if (event.getObject() != null) {
			FacesMessage msg = new FacesMessage("Tipo Documento Selected:" + ((Grupo) event.getObject()).getId());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.grupo = ((Grupo) event.getObject());		

		}
	}	
	
	public LazyDataModel<Grupo> getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyDataModel<Grupo> lazyModel) {
		this.lazyModel = lazyModel;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public GrupoService getGrupoService() {
		return grupoService;
	}

	public void setGrupoService(GrupoService grupoService) {
		this.grupoService = grupoService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}	
	
}
