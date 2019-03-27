package br.edu.ifpb.pweb1.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.edu.ifpb.pweb1.model.dao.UsuarioDaoImpl;
import br.edu.ifpb.pweb1.model.domain.Usuario;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginMB {

	private Usuario usuarioLogado = null;

	private String email;
	private String senha;

	@PostConstruct
	public void inicial() {

	}

	public String efetuarLogin() {
		UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl();
		try {
			if (usuarioDao.login(email, senha)) {
				usuarioLogado = usuarioDao.buscarPorEmail(email);
				usuarioLogado.setSenha("");
				senha = "";				
				return "sucesso";
			}			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Falha ao efetura login", "Falha ao efetuar login, usuário ou senha inválidos"));
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return "falha";
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
