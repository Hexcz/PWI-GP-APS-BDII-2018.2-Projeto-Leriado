CREATE INDEX indice_usuario_id ON usuario(id);
CREATE INDEX indice_usuario_email ON usuario(email);

CREATE INDEX indice_grupo_id ON grupo(id);

CREATE INDEX indice_texto_id ON texto(id);

CREATE INDEX indice_comentario_id ON comentario(textoid);

CREATE INDEX indice_visualiza_id ON visualiza(textoid,usuarioid);

CREATE INDEX indice_segue_id ON segue(segueid, seguidoid);

CREATE INDEX indice_curte_id ON curte(textoid, usuarioid);

CREATE INDEX indice_marca_id ON marca(textoid, usuarioid);

CREATE INDEX indice_admgrupo_id ON admgrupo(usuarioid,grupoid);

CREATE INDEX indice_participagrupo_id ON participagrupo(usuarioid,grupoid);

CREATE INDEX indice_compartilha_id ON compartilha(usuarioid,textoid,grupoid);

CREATE INDEX indice_fotoperfil_id ON fotoperfil(usuarioid,textoid);









