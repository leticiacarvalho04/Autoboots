package com.autobots.automanager.modelo.adicionadorLink;

import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.entidades.Veiculo;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkVeiculo implements AdicionadorLink<Veiculo> {
	@Override
	public void adicionarLink(List<Veiculo> lista){
		for(Veiculo veiculo: lista){
			long id = veiculo.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(VeiculoControle.class)
							.obterVeiculo())
					.withSelfRel();
			veiculo.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Veiculo objeto){
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VeiculoControle.class)
						.obterVeiculo())
				.withRel("veiculos");
		objeto.add(linkProprio);
	}
}
