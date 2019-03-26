package com.ifpb.edu.model.domain.publicacao;

import java.time.LocalDateTime;

import com.ifpb.edu.model.domain.Grupo;
import com.ifpb.edu.model.domain.Usuario;

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
