package br.edu.ifpb.pweb1.controller;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

@ManagedBean
@SessionScoped
public class IdiomaBean {
	
	private String idioma;
	
	@PostConstruct
	public void init() {
		idioma = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
	}

	public void mudarIdioma(ValueChangeEvent event) {
		idioma = (String)event.getNewValue();		
		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(idioma));
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
}
