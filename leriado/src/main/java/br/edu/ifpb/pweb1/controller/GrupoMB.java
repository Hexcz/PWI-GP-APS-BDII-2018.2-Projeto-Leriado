package br.edu.ifpb.pweb1.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "grupoBean")
@ViewScoped
public class GrupoMB {
	
	@ManagedProperty("#{loginBean}")
	private LoginMB loginMb;
	
	@PostConstruct
	public void inicial() {
		
	}
	
	public String grupo() {
		loginMb.setPaginaAtual("grupo");
		System.out.println("Grupo!!");
		return "";
	}

	public LoginMB getLoginMb() {
		return loginMb;
	}

	public void setLoginMb(LoginMB loginMb) {
		this.loginMb = loginMb;
	}
	
	

}
