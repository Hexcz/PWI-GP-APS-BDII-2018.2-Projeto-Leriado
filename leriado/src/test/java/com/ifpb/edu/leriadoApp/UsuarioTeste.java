package com.ifpb.edu.leriadoApp;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ifpb.edu.model.dao.UsuarioDaoImpl;
import com.ifpb.edu.model.domain.Usuario;
import com.ifpb.edu.model.jdbc.DataAccessException;

public class UsuarioTeste {

	private UsuarioDaoImpl usuarioDAO = null;
	private Usuario usuario = null;

	@Before
	public void inicio() {
		usuarioDAO = new UsuarioDaoImpl();
		usuario = new Usuario();
	}

	@Test
	public void criaUsuario() {
		usuario.setEmail("ddaianealiciaantonellaalmeida@vivalle.com.br");
		usuario.setSenha("469LCrQdAY");
		usuario.setNome("Daiane");
		usuario.setSobrenome("Alícia Antonella Almeida");
		usuario.setSexo("F");
		usuario.setDatanasc(LocalDate.of(1997, 05, 22));
		usuario.setAcesso(1);
		usuario.setTelefone("(83) 98102-9547");
		usuario.setRua("Rua Coronel Juvêncio Carneiro");
		usuario.setCidade("Cajazeiras");
		usuario.setEstado("PB");
		usuario.setNumero("489");
		usuario.setCep("58900-970");
		try {
			usuarioDAO.criar(usuario);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void segueUsuario() {
		Usuario seguidor;
		try {
			seguidor = usuarioDAO.buscarPorId(4);
			usuario = usuarioDAO.buscarPorId(3);
			usuarioDAO.seguir(seguidor, usuario);
			usuarioDAO.seguir(usuario, seguidor);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void amigosUsuario() {
		List<Usuario> amigos = null;
		try {
			usuario.setId(3);
			amigos = usuarioDAO.amigos(usuario,0,1);
			for (Usuario user : amigos) {
				System.out.print(user.getId() + " ");
				System.out.println(user.getNome());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void defazerAmizadeTeste() {
		try {
			Usuario u2 = new Usuario();
			u2.setId(3);
			usuario.setId(4);
			usuarioDAO.desfazerAmizade(u2, usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void buscarNomeTeste() {
		List<Usuario> usuarios = null;
		try {			
			usuarios = usuarioDAO.buscarNome("a",1,2);
			for (Usuario user : usuarios) {
				System.out.print(user.getId() + " ");
				System.out.println(user.getNome());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void qtdBuscarNomeTeste() {
		try {
			System.out.println(usuarioDAO.qtdBuscarNome("eloa"));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void qtdAmigosTeste() {
		usuario.setId(1);
		try {
			System.out.println(usuarioDAO.qtdAmigos(usuario));
		}catch (Exception e) {
			e.printStackTrace();			
		}
	}

}
