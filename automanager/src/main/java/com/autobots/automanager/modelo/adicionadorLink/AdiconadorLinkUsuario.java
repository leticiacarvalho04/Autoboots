package com.autobots.automanager.modelo.adicionadorLink;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entidades.Usuario;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdiconadorLinkUsuario implements AdicionadorLink<Usuario> {
	@Override
	public void adicionarLink(List<Usuario> lista){
		for(Usuario usuario: lista){
			long id = usuario.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(UsuarioControle.class)
							.obterUsuario())
					.withSelfRel();
			usuario.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Usuario objeto){
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(UsuarioControle.class)
						.obterUsuario())
				.withRel("usuarios");
		objeto.add(linkProprio);
	}
}
