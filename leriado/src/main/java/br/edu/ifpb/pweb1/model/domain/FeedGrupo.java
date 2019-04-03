package br.edu.ifpb.pweb1.model.domain;

import java.util.List;

public class FeedGrupo {
	
		private Grupo grupo;
		private boolean participa;
		private boolean administrador;
		private int qtdParticipantes;
		private List<Usuario> usuarios;
		
		public FeedGrupo() {}
		
		public FeedGrupo(Grupo grupo, boolean participa, boolean administrador, int qtdParticipantes) {
			super();
			this.grupo = grupo;
			this.participa = participa;
			this.administrador = administrador;
			this.qtdParticipantes = qtdParticipantes;
		}

		public Grupo getGrupo() {
			return grupo;
		}

		public void setGrupo(Grupo grupo) {
			this.grupo = grupo;
		}

		public boolean isParticipa() {
			return participa;
		}

		public void setParticipa(boolean participa) {
			this.participa = participa;
		}

		public boolean isAdministrador() {
			return administrador;
		}

		public void setAdministrador(boolean administrador) {
			this.administrador = administrador;
		}

		public int getQtdParticipantes() {
			return qtdParticipantes;
		}

		public void setQtdParticipantes(int qtdParticipantes) {
			this.qtdParticipantes = qtdParticipantes;
		}

		public List<Usuario> getUsuarios() {
			return usuarios;
		}

		public void setUsuarios(List<Usuario> usuarios) {
			this.usuarios = usuarios;
		}
		
		
}
