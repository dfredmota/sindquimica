package br.developersd3.sindquimica.controllers;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import br.developersd3.sindquimica.datatable.LazySindicatoDataModel;
import br.developersd3.sindquimica.models.Sindicato;
import br.developersd3.sindquimica.service.SindicatoService;
import br.developersd3.sindquimica.util.SessionUtils;


@Model
@SessionScoped
public class SindicatoController implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;
	
	private LazyDataModel<Sindicato> lazyModel;
	
	private Sindicato selectedSindicato;

    @Inject
    @Named("sindicatoService")
	private SindicatoService sindicatoService;
	

    @PostConstruct
    public void init() {
        lazyModel = new LazySindicatoDataModel(sindicatoService.all());
    }

	//validate login
	public String validateUsernamePassword() {
		

		FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Username and Passowrd",
							"Please enter correct username and Password"));
			return "login";
		
	}

	//logout event, invalidate session
	public String logout() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return "login";
	}
	
	
	public LazyDataModel<Sindicato> getLazyModel() {
        return lazyModel;
    }
 
    public Sindicato getSelectedSindicato() {
        return selectedSindicato;
    }
   
 
    public void setSelectedSindicato(Sindicato selectedSindicato) {
		this.selectedSindicato = selectedSindicato;
	}

     
    public void setService(SindicatoService service) {
        this.sindicatoService = service;
    }
     
    public SindicatoService getSindicatoService() {
		return sindicatoService;
	}

	public void setSindicatoService(SindicatoService sindicatoService) {
		this.sindicatoService = sindicatoService;
	}

	public void setLazyModel(LazyDataModel<Sindicato> lazyModel) {
		this.lazyModel = lazyModel;
	}

	public void onRowSelect(SelectEvent event) {
		
		if(event.getObject() != null){
        FacesMessage msg = new FacesMessage("Sindicato Selected:" +((Sindicato) event.getObject()).getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
    }
}
