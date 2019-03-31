package br.edu.ifpb.pweb1.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import br.edu.ifpb.pweb1.model.dao.UsuarioDaoImpl;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

@ManagedBean(name="amigoBean")
@RequestScoped
public class AmigoMB {
	
	private int qtdAmigos;
	private int qtdSolicitacoes;
	private UsuarioDaoImpl usuarioDao;
	private List<Usuario> amigos;
	private List<Usuario> solicitacoes;
	
	@ManagedProperty("#{loginBean}")
	private LoginMB loginMb;
	
	@PostConstruct
	private void incio() {
		usuarioDao = new UsuarioDaoImpl();		
		listarAmigos();
	}
	
	public String listarAmigos() {
		try {
			qtdAmigos = usuarioDao.qtdAmigos(loginMb.getUsuarioLogado());
			qtdSolicitacoes = usuarioDao.qtdSolicitacoesAmizades(loginMb.getUsuarioLogado());
			amigos = usuarioDao.amigos(loginMb.getUsuarioLogado());
		} catch (DataAccessException e) {		
			e.printStackTrace();
		}
		return "";
	}

	public List<Usuario> getAmigos() {
		return amigos;
	}

	public void setAmigos(List<Usuario> amigos) {
		this.amigos = amigos;
	}

	public int getQtdAmigos() {
		return qtdAmigos;
	}

	public void setQtdAmigos(int qtdAmigos) {
		this.qtdAmigos = qtdAmigos;
	}

	public int getQtdSolicitacoes() {
		return qtdSolicitacoes;
	}

	public void setQtdSolicitacoes(int qtdSolicitacoes) {
		this.qtdSolicitacoes = qtdSolicitacoes;
	}

	public List<Usuario> getSolicitacoes() {
		return solicitacoes;
	}

	public void setSolicitacoes(List<Usuario> solicitacoes) {
		this.solicitacoes = solicitacoes;
	}

	public LoginMB getLoginMb() {
		return loginMb;
	}

	public void setLoginMb(LoginMB loginMb) {
		this.loginMb = loginMb;
	}
	
	

}
