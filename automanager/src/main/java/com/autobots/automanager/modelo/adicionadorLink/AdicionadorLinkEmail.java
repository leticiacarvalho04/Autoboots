package com.autobots.automanager.modelo.adicionadorLink;

import com.autobots.automanager.controles.EmailControle;
import com.autobots.automanager.entidades.Email;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkEmail implements AdicionadorLink<Email> {
	@Override
	public void adicionarLink(List<Email> lista) {
        for(Email email: lista){
			long id = email.getId();
	        Link linkProprio = WebMvcLinkBuilder
			        .linkTo(WebMvcLinkBuilder
			                .methodOn(EmailControle.class)
			                .obterEmail())
			        .withSelfRel();
			email.add(linkProprio);
        }
    }
	
	@Override
	public void adicionarLink(Email objeto){
		Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmailControle.class)
                        .obterEmail())
                .withRel("emails");
        objeto.add(linkProprio);
	}
}
