package leriado;

import java.time.LocalDate;

import org.junit.Test;

import br.edu.ifpb.pweb1.model.dao.UsuarioDaoImpl;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public class UsuarioTeste {
	
	@Test
	public void criaTeste() {
		Usuario usuario = new Usuario(
				0, 
				"zedascouves@gmail.com", 
				"123456", 
				"José", 
				"das Couves",
				"M", 
				LocalDate.now(),
				1, 
				"telefone", 
				"rua", 
				"cidade", 
				"estado", 
				"numero", 
				"cep");
		try {
			new UsuarioDaoImpl().criar(usuario);
		} catch (DataAccessException e) {			
			e.printStackTrace();
		}
	}

}
