package br.edu.ifpb.pweb1.model.domain;

import java.time.LocalDateTime;

public class Compartilha {
	private LocalDateTime dataHora;
	private Usuario usuario;
	private Texto texto;
	private Grupo grupo;
	
	public Compartilha() {
	}

	public Compartilha(LocalDateTime dataHora, Usuario usuario, Texto texto, Grupo grupo) {
		super();
		this.dataHora = dataHora;
		this.usuario = usuario;
		this.texto = texto;
		this.grupo = grupo;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Texto getTexto() {
		return texto;
	}

	public void setTexto(Texto texto) {
		this.texto = texto;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	@Override
	public String toString() {
		return "Compartilha [dataHora=" + dataHora + ", usuario=" + usuario + ", texto=" + texto + ", grupo=" + grupo
				+ "]";
	}
	
}
