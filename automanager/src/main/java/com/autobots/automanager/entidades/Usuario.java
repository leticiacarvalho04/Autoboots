package com.autobots.automanager.entidades;

import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = { "mercadorias", "vendas", "veiculos" })
@Entity
public class Usuario extends RepresentationModel<Usuario> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column
	private String nomeSocial;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<PerfilUsuario> perfis = new HashSet<>();
	
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Telefone> telefones = new HashSet<>();
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private Endereco endereco;
	
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Documento> documentos = new HashSet<>();
	
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Email> emails = new HashSet<>();
	
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Credencial> credenciais = new HashSet<>();
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Mercadoria> mercadorias = new HashSet<>();
	
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JsonIgnore
	private Set<Venda> vendas = new HashSet<>();
	
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JsonIgnore
	private Set<Veiculo> veiculos = new HashSet<>();
}