package br.edu.ifpb.pweb1.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import br.edu.ifpb.pweb1.model.dao.impdb.UsuarioDaoImpl;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

@ManagedBean(name="cadastroBean")
@RequestScoped
public class CadastroMB {
	
	private Logger log;	
	private Usuario usuario;
	private UsuarioDaoImpl usuarioDao;
	
	@ManagedProperty("#{loginBean}")
	private LoginMB loginMb;
	
	@PostConstruct
	private void inicio() {
		usuarioDao = new UsuarioDaoImpl();
		limpar();
	}
			
	private void limpar() {
		usuario = new Usuario();
//		usuario.setDatanasc(LocalDate.now());
		usuario.setAcesso(1);
	}
	
	public String cadastrar() {
		try {
			usuarioDao.criar(usuario);
		} catch (DataAccessException e) {
			log.info("Falha ao criar usuário");
			return "falha";
		}
		limpar();
		return "sucesso";
	}
	
	public String editarCadastro() {
		usuario = new Usuario();
		try {
			usuarioDao.buscarPorId(usuario, loginMb.getUsuarioLogado().getId());
		} catch (DataAccessException e) {		
			e.printStackTrace();
		} 
		loginMb.setPaginaAtual("editarUsuario");
		return "";
	}
	
	public String salvarCadastro() {
		try {
			switch(loginMb.getPaginaAtual()) {
			case "editarUsuario":
				int usuarioId =  loginMb.getUsuarioLogado().getId();
				usuarioDao.atualizar(usuario, usuarioId);
				loginMb.carregarPerfil();
				loginMb.setPaginaAtual("feed");
				return "goHome";		
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
		
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LoginMB getLoginMb() {
		return loginMb;
	}

	public void setLoginMb(LoginMB loginMb) {
		this.loginMb = loginMb;
	}



}
