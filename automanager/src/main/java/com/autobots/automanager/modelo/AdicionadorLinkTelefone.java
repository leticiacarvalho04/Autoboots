package com.autobots.automanager.modelo;

import com.autobots.automanager.controles.EnderecoControle;
import com.autobots.automanager.controles.TelefoneControle;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkTelefone implements AdicionadorLink<Telefone> {
	@Override
	public void adicionarLink(List<Telefone> lista){
		for(Telefone telefone: lista){
			long id = telefone.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(TelefoneControle.class)
							.obterTelefone())
					.withSelfRel();
			telefone.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Telefone objeto){
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(TelefoneControle.class)
						.obterTelefone())
				.withRel("telefones");
		objeto.add(linkProprio);
	}
}
