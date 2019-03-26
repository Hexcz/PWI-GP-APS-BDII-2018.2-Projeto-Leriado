﻿
-- /*PROCEDIMENTO ARMAZENADO QUE RETORNA O TIPO DO TEXTO*/
-- CREATE OR REPLACE FUNCTION TipoTexto(INT)
-- RETURNS VARCHAR
-- 	AS $$
-- 		DECLARE
-- 			num ALIAS FOR $1;
-- 			tipo VARCHAR;
-- 		BEGIN
-- 			SELECT 
-- 				CASE
-- 					WHEN EXISTS(SELECT * FROM comentario WHERE textoid=num) THEN 'COMENTARIO'
--  					WHEN EXISTS(SELECT * FROM foto WHERE publicacaoid=num) THEN 'FOTO'
--  					WHEN EXISTS(SELECT * FROM noticia WHERE publicacaoid=num) THEN 'NOTICIA'
--  					WHEN EXISTS(SELECT * FROM link WHERE publicacaoid=num) THEN 'LINK'
--  					WHEN EXISTS(SELECT * FROM publicacao WHERE textoid=num) THEN 'PUBLICACAO'
--  					ELSE 'FALHA'
-- 				END
-- 			FROM texto T INTO tipo;
-- 			RETURN tipo;
-- 		END;
-- 	$$ LANGUAGE PLPGSQL;



/*PROCEDIMENTO ARMAZENADO QUE RETIRA OS ACENTOS*/
CREATE FUNCTION semAcento(TEXT) 
RETURNS TEXT
	AS $$
		SELECT TRANSLATE($1,'ÀÁÂÃÄÅĀĂĄÈÉÊËĒĔĖĘĚÌÍÎÏĨĪĮİÒÓÔÕÖØŌŎŐÙÚÛÜŨŪŬŮŰŲàáâãäåāăąèéêëēĕėęěìíîïĩīĭįòóôõöøōŏőùúûüũūŭůųÇçÑñÝýÿĆćĈĉĊċČčĎďĐđĜĝĞğĠġĢģĤĥĦħ',
			         'AAAAAAAAAEEEEEEEEEIIIIIIIIOOOOOOOOOUUUUUUUUUUaaaaaaaaaeeeeeeeeeiiiiiiiiooooooooouuuuuuuuuCcNnYyyCcCcCcCcDdDdGgGgGgGgHhHh'); 
	$$ LANGUAGE sql IMMUTABLE STRICT; 