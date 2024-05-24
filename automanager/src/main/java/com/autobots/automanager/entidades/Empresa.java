package com.autobots.automanager.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Empresa extends RepresentationModel<Empresa> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String razaoSocial;
	
	@Column
	private String nomeFantasia;
	
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Telefone> telefones = new HashSet<>();
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private Endereco endereco;
	
	@Column(nullable = false)
	private Date cadastro;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Usuario> usuarios = new HashSet<>();
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Mercadoria> mercadorias = new HashSet<>();
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Servico> servicos = new HashSet<>();
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Venda> vendas = new HashSet<>();
}