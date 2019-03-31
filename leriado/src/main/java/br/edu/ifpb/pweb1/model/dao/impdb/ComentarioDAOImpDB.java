package br.edu.ifpb.pweb1.model.dao.impdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pweb1.model.dao.ComentarioDAO;
import br.edu.ifpb.pweb1.model.domain.Texto;
import br.edu.ifpb.pweb1.model.jdbc.ConnectionFactory;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;


public class ComentarioDAOImpDB implements ComentarioDAO {

	private Connection connection;
	private TextoDAOImpDB textoDao;

	public ComentarioDAOImpDB() {
		connection = ConnectionFactory.getInstance().getConnection();
		textoDao = new TextoDAOImpDB();
	}

	@Override
	public void cria(Texto comentario, int textoId) throws DataAccessException {
		try {
			textoDao.cria(comentario);
			String query = "INSERT INTO comentario (textoid,respondeid) " + "VALUES (?,?)";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, comentario.getId());
			stm.setInt(2, textoId);
			stm.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Falha ao criar comentário");
		}
	}

	@Override
	public void edita(Texto comentario) throws DataAccessException {		
		try {
			textoDao.edita(comentario);
		} catch (Exception e) {
			throw new DataAccessException("Falha ao editar comentário");
		}
	}

	@Override
	public void exclui(Texto comentario) throws DataAccessException {		
		try {			
			textoDao.exclui(comentario);
		} catch (Exception e) {
			throw new DataAccessException("Falha ao excluir comentário");
		}
	}

	@Override
	public int quant(int textoId) throws DataAccessException {
		try {
			String query = "SELECT COUNT(*) FROM comentario " + "WHERE respondeid = ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, textoId);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			throw new DataAccessException("Falha ao recuperar a quantidade de comentários");
		}
		return 0;
	}

	@Override
	public List<Texto> lista(int textoId) throws DataAccessException {
		List<Texto> cometarios = new ArrayList<>();		
		try {
			String query = "SELECT textoid, " +
					  " (SELECT datahora FROM texto T WHERE T.id = C.textoid) AS datahora  " +	
					  " FROM comentario C" +
					  " WHERE C.respondeid = ? " +
					  " ORDER BY datahora DESC ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, textoId);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {				
				cometarios.add(textoDao.buscar(rs.getInt("textoid")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Falha ao listar comentários");
		}
		return cometarios;
	}

	@Override
	public List<Texto> lista(int textoId, int inicio, int quant) throws DataAccessException {
		List<Texto> cometarios = new ArrayList<>();		
		try {
			String query = "SELECT textoid, " +
					  " (SELECT datahora FROM texto T WHERE T.id = C.textoid) AS datahora  " +	
					  " FROM comentario C" +
					  " WHERE C.respondeid = ? " +
					  " ORDER BY datahora DESC "
					  + "OFFSET ? LIMIT ? ";			
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, textoId);
			stm.setInt(2, inicio);
			stm.setInt(3, quant);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {				
				cometarios.add(textoDao.buscar(rs.getInt("textoid")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Falha ao listar comentários");
		}
		return cometarios;
	}

}
