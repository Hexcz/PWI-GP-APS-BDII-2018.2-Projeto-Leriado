package br.edu.ifpb.pweb1.model.dao.impdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pweb1.model.dao.CurteDAO;
import br.edu.ifpb.pweb1.model.domain.Curte;
import br.edu.ifpb.pweb1.model.domain.Texto;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.ConnectionFactory;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public class CurteDAOImpDB implements CurteDAO{
	
	private Connection connection;
		
	public CurteDAOImpDB() {
		connection = ConnectionFactory.getInstance().getConnection();
	}
	
	private void atualizaTexto(int textoid) throws DataAccessException{
		TextoDAOImpDB textoDao = new TextoDAOImpDB(); 
		Texto texto = textoDao.buscar(textoid);
		texto.setQtdCurtidas(quant(texto));
		textoDao.edita(texto);
	}

	@Override
	public void cria(Curte curte) throws DataAccessException{
		cria(curte.getTexto().getId(), curte.getUsuario().getId());		
	}
		
	@Override
	public void cria(int textoId, int usuarioId) throws DataAccessException {
		try {
			String query = "INSERT INTO curte (textoid,usuarioid) "
					+ "VALUES(?,?)";
			PreparedStatement stm =  connection.prepareStatement(query);
			stm.setInt(1,textoId);
			stm.setInt(2,usuarioId);			
			stm.executeUpdate();
			atualizaTexto(textoId);
		}catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Falha ao criar curtida");
		}
		
	}

	@Override
	public void exclui(Curte curte) throws DataAccessException{
		exclui(curte.getTexto().getId(), curte.getUsuario().getId());		
	}
	

	@Override
	public void exclui(int textoId, int usuarioId) throws DataAccessException {
		try {
			String query = "DELETE FROM curte "
					+ "WHERE (textoid=?) AND (usuarioid=?) ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1,textoId);
			stm.setInt(2, usuarioId);
			stm.executeUpdate();	
			atualizaTexto(textoId);
		}catch (Exception e) {			
			throw new DataAccessException("Falha ao excluir curtida");
		}		
	}

	@Override
	public int quant(Texto texto) throws DataAccessException{
		try {
			String query = "SELECT COUNT(*) FROM curte "
					+ "WHERE textoid = ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, texto.getId());
			ResultSet rs = stm.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		}catch (Exception e) {
			throw new DataAccessException("Falha ao recuperar a quantidade de curtidas.");
		}
		return 0;
	}

	@Override
	public List<Curte> lista(Texto texto) throws DataAccessException{
		List<Curte> cutidas = new ArrayList<Curte>();
		TextoDAOImpDB textoDAO = new TextoDAOImpDB();
		UsuarioDaoImpl usuarioDAO = new UsuarioDaoImpl();
		Usuario usuario = null;
		int ti = texto.getId();
		try {
			texto = textoDAO.buscar(ti);
			String query = "SELECT * FROM curte "
					+ "WHERE textoid = ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, texto.getId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				usuario = usuarioDAO.buscarPorId(rs.getInt("usuarioid"));
				cutidas.add(new Curte(texto, usuario, rs.getTimestamp("datahora").toLocalDateTime()));
			}
			
		}catch (Exception e) {
			throw new DataAccessException("Falha ao listar curtidas.");
		}		
		return cutidas;
	}

	@Override
	public boolean existe(int textoId, int usuarioId) throws DataAccessException {
		try {
			String query = "SELECT EXISTS ( "
					+ "SELECT FROM curte WHERE (textoid = ?) AND (usuarioid = ?) )";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, textoId);
			stm.setInt(2, usuarioId);
			ResultSet rs = stm.executeQuery();
			if(rs.next()) {
				return rs.getBoolean(1);
			}
		}catch (Exception e) {
			throw new DataAccessException("Falha ao consultar curtida.");
		}
		return false;
	}
	
	

}
