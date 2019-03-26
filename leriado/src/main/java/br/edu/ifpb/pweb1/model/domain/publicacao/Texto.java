package br.edu.ifpb.pweb1.model.domain.publicacao;

import java.time.LocalDateTime;
import java.util.List;

import br.edu.ifpb.pweb1.model.domain.Usuario;

public class Texto {

	private int id = 0;
	private Boolean ativo = true;	
	private LocalDateTime datahora = LocalDateTime.now();
	private Usuario usuario;
	private Publicacao publicacao;
	private int qtdCurtidas;
	private int qtdComentarios;
	private List<Texto> comentarios;

	public Texto() {
	}

	public Texto(int id, Boolean ativo, LocalDateTime datahora, Usuario usuario, Publicacao publicacao,
			int qtdCurtidas, int qtdComentarios, List<Texto> comentarios) {
		super();
		this.id = id;
		this.ativo = ativo;
		this.datahora = datahora;
		this.usuario = usuario;
		this.publicacao = publicacao;
		this.qtdCurtidas = qtdCurtidas;
		this.qtdComentarios = qtdComentarios;
		this.comentarios = comentarios;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public LocalDateTime getDatahora() {
		return datahora;
	}

	public void setDatahora(LocalDateTime datahora) {
		this.datahora = datahora;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Publicacao getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}

	public int getQtdCurtidas() {
		return qtdCurtidas;
	}

	public void setQtdCurtidas(int qtdCurtidas) {
		this.qtdCurtidas = qtdCurtidas;
	}

	public int getQtdComentarios() {
		return qtdComentarios;
	}

	public void setQtdComentarios(int qtdComentarios) {
		this.qtdComentarios = qtdComentarios;
	}

	public List<Texto> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Texto> comentarios) {
		this.comentarios = comentarios;
	}
	
}
