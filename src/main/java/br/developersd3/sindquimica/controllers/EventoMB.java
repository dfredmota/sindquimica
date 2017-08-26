package br.developersd3.sindquimica.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.UploadedFile;

import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Evento;
import br.developersd3.sindquimica.models.Grupo;
import br.developersd3.sindquimica.models.ParticipanteEvento;
import br.developersd3.sindquimica.models.Usuario;
import br.developersd3.sindquimica.service.EventoService;
import br.developersd3.sindquimica.service.GrupoService;
import br.developersd3.sindquimica.service.ParticipanteEventoService;
import br.developersd3.sindquimica.service.UsuarioService;
import br.developersd3.sindquimica.util.DataConnect;
import br.developersd3.sindquimica.util.SessionUtils;
import br.developersd3.sindquimica.util.Validations;

@ManagedBean(name = "eventoMB")
@SessionScoped
public class EventoMB implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;
 
    private ScheduleModel lazyEventModel;
 
    private ScheduleEvent event = new DefaultScheduleEvent();
    
    private ScheduleModel eventModel;
    
    private Evento evento;
    
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    @ManagedProperty(name = "eventoService", value = "#{eventoService}")
    private EventoService eventoService;
    
	@ManagedProperty(name = "usuarioService", value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	@ManagedProperty(name = "grupoService", value = "#{grupoService}")
	private GrupoService grupoService;
	
	@ManagedProperty(name = "participanteEventoService", value = "#{participanteEventoService}")
	private ParticipanteEventoService participanteEventoService;
	
	private List<Usuario> usuarios;
		
    private Usuario selectedUsuario;
    
    private List<Usuario> selectedUsuarios;
    
	private List<Grupo> grupos;
	
    private Grupo selectedGrupo;
    private List<Grupo> selectedGrupos;
    
    private List<ParticipanteEvento> participantes;
    
    private ParticipanteEvento participante;
    
    private List<Evento> eventos;
    
    private UploadedFile file;
 
    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        
        this.eventos = eventoService.all(getEmpresaSistema());
        
        if(this.eventos != null){
        	
        for(Evento ev : this.eventos){
        	
       	DefaultScheduleEvent eventoDB = new DefaultScheduleEvent(ev.getDescricao(), ev.getInicio(), ev.getFim());
       	
       	eventoDB.setDescription(ev.getId().toString());
        	
       	if(ev.getStatus() == null || ev.getStatus())    	
        eventModel.addEvent(eventoDB);	        	
        	
        }
        	
        }
        
        
         setSelectedUsuarios(new ArrayList<Usuario>());
         
         setSelectedGrupos(new ArrayList<Grupo>());
        
		 usuarios = usuarioService.all(getEmpresaSistema());
	     
	     grupos   = grupoService.all(getEmpresaSistema());
	     
	     this.participante = new ParticipanteEvento();
	     
	     this.evento = new Evento();
	     
	     this.evento.setParticipantes(new ArrayList<ParticipanteEvento>());
	     
	     this.participantes = new ArrayList<ParticipanteEvento>();
    }
    
    private void inicializaEvents(){
    	
        eventModel = new DefaultScheduleModel();
        
        this.eventos = eventoService.all(getEmpresaSistema());
        
        if(this.eventos != null){
        	
        for(Evento ev : this.eventos){
        	
       	DefaultScheduleEvent eventoDB = new DefaultScheduleEvent(ev.getDescricao(), ev.getInicio(), ev.getFim());
       	
       	eventoDB.setDescription(ev.getId().toString());
        	
       	if(ev.getStatus() == null || ev.getStatus())    	
        eventModel.addEvent(eventoDB);	        	
        	
        }
        	
        }        
        
         setSelectedUsuarios(new ArrayList<Usuario>());
         
         setSelectedGrupos(new ArrayList<Grupo>());
        
		 usuarios = usuarioService.all(getEmpresaSistema());
	     
	     grupos   = grupoService.all(getEmpresaSistema());
	     
	     this.participante = new ParticipanteEvento();
	     
	     this.evento = new Evento();
	     
	     this.evento.setParticipantes(new ArrayList<ParticipanteEvento>());
	     
	     this.participantes = new ArrayList<ParticipanteEvento>();
    	
    }
    
    
    public String addParticipante(){
    	
    	boolean jaExiste = false;
    	
    	for(ParticipanteEvento p : this.getParticipantes()){
    		
    		if(p.getNome().equals(participante.getNome())){
    			
    			jaExiste = true;
    			break;
    		}
    		
    	}
    	
    	if(!jaExiste)    	{
    	
    	boolean isValidEmail = Validations.isValidEmailAddress(participante.getEmail().trim());	
    		
    	if(!isValidEmail){
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Email Inválido!","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;
			
		}	
    	
    	this.getParticipantes().add(participante); 
    	
    	}else{
    		
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Participante já consta na lista!","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;	
    		
    	}
    	
    	
    	participante = new ParticipanteEvento();
    	
    	return null;
    }
    
    public String deleteParticipante(){
    	
    	for(ParticipanteEvento p : this.getParticipantes()){
    		
    		if(p.getNome().equals(participante.getNome())){
    			
    			this.getParticipantes().remove(participante);
    			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Participante Excluído com sucesso!","");
    			FacesContext.getCurrentInstance().addMessage(null, msg);	
    			break;
    		}
    		
    	} 
    	
    	participante = new ParticipanteEvento();
    	
    	return null;
    }
     
    public Date getRandomDate(Date base) {
        Calendar date = Calendar.getInstance();
        date.setTime(base);
        date.add(Calendar.DATE, ((int) (Math.random()*30)) + 1);    //set random day of month
         
        return date.getTime();
    }
     
    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);
         
        return calendar.getTime();
    }
     
    public ScheduleModel getEventModel() {
        return eventModel;
    }
     
    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }
      
    public ScheduleEvent getEvent() {
        return event;
    }
 
    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }
     
    // public void addEvent(ActionEvent actionEvent)
    public String addEvent() {
      
    	try{
    		
    		if(getSelectedUsuarios().isEmpty() && getSelectedGrupos().isEmpty()) {
    			
    			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Favor Adicionar 1 usuário ou grupo ao evento.","");
    			FacesContext.getCurrentInstance().addMessage(null, msg);
    			
    			return null;
    			
    		}
    	
    	if(this.evento.getId() == null){
    		
    		this.evento.setUsuarios(getSelectedUsuarios());
    		
    		this.evento.setGrupo(getSelectedGrupos());
        	       	
    		DefaultScheduleEvent eventoDB = new DefaultScheduleEvent(this.evento.getDescricao(), this.evento.getInicio(), this.evento.getFim());
           	
            
            // cadastrar evento na base de dados
            
            this.evento.setUsuarios(getSelectedUsuarios());
            
            this.evento.setGrupo(getSelectedGrupos());
            
            // persisti os usuarios externos antes de salvar o evento
            if(this.participantes != null){
            	
            	if(this.evento.getParticipantes() == null)
            		this.evento.setParticipantes(new ArrayList<ParticipanteEvento>());
            	
            	for(ParticipanteEvento par: this.participantes){
            		
            		par = participanteEventoService.create(par, getEmpresaSistema());
            		
            		if(par != null){
            		this.evento.getParticipantes().add(par);  
            		
            		sendEmailParticipantes(par.getEmail(),par.getNome(),this.evento.getDescricao(),this.evento.getInicio(),this.evento.getFim(),
            				this.evento.getLocal());
            	
            		}
            	}
            	
            }
                        
            try {
            	
            	// seta a imagem caso exista
            	
    	
            	
            	evento.setStatus(true);
            	
            	evento = eventoService.create(evento, getEmpresaSistema());
            	            	
            	if(evento != null && evento.getId() != null){            		
            		
                	if (file != null && !file.getFileName().isEmpty()) {       	        	

                		DataConnect.salvaImagemEvento(evento.getId(),file.getContents());                		
                	}                 		
            		
            	}
				
	           	eventoDB.setDescription(this.evento.getId().toString());
    	   		
	            eventModel.addEvent(eventoDB);
				
				FacesMessage msg = new FacesMessage("Evento Criado com sucesso!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} catch (GenericException e) {
				e.printStackTrace();
			}
            
        }
        else{
        	        	
            eventModel.updateEvent(event);
        }
    	
        event = new DefaultScheduleEvent();
        
        
        
        
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	List<String> tokensUsuarios = new ArrayList<String>();
		
		if(getSelectedUsuarios() != null){
			
		for(Usuario user : getSelectedUsuarios()){
			
			if(user.getToken() != null && !user.getToken().isEmpty())
			tokensUsuarios.add(user.getToken());
			
		}
			
		}
		
		if(tokensUsuarios != null && !tokensUsuarios.isEmpty()){		
			
			for(String token : tokensUsuarios){
			
			try {
				sendMessageFirebase(token,"Você tem um Novo Evento!");
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
			
		}
    	
    	try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../evento/evento.xhtml?redirect=true");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		
    	
     	return null;
    }
    
 // public void addEvent(ActionEvent actionEvent)
    
    public String cancelarEvent() {
    	
		this.evento.setUsuarios(getSelectedUsuarios());
		
		this.evento.setGrupo(getSelectedGrupos());
		
		this.evento.setStatus(false);
		
		try {
			eventoService.update(evento);
		} catch (GenericException e1) {
			e1.printStackTrace();
		}
		
		List<String> tokensUsuarios = new ArrayList<String>();
		
		if(getSelectedUsuarios() != null){
			
		for(Usuario user : getSelectedUsuarios()){
			
			if(user.getToken() != null && !user.getToken().isEmpty())
			tokensUsuarios.add(user.getToken());
			
		}
			
		}
		
		if(tokensUsuarios != null && !tokensUsuarios.isEmpty()){		
			
			for(String token : tokensUsuarios){
			
			try {
				sendMessageFirebase(token,"Cancelamento de Evento!");
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
			
		}
		
		FacesMessage msg = new FacesMessage("Evento Cancelado com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		inicializaEvents();
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../evento/evento.xhtml?redirect=true");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
    	return null;
    }
    
    
    public String updateEvent() {
      
    	try{
    	  		
    		this.evento.setUsuarios(getSelectedUsuarios());
    		
    		this.evento.setGrupo(getSelectedGrupos());
        	          	
    		for(ScheduleEvent ev : eventModel.getEvents()){
    			
    			if(ev.getDescription().equalsIgnoreCase(this.evento.getId().toString())){
    				
    				eventModel.getEvents().remove(ev);
    				break;
    			}   			
    			
    		}
           	
    		DefaultScheduleEvent eventoDB = new DefaultScheduleEvent(this.evento.getDescricao(), this.evento.getInicio(), this.evento.getFim());
           	
           	eventoDB.setDescription(this.evento.getId().toString());
        	   		
            eventModel.addEvent(eventoDB);
                        
            // persisti os usuarios externos antes de salvar o evento
            if(this.participantes != null){
            	
            	for(ParticipanteEvento par: this.participantes){
            		
            		par = participanteEventoService.create(par, getEmpresaSistema());
            		
            		this.evento.getParticipantes().add(par);  
            		
            		sendEmailParticipantes(par.getEmail(),par.getNome(),this.evento.getDescricao(),this.evento.getInicio(),this.evento.getFim(),
            				this.evento.getLocal());
            	}
            	
            }
                        
            try {
            	
            	if(evento != null && evento.getId() != null){            		
            		
                	if (file != null && !file.getFileName().isEmpty()) {       	        	

                		DataConnect.salvaImagemEvento(evento.getId(),file.getContents());                		
                	}                 		
            		
            	}            	
            	
				eventoService.update(evento);
				FacesMessage msg = new FacesMessage("Evento Atualizado com sucesso!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} catch (GenericException e) {
				e.printStackTrace();
			}

        	        	
        eventModel.updateEvent(event);
                
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	List<String> tokensUsuarios = new ArrayList<String>();
		
		if(getSelectedUsuarios() != null){
			
		for(Usuario user : getSelectedUsuarios()){
			
			if(user.getToken() != null && !user.getToken().isEmpty())
			tokensUsuarios.add(user.getToken());
			
		}
			
		}
		
		if(tokensUsuarios != null && !tokensUsuarios.isEmpty()){		
			
			for(String token : tokensUsuarios){
			
			try {
				sendMessageFirebase(token,"Você tem um Novo Evento!");
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
			
		}
    	
    	try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../evento/evento.xhtml?redirect=true");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		
    	
     	return null;
    }
    
	private void sendMessageFirebase(String token,String conteudo) throws JSONException, ClientProtocolException, IOException{
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
		post.setHeader("Content-type", "application/json");
		post.setHeader("Authorization", "key=AAAAg6OKGls:APA91bHCLYvN31Zk09s6FmLy5k6pFYGGj74Ah9JSSLlFAMVoxupEVBEe8MFMPAdfyuqw-TsSPdJ_fjmjUzuKFcNXTcDlDHnroM0kGQPt6RDjNpO2hA-rpOU7YTn44SOdMCp9l6fUErc0");

		JSONObject message = new JSONObject();
		message.put("to", token);
		message.put("priority", "high");

		JSONObject notification = new JSONObject();
		notification.put("title", "Sindquimica");
		notification.put("body", conteudo);

		message.put("notification", notification);

		post.setEntity(new StringEntity(message.toString(), "UTF-8"));
		HttpResponse response = client.execute(post);
		System.out.println(response);
		System.out.println(message);
	}

    
    public String voltar(){
    	
    	try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../evento/evento.xhtml?redirect=true");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
     
	public void sendEmailParticipantes(String email,String nomeUsuario,String evento,Date dataInicio,Date dataFim,String local){
		
		try{
		
		final String username = "2biportal@gmail.com";
		final String password = "eaccsl2017";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		MimeMessage message = new MimeMessage(session);

		message.setFrom(new InternetAddress("2biportal@gmail.com"));

		message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));

		message.setSubject("Notificação de Evento - Sindquimica","UTF-8");

		message.setText("Caro ,"+nomeUsuario
					+ "\n\n Segue os dados do Evento:"+
						"\n\n Evento: "+evento+" \n\n\n"
								+ "Data de Ínicio:"+df.format(dataInicio)+" \n\n"
								+ "Data de Fim:"+df.format(dataFim)+" \n\n "+
								  "Local: "+local+"\n\n"
										+ "Atenciosamente, \n\n "
										+ "Equipe Sindquimica.","UTF-8");

		Transport.send(message);

		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
    
    public void onEventSelect(SelectEvent selectEvent) {
        
    	event = (ScheduleEvent) selectEvent.getObject();
               
        this.evento = eventoService.getById(Integer.parseInt(event.getDescription()), getEmpresaSistema());  
        
        // verifica na base de dados qual dos usuarios ja visualizou no app e confirmou presença
        
        for(Usuario user : this.evento.getUsuarios()){
        	
        	setConfirmouAndVisualizou(this.evento.getId(),user);        	
        	
        }        
        
        setSelectedUsuarios(this.evento.getUsuarios());
        
        usuarios = usuarioService.all(getEmpresaSistema());
        
        setUsuarios(usuarios);
        
        // faz um for pra setar o confirmou e visualizou 
        for(Usuario us : usuarios){
        	
        	
        	for(Usuario u : this.evento.getUsuarios()){
        		
        		
        		if(us.getId() == u.getId()){
        			
        			us.setConfirmou(u.getConfirmou());
        			us.setVisualizou(u.getVisualizou());
        		}
        	}
        	
        	
        	
        }
        
        setSelectedGrupos(this.evento.getGrupo());
        
        setGrupos(grupoService.all(getEmpresaSistema()));
        
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../evento/edit.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private Usuario setConfirmouAndVisualizou(Integer eventoId,Usuario usuario){
    	
		Connection con = null;
		PreparedStatement ps = null;

		try {
			
			con = br.developersd3.sindquimica.util.DataConnect.getConnection();
			ps = con.prepareStatement(" Select confirmou,visualizou from evento_usuario where evento_id = "+eventoId+" "
					+ " and usuario_id="+usuario.getId());
			

			ResultSet rs = ps.executeQuery();
			
			System.out.println(ps);

			if (rs.next()) {
								
				Integer confirmou = rs.getInt("confirmou");
				
				if(confirmou != null && confirmou != 0){
				
				if(confirmou == 1)
				usuario.setConfirmou(true);
				else
				usuario.setConfirmou(false);	
				
				}
				
				Boolean visualizou = rs.getBoolean("visualizou");
				
				if(visualizou != null && visualizou)
					usuario.setVisualizou(true);
				else
					usuario.setVisualizou(false);

			}
				
			
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return usuario;
		} finally {
			br.developersd3.sindquimica.util.DataConnect.close(con);
		}
		return usuario;   	
    	
    }
    

     
    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
           
        setSelectedUsuarios(new ArrayList<Usuario>());
        setSelectedGrupos(new ArrayList<Grupo>());
        
        this.evento = new Evento();
        
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../evento/insert.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public EventoService getEventoService() {
		return eventoService;
	}

	public void setEventoService(EventoService eventoService) {
		this.eventoService = eventoService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public GrupoService getGrupoService() {
		return grupoService;
	}

	public void setGrupoService(GrupoService grupoService) {
		this.grupoService = grupoService;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
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

	public void setLazyEventModel(ScheduleModel lazyEventModel) {
		this.lazyEventModel = lazyEventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public List<ParticipanteEvento> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<ParticipanteEvento> participantes) {
		this.participantes = participantes;
	}

	public ParticipanteEvento getParticipante() {
		return participante;
	}

	public void setParticipante(ParticipanteEvento participante) {
		this.participante = participante;
	}	
	
	public ParticipanteEventoService getParticipanteEventoService() {
		return participanteEventoService;
	}

	public void setParticipanteEventoService(ParticipanteEventoService participanteEventoService) {
		this.participanteEventoService = participanteEventoService;
	}

	public List<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	public SimpleDateFormat getDf() {
		return df;
	}

	public void setDf(SimpleDateFormat df) {
		this.df = df;
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
    
    
}