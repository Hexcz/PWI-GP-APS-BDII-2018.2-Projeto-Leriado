package br.edu.ifpb.pweb1.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import br.edu.ifpb.pweb1.model.domain.Usuario;

@ManagedBean(eager=true)
@ApplicationScoped
public class UsuariosOnline {
	private List<Usuario> usuariosOn = new ArrayList<>();

	public void adicionarUsuarioOnline(Usuario novoUsuario) {
		novoUsuario.setOnline(true);
		usuariosOn.add(novoUsuario);
	}
	
	public void removerUsuarioOnline(Usuario usuario) {
		usuariosOn.remove(usuario);
	}

	public List<Usuario> getUsuariosOn() {
		return usuariosOn;
	}

	public void setUsuariosOn(List<Usuario> usuariosOn) {
		this.usuariosOn = usuariosOn;
	}
}
