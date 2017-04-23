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
import br.developersd3.sindquimica.models.EmpresaAssociada;
import br.developersd3.sindquimica.models.Grupo;
import br.developersd3.sindquimica.models.Segmento;
import br.developersd3.sindquimica.models.Usuario;
import br.developersd3.sindquimica.service.CnaeService;
import br.developersd3.sindquimica.service.EmpresaAssociadaService;
import br.developersd3.sindquimica.service.GrupoService;
import br.developersd3.sindquimica.service.SegmentoService;
import br.developersd3.sindquimica.service.UsuarioService;
import br.developersd3.sindquimica.util.SessionUtils;

@ManagedBean(name = "grupoMB")
@SessionScoped
public class GrupoMB implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private List<Grupo> lista;

	@ManagedProperty(value = "#{grupo}")
	private Grupo grupo;

	@ManagedProperty(name = "grupoService", value = "#{grupoService}")
	private GrupoService grupoService;
	
	@ManagedProperty(name = "usuarioService", value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	@ManagedProperty(name = "empresaAssociadaService", value = "#{empresaAssociadaService}")
	private EmpresaAssociadaService empresaAssociadaService;
	
	@ManagedProperty(name = "segmentoService", value = "#{segmentoService}")
	private SegmentoService segmentoService;

	private Integer empresaAssociadaId;
	
	private EmpresaAssociada empresaAssociada;
	
	private List<EmpresaAssociada> empresasAssociadas;
	
	private Integer usuarioId;
	
	private Usuario usuario;
	
	private List<Usuario> usuarios;
	
	private Integer     cnaeId;
	
	private List<Cnae>  cnaes;
	
	private List<Segmento> segmentos;
	
	private Integer     segmentoId;
	
	private Segmento segmento;
	
	private String descricaoFiltro;
	
	@ManagedProperty(name = "cnaeService", value = "#{cnaeService}")
	private CnaeService cnaeService;
	
	@PostConstruct
	public void init() {
		lista = grupoService.all(getEmpresaSistema());
		this.cnaes = cnaeService.all(getEmpresaSistema());

	}
	
	public String searchByFilters(){
		
		this.grupo = new Grupo();
		
		this.grupo.setNome(descricaoFiltro);
		
		lista = grupoService.searchByFilters(this.grupo,null);		
		
		return null;
		
		
	}
	
	public String search(){
		
		if(this.cnaeId != null){
			
		List<EmpresaAssociada> listaEmpresa = empresaAssociadaService.findAllByCnae(cnaeId);	
			
		List<Integer> idsEmpresaCnaes = new ArrayList<Integer>();	
			
		List<Grupo> gruposCnaes = new ArrayList<Grupo>();
		
		if(listaEmpresa != null){
			
		for(EmpresaAssociada emp : listaEmpresa)
			idsEmpresaCnaes.add(emp.getId());
			
		
		for(Integer id : idsEmpresaCnaes){
			
			List<Grupo> lista = grupoService.findAllByEmpresaAssociada(id);
			
			if(lista != null && !lista.isEmpty()){
				
				for(Grupo gr : lista)				
				gruposCnaes.add(gr);
			}
			
		}
		
		lista = new ArrayList<Grupo>(gruposCnaes);
			
		}
			
		}
			
		return null;
	}
	
	public String addUsuario(){
		
		Usuario user = usuarioService.getById(usuarioId,getEmpresaSistema());
		
		if(!this.grupo.getUsuarios().contains(user)){
			
			user.setEmpresaSistema(getEmpresaSistema());
			this.grupo.getUsuarios().add(user);
		}else{
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Esse Usuário já existe na lista!", "Esse Usuário já existe na lista!"));
		}
		
		return null;
	}
	
	
	public String addEmpresaAssociada(){
		
		EmpresaAssociada empresa = empresaAssociadaService.getById(empresaAssociadaId,getEmpresaSistema());
		
		if(!this.grupo.getEmpresaAssociada().contains(empresa)){
			empresa.setEmpresaSistema(getEmpresaSistema());
			this.grupo.getEmpresaAssociada().add(empresa);
		}else{
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Essa Empresa Associada já existe na lista!", "Essa Empresa Associada já existe na lista!"));
		}
		
		return null;
	}
	
	public String addSegmento(){
		
		Segmento segmento = segmentoService.getById(segmentoId,getEmpresaSistema());
		
		if(!this.grupo.getSegmentos().contains(segmento)){
			this.grupo.getSegmentos().add(segmento);
		}else{
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Esse Segmento já existe na lista!", "Esse Segmento já existe na lista!"));
		}
		
		return null;
	}
	
	public String deleteSegmento(){
		
		Segmento segmento = segmentoService.getById(segmentoId,getEmpresaSistema());
		
		for(Segmento em : this.grupo.getSegmentos()){
			
			if(em.getId().equals(segmento.getId()));
			this.grupo.getSegmentos().remove(em);
			break;
		}
				
		return null;
	}
	
	public String deleteEmpresaAssociada(){
		
		EmpresaAssociada empresa = empresaAssociadaService.getById(empresaAssociadaId,getEmpresaSistema());
		
		for(EmpresaAssociada em : this.grupo.getEmpresaAssociada()){
			
			if(em.getId().equals(empresa.getId()));
			this.grupo.getEmpresaAssociada().remove(em);
			break;
		}
				
		return null;
	}
	
	public String deleteUsuario(){
		
		Usuario user = usuarioService.getById(usuarioId,getEmpresaSistema());
		
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

		this.grupo = grupoService.getById(Integer.parseInt(idSindicato),getEmpresaSistema());
		
		if(this.grupo.getUsuarios() == null)
			this.grupo.setUsuarios(new ArrayList<Usuario>());
		
		this.usuarios = usuarioService.all(getEmpresaSistema());
		this.empresasAssociadas = empresaAssociadaService.all(getEmpresaSistema());
		this.segmentos = segmentoService.all(getEmpresaSistema());

		return "prepareUpdate";
	}
	
	public String prepareInsert() {

		this.grupo = new Grupo();
		
		this.grupo.setUsuarios(new ArrayList<Usuario>());
		
		this.grupo.setEmpresaAssociada(new ArrayList<EmpresaAssociada>());
		
		this.grupo.setSegmentos(new ArrayList<Segmento>());
		
		this.usuarios = usuarioService.all(getEmpresaSistema());
		
		this.empresasAssociadas = empresaAssociadaService.all(getEmpresaSistema());
		
		this.segmentos = segmentoService.all(getEmpresaSistema());
		
		return "prepareInsert";
	}

	public String create() {

		String str = "insert";
		
		
		try {
			
			if(grupo.getEmpresaAssociada() == null)
				grupo.setEmpresaAssociada(new ArrayList<EmpresaAssociada>());
			
			if(grupo.getSegmentos() != null && !grupo.getSegmentos().isEmpty()){
				
				// recupera a lista de empresas com o segmento e adicona ao grupo
				
				for(Segmento seg : grupo.getSegmentos()){
					
					
				 List<EmpresaAssociada> lista = empresaAssociadaService.findAllBySegmento(seg.getId());
					
				 
				 if(lista != null && !lista.isEmpty()){
					 
					for(EmpresaAssociada emp : lista){
						 
						 if(!this.grupo.getEmpresaAssociada().contains(emp))
						 this.grupo.getEmpresaAssociada().add(emp);
						 
					 }					 
					 
				 }
					
					
					
				}
				
				
				
			}
			
			
			
			
			
			grupoService.create(grupo,getEmpresaSistema());
		
		
		} catch (GenericException e1) {

		}
		
		FacesMessage msg = new FacesMessage("Grupo Criado com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lista = grupoService.all(getEmpresaSistema());

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
			
			FacesMessage msg = new FacesMessage("Grupo Atualizado com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			lista = grupoService.all(getEmpresaSistema());

		} catch (Exception e) {
			str = "updateError";
		}

		return str;
	}

	public String delete() {

		String str = "delete";

		try {
			
		grupoService.delete(grupo);	
		
		FacesMessage msg = new FacesMessage("Grupo excluído com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lista = grupoService.all(getEmpresaSistema());

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

	public List<Grupo> getLista() {
		return lista;
	}

	public void setLista(List<Grupo> lista) {
		this.lista = lista;
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

	public EmpresaAssociadaService getEmpresaAssociadaService() {
		return empresaAssociadaService;
	}

	public void setEmpresaAssociadaService(EmpresaAssociadaService empresaAssociadaService) {
		this.empresaAssociadaService = empresaAssociadaService;
	}

	public Integer getEmpresaAssociadaId() {
		return empresaAssociadaId;
	}

	public void setEmpresaAssociadaId(Integer empresaAssociadaId) {
		this.empresaAssociadaId = empresaAssociadaId;
	}

	public EmpresaAssociada getEmpresaAssociada() {
		return empresaAssociada;
	}

	public void setEmpresaAssociada(EmpresaAssociada empresaAssociada) {
		this.empresaAssociada = empresaAssociada;
	}

	public List<EmpresaAssociada> getEmpresasAssociadas() {
		return empresasAssociadas;
	}

	public void setEmpresasAssociadas(List<EmpresaAssociada> empresasAssociadas) {
		this.empresasAssociadas = empresasAssociadas;
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

	public SegmentoService getSegmentoService() {
		return segmentoService;
	}

	public void setSegmentoService(SegmentoService segmentoService) {
		this.segmentoService = segmentoService;
	}

	public List<Segmento> getSegmentos() {
		return segmentos;
	}

	public void setSegmentos(List<Segmento> segmentos) {
		this.segmentos = segmentos;
	}

	public Integer getSegmentoId() {
		return segmentoId;
	}

	public void setSegmentoId(Integer segmentoId) {
		this.segmentoId = segmentoId;
	}
	
	public Segmento getSegmento() {
		return segmento;
	}

	public void setSegmento(Segmento segmento) {
		this.segmento = segmento;
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
