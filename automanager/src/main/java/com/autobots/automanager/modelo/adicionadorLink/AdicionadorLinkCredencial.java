package com.autobots.automanager.modelo.adicionadorLink;

import com.autobots.automanager.controles.CredencialControle;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkCredencial implements AdicionadorLink<Credencial> {
	
	@Override
	public void adicionarLink(List<Credencial> lista) {
		for (Credencial credencial : lista) {
			adicionarLink(credencial);
		}
	}
	
	@Override
	public void adicionarLink(Credencial objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(CredencialControle.class)
						.obterCredenciais())
				.withSelfRel();
		objeto.add(linkProprio);
	}
}