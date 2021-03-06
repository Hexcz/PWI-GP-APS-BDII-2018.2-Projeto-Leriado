package br.edu.ifpb.pweb1.model.domain;

public class Marca {
	
	private Texto texto;
	private Usuario usuario;
	
	public Marca() {
	}

	public Marca(Texto texto, Usuario usuario) {
		this.texto = texto;
		this.usuario = usuario;
	}

	public Texto getTexto() {
		return texto;
	}

	public void setTexto(Texto texto) {
		this.texto = texto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Marca [texto=" + texto + ", usuario=" + usuario + "]";
	}
	
}
