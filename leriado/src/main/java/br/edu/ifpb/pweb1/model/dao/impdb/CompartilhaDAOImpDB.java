package br.edu.ifpb.pweb1.model.dao.impdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pweb1.model.dao.CompartilhaDAO;
import br.edu.ifpb.pweb1.model.domain.Compartilha;
import br.edu.ifpb.pweb1.model.domain.Grupo;
import br.edu.ifpb.pweb1.model.domain.Texto;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.ConnectionFactory;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public class CompartilhaDAOImpDB implements CompartilhaDAO {

	private Connection connection;

	public CompartilhaDAOImpDB() {
		connection = ConnectionFactory.getInstance().getConnection();
	}

	private Compartilha lerTabela(ResultSet rs) throws DataAccessException {
		Texto texto = null;
		try {
			texto = new TextoDAOImpDB().buscar(rs.getInt("textoid"));			
			Grupo grupo = new GrupoDaoImpl().busca(rs.getInt("grupoid"));
			Usuario usuario = new UsuarioDaoImpl().buscarPorId(rs.getInt("usuarioid"));
			Compartilha compartilha = new Compartilha();
			compartilha.setDataHora(rs.getTimestamp("datahora").toLocalDateTime());
			compartilha.setUsuario(usuario);
			compartilha.setTexto(texto);
			compartilha.setGrupo(grupo);
			return compartilha; 					
		} catch (Exception e) {
			throw new DataAccessException("Falha ao ler tabela de publicação inválido");
		}		
	}

	@Override
	public void cria(Compartilha compartilha) throws DataAccessException {
		exclui(compartilha);
		cria(compartilha.getUsuario().getId(),
				compartilha.getTexto().getId(),
				compartilha.getGrupo().getId());
	}	

	@Override
	public void cria(int usuarioId, int textoId, int grupoId) throws DataAccessException {
		try {			
			String query = "INSERT INTO compartilha (usuarioid,textoid,grupoid) " + "VALUES (?,?,?) ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuarioId);
			stm.setInt(2, textoId);
			stm.setInt(3, grupoId);
			stm.execute();
		} catch (Exception e) {
			throw new DataAccessException("Falha ao compartilhar publicação.");
		}
	}

	@Override
	public void exclui(Compartilha compartilha) throws DataAccessException {
		exclui(
				compartilha.getUsuario().getId(),
				compartilha.getTexto().getId(),
				compartilha.getGrupo().getId());	
	}
	
	@Override
	public void exclui(int usuarioId, int textoId, int grupoId) throws DataAccessException {
		try {
			String query = "DELETE FROM compartilha " + " WHERE (usuarioid = ?) AND " + " (textoid = ?) AND "
					+ " (grupoid = ?)";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuarioId);
			stm.setInt(2, textoId);
			stm.setInt(3, grupoId);
			stm.execute();
		} catch (Exception e) {
			throw new DataAccessException("Falha ao remover compartilhamento");
		}
	}
	

	@Override
	public boolean existe(int usuarioId, int textoId, int grupoId) throws DataAccessException {
		try {
			String query = "SELECT COUNT(*) FROM compartilha " +
					"  WHERE (usuarioid = ?) AND (textoid = ?) AND (grupoid = ?) ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuarioId);
			stm.setInt(2, textoId);
			stm.setInt(3, grupoId);
			ResultSet rs = stm.executeQuery();
			if(rs.next())
				return (rs.getInt(1) > 0);			
		}catch (Exception e) {
			throw new DataAccessException("Falha ao verificar se compartilhamento existe");
		}
		return false;
	}

	@Override
	public int quant() throws DataAccessException {
		try {
			String query = "SELECT COUNT(*) FROM compartilha";
			PreparedStatement stm = connection.prepareStatement(query);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			throw new DataAccessException("Falha ao recuperar quantidade");
		}
		return 0;
	}

	@Override
	public int quant(Grupo grupo) throws DataAccessException {
		try {
			String query = "SELECT COUNT(*) FROM compartilha " + "WHERE grupoid = ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, grupo.getId());
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			throw new DataAccessException("Falha ao recuperar quantidade");
		}
		return 0;
	}

	@Override
	public int quant(Usuario usuario) throws DataAccessException {
		try {
			String query = "SELECT COUNT(*) FROM compartilha " + "WHERE usuarioid = ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuario.getId());
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			throw new DataAccessException("Falha ao recuperar quantidade");
		}
		return 0;
	}

	@Override
	public int quant(Texto texto) throws DataAccessException {
		try {
			String query = "SELECT COUNT(*) FROM compartilha " + "WHERE textoid = ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, texto.getId());
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			throw new DataAccessException("Falha ao recuperar quantidade");
		}
		return 0;
	}

	@Override
	public List<Compartilha> lista() throws DataAccessException {
		List<Compartilha> comp = new ArrayList<Compartilha>();
		try {
			String query = "SELECT *, tipotexto(textoid) AS tipo FROM compartilha " + " ORDER BY datahora DESC ";
			PreparedStatement stm = connection.prepareStatement(query);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				comp.add(lerTabela(rs));
			}

		} catch (Exception e) {
			throw new DataAccessException("Falha ao listar compartilhamento");
		}
		return comp;
	}

	@Override
	public List<Compartilha> lista(Grupo grupo) throws DataAccessException {
		List<Compartilha> comp = new ArrayList<Compartilha>();
		try {
			String query = "SELECT * FROM compartilha " + " WHERE grupoid = ? "
					+ " ORDER BY datahora DESC ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, grupo.getId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				comp.add(lerTabela(rs));
			}

		} catch (Exception e) {
			throw new DataAccessException("Falha ao listar compartilhamento");
		}
		return comp;
	}

	@Override
	public List<Compartilha> lista(Usuario usuario) throws DataAccessException {
		List<Compartilha> comp = new ArrayList<Compartilha>();
		try {
			String query = "SELECT * FROM compartilha " + " WHERE usuarioid = ? "
					+ " ORDER BY datahora DESC ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuario.getId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				comp.add(lerTabela(rs));
			}

		} catch (Exception e) {
			throw new DataAccessException("Falha ao listar compartilhamento");
		}
		return comp;
	}

	@Override
	public List<Compartilha> lista(Texto texto) throws DataAccessException {
		List<Compartilha> comp = new ArrayList<Compartilha>();
		try {
			String query = "SELECT * FROM compartilha " + " WHERE publicacaoid = ? "
					+ " ORDER BY datahora DESC ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, texto.getId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				comp.add(lerTabela(rs));
			}

		} catch (Exception e) {
			throw new DataAccessException("Falha ao listar compartilhamento");
		}
		return comp;
	}

	@Override
	public List<Compartilha> lista(int inicio, int quant) throws DataAccessException {
		List<Compartilha> comp = new ArrayList<Compartilha>();
		try {
			String query = "SELECT * FROM compartilha" + " ORDER BY datahora DESC "
					+ " OFFSET ? LIMIT ?";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, inicio);
			stm.setInt(2, quant);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				comp.add(lerTabela(rs));
			}

		} catch (Exception e) {
			throw new DataAccessException("Falha ao listar compartilhamento");
		}
		return comp;
	}

	@Override
	public List<Compartilha> lista(Grupo grupo, int inicio, int quant) throws DataAccessException {
		List<Compartilha> comp = new ArrayList<Compartilha>();
		try {
			String query = "SELECT * FROM compartilha " + " WHERE grupoid = ? "
					+ " ORDER BY datahora DESC " + " OFFSET ? LIMIT ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, grupo.getId());
			stm.setInt(2, inicio);
			stm.setInt(3, quant);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				comp.add(lerTabela(rs));
			}

		} catch (Exception e) {
			throw new DataAccessException("Falha ao listar compartilhamento");
		}
		return comp;
	}

	@Override
	public List<Compartilha> lista(Usuario usuario, int inicio, int quant) throws DataAccessException {
		List<Compartilha> comp = new ArrayList<Compartilha>();
		try {
			String query = "SELECT * FROM compartilha " + " WHERE usuarioid = ? "
					+ " ORDER BY datahora DESC " + " OFFSET ? LIMIT ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuario.getId());
			stm.setInt(2, inicio);
			stm.setInt(3, quant);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				comp.add(lerTabela(rs));
			}

		} catch (Exception e) {
			throw new DataAccessException("Falha ao listar compartilhamento");
		}
		return comp;
	}

	@Override
	public List<Compartilha> lista(Texto texto, int inicio, int quant) throws DataAccessException {
		List<Compartilha> comp = new ArrayList<Compartilha>();
		try {
			String query = "SELECT * FROM compartilha " + " WHERE publicacaoid = ? "
					+ " ORDER BY datahora DESC " + " OFFSET ? LIMIT ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, texto.getId());
			stm.setInt(2, inicio);
			stm.setInt(3, quant);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				comp.add(lerTabela(rs));
			}

		} catch (Exception e) {
			throw new DataAccessException("Falha ao listar compartilhamento");
		}
		return comp;
	}

	@Override
	public int quantFeed(Usuario usuario) throws DataAccessException {
		try {
			String query = "SELECT COUNT(*) FROM compartilha C WHERE "
					+ "EXISTS (SELECT FROM segue S WHERE (S.seguidoid = C.usuarioid) AND (S.segueid = ?)) OR "
					+ "EXISTS (SELECT FROM participagrupo P WHERE (C.grupoid = P.grupoid) AND (P.usuarioid = ?)) ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuario.getId());
			stm.setInt(2, usuario.getId());
			ResultSet rs = stm.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			throw new DataAccessException("Falha ao recuperar a quantidade de publicções no feed");
		}
		return 0;
	}

	@Override
	public List<Compartilha> feed(Usuario usuario, int inicio, int quant) throws DataAccessException {
		List<Compartilha> comps = new ArrayList<Compartilha>();
		try {
			String query = "SELECT * FROM compartilha C WHERE "
					+ "EXISTS (SELECT FROM segue S WHERE (S.seguidoid = C.usuarioid) AND (S.segueid = ?)) OR "
					+ "EXISTS (SELECT FROM participagrupo P WHERE (C.grupoid = P.grupoid) AND (P.usuarioid = ?)) "
					+ "ORDER BY C.datahora DESC " + "OFFSET ? LIMIT ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuario.getId());
			stm.setInt(2, usuario.getId());
			stm.setInt(3, inicio);
			stm.setInt(4, quant);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				comps.add(lerTabela(rs));
			}
		} catch (Exception e) {
			throw new DataAccessException("Falha ao recuperar o feed");
		}
		return comps;
	}

}
