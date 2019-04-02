package br.edu.ifpb.pweb1.model.domain;

public class Arquivo {
	
	private String nome;
	private String nomeOriginal;
	private long tamanho;
	private String descricao;
	private boolean umaFoto;
	
	public Arquivo() {};
	
	public Arquivo(String nome, String nomeOriginal, long tamanho, String descricao, boolean umaFoto) {
		super();
		this.nome = nome;
		this.nomeOriginal = nomeOriginal;
		this.tamanho = tamanho;
		this.descricao = descricao;
		this.umaFoto = umaFoto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeOriginal() {
		return nomeOriginal;
	}

	public void setNomeOriginal(String nomeOriginal) {
		this.nomeOriginal = nomeOriginal;
	}

	public long getTamanho() {
		return tamanho;
	}

	public void setTamanho(long tamanho) {
		this.tamanho = tamanho;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isUmaFoto() {
		return umaFoto;
	}

	public void setUmaFoto(boolean umaFoto) {
		this.umaFoto = umaFoto;
	}

	@Override
	public String toString() {
		return "Arquivo [nome=" + nome + ", nomeOriginal=" + nomeOriginal + ", tamanho=" + tamanho + ", descricao="
				+ descricao + ", eFoto=" + umaFoto + "]";
	}
	
}
