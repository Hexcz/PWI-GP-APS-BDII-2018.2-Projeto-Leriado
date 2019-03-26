package br.edu.ifpb.pweb1.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.ifpb.pweb1.model.dao.UsuarioDaoImpl;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

@Named("cadastroBean")
@SessionScoped
public class CadastroMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Usuario usuario;
	
	private String data;
		
	private void limpar() {
		usuario = new Usuario();
	}
	
	public String cadastrar() {		
		usuario.setDatanasc(LocalDate.now());
		usuario.setAcesso(1);
		try {
			new UsuarioDaoImpl().criar(usuario);
		} catch (DataAccessException e) {
			System.out.println(usuario);
			e.printStackTrace();
		}
		limpar();
		return null;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}


}
