package br.edu.ifpb.pweb1.model.dao.impdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pweb1.model.dao.GrupoDao;
import br.edu.ifpb.pweb1.model.domain.Grupo;
import br.edu.ifpb.pweb1.model.jdbc.ConnectionFactory;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public class GrupoDaoImpl implements GrupoDao{
	private Connection connection;
	
	public GrupoDaoImpl() {
		connection = ConnectionFactory.getInstance().getConnection();
	}
	
	private Grupo lerTabela(ResultSet rs) throws DataAccessException{
		try {
			return new Grupo(
				rs.getInt("id"),
				rs.getBoolean("ativo"),
				rs.getTimestamp("dataHora").toLocalDateTime(),
				rs.getString("nome"),
				rs.getString("descricao"),
				rs.getString("foto"));
		}catch (Exception e) {
			throw new DataAccessException("Falha ao recuperar da tabela");
		}
	}
	
	@Override
	public void criar(Grupo novoGrupo) throws DataAccessException {
		try {
		String sql = "INSERT INTO grupo (ativo,nome,descricao,foto)"
				+ " VALUES (?,?,?,?)"
				+ " RETURNING id ";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setBoolean(1, novoGrupo.isAtivo());
		statement.setString(2, novoGrupo.getNome());
		statement.setString(3, novoGrupo.getDescricao());
		statement.setString(4, novoGrupo.getFoto());
		ResultSet rs = statement.executeQuery();
		rs.next();
		novoGrupo.setId(rs.getInt(1));
		}catch (Exception e) {
			throw new DataAccessException("Falha ao criar Grupo");
		}
	}

	@Override
	public void excluir(int idGrupo) throws DataAccessException {
		try {
		String query = "DELETE FROM grupo WHERE id=?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, idGrupo);
		statement.execute();
		}catch (Exception e) {
			throw new DataAccessException("Falha ao excluir grupo");
		}
	}
	
	@Override
	public void editar(Grupo grupo) throws DataAccessException {
		try {
			String query = "UPDATE grupo SET"
					+ " ativo=?, datahora=?, nome=?, descricao=?, foto=? "
					+ " WHERE id = ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setBoolean(1, grupo.isAtivo());
			stm.setTimestamp(2, Timestamp.valueOf(grupo.getDataHora()));
			stm.setString(3, grupo.getNome());
			stm.setString(4, grupo.getDescricao());
			stm.setString(5, grupo.getFoto());
			stm.setInt(6, grupo.getId());
			stm.executeUpdate();
		}catch (Exception e) {
			throw new DataAccessException("Falha ao editar grupo");
		}
		
	}

	@Override
	public boolean participa(int usuarioId, int grupoId) throws DataAccessException {
		try {
			String query = "SELECT FROM participagrupo"
					+ " WHERE usuarioid = ? AND grupoid = ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuarioId);
			stm.setInt(2, grupoId);
			ResultSet rs =  stm.executeQuery();		
			return rs.next();
		}catch (Exception e) {
			throw new DataAccessException("Falha ao verificar se usuario participa do grupo");			
		}		
	}
	
	@Override
	public boolean eAdministrador(int usuarioId, int grupoId) throws DataAccessException {
		try {
			String query = "SELECT FROM admgrupo"
					+ " WHERE usuarioid = ? AND grupoid = ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuarioId);
			stm.setInt(2, grupoId);
			ResultSet rs =  stm.executeQuery();		
			return rs.next();
		}catch (Exception e) {
			throw new DataAccessException("Falha ao verificar se usuario participa do grupo");			
		}	
	}

	@Override
	public int qtdParticipantes(int grupoId) throws DataAccessException {
		try {
			String query = "SELECT count(*) FROM participagrupo"
					+ " WHERE grupoid = ? ";
			PreparedStatement stm = connection.prepareStatement(query);		
			stm.setInt(1, grupoId);
			ResultSet rs =  stm.executeQuery();
			if(rs.next())
				return rs.getInt(1);
			return 0;
		}catch (Exception e) {
			throw new DataAccessException("Falha ao recuperar a quantidade de participantes");
		}
	}

	@Override
	public void adicionarUsuario(int idGrupo,int idUsuario) throws DataAccessException {
		try {
		String sql = new String("INSERT INTO participagrupo (usuarioid,grupoid) VALUES (?,?)");
		PreparedStatement statement = connection.prepareStatement(sql); 		
		statement.setInt(1, idUsuario);
		statement.setInt(2,idGrupo);
		statement.execute();
		}catch (Exception e) {
			throw new DataAccessException("Falha ao adicioanar Usuario ao grupo");
		}
	}

	@Override
	public void removerUsuario(int idGrupo,int idUsuario) throws DataAccessException {
		try {
		String sql = new String("DELETE FROM participagrupo WHERE usuarioid = ? and grupoid = ?");
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, idUsuario);
		statement.setInt(2, idGrupo);
		statement.execute();
		}catch (Exception e) {
			throw new DataAccessException("Falha ao remover usuario ao grupo");
		}
	}

	@Override
	public Grupo busca(int id) throws DataAccessException {
		try {
			String query = "SELECT * FROM grupo "
					+ "WHERE id = ? and ativo = true";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				return lerTabela(rs);
			}
		}catch (Exception e) {
			throw new DataAccessException("Fala ao buscar grupo");
		}
		return null;
	}
	
	public int buscaIdPorNome(String nome) throws DataAccessException {
		try {
			String query = "SELECT id FROM grupo "
					+ "WHERE nome = ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setString(1, nome);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		}catch (Exception e) {
			throw new DataAccessException("Fala ao buscar grupo");
		}
		return -1;
	}

	@Override
	public List<Grupo> buscarGruposUsuarioParticipa(int idUsuario) throws DataAccessException {
		List<Grupo> gruposUsuarioParticipa = new ArrayList<>();
		try {
			String query = "SELECT * FROM grupo G WHERE  "
					+ " EXISTS(SELECT FROM participagrupo "
					+ " WHERE G.id = grupoid AND usuarioid = ?)";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, idUsuario);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				gruposUsuarioParticipa.add(lerTabela(rs));
			}
		}catch (Exception e) {
			throw new DataAccessException("Fala ao buscar grupo");
		}
		return gruposUsuarioParticipa;
	}

	@Override
	public List<Grupo> admsGrupo(int idUsuario) throws DataAccessException {
		List<Grupo> IDgruposUsuarioadministra = new ArrayList<>();
		List<Grupo> gruposUsuarioAdministra;
		try {
			String query = "SELECT grupoid FROM admgrupo "
					+ "WHERE usuarioid = ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, idUsuario);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				Grupo gp = new Grupo();
				gp.setId(rs.getInt(1));
				IDgruposUsuarioadministra.add(gp);
			}
			gruposUsuarioAdministra = new ArrayList<>();
			for(Grupo gp : IDgruposUsuarioadministra ) {
				gruposUsuarioAdministra.add(busca(gp.getId()));
			}
		}catch (Exception e) {
			throw new DataAccessException("Fala ao buscar grupo");
		}
		return gruposUsuarioAdministra;
	}

	@Override
	public void adicionarAdministrador(int idUsuario, int idGrupo) throws DataAccessException {
		try {
			String sql = new String("INSERT INTO admgrupo (usuarioid,grupoid) VALUES (?,?)");
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, idUsuario);
			statement.setInt(2,idGrupo);
			statement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
