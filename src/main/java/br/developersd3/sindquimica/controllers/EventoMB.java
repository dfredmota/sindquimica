package br.developersd3.sindquimica.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Evento;
import br.developersd3.sindquimica.models.Grupo;
import br.developersd3.sindquimica.models.ParticipanteEvento;
import br.developersd3.sindquimica.models.Usuario;
import br.developersd3.sindquimica.service.EventoService;
import br.developersd3.sindquimica.service.GrupoService;
import br.developersd3.sindquimica.service.ParticipanteEventoService;
import br.developersd3.sindquimica.service.UsuarioService;
import br.developersd3.sindquimica.util.SessionUtils;

@ManagedBean(name = "eventoMB")
@SessionScoped
public class EventoMB implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;
 
    private ScheduleModel lazyEventModel;
 
    private ScheduleEvent event = new DefaultScheduleEvent();
    
    private ScheduleModel eventModel;
    
    private Evento evento;
    
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
    
    DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
 
    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        
        this.eventos = eventoService.all(getEmpresaSistema());
        
        if(this.eventos != null){
        	
        for(Evento ev : this.eventos){
        	
        eventModel.addEvent(new DefaultScheduleEvent(ev.getDescricao(), ev.getInicio(), ev.getFim()));	        	
        	
        }
        	
        }
        
//        eventModel.addEvent(new DefaultScheduleEvent("Champions League Match", previousDay8Pm(), previousDay11Pm()));
//        eventModel.addEvent(new DefaultScheduleEvent("Birthday Party", today1Pm(), today6Pm()));
//        eventModel.addEvent(new DefaultScheduleEvent("Breakfast at Tiffanys", nextDay9Am(), nextDay11Am()));
//        eventModel.addEvent(new DefaultScheduleEvent("Plant the new garden stuff", theDayAfter3Pm(), fourDaysLater3pm()));
         
        lazyEventModel = new LazyScheduleModel() {
             
            @Override
            public void loadEvents(Date start, Date end) {
                Date random = getRandomDate(start);
                addEvent(new DefaultScheduleEvent("Lazy Event 1", random, random));
                 
                random = getRandomDate(start);
                addEvent(new DefaultScheduleEvent("Lazy Event 2", random, random));
            }   
        };
        
		 usuarios = usuarioService.all(getEmpresaSistema());
	     
	     grupos   = grupoService.all(getEmpresaSistema());
	     
	     this.participante = new ParticipanteEvento();
	     
	     this.evento = new Evento();
	     
	     this.evento.setParticipantes(new ArrayList<ParticipanteEvento>());
	     
	     this.participantes = new ArrayList<ParticipanteEvento>();
    }
    
    
    public String addParticipante(){
    	
    	this.getParticipantes().add(participante);  	
    	
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
 
    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }
     
    private Date previousDay8Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 8);
         
        return t.getTime();
    }
     
    private Date previousDay11Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 11);
         
        return t.getTime();
    }
     
    private Date today1Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 1);
         
        return t.getTime();
    }
     
    private Date theDayAfter3Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 2);     
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 3);
         
        return t.getTime();
    }
 
    private Date today6Pm() {
        Calendar t = (Calendar) today().clone(); 
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 6);
         
        return t.getTime();
    }
     
    private Date nextDay9Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 9);
         
        return t.getTime();
    }
     
    private Date nextDay11Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 11);
         
        return t.getTime();
    }
     
    private Date fourDaysLater3pm() {
        Calendar t = (Calendar) today().clone(); 
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 4);
        t.set(Calendar.HOUR, 3);
         
        return t.getTime();
    }
     
    public ScheduleEvent getEvent() {
        return event;
    }
 
    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }
     
    public void addEvent(ActionEvent actionEvent) {
      
    	try{
    	
    	if(event.getId() == null){
        	       	
        	event = new DefaultScheduleEvent(this.evento.getDescricao(), this.evento.getInicio(), this.evento.getFim());
        	
            eventModel.addEvent(event);
            
            // cadastrar evento na base de dados
            
            this.evento.setUsuarios(getSelectedUsuarios());
            
            this.evento.setGrupo(getSelectedGrupos());
            
            // persisti os usuarios externos antes de salvar o evento
            if(this.participantes != null){
            	
            	for(ParticipanteEvento par: this.participantes){
            		
            		par = participanteEventoService.create(par, getEmpresaSistema());
            		
            		this.evento.getParticipantes().add(par);  
            		
            		sendEmailParticipantes(par.getEmail(),par.getNome(),this.evento.getDescricao(),this.evento.getInicio(),this.evento.getFim());
            	}
            	
            }
                        
            try {
				eventoService.create(evento, getEmpresaSistema());
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
    	
    	try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../evento/evento.xhtml?redirect=true");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
     
	public void sendEmailParticipantes(String email,String nomeUsuario,String evento,Date dataInicio,Date dataFim){
		
		try{
		
		final String username = "dfredmota@gmail.com";
		final String password = "Scorge@3873";

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

		Message message = new MimeMessage(session);

		message.setFrom(new InternetAddress("dfredmota@gmail.com"));

		message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));

		message.setSubject("Notificação de Evento - Sindquimica");

		message.setText("Caro ,"+nomeUsuario
					+ "\n\n Segue os dados do Evento:"+
						"\n\n Evento: "+evento+" \n\n\n"
								+ "Data de Ínicio:"+df.format(dataInicio)+" \n\n"
								+ "Data de Fim:"+df.format(dataFim)+" \n\n "
										+ "Atenciosamente, \n\n "
										+ "Equipe Sindquimica.");

		Transport.send(message);

		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
    
    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        
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


	private Integer getEmpresaSistema(){
		HttpSession session = SessionUtils.getSession();
		Integer empresaSistemaId = (Integer)session.getAttribute("empresaSistemaId");
		
		return empresaSistemaId;
	}
    
    
}