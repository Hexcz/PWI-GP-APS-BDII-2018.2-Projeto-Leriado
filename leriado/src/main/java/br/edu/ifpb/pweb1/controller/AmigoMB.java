package br.edu.ifpb.pweb1.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.edu.ifpb.pweb1.model.dao.impdb.UsuarioDaoImpl;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

@ManagedBean(name="amigoBean")
@ViewScoped
public class AmigoMB {
	
	private int qtdAmigos;
	private int qtdSolicitacoes;
	private UsuarioDaoImpl usuarioDao;
	private List<Usuario> amigos;
	private List<Usuario> solicitacoes;
	private List<Usuario> buscados;
	private List<Usuario> sugestoes;
	private String queryAmigos;
	private Usuario amigo;
	

	@ManagedProperty("#{loginBean}")
	private LoginMB loginMb;
	
	@PostConstruct
	private void incio() {
		usuarioDao = new UsuarioDaoImpl();		
		queryAmigos = "";
		buscados = new ArrayList<>();
		listarAmigos();
	}
	
	public String getQueryAmigos() {
		return queryAmigos;
	}

	public void setQueryAmigos(String queryAmigos) {
		this.queryAmigos = queryAmigos;
	}

	public String listarAmigos() {
		try {
			qtdAmigos = usuarioDao.qtdAmigos(loginMb.getUsuarioLogado());
			qtdSolicitacoes = usuarioDao.qtdSolicitacoesAmizades(loginMb.getUsuarioLogado());
			amigos = usuarioDao.amigos(loginMb.getUsuarioLogado());
			solicitacoes = usuarioDao.solicitacoesAmizades(loginMb.getUsuarioLogado());
			usuarioDao.carregarFotoPerfil(amigos);
			sugestoes = usuarioDao.sugestaoAmizade(loginMb.getUsuarioLogado());
			usuarioDao.carregarFotoPerfil(sugestoes);
		} catch (DataAccessException e) {		
			e.printStackTrace();
		}
		return "";
	}
	
	public String buscarAmigos() {		
		try {
			buscados = usuarioDao.buscar(queryAmigos);
			usuarioDao.carregarFotoPerfil(buscados);
			usuarioDao.mudarStatus(loginMb.getUsuarioLogado(), buscados);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		loginMb.setPaginaAtual("buscaAmigo");
		queryAmigos="";
		return "";
	}
	
	public String iniciarAmizade() {		
		try {
			usuarioDao.seguir(loginMb.getUsuarioLogado(), amigo);
			usuarioDao.mudarStatus(loginMb.getUsuarioLogado(), amigo);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}			
		listarAmigos();
		return "";
	}
	
	public String finalizarAmizade() {		
		try {
			usuarioDao.desfazerAmizade(loginMb.getUsuarioLogado(), amigo);
			usuarioDao.mudarStatus(loginMb.getUsuarioLogado(), amigo);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		listarAmigos();
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

	public List<Usuario> getBuscados() {
		return buscados;
	}

	public void setBuscados(List<Usuario> buscados) {
		this.buscados = buscados;
	}
	
	public List<Usuario> getSugestoes() {
		return sugestoes;
	}

	public void setSugestoes(List<Usuario> sugestoes) {
		this.sugestoes = sugestoes;
	}

	public Usuario getAmigo() {
		return amigo;
	}

	public void setAmigo(Usuario amigo) {
		this.amigo = amigo;
	}

	public LoginMB getLoginMb() {
		return loginMb;
	}

	public void setLoginMb(LoginMB loginMb) {
		this.loginMb = loginMb;
	}


}
