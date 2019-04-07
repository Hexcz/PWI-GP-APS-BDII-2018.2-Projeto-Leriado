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

@ManagedBean(name="amigoBean",eager=true)
@ViewScoped
public class AmigoMB {
	
	private int qtdAmigos;
	private int qtdSolicitacoes;
	private int qtdBuscados;
	private UsuarioDaoImpl usuarioDao;
	private List<Usuario> amigos;
	private List<Usuario> solicitacoes;
	private List<Usuario> buscados;
	private List<Usuario> sugestoes;
	private String queryAmigos;
	private Usuario amigo;
	

	@ManagedProperty("#{loginBean}")
	private LoginMB loginMb;
	
	@ManagedProperty("#{usuariosOnline}")
	private UsuariosOnline usuariosOnlineMB;
	
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
			tratarUsuarios(amigos);
			amigosOnline(amigos);
			usuarioDao.carregarFotoPerfil(amigos);
			solicitacoes = usuarioDao.solicitacoesAmizades(loginMb.getUsuarioLogado());
			usuarioDao.carregarFotoPerfil(solicitacoes);
			sugestoes = usuarioDao.sugestaoAmizade(loginMb.getUsuarioLogado());
			usuarioDao.carregarFotoPerfil(sugestoes);
		} catch (DataAccessException e) {		
			e.printStackTrace();
		}
		return "";
	}
	
	private void tratarUsuarios(List<Usuario> amigosUsuario) {
//		A FUNÇÃO SETA O ATRIBUTO ONLINE PARA FALSE DE TODOS OS USUÁRIOS AMIGOS RESGATADOS
//		PARA QUANDO FOR COMPARADO NÃO SEJA LANÇADO UM NULL POINTER
		for(Usuario usuario: amigosUsuario) {
			usuario.setOnline(false);
		}
		
	}

	private void amigosOnline(List<Usuario> amigosUsuario) {
//		lista de amigos
		for(Usuario amigoUsuarioLogado: amigosUsuario) {
//			lista de usuarios online
			for(Usuario usuarioAtivo : usuariosOnlineMB.getUsuariosOn()) {
				if(amigoUsuarioLogado.getEmail().equals(usuarioAtivo.getEmail())) {
					amigoUsuarioLogado.setOnline(true);
				}
			}
		}
	}

	public String buscarAmigos() {		
		try {			
			buscados = usuarioDao.buscar(queryAmigos);
			qtdBuscados = buscados.size();
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

	public int getQtdBuscados() {
		return qtdBuscados;
	}

	public void setQtdBuscados(int qtdBuscados) {
		this.qtdBuscados = qtdBuscados;
	}

	public LoginMB getLoginMb() {
		return loginMb;
	}

	public void setLoginMb(LoginMB loginMb) {
		this.loginMb = loginMb;
	}

	public UsuariosOnline getUsuariosOnlineMB() {
		return usuariosOnlineMB;
	}

	public void setUsuariosOnlineMB(UsuariosOnline usuariosOnlineMB) {
		this.usuariosOnlineMB = usuariosOnlineMB;
	}


}
