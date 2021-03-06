package br.edu.ifpb.pweb1.model.dao.impdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;

import br.edu.ifpb.pweb1.model.dao.TextoDAO;
import br.edu.ifpb.pweb1.model.domain.Publicacao;
import br.edu.ifpb.pweb1.model.domain.Texto;
import br.edu.ifpb.pweb1.model.jdbc.ConnectionFactory;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;
import redis.clients.jedis.Jedis;

import java.util.logging.Logger;

public class TextoDAOImpDB implements TextoDAO {

	private Connection connection;
	private PublicacaoDAOImpDB publicacaoDAO;
	private UsuarioDaoImpl usuarioDAO;
	private Gson gson;
	private Jedis jedis;
	private Long jedisTimeout;
	private static String prefRedis = "text";	
	
	private static Logger log = Logger.getLogger(TextoDAOImpDB.class.getName());
	
	public TextoDAOImpDB() {
		connection = ConnectionFactory.getInstance().getConnection();
		publicacaoDAO = new PublicacaoDAOImpDB();
		usuarioDAO = new UsuarioDaoImpl();
		/*Startando o redis*/
		jedis = ConnectionFactory.getInstance().getRedis();
		jedisTimeout = ConnectionFactory.getInstance().getRedisTimeout();
		gson = new Gson();
	}
	
	private void salvaRedis(Texto texto) {	
		log.info("Salva Texto no Redis");
		String json = gson.toJson(texto);
		jedis.psetex(prefRedis+texto.getId(), jedisTimeout, json);		
	}	
	
	private boolean buscaRedis(int id, Texto texto) {
		log.info("Buscar Texto no Redis");
		if(!jedis.exists(prefRedis+id))
			return false;
		String json = jedis.get(prefRedis+id);
		jedis.psetex(prefRedis+id, jedisTimeout, json);
		Texto nTexto = gson.fromJson(json, Texto.class);
		texto.setId(nTexto.getId());
		texto.setAtivo(nTexto.getAtivo());
		texto.setDatahora(nTexto.getDatahora());
		texto.setUsuario(nTexto.getUsuario());
		texto.setQtdCurtidas(nTexto.getQtdCurtidas());
		texto.setQtdComentarios(nTexto.getQtdComentarios());
		texto.setPublicacao(nTexto.getPublicacao());
		return true;
	}
	
	private void excluiRedis(int id) {
		log.info("Excluir Texto no Redis");
		log.info("Salva no Redis");
		jedis.del(prefRedis+id);		
	}
	
	private void lerTabela(Texto texto, ResultSet rs) throws DataAccessException, SQLException {
		texto.setId(rs.getInt("id"));
		texto.setAtivo(rs.getBoolean("ativo"));
		texto.setDatahora(rs.getTimestamp("datahora").toLocalDateTime());		
		texto.setUsuario(usuarioDAO.buscarPorId(rs.getInt("usuarioid")));
		texto.setQtdCurtidas(rs.getInt("qtdCurtidas"));
		texto.setQtdComentarios(rs.getInt("qtdComentarios"));
		texto.setPublicacao(publicacaoDAO.buscar(rs.getInt("id")));
		salvaRedis(texto);
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
			Publicacao publicacao = texto.getPublicacao();
			publicacao.setTextoId(texto.getId());
			new PublicacaoDAOImpDB().cria(publicacao);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Falha ao criar texto");
		}
		salvaRedis(texto);
		return texto.getId();
	}

	@Override
	public void edita(Texto texto) throws DataAccessException {
		try {
			String query = "UPDATE texto SET " 
					+ " ativo=?,datahora=?,usuarioid=?, qtdCurtidas=?, qtdComentarios=? "
					+ " WHERE id=? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setBoolean(1, texto.getAtivo());			
			stm.setTimestamp(2, java.sql.Timestamp.valueOf(texto.getDatahora()));
			stm.setInt(3, texto.getUsuario().getId());
			stm.setInt(4, texto.getQtdCurtidas());
			stm.setInt(5, texto.getQtdComentarios());
			stm.setInt(6, texto.getId());
			
			stm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Falha ao editar texto");
		}
		salvaRedis(texto);
	}

	@Override
	public void exclui(int textoId) throws DataAccessException {
		try {
			String query = "DELETE FROM texto" + " WHERE id = ?";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, textoId);
			stm.executeUpdate();
			
			publicacaoDAO.exclui(textoId);
		} catch (Exception e) {
			throw new DataAccessException("Falha ao excluir texto");
		}
		excluiRedis(textoId);
	}

	@Override
	public void exclui(Texto texto) throws DataAccessException {
		exclui(texto.getId());
	}

	@Override
	public Texto buscar(int id) throws DataAccessException {
		Texto texto = new Texto();
		try {
			buscar(id, texto);
		} catch (Exception e) {
			throw new DataAccessException("Falha ao buscar texto");
		}
		return texto;
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
		if (buscaRedis(id, texto)) return;
		try {
			String query = "SELECT * FROM texto " + "WHERE id = ? ";
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
			String query = "SELECT * FROM texto ";
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
			String query = "SELECT * FROM texto" + " OFFSET ? LIMIT ? ";
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

	@Override
	public void atualizeContagem(Texto texto) throws DataAccessException {		
		texto.setQtdCurtidas(new CurteDAOImpDB().quant(texto));
		edita(texto);		
	}
	
	

}
