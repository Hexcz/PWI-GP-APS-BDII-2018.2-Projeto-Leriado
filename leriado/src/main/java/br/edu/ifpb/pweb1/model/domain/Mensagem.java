package br.edu.ifpb.pweb1.model.domain;

import java.time.LocalDate;

import org.bson.codecs.pojo.annotations.BsonId;

public class Mensagem {
	private String idRemetente;
	private String idDestinatario;
	private LocalDate dataEnvio;
	private String conteudo;
	private String nomeRemetente;
	private String idConversa;
	
	public Mensagem() {}
	public Mensagem(String idRemetente, String idDestinatario, String conteudo, String nomeRemetente, String idConversa) {
		this.idRemetente = idRemetente;
		this.idDestinatario = idDestinatario;
		this.dataEnvio = LocalDate.now();
		this.conteudo = conteudo;
		this.nomeRemetente = nomeRemetente;
		this.idConversa = idConversa;
	}
	public String getIdRemetente() {
		return idRemetente;
	}
	public void setIdRemetente(String idRemetente) {
		this.idRemetente = idRemetente;
	}
	public String getIdDestinatario() {
		return idDestinatario;
	}
	public void setIdDestinatario(String idDestinatario) {
		this.idDestinatario = idDestinatario;
	}
	public LocalDate getDataEnvio() {
		return dataEnvio;
	}
	public void setDataEnvio(LocalDate dataEnvio) {
		this.dataEnvio = dataEnvio;
	}
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	
	public String getNomeRemetente() {
		return nomeRemetente;
	}
	public void setNomeRemetente(String nomeRemetente) {
		this.nomeRemetente = nomeRemetente;
	}
	public String getIdConversa() {
		return idConversa;
	}
	public void setIdConversa(String idConversa) {
		this.idConversa = idConversa;
	}
	@Override
	public String toString() {
		return "Mensagem [idRemetente=" + idRemetente + ", idDestinatario=" + idDestinatario + ", dataEnvio="
				+ dataEnvio + ", conteudo=" + conteudo + ", nomeRemetente=" + nomeRemetente + ", idConversa="
				+ idConversa + "]";
	}
	
}
