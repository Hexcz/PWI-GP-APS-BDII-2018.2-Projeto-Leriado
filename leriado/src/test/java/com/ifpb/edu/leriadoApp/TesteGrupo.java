package com.ifpb.edu.leriadoApp;

import org.junit.Test;

import br.edu.ifpb.pweb1.model.dao.impdb.GrupoDaoImpl;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public class TesteGrupo {

	
	@Test
	public void testeGrupo() {
		GrupoDaoImpl grupoDao = new GrupoDaoImpl();
		try {
			System.out.println(grupoDao.buscarGruposUsuarioParticipa(1));
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
