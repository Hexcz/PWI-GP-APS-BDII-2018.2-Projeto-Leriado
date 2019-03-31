package br.edu.ifpb.pweb1.model.domain;

import java.util.List;

public class FeedPublicacao {
	
	private Compartilha compartilha;
	private boolean curtido;
	private List<Texto> comentarios;
	
	public FeedPublicacao() {}
	
	public FeedPublicacao(Compartilha compartilha, boolean curtido, List<Texto> comentarios) {
		this.compartilha = compartilha;
		this.curtido = curtido;
		this.comentarios = comentarios;
	}

	public Compartilha getCompartilha() {
		return compartilha;
	}

	public void setCompartilha(Compartilha compartilha) {
		this.compartilha = compartilha;
	}

	public boolean isCurtido() {
		return curtido;
	}

	public void setCurtido(boolean curtido) {
		this.curtido = curtido;
	}

	public List<Texto> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Texto> comentarios) {
		this.comentarios = comentarios;
	}
	
}
