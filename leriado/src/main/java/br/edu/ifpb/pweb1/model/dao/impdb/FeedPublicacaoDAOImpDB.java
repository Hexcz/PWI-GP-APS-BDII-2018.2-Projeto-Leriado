package br.edu.ifpb.pweb1.model.dao.impdb;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pweb1.model.dao.FeedPublicacaoDAO;
import br.edu.ifpb.pweb1.model.domain.Compartilha;
import br.edu.ifpb.pweb1.model.domain.FeedPublicacao;
import br.edu.ifpb.pweb1.model.domain.Grupo;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

/**
 * @author isleimar
 *
 */
public class FeedPublicacaoDAOImpDB implements FeedPublicacaoDAO{
	
	private Usuario self;
	private CurteDAOImpDB curteDao;
	private CompartilhaDAOImpDB compartilhaDao;
	private ComentarioDAOImpDB comentarioDao;
	
	public FeedPublicacaoDAOImpDB(Usuario self) {
		this.self = self;
		curteDao = new CurteDAOImpDB();		
		compartilhaDao = new CompartilhaDAOImpDB();
		comentarioDao = new ComentarioDAOImpDB();
	}
	
		
	private FeedPublicacao criarFeed(Compartilha compartilha) throws DataAccessException {
		return new FeedPublicacao(compartilha, curteDao.existe(compartilha.getTexto().getId(), self.getId()), new ArrayList<>());	
	}
	
	private List<FeedPublicacao> feedCompartilhas(List<Compartilha> compartilhas) throws DataAccessException{
		List<FeedPublicacao> feeds = new ArrayList<>();		
		for (Compartilha compartilha : compartilhas) {
			FeedPublicacao feed = criarFeed(compartilha);
			if( feed != null) {
				feeds.add(feed);
			}
		}
		return feeds;		
	}

	@Override
	public int quantFeed() throws DataAccessException {		
		return compartilhaDao.quantFeed(self);
	}

	@Override
	public int quantFeed(Usuario usuario) throws DataAccessException {		
		return compartilhaDao.quant(usuario);
	}

	@Override
	public int quantFeed(Grupo grupo) throws DataAccessException {
		return compartilhaDao.quant(grupo);
	}

	@Override
	public List<FeedPublicacao> listaFeed(int inicio, int quant) throws DataAccessException {	
		return feedCompartilhas(compartilhaDao.feed(self, inicio, quant));		
	}

	@Override
	public List<FeedPublicacao> listaUsuario(Usuario usuario, int inicio, int quant) throws DataAccessException {
		return feedCompartilhas(compartilhaDao.lista(usuario, inicio, quant));		
	}

	@Override
	public List<FeedPublicacao> listaGrupo(Grupo grupo, int inicio, int quant) throws DataAccessException {
		return feedCompartilhas(compartilhaDao.lista(grupo, inicio, quant));
	}

	@Override
	public void carregarComentarios(FeedPublicacao feedPublicacao) throws DataAccessException {
		feedPublicacao.setComentarios(comentarioDao.lista(feedPublicacao.getCompartilha().getTexto().getId()));
		
	}

	@Override
	public void carregarComentarios(FeedPublicacao feedPublicacao, int inicio, int quant) throws DataAccessException {
		feedPublicacao.setComentarios(comentarioDao.lista(feedPublicacao.getCompartilha().getTexto().getId(),inicio,quant));
		
	}

	public Usuario getSelf() {
		return self;
	}

	public void setSelf(Usuario self) {
		this.self = self;
	}

}
