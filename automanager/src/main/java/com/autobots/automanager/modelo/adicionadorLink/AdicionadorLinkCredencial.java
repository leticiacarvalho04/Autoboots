package com.autobots.automanager.modelo.adicionadorLink;

import com.autobots.automanager.controles.CredencialControle;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkCredencial implements AdicionadorLink<CredencialUsuarioSenha> {
	@Override
	public void adicionarLink(List<CredencialUsuarioSenha> lista) {
        for(CredencialUsuarioSenha credencial : lista) {
			long id = credencial.getId();
	        Link linkProprio = WebMvcLinkBuilder
			        .linkTo(WebMvcLinkBuilder
			         .methodOn(CredencialControle.class)
			         .obterCredenciais())
			         .withSelfRel();
			credencial.add(linkProprio);
        }
    }

	@Override
	public void adicionarLink(CredencialUsuarioSenha objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(CredencialControle.class)
						.obterCredenciais())
				.withRel("documentos");
		objeto.add(linkProprio);
	}
}
