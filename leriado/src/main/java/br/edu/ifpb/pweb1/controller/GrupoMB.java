package br.edu.ifpb.pweb1.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.edu.ifpb.pweb1.model.dao.impdb.GrupoDaoImpl;
import br.edu.ifpb.pweb1.model.dao.impdb.UsuarioDaoImpl;
import br.edu.ifpb.pweb1.model.domain.FeedGrupo;
import br.edu.ifpb.pweb1.model.domain.Grupo;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;
import br.edu.ifpb.pweb1.util.JsfUtil;

@ManagedBean(name = "grupoBean")
@ViewScoped
public class GrupoMB {
	
	private List<FeedGrupo> Feedgrupos;
	private Usuario usuario;
	private Grupo grupo;
	private String emailUsuario;
	private GrupoDaoImpl grupoDao;
	
	@ManagedProperty("#{loginBean}")
	private LoginMB loginMb;
	
	@PostConstruct
	public void inicial() {
		grupoDao = new GrupoDaoImpl();
		
	}
	
	public String paginaGrupo() {
		loginMb.setPaginaAtual("grupo");		
		return "";
	}
	
	public String criarGrupo() {
		grupo = new Grupo();
		loginMb.setPaginaAtual("criarGrupo");
		return "";
	}
	
	public String editarGrupo() {
		loginMb.setPaginaAtual("editarGrupo");
		return "";
	}
	
	public String salvarGrupo() {
		try {
			grupoDao.criar(grupo);
		} catch (DataAccessException e) {		
			JsfUtil.addErrorMessage("Impossível salvar o grupo");
		}
		loginMb.setPaginaAtual("grupo");
		return "";
	}
	
	
	public String removerUsuario() {
		try {
			new GrupoDaoImpl().removerUsuario(grupo.getId(), usuario.getId());
			
			emailUsuario = "";
			grupo = null;
			loginMb.carrgarGrupos();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String excluirGrupo() {
		try {
			new GrupoDaoImpl().excluir(grupo.getId());
			
			emailUsuario = "";
			grupo = null;
			loginMb.carrgarGrupos();
		} catch (Exception e) {		
			e.printStackTrace();
		}		
		return "";
	}
	
	public String adicionarUsuario() {
		
		try {
			Usuario usuario = new UsuarioDaoImpl().buscarPorEmail(emailUsuario);
			if(usuario == null)
				return "";
			new GrupoDaoImpl().adicionarUsuario(grupo.getId(), usuario.getId());
			grupo = null;
			loginMb.carrgarGrupos();
			loginMb.carrgarGrupos();
			grupo = null;
			loginMb.carrgarGrupos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	

	public LoginMB getLoginMb() {
		return loginMb;
	}

	public void setLoginMb(LoginMB loginMb) {
		this.loginMb = loginMb;
	}

	public List<FeedGrupo> getFeedgrupos() {
		return Feedgrupos;
	}

	public void setFeedgrupos(List<FeedGrupo> feedgrupos) {
		Feedgrupos = feedgrupos;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}
	

}
