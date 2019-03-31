package br.edu.ifpb.pweb1.controller;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import javax.servlet.http.Part;

public class GerirArquivos {
	
	public static String Md5(String texto) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(texto.getBytes(), 0, texto.length());
			return new BigInteger(1, m.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";		
	}
	
	public static String corrigirNomePasta(String nomePasta) {
		int tamNomePasta = nomePasta.length();
		String novoNomePasta = nomePasta;
		if((!nomePasta.isEmpty()) && (nomePasta.charAt(tamNomePasta -1)!= File.separatorChar) ){
			novoNomePasta = nomePasta + File.separator;
		}
		return novoNomePasta;
	}
	
	public static String novoNomeArquivo(String nomePasta) {
		File pasta;
		String novoNome = "";
		nomePasta = corrigirNomePasta(nomePasta);								
		try {		
			pasta = new File(nomePasta);			
			if (pasta.isDirectory()) {
				do {
					novoNome = Md5(LocalDateTime.now().toString());
				}while(new File(nomePasta + novoNome).exists());				
			}
		}catch (Exception e) {
			e.printStackTrace();			
		}
		return novoNome;
	}
	
	public static String salvarArquivoPasta(Part arquivo, String pasta) {
		String nomeArquivo;
		pasta = corrigirNomePasta(pasta);
		nomeArquivo = novoNomeArquivo(pasta);		
		if( (arquivo != null) && (!nomeArquivo.isEmpty())) {
			try (InputStream file = arquivo.getInputStream()){		
				Files.copy(file , new File(pasta + nomeArquivo).toPath(), StandardCopyOption.REPLACE_EXISTING);
				return nomeArquivo;
			}catch (Exception e) {
				e.printStackTrace();				
			}
		} 
		return null;		
	}
}
