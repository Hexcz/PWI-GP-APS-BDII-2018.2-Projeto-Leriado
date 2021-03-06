package br.edu.ifpb.pweb1.model.dao.impdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import br.edu.ifpb.pweb1.model.dao.UsuarioDao;
import br.edu.ifpb.pweb1.model.domain.Texto;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.ConnectionFactory;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public class UsuarioDaoImpl implements UsuarioDao{
	private Connection connection;
	private Driver driveNeo4j;
	
	public UsuarioDaoImpl() {
		connection = ConnectionFactory.getInstance().getConnection();
		driveNeo4j = ConnectionFactory.getInstance().getDriveNeo4J();
	}
	
	private Usuario lerTabela(ResultSet rs) throws SQLException {
		Usuario usuario = new Usuario();
		lerTabela(usuario, rs);		
		return usuario;
	}
	
	private void lerTabela(Usuario usuario, ResultSet rs) throws SQLException {		
		usuario.setId(rs.getInt("id"));
		usuario.setAtivo(rs.getBoolean("ativo"));
		usuario.setEmail(rs.getString("email"));
		usuario.setSenha("********");  //usuario.setSenha(rs.getString("senha"));
		usuario.setNome(rs.getString("nome"));
		usuario.setSobrenome(rs.getString("sobrenome"));
		usuario.setSexo(rs.getString("sexo"));
		usuario.setDatanasc(rs.getDate("datanasc").toLocalDate());
		usuario.setAcesso(rs.getInt("acesso"));
		usuario.setTelefone(rs.getString("telefone"));
		usuario.setRua(rs.getString("rua"));
		usuario.setCidade(rs.getString("cidade"));
		usuario.setEstado(rs.getString("estado"));
		usuario.setNumero(rs.getString("numero"));
		usuario.setCep(rs.getString("cep"));		
	}

	@Override
	public void criar(Usuario usuario) throws DataAccessException {
		try {
			String sql = "INSERT INTO usuario(email, senha, nome, sobrenome, sexo, datanasc, acesso, telefone, rua, cidade, estado, numero, cep)"
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" + " RETURNING id";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, usuario.getEmail());
			statement.setString(2, usuario.getSenha());
			statement.setString(3, usuario.getNome());
			statement.setString(4, usuario.getSobrenome());
			statement.setString(5, usuario.getSexo());
			statement.setDate(6, java.sql.Date.valueOf(usuario.getDatanasc()));
			statement.setInt(7, usuario.getAcesso());
			statement.setString(8, usuario.getTelefone());
			statement.setString(9, usuario.getRua());
			statement.setString(10, usuario.getCidade());
			statement.setString(11, usuario.getEstado());
			statement.setString(12, usuario.getNumero());
			statement.setString(13, usuario.getCep());
			ResultSet rs = statement.executeQuery();
			rs.next();
			usuario.setId(rs.getInt(1));
			/*NEO4J*/
			try(Session session = driveNeo4j.session()){
				Map<String, Object> mapa = new HashMap<>();
				mapa.put("id", usuario.getId());
				mapa.put("nome",usuario.getNome());
				
				session.run("CREATE (:Usuario{id:$id,nome:$nome})",mapa);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Falha ao criar usuário");
		}
	}

	@Override
	public void atualizar(Usuario usuarioNovo, Integer idUsuario) throws DataAccessException{
		try {
			String query = new String("UPDATE usuario SET "
					+ "ativo=?, email=?,senha=?,nome=?,sobrenome=?,sexo=?,datanasc=?,acesso=?,telefone=?,rua=?,cidade=?,estado=?,numero=?,cep=?"
					+ " WHERE id = ?");
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setBoolean(1, usuarioNovo.isAtivo());
			statement.setString(2, usuarioNovo.getEmail());
			statement.setString(3, usuarioNovo.getSenha());
			statement.setString(4, usuarioNovo.getNome());
			statement.setString(5, usuarioNovo.getSobrenome());
			statement.setString(6, usuarioNovo.getSexo());
			statement.setDate(7, java.sql.Date.valueOf(usuarioNovo.getDatanasc()));
			statement.setInt(8, usuarioNovo.getAcesso());
			statement.setString(9, usuarioNovo.getTelefone());
			statement.setString(10, usuarioNovo.getRua());
			statement.setString(11, usuarioNovo.getCidade());
			statement.setString(12, usuarioNovo.getEstado());
			statement.setString(13, usuarioNovo.getNumero());
			statement.setString(14, usuarioNovo.getCep());
			statement.setInt(15, idUsuario);
			statement.execute();
			/*NEO4J*/
			usuarioNovo.setId(idUsuario);
			try(Session session = driveNeo4j.session()){
				Map<String, Object> mapa = new HashMap<>();
				mapa.put("id", idUsuario);
				mapa.put("nome", usuarioNovo.getNome());
				session.run("MATCH(u:Usuario{id:$id})"
						+ " SET u.nome = $nome", mapa);				
			}
		} catch (Exception e) {
			throw new DataAccessException("Falha ao atualizar usuário");
		}
	}
	
	@Override
	public void remover(Integer idUsuario) throws DataAccessException{
		try {
		String query = "DELETE FROM usuario  WHERE id=?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, idUsuario);
		statement.executeUpdate();
		/*NEO4J*/
		try (Session session = driveNeo4j.session()){
			Map<String, Object> mapa = new HashMap<>();
			mapa.put("id", idUsuario);
			session.run("MATCH(u:Usuario{id:$id}) "
					+ " DETACH DELETE(u) ", mapa);
		}
		}catch (Exception e) {
			throw new DataAccessException("Falha ao remover usuário");
		}
	}
	
	@Override
	public void mudarFotoPerfil(Usuario usuario, Texto texto) throws DataAccessException {
		try {
			mudarFotoPerfil(usuario.getId(), texto.getId());
		}catch (Exception e) {
			throw new DataAccessException("Falha ao mudar foto do perfil");
		}
	}

	@Override
	public void mudarFotoPerfil(int usuarioId, int textoId) throws DataAccessException {
		try {
			String query = "INSERT INTO fotoperfil (usuarioid, textoid) VALUES (?,?)";								
			PreparedStatement stm = connection.prepareStatement(query);			
			stm.setInt(1, usuarioId);
			stm.setInt(2, textoId);
			stm.execute();
		}catch (Exception e) {
			throw new DataAccessException("Falha ao mudar foto do perfil");
		}		
	}
	
	@Override
	public void carregarFotoPerfil(Usuario usuario) throws DataAccessException {
		try {
			int usuarioId = usuario.getId();
			usuario.setArquivoFoto(carregarFotoPerfil(usuarioId));
		}catch (Exception e) {
			throw new DataAccessException("Falha ao carregar foto do perifl");
		}
	}	

	@Override
	public String carregarFotoPerfil(int usuarioId) throws DataAccessException {
		try {
			String query = "SELECT textoid FROM fotoperfil"
					+ " WHERE usuarioid = ? "
					+ " ORDER BY datahora DESC"
					+ " LIMIT 1";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuarioId);
			ResultSet rs = stm.executeQuery();
			if(! rs.next())	return "";
			int textoId = rs.getInt("textoid");
			Texto texto = new TextoDAOImpDB().buscar(textoId);
			try {
				String arquivo = texto.getPublicacao().getArquivos().get(0).getNome();
				return arquivo;
			}catch (Exception e) {
				return "";
			}
		}catch (Exception e) {
			throw new DataAccessException("Falha ao carregar foto do perifl");
		}
	}
	
	@Override
	public void carregarFotoPerfil(List<Usuario> usuarios) throws DataAccessException {
		for (Usuario usuario : usuarios) {
			carregarFotoPerfil(usuario);			
		}		
	}

	@Override
	public Usuario buscarPorId(Integer id) throws DataAccessException {
		Usuario usuario = new Usuario();
		buscarPorId(usuario, id);
		return usuario;
	}
	
	
	
	@Override
	public void buscarPorId(Usuario usuario, Integer id) throws DataAccessException {
		try {
			String query = new String("SELECT * FROM usuario WHERE id = ?");
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();
			if (result.next()) {				
				lerTabela(usuario, result);
			}			
		} catch (Exception e) {
			throw new DataAccessException("Falha ao buscar por ID");
		}		
	}

	@Override
	public Usuario buscarPorEmail(String email) throws DataAccessException {
		try {
			String sql = "select * from usuario where email=? and ativo=true";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, email);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return lerTabela(result);
			}
			return null;
		} catch (Exception e) {
			throw new DataAccessException("Falha ao buscar por e-mail");
		}
	}
	
	@Override
	public void buscar(Usuario usuario) throws DataAccessException {
		int usuarioId = usuario.getId();
		buscarPorId(usuario, usuarioId);		
	}

	@Override
	public List<Usuario> buscar(String consulta) throws DataAccessException {
		List<Usuario> usuarios = new ArrayList<>();
		try {
			String query ="SELECT * FROM usuario WHERE "
					+ " ((semAcento(nome) ilike semAcento(?)) OR " + 
					" (semAcento(sobrenome) ilike semAcento(?)) OR "
					+ "(semAcento(email) ilike semAcento(?))) "
					+ " ORDER BY LOWER(semAcento(nome)) ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setString(1, "%"+consulta+"%");
			stm.setString(2, "%"+consulta+"%");
			stm.setString(3, "%"+consulta+"%");
			ResultSet rs = stm.executeQuery();
			while(rs.next()) {
				usuarios.add(lerTabela(rs));
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Falha ao buscar usuario pelo nome");
		}
		return usuarios;
	}
	
	@Override
	public List<Usuario> buscar(String consulta, int inicio, int quant) throws DataAccessException {
		List<Usuario> usuarios = new ArrayList<>();
		try {
			String query ="SELECT * FROM usuario WHERE "
					+ " ((semAcento(nome) ilike semAcento(?)) OR " + 
					" (semAcento(sobrenome) ilike semAcento(?)) OR "
					+ "(semAcento(email) ilike semAcento(?))) "					
					+ " ORDER BY LOWER(semAcento(nome)) "
					+ " OFFSET ? LIMIT ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setString(1, "%"+consulta+"%");
			stm.setString(2, "%"+consulta+"%");
			stm.setString(3, "%"+consulta+"%");
			stm.setInt(4, inicio);
			stm.setInt(5, quant);
			ResultSet rs = stm.executeQuery();
			while(rs.next()) {
				usuarios.add(lerTabela(rs));
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Falha ao buscar usuario pelo nome");
		}
		return usuarios;
	}

	@Override
	public int qtdBuscarNome(String nome) throws DataAccessException {
		try {
			String query ="SELECT COUNT(*) FROM usuario WHERE "
					+ " ((semAcento(nome) ilike semAcento(?)) OR " + 
					" (semAcento(sobrenome) ilike semAcento(?)))";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setString(1, "%"+nome+"%");
			stm.setString(2, "%"+nome+"%");
			ResultSet rs = stm.executeQuery();
			if(rs.next())
				return rs.getInt(1);
		}catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Falha ao buscar usuario pelo nome");
		}
		return 0;
	}

	@Override
	public boolean login(String email, String senha) throws DataAccessException {
		try {
			String query = "select count(*) from usuario where email=? and senha=? and ativo=TRUE";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, email);
			statement.setString(2, senha);
			ResultSet result = statement.executeQuery();
			result.next();
			if (result.getInt(1) == 1) {
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new DataAccessException("Falha ao efetuar o login");
		}
	}

	@Override
	public void seguir(Usuario seguidor, Usuario seguido) throws DataAccessException {
		try {
			String query = "INSERT INTO segue (segueid, seguidoid) VALUES (?,?)";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, seguidor.getId());
			stm.setInt(2, seguido.getId());
			stm.execute();			
			try(Session session = driveNeo4j.session()){
				 Map<String, Object> mapa = new HashMap<>();
				 mapa.put("seguidorId", seguidor.getId());
				 mapa.put("seguidoId", seguido.getId());
				 session.run("MATCH (seguidor:Usuario{id:$seguidorId}), "
				 		+ " (seguido:Usuario{id:$seguidoId})"
				 		+ " CREATE (seguidor)-[:SEGUE]->(seguido) ", mapa);
				 
			}
		}catch (Exception e) {
			throw new DataAccessException("Falha ao seguir usuário");
		}
	}
	

	@Override
	public void desfazerAmizade(Usuario usuario1, Usuario usuario2) throws DataAccessException {
		try {
			String query = "DELETE FROM segue WHERE "
					+ " (segueid = ? AND seguidoid = ?) OR "
					+ " (segueid = ? AND seguidoid = ?)";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuario1.getId());
			stm.setInt(2, usuario2.getId());
			stm.setInt(3, usuario2.getId());
			stm.setInt(4, usuario1.getId());
			stm.execute();
			try(Session session = driveNeo4j.session()){
				Map<String, Object> mapa = new HashMap<>();
				mapa.put("u1", usuario1.getId());
				mapa.put("u2", usuario2.getId());
				session.run("MATCH (:Usuario{id:$u1})-[s:SEGUE]-(:Usuario{id:$u2}) "
						+ " DELETE s",mapa);
			}
		}catch (Exception e) {
			throw new DataAccessException("Falha ao desfazer amizade");
		}
		
	}

	@Override
	public List<Usuario> amigos(Usuario usuario) throws DataAccessException {
		List<Usuario> lista = new ArrayList<>();
		try {
			String query = "SELECT * FROM usuario WHERE "
					+ " EXISTS (SELECT FROM segue WHERE (segueid = ?) AND (seguidoid = id) ) AND"
					+ " EXISTS (SELECT FROM segue WHERE (segueid = id) AND (seguidoid = ?) ) "
					+ " ORDER BY LOWER(semAcento(nome)) ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuario.getId());
			stm.setInt(2, usuario.getId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				lista.add(lerTabela(rs));
			}
		}catch (Exception e) {
			throw new DataAccessException("Falha ao listar amigos");
		}
		return lista;
	}

	@Override
	public List<Usuario> amigos(Usuario usuario, int inicio, int quant) throws DataAccessException {
		List<Usuario> lista = new ArrayList<>();
		try {
			String query = "SELECT * FROM usuario WHERE "
					+ " EXISTS (SELECT FROM segue WHERE (segueid = ?) AND (seguidoid = id) ) AND"
					+ " EXISTS (SELECT FROM segue WHERE (segueid = id) AND (seguidoid = ?) ) "
					+ " ORDER BY LOWER(semAcento(nome)) "
					+ " OFFSET ? LIMIT ? ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuario.getId());
			stm.setInt(2, usuario.getId());
			stm.setInt(3, inicio);
			stm.setInt(4, quant);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				lista.add(lerTabela(rs));
			}
		}catch (Exception e) {
			throw new DataAccessException("Falha ao listar amigos");
		}
		return lista;
	}

	@Override
	public int qtdAmigos(Usuario usuario) throws DataAccessException {
		try {
			String query = "SELECT COUNT(*) FROM usuario WHERE "
					+ " EXISTS (SELECT FROM segue WHERE (segueid = ?) AND (seguidoid = id) ) AND"
					+ " EXISTS (SELECT FROM segue WHERE (segueid = id) AND (seguidoid = ?) ) ";					
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, usuario.getId());
			stm.setInt(2, usuario.getId());			
			ResultSet rs = stm.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		}catch (Exception e) {
			throw new DataAccessException("Falha ao listar amigos");
		}
		return 0;
	}

	@Override
	public List<Usuario> solicitacoesAmizades(Usuario self) throws DataAccessException {
		List<Usuario> lista = new ArrayList<>();
		try {
			String query = "SELECT * FROM segue S JOIN usuario U ON U.id = S.segueid WHERE "
					+ " NOT EXISTS (SELECT FROM segue A WHERE (S.segueid = A.seguidoid) AND (S.seguidoid = A.segueid)) AND "
					+ " S.seguidoid = ? "
					+ " ORDER BY S.datahora DESC ";					
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, self.getId());			
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				lista.add(lerTabela(rs));
			}
		}catch (Exception e) {
			throw new DataAccessException("Falha ao listar solicitação de amizades.");
		}
		return lista;
	}

	@Override
	public List<Usuario> solicitacoesAmizades(Usuario self, int inicio, int quant) throws DataAccessException {
		List<Usuario> lista = new ArrayList<>();
		try {
			String query = "SELECT * FROM segue S JOIN usuario U ON U.id = S.segueid WHERE "
					+ " NOT EXISTS (SELECT FROM segue A WHERE (S.segueid = A.seguidoid) AND (S.seguidoid = A.segueid)) AND "
					+ " S.seguidoid = ? "
					+ " ORDER BY S.datahora DESC "
					+ " OFFSET ? LIMIT ?";					
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, self.getId());		
			stm.setInt(2, inicio);
			stm.setInt(3, quant);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				lista.add(lerTabela(rs));
			}
		}catch (Exception e) {
			throw new DataAccessException("Falha ao listar solicitação de amizades.");
		}
		return lista;
	}

	@Override
	public int qtdSolicitacoesAmizades(Usuario self) throws DataAccessException {
		try {
			String query = "SELECT COUNT(*) FROM segue S JOIN usuario U ON U.id = S.segueid WHERE "
					+ " NOT EXISTS (SELECT FROM segue A WHERE (S.segueid = A.seguidoid) AND (S.seguidoid = A.segueid)) AND "
					+ " S.seguidoid = ? ";										
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, self.getId());			
			ResultSet rs = stm.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		}catch (Exception e) {
			throw new DataAccessException("Falha ao recuperar a quantidade de solicitações de amizade.");
		}
		return 0;
	}
		
	@Override
	public List<Usuario> sugestaoAmizade(Usuario self) throws DataAccessException {
		List<Usuario> sugs = new ArrayList<>();
		try(Session session = driveNeo4j.session()){
			 Map<String, Object> mapa = new HashMap<>();
			 mapa.put("selfId", self.getId());
			 
			 StatementResult sr = session.run("MATCH (self:Usuario)--(:Usuario)--(sugestao:Usuario) "
			 		+ " WHERE (self.id = $selfId) AND NOT (sugestao)--(self) "
			 		+ " AND NOT (sugestao.id = self.id) " 
			 		+ " RETURN DISTINCT sugestao.id ",mapa);
			 
			 while(sr.hasNext()){
				 Record rc = sr.next();
				 sugs.add(buscarPorId(rc.get("sugestao.id", 0)));
			 }
		}
		return sugs;
	}

	@Override
	public void mudarStatus(Usuario self, Usuario usuario) throws DataAccessException {
		
		try {
			boolean segue = false;
			boolean seguido = false;
			PreparedStatement stm = null;
			ResultSet rs = null;
			if(self.getId() == usuario.getId()) {
				usuario.setStatus("self");
				return;
			}
			
			String query = "SELECT FROM segue WHERE segueid = ? AND seguidoid = ? ";
			stm = connection.prepareStatement(query);
			stm.setInt(1, self.getId());
			stm.setInt(2, usuario.getId());
			rs = stm.executeQuery();
			segue = (rs.next());
			stm.close();
			
			stm = connection.prepareStatement(query);
			stm.setInt(1, usuario.getId());
			stm.setInt(2, self.getId());
			rs = stm.executeQuery();
			seguido = (rs.next());
			stm.close();
			
			if(segue && seguido) {
				usuario.setStatus("amigo");
			}
			
			if(segue && !seguido) {
				usuario.setStatus("enviado");
			}
			
			if(!segue && seguido) {
				usuario.setStatus("recebida");
			}
			
			if(!segue && !seguido) {
				usuario.setStatus("nada");
			}
			
		}catch (Exception e) {
			throw new DataAccessException("Falha ao mudar status de usuário");
		}
		
	}
	
	

	@Override
	public List<Usuario> participaGrupo(int grupoId) throws DataAccessException {
		List<Usuario> usuarios = new ArrayList<>();
		try {
			String query = " SELECT * FROM usuario U "+
					" WHERE EXISTS(SELECT FROM participagrupo " +
					" WHERE U.id = usuarioid AND grupoid = ?) ";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, grupoId);
			ResultSet rs = stm.executeQuery();
			while(rs.next()) {
				usuarios.add(lerTabela(rs));
			}
			carregarFotoPerfil(usuarios);			
		} catch (Exception e) {
			throw new DataAccessException("Falha ao listar usuarios do grupo"); 
		}
		return usuarios;
	}

	@Override
	public void mudarStatus(Usuario self, List<Usuario> usuarios) throws DataAccessException {
		for (Usuario usuario : usuarios) {
			mudarStatus(self, usuario);
		}
		
	}	
	
}
