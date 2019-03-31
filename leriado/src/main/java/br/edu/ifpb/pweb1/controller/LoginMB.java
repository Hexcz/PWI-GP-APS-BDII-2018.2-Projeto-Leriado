package br.edu.ifpb.pweb1.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import br.edu.ifpb.pweb1.model.dao.GrupoDaoImpl;
import br.edu.ifpb.pweb1.model.dao.UsuarioDaoImpl;
import br.edu.ifpb.pweb1.model.domain.Usuario;

/**
 * @author isleimar
 *
 */
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginMB {

	private Usuario usuarioLogado = null;

	private String email;
	private String senha;
	
	private List<String> seusGrupos;

	@PostConstruct
	public void inicial() {
		seusGrupos = new ArrayList<>();
		
	}

	public String efetuarLogin() {
		UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl();
		try {
			if (usuarioDao.login(email, senha)) {
				usuarioLogado = usuarioDao.buscarPorEmail(email);
				usuarioLogado.setSenha("");
				senha = "";				
				
				seusGrupos = new GrupoDaoImpl().buscarGruposUsuarioParticipa(usuarioLogado.getId());
								
				return "sucesso";
			}			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Falha ao efetura login", "Falha ao efetuar login, usuário ou senha inválidos"));
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return "falha";
	}
	
	public String logout() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		request.getSession(false).invalidate();
		return "goLogin";
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

	public List<String> getSeusGrupos() {
		return seusGrupos;
	}

	public void setSeusGrupos(List<String> seusGrupos) {
		this.seusGrupos = seusGrupos;
	}

}
