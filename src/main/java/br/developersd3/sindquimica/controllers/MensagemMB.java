package br.developersd3.sindquimica.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.UploadedFile;

import br.developersd3.sindquimica.datatable.LazyMensagemDataModel;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Grupo;
import br.developersd3.sindquimica.models.Mensagem;
import br.developersd3.sindquimica.models.Usuario;
import br.developersd3.sindquimica.service.GrupoService;
import br.developersd3.sindquimica.service.MensagemService;
import br.developersd3.sindquimica.service.UsuarioService;
import br.developersd3.sindquimica.util.SessionUtils;

@ManagedBean(name = "mensagemMB")
@RequestScoped
public class MensagemMB implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private LazyDataModel<Mensagem> lazyModel;

	@ManagedProperty(value = "#{mensagem}")
	private Mensagem mensagem;

	@ManagedProperty(name = "mensagemService", value = "#{mensagemService}")
	private MensagemService mensagemService;
	
	@ManagedProperty(name = "usuarioService", value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	@ManagedProperty(name = "grupoService", value = "#{grupoService}")
	private GrupoService grupoService;
	
	private List<Usuario> usuarios;
		
    private Usuario selectedUsuario;
    private List<Usuario> selectedUsuarios;
    
	private List<Grupo> grupos;
	
    private Grupo selectedGrupo;
    private List<Grupo> selectedGrupos;
    
    private List<Mensagem> mensagens;
    
    private Panel panel;
    
    private UploadedFile file;
    
    DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
    
    private DefaultStreamedContent imagem;
    
    private Map<String,DefaultStreamedContent> mapImages;
    
	@PostConstruct
	public void init() {
		lazyModel = new LazyMensagemDataModel(mensagemService.all(getEmpresaSistema()));
		
		mapImages = new HashMap<String,DefaultStreamedContent>();
				
		montaImagens();
		
		montaTimeLine();    

	}
	
	private void montaImagens(){
		
		List<Mensagem> lista = mensagemService.all(getEmpresaSistema());
		
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String dir = servletContext.getRealPath("/")+"/images/";
		
		if(lista != null && !lista.isEmpty()){
			
		for(Mensagem m : lista){	
			
		DefaultStreamedContent	imagem = null;	
			
		if(m.getFileName() != null && !m.getFileName().isEmpty()){
		try {
		imagem = new DefaultStreamedContent(new FileInputStream(new File(dir, m.getFileName())));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
			
		}	
		
		mapImages.put(m.getId().toString(), imagem);
				
		}
			
		}
		
		
	}
	
	public void montaTimeLine(){
		
		 usuarios = usuarioService.all(getEmpresaSistema());
	     
	     grupos   = grupoService.all(getEmpresaSistema());
	     
	     this.mensagem = new Mensagem();
	     
	     panel = new Panel();
	     
	     panel.setStyle("border:0px");
	     
	     this.mensagens = mensagemService.all(getEmpresaSistema());	     
	     
	     Application app = FacesContext.getCurrentInstance().getApplication();
	     
	     if(mensagens != null && !mensagens.isEmpty()){
	    	 
	    	 
	    	 for(Mensagem msg : mensagens){
	    		 
	    		 Fieldset field = new Fieldset();
	    		 
	    		 field.setLegend(msg.getUsuario().getNome()+" -  "+df.format(msg.getCreatedAt()));
	    		 	          
	    	     PanelGrid grid = new PanelGrid();
	    	     
	    	     grid.setColumns(2);
	    	     
	    	     grid.setStyle("border:0px");  
	    	     
	    	     HtmlOutputLabel labelMsg = new HtmlOutputLabel();
	    	     labelMsg.setValue(msg.getConteudo());
	    	     
	    	     labelMsg.setStyle("border:0px");	    	    
	    	     	    	     
	    	     // verifica se a mensagem possui imagem
	    	     if(msg.getFileName() != null && !msg.getFileName().isEmpty()){
	    	    	 
	    	      GraphicImage image = new GraphicImage(); 
	    	    	 
	    	      image.setWidth("160px");
	    	      
	    	      //image.setValueExpression("value", createValueExpression("#{mensagemMB.image('"+msg.getFileName()+"')}", String.class));  
	    	      
	    	      image.setValueBinding("value",app.createValueBinding("#{mensagemMB.mapImages['"+msg.getId().toString()+"']}"));
	    	      
	    	      image.setCache(false);
	    	      
	    	      UIParameter param = new UIParameter();
	    	      param.setName("filename");
	    	      param.setValue(msg.getFileName());
	    	      image.getChildren().add(param);
	    	      
	    	      grid.getChildren().add(image);
	    	    	 	    	    	 
	    	     }	    
	    	     
	    	     grid.getChildren().add(labelMsg);
	    	     	    	     
	    	     field.getChildren().add(grid);
	    	     
	    	     
	    	     panel.getChildren().add(field);  		 
	    		 
	    	     
	    	     HtmlOutputLabel br = new HtmlOutputLabel();
	    	     br.setValue("                             "
	    	     		+ "     ");
	    	     
	    	     panel.getChildren().add(br);
	    	     
	    	 }
	    	     	 
	    	 
	     }
		
	}
	
	
	public void onSelect(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
    }
     
    public void onUnselect(UnselectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
    }
     
    public void onReorder() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
    } 

	public String prepareUpdate() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idSindicato = params.get("idMensagem");

		this.mensagem = mensagemService.getById(Integer.parseInt(idSindicato),getEmpresaSistema());

		return "prepareUpdate";
	}
	

	public String postMessage() {

		String str = "sendMessageOK";
		
		if(getSelectedGrupos() !=null)		
		mensagem.setGrupos(getSelectedGrupos());
		
		if(getSelectedUsuarios() !=null)
		mensagem.setUsuarios(getSelectedUsuarios());
		
		if (file != null && !file.getFileName().isEmpty()) {       	        	

			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	        
			String dir = servletContext.getRealPath("/")+"/images/";
			
			// /appservers/images/
			// /home/fred/images/
			File file1 = new File(dir, this.mensagem.getConteudo().substring(0,2) + "_" + file.getFileName());
				
			this.mensagem.setFileName(this.mensagem.getConteudo().substring(0,2) + "_" + file.getFileName());
			
			try {
				FileOutputStream fos = new FileOutputStream(file1);
				fos.write(file.getContents());
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		
		
		// recupera usuario logado
		
		HttpSession session = SessionUtils.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		
		Usuario usuarioSessao = usuarioService.getById(userId,getEmpresaSistema());
		
		mensagem.setUsuario(usuarioSessao);
		
		mensagem.setEmpresaSistema(getEmpresaSistema());
			
		try {
			mensagemService.create(mensagem,getEmpresaSistema());
		} catch (GenericException e1) {

		}
		
		FacesMessage msg = new FacesMessage("Mensagem Criada com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazyMensagemDataModel(mensagemService.all(getEmpresaSistema()));
		
		mensagens = mensagemService.all(getEmpresaSistema());
		
		setSelectedUsuarios(new ArrayList<Usuario>());
		setSelectedGrupos(new ArrayList<Grupo>());
		
		this.mensagem = new Mensagem();

		try {

		} catch (Exception e) {
			str = "sendMessageError";

		}
		
		montaImagens();
		
		montaTimeLine();

		return str;
	}

	public String update() {

		String str = "update";

		try {

			mensagemService.update(mensagem);
			
			FacesMessage msg = new FacesMessage("Mensagem Atualizada com sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			lazyModel = new LazyMensagemDataModel(mensagemService.all(getEmpresaSistema()));

		} catch (Exception e) {
			str = "updateError";
		}

		return str;
	}

	public String delete() {

		String str = "delete";

		try {
			
		mensagemService.delete(mensagem);	
		
		FacesMessage msg = new FacesMessage("Mensagem excluído com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		lazyModel = new LazyMensagemDataModel(mensagemService.all(getEmpresaSistema()));

		} catch (Exception e) {
			str = "deleteError";

		}

		return str;
	}
	
	private static ValueExpression createValueExpression(String valueExpression, Class<?> valueType) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    return context.getApplication().getExpressionFactory()
	        .createValueExpression(context.getELContext(), valueExpression, valueType);
	}
	
		
	public LazyDataModel<Mensagem> getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyDataModel<Mensagem> lazyModel) {
		this.lazyModel = lazyModel;
	}

	public Mensagem getMensagem() {
		return mensagem;
	}

	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}

	public MensagemService getMensagemService() {
		return mensagemService;
	}

	public void setMensagemService(MensagemService mensagemService) {
		this.mensagemService = mensagemService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public Usuario getSelectedUsuario() {
		return selectedUsuario;
	}

	public void setSelectedUsuario(Usuario selectedUsuario) {
		this.selectedUsuario = selectedUsuario;
	}

	public List<Usuario> getSelectedUsuarios() {
		return selectedUsuarios;
	}

	public void setSelectedUsuarios(List<Usuario> selectedUsuarios) {
		this.selectedUsuarios = selectedUsuarios;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public GrupoService getGrupoService() {
		return grupoService;
	}

	public void setGrupoService(GrupoService grupoService) {
		this.grupoService = grupoService;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public Grupo getSelectedGrupo() {
		return selectedGrupo;
	}

	public void setSelectedGrupo(Grupo selectedGrupo) {
		this.selectedGrupo = selectedGrupo;
	}

	public List<Grupo> getSelectedGrupos() {
		return selectedGrupos;
	}

	public void setSelectedGrupos(List<Grupo> selectedGrupos) {
		this.selectedGrupos = selectedGrupos;
	}

	public Panel getPanel() {
		return panel;
	}

	public void setPanel(Panel panel) {
		this.panel = panel;
	}

	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<Mensagem> mensagens) {
		this.mensagens = mensagens;
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

	public DefaultStreamedContent getImagem() {
		return imagem;
	}

	public void setImagem(DefaultStreamedContent imagem) {
		this.imagem = imagem;
	}

	public Map<String, DefaultStreamedContent> getMapImages() {
		return mapImages;
	}

	public void setMapImages(Map<String, DefaultStreamedContent> mapImages) {
		this.mapImages = mapImages;
	}	

}
