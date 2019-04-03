package br.edu.ifpb.pweb1.model.dao.impdb;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pweb1.model.dao.FeedGrupoDAO;
import br.edu.ifpb.pweb1.model.domain.FeedGrupo;
import br.edu.ifpb.pweb1.model.domain.Grupo;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public class FeedGrupoDAOImpDB implements FeedGrupoDAO {
	
	private Usuario self;
	private GrupoDaoImpl grupoDao;
	
	public FeedGrupoDAOImpDB(Usuario self) {
		this.self = self;
		grupoDao = new GrupoDaoImpl();
	}
	
	private void atualizarFeedGrupo(FeedGrupo feedGrupo)throws DataAccessException {
		UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl();
		feedGrupo.setParticipa(grupoDao.participa(self.getId(), feedGrupo.getGrupo().getId()));
		feedGrupo.setAdministrador(grupoDao.eAdministrador(self.getId(), feedGrupo.getGrupo().getId()));
		feedGrupo.setQtdParticipantes(grupoDao.qtdParticipantes(feedGrupo.getGrupo().getId()));
		feedGrupo.setUsuarios(usuarioDao.participaGrupo(feedGrupo.getGrupo().getId()));
		usuarioDao.mudarStatus(self, feedGrupo.getUsuarios());
	}
	

	@Override
	public void criar(FeedGrupo novoFeedGrupo) throws DataAccessException {
		grupoDao.criar(novoFeedGrupo.getGrupo());
		atualizarFeedGrupo(novoFeedGrupo);
		
	}

	@Override
	public void excluir(int idFeedGrupo) throws DataAccessException {
		grupoDao.excluir(idFeedGrupo);		
	}

	@Override
	public void adicionarUsuario(int idFeedGrupo, int idUsuario) throws DataAccessException {
		grupoDao.adicionarUsuario(idFeedGrupo, idUsuario);
		
	}

	@Override
	public void removerUsuario(int idFeedGrupo, int idUsuario) throws DataAccessException {
		grupoDao.removerUsuario(idFeedGrupo, idUsuario);
		
	}

	@Override
	public FeedGrupo busca(int id) throws DataAccessException {
		FeedGrupo novoFeedGrupo = new FeedGrupo();
		novoFeedGrupo.setGrupo(grupoDao.busca(id));
		atualizarFeedGrupo(novoFeedGrupo);		
		return novoFeedGrupo;
	}

	@Override
	public int buscaIdPorNome(String nome) throws DataAccessException {
		return grupoDao.buscaIdPorNome(nome);
	}

	@Override
	public List<FeedGrupo> buscarGruposUsuarioParticipa(int idUsuario) throws DataAccessException {
		List<FeedGrupo> lista = new ArrayList<>();
		List<Grupo> grupos = grupoDao.buscarGruposUsuarioParticipa(idUsuario);
		for (Grupo grupo : grupos) {
			FeedGrupo feedGrupo = new FeedGrupo();
			feedGrupo.setGrupo(grupo);
			atualizarFeedGrupo(feedGrupo);
			lista.add(feedGrupo);
		}
		return lista;
	}

	@Override
	public List<FeedGrupo> admsGrupo(int idUsuario) throws DataAccessException {
		List<FeedGrupo> lista = new ArrayList<>();
		List<Grupo> grupos = grupoDao.admsGrupo(idUsuario);
		for (Grupo grupo : grupos) {
			FeedGrupo feedGrupo = new FeedGrupo();
			feedGrupo.setGrupo(grupo);
			atualizarFeedGrupo(feedGrupo);
			lista.add(feedGrupo);
		}
		return lista;
	}

	@Override
	public void adicionarAdministrador(int idUsuario, int idFeedGrupo) throws DataAccessException {
		grupoDao.adicionarAdministrador(idUsuario, idFeedGrupo);
		
	}

	public Usuario getSelf() {
		return self;
	}

	public void setSelf(Usuario self) {
		this.self = self;
	}

	

}
