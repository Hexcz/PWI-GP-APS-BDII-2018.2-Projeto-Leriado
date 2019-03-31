package br.edu.ifpb.pweb1.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.edu.ifpb.pweb1.model.dao.impdb.UsuarioDaoImpl;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

@ManagedBean(name="cadastroBean")
@RequestScoped
public class CadastroMB {
	
	private Logger log;
	
	private Usuario usuario;
	
	@PostConstruct
	private void inicio() {
		limpar();
	}
			
	private void limpar() {
		usuario = new Usuario();
//		usuario.setDatanasc(LocalDate.now());
		usuario.setAcesso(1);
	}
	
	public String cadastrar() {
		try {
			new UsuarioDaoImpl().criar(usuario);
		} catch (DataAccessException e) {
			log.info("Falha ao criar usuário");
			return "falha";
		}
		limpar();
		return "sucesso";
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

//	public String getData() {
//		if(usuario == null)
//			return "";
//		if(usuario.getDatanasc()== null)
//			return "";
//		DateTimeFormatter frm = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//		this.data = usuario.getDatanasc().format(frm);
//		return this.data;
//	}
//
//	public void setData(String data) {
//		this.data = data;
//		if((usuario != null) && (!(data.isEmpty()))) {
//			DateTimeFormatter frm = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			usuario.setDatanasc(LocalDate.parse(data, frm));			
//		}
//				
//	}


}
