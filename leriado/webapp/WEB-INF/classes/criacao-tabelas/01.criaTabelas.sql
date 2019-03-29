﻿/*CRIACAO DA TABELA USUARIO*/
CREATE TABLE usuario(
	id SERIAL,
	ativo BOOLEAN DEFAULT TRUE NOT NULL,
	email VARCHAR (100) UNIQUE NOT NULL,
	senha VARCHAR (50) NOT NULL,
	nome VARCHAR (50) NOT NULL,
	sobrenome VARCHAR (100) NOT NULL,
	sexo VARCHAR (1) NOT NULL,
	datanasc DATE NOT NULL,
	acesso INT NOT NULL DEFAULT 1,
	telefone VARCHAR(30),
	rua VARCHAR(100),
	cidade VARCHAR(100),
	estado VARCHAR(50),
	numero VARCHAR(10),
	cep VARCHAR(50),
	CONSTRAINT usuario_pk PRIMARY KEY (id)
);

/*CRIACAO DA TABELA GRUPO*/
CREATE TABLE grupo(
	id SERIAL,
	ativo BOOLEAN DEFAULT TRUE NOT NULL,
	datahora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	nome VARCHAR(50) NOT NULL UNIQUE,
	descricao TEXT,
	foto VARCHAR(32),
	CONSTRAINT grupo_pk PRIMARY KEY(id),
	CONSTRAINT grupo_tamnome CHECK (LENGTH(nome)>=6) 
);

/*CRIACAO DA TABELA TEXTO*/
CREATE TABLE texto(
	id SERIAL,
	ativo BOOLEAN DEFAULT TRUE NOT NULL,	
	datahora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	usuarioid INT,
    qtdCurtidas INT DEFAULT 0,
    qtdComentarios INT DEFAULT 0,
	CONSTRAINT texto_pk PRIMARY KEY(id),
	CONSTRAINT texto_usuario_fk FOREIGN KEY (usuarioid) 
		REFERENCES usuario(id)
		ON UPDATE CASCADE ON DELETE CASCADE
);

/*CRIACAO DA TABELA COMENTARIO*/
CREATE TABLE comentario(
	textoid INT,
	respondeid INT,
	CONSTRAINT comentario_pk PRIMARY KEY (textoid), 
	CONSTRAINT comentario_textoid_fk FOREIGN KEY (textoid)
		REFERENCES texto(id)
		ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT comentario_respondeid_fk FOREIGN KEY (respondeid)
		REFERENCES texto(id)
		ON UPDATE CASCADE ON DELETE CASCADE
);

/*CRIACAO DA TABELA VISUALIZA*/
CREATE TABLE visualiza(
	textoid INT,
	usuarioid INT,
	datahora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT visualiza_pk PRIMARY KEY (textoid,usuarioid),
	CONSTRAINT visualiza_textoid_fk FOREIGN KEY (textoid)
		REFERENCES texto(id)
		ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT visualiza_usuarioid_fk FOREIGN KEY (usuarioid)
		REFERENCES usuario(id)
		ON UPDATE CASCADE ON DELETE CASCADE
);

/*CRIACAO DA TABELA SEGUE*/
CREATE TABLE segue(
	segueid INT,
	seguidoid INT,
	datahora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT segue_pk PRIMARY KEY (segueid, seguidoid),
	CONSTRAINT segue_segueid_fk FOREIGN KEY (segueid)
		REFERENCES usuario(id)
		ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT segue_seguidoid_fk FOREIGN KEY (segueid)
		REFERENCES usuario(id)
		ON UPDATE CASCADE ON DELETE CASCADE
);

/*CRIACAO DA TABELA CURTE*/
CREATE TABLE curte(
	textoid INT,
	usuarioid INT,
	datahora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT curte_pk PRIMARY KEY (textoid, usuarioid),
	CONSTRAINT curte_textoid_fk FOREIGN KEY (textoid)
		REFERENCES texto(id)
		ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT curte_usuarioid_fk FOREIGN KEY (usuarioid)
		REFERENCES usuario(id)
		ON UPDATE CASCADE ON DELETE CASCADE
);

/*CRIACAO DA TABELA MARCA*/
CREATE TABLE marca(
	textoid INT,
	usuarioid INT,	
	CONSTRAINT marca_pk PRIMARY KEY (textoid, usuarioid),
	CONSTRAINT marca_textoid_fk FOREIGN KEY (textoid)
		REFERENCES texto(id)
		ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT marca_usuarioid_fk FOREIGN KEY (usuarioid)
		REFERENCES usuario(id)
		ON UPDATE CASCADE ON DELETE CASCADE
);

/*CRIACAO DA TABELA ADMGRUPO*/
CREATE TABLE admgrupo(
	usuarioid INT,
	grupoid INT,	
	CONSTRAINT admgrupo_pk PRIMARY KEY (usuarioid,grupoid),
	CONSTRAINT admgrupo_usuarioid_fk FOREIGN KEY (usuarioid)
		REFERENCES usuario(id)
		ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT admgrupo_grupoid_fk FOREIGN KEY (grupoid)
		REFERENCES grupo(id)
		ON UPDATE CASCADE ON DELETE CASCADE
);

/*CRIACAO DA TABELA PARTICIPAGRUPO*/
CREATE TABLE participagrupo(
	usuarioid INT,
	grupoid INT,	
	datahora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT participagrupo_pk PRIMARY KEY (usuarioid,grupoid),
	CONSTRAINT participagrupo_usuarioid_fk FOREIGN KEY (usuarioid)
		REFERENCES usuario(id)
		ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT participagrupo_grupoid_fk FOREIGN KEY (grupoid)
		REFERENCES grupo(id)
		ON UPDATE CASCADE ON DELETE CASCADE
);

/*CRIACAO DA TABELA COMPARTILHA*/
CREATE TABLE compartilha(
	datahora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	usuarioid INT,
	textoid INT,	
	grupoid INT,
	CONSTRAINT compartilha_pk PRIMARY KEY (usuarioid,textoid,grupoid),
	CONSTRAINT compartilha_usuarioid_fk FOREIGN KEY (usuarioid)
		REFERENCES usuario(id)
		ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT compartilha_textoid_fk FOREIGN KEY (textoid)
		REFERENCES texto(id)
		ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT compartilha_grupoid_fk FOREIGN KEY (grupoid)
		REFERENCES grupo(id)
		ON UPDATE CASCADE ON DELETE CASCADE
);

/*CRIACAO DA TABELA FOTOPERFIL*/
-- CREATE TABLE fotoperfil(
-- 	usuarioid INT,
-- 	fotoid INT,	
-- 	CONSTRAINT fotoperfil_pk PRIMARY KEY (usuarioid,fotoid),
-- 	CONSTRAINT fotoperfil_usuarioid_fk FOREIGN KEY (usuarioid)
-- 		REFERENCES usuario(id)
-- 		ON UPDATE CASCADE ON DELETE CASCADE,
-- 	CONSTRAINT fotoperfil_fotoid_fk FOREIGN KEY (fotoid)
-- 		REFERENCES foto(publicacaoid)
-- 		ON UPDATE CASCADE ON DELETE CASCADE
-- );
