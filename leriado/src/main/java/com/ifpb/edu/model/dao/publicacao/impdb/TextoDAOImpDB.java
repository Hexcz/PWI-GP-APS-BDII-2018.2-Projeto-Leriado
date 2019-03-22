package com.ifpb.edu.model.dao.publicacao.impdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ifpb.edu.model.dao.UsuarioDaoImpl;
import com.ifpb.edu.model.dao.publicacao.TextoDAO;
import com.ifpb.edu.model.domain.publicacao.Texto;
import com.ifpb.edu.model.jdbc.ConnectionFactory;
import com.ifpb.edu.model.jdbc.DataAccessException;

public class TextoDAOImpDB implements TextoDAO {

	private Connection connection;

	public TextoDAOImpDB() {
		connection = ConnectionFactory.getInstance().getConnection();
	}

	private void lerTabela(Texto texto, ResultSet rs) throws DataAccessException, SQLException {
		texto.setId(rs.getInt("id"));
		texto.setAtivo(rs.getBoolean("ativo"));
		texto.setDatahora(rs.getTimestamp("datahora").toLocalDateTime());
		texto.setUsuario(new UsuarioDaoImpl().buscarPorId(rs.getInt("usuarioid")));
	}

	@Override
	public int cria(Texto texto) throws DataAccessException {
		try {
			String query = "INSERT INTO texto (usuarioid) " + "VALUES(?) " + "RETURNING id,ativo,datahora";
			PreparedStatement stm = connection.prepareStatement(query);			
			stm.setInt(1, texto.getUsuario().getId());
			stm.execute();
			ResultSet rs = stm.getResultSet();
			if (rs.next()) {
				texto.setId(rs.getInt("id"));
				texto.setAtivo(rs.getBoolean("ativo"));
				texto.setDatahora(rs.getTimestamp("datahora").toLocalDateTime());
			}
			texto.getPublicacao().setTextoId(texto.getId());
			new PublicacaoDAOImpDB().cria(texto.getPublicacao());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Falha ao criar texto");
		}
		return texto.getId();
	}

	@Override
	public void edita(Texto texto) throws DataAccessException {
		try {
			String query = "UPDATE texto SET " + "ativo=?,datahora=?,usuarioid=?" + "WHERE id=?";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setBoolean(1, texto.getAtivo());			
			stm.setTimestamp(2, java.sql.Timestamp.valueOf(texto.getDatahora()));
			stm.setInt(3, texto.getUsuario().getId());
			stm.setInt(4, texto.getId());
			stm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Falha ao editar texto");
		}
	}

	@Override
	public void exclui(Texto texto) throws DataAccessException {
		try {
			String query = "DELETE FROM texto" + " WHERE id = ?";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, texto.getId());
			stm.executeUpdate();
		} catch (Exception e) {
			throw new DataAccessException("Falha ao excluir texto");
		}

	}

	@Override
	public Optional<Texto> buscar(int id) throws DataAccessException {
		Texto texto = new Texto();
		try {
			buscar(id, texto);
		} catch (Exception e) {
			throw new DataAccessException("Falha ao buscar texto");
		}
		return Optional.of(texto);
	}

	@Override
	public void buscar(Texto texto) throws DataAccessException {
		try {
			int id = texto.getId();
			buscar(id, texto);
		} catch (Exception e) {
			throw new DataAccessException("Falha ao buscar texto");
		}
	}

	@Override
	public void buscar(int id, Texto texto) throws DataAccessException {
		try {
			String query = "SELECT *, tipotexto(id) AS tipo FROM texto " + "WHERE id = ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				lerTabela(texto, rs);
			} else
				throw new Exception();
		} catch (Exception e) {
			throw new DataAccessException("Falha ao buscar texto");
		}
	}

	@Override
	public int quant() throws DataAccessException {
		try {
			String query = "SELECT COUNT(*) FROM texto ";
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(query);
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			throw new DataAccessException("Falha ao recuperar a quantidade de textos");
		}
		return 0;
	}

	@Override
	public List<Texto> lista() throws DataAccessException {
		List<Texto> textos = new ArrayList<Texto>();
		try {
			String query = "SELECT *, tipotexto(id) AS tipo FROM texto ";
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				Texto texto = new Texto();
				lerTabela(texto, rs);
				textos.add(texto);
			}

		} catch (Exception e) {
			throw new DataAccessException("Falha ao listar textos.");
		}
		return textos;
	}

	@Override
	public List<Texto> lista(int inicio, int quant) throws DataAccessException {
		List<Texto> textos = new ArrayList<Texto>();
		try {
			String query = "SELECT *, tipotexto(id) AS tipo FROM texto" + " OFFSET ? LIMIT ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, inicio);
			stm.setInt(2, quant);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				Texto texto = new Texto();
				lerTabela(texto, rs);
				textos.add(texto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Falha ao listar textos.");
		}
		return textos;
	}

}
