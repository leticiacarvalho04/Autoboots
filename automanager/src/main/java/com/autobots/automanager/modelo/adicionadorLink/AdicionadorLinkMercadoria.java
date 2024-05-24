package com.autobots.automanager.modelo.adicionadorLink;

import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.entidades.Mercadoria;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<Mercadoria>{
	@Override
	public void adicionarLink(List<Mercadoria> lista) {
		for(Mercadoria mercadoria: lista){
			long id = mercadoria.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(MercadoriaControle.class)
							.obterMercadoria())
					.withSelfRel();
			mercadoria.add(linkProprio);
		}
    }

	@Override
	public void adicionarLink(Mercadoria objeto){
		long id = objeto.getId();
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(MercadoriaControle.class)
						.obterMercadoria())
				.withRel("mercadorias");
		objeto.add(linkProprio);
	}
}
