package br.edu.ifpb.pweb1.model.domain.publicacao;

import java.util.List;

import org.bson.codecs.pojo.annotations.BsonId;

public class Publicacao {
	
	@BsonId
	private int textoId = 0;
	private String titulo;
	private String conteudo;
	private String latitude;
	private String longitude;
	private List<Arquivo> arquivos;	

	public Publicacao() {
	}

	public Publicacao(int textoId, String titulo, String conteudo, List<Arquivo> arquivos) {
		super();
		this.textoId = textoId;
		this.titulo = titulo;
		this.conteudo = conteudo;
		this.arquivos = arquivos;
	}

	public int getTextoId() {
		return textoId;
	}

	public void setTextoId(int textoId) {
		this.textoId = textoId;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public List<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}

	@Override
	public String toString() {
		return "Publicacao [textoId=" + textoId + ", titulo=" + titulo + ", conteudo=" + conteudo + ", arquivos="
				+ arquivos + "]";
	}
	
}
