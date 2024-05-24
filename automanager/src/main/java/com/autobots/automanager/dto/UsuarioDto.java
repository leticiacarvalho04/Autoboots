package com.autobots.automanager.dto;

import com.autobots.automanager.entidades.*;
import com.autobots.automanager.enumeracoes.PerfilUsuario;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UsuarioDto {
	private Long id;
	private String nome;
	private String nomeSocial;
	private Set<Long> documentos;
	private Set<Long> telefones;
	private Long endereco;
	private Set<Long> emails;
	private Set<Long> credenciais;
	private Set<Long> mercadorias;
	private Set<Long> vendas;
	private Set<Long> veiculos;
	private List<PerfilUsuario> perfis;
	
	public Usuario toEntity(List<Documento> documentos, Endereco endereco, List<Telefone> telefones, List<Email> emails, List<PerfilUsuario> perfis, List<CredencialUsuarioSenha> credenciais, List<Mercadoria> mercadorias, List<Venda> vendas, List<Veiculo> veiculos) {
		Usuario usuario = new Usuario();
		usuario.setId(this.id);
		usuario.setNome(this.nome);
		usuario.setNomeSocial(this.nomeSocial);
		usuario.setDocumentos(documentos.stream().collect(Collectors.toSet()));
		usuario.setEndereco(endereco);
		usuario.setTelefones(telefones.stream().collect(Collectors.toSet()));
		usuario.setEmails(emails.stream().collect(Collectors.toSet()));
		usuario.setPerfis(perfis.stream().collect(Collectors.toSet()));
		usuario.setCredenciais(credenciais.stream().collect(Collectors.toSet()));
		usuario.setMercadorias(mercadorias.stream().collect(Collectors.toSet()));
		usuario.setVendas(vendas.stream().collect(Collectors.toSet()));
		usuario.setVeiculos(veiculos.stream().collect(Collectors.toSet()));
		return usuario;
	}
	
	public void updateEntity(Usuario usuario, List<Documento> documentos, Endereco endereco, List<Telefone> telefones, List<Email> emails, List<PerfilUsuario> perfis, List<CredencialUsuarioSenha> credenciais, List<Mercadoria> mercadorias, List<Venda> vendas, List<Veiculo> veiculos) {
		usuario.setNome(this.nome);
		usuario.setNomeSocial(this.nomeSocial);
		updateCollection(usuario.getDocumentos(), documentos);
		usuario.setEndereco(endereco);
		updateCollection(usuario.getTelefones(), telefones);
		updateCollection(usuario.getEmails(), emails);
		usuario.setPerfis(perfis.stream().collect(Collectors.toSet()));
		updateCollection(usuario.getCredenciais(), credenciais);
		updateCollection(usuario.getMercadorias(), mercadorias);
		updateCollection(usuario.getVendas(), vendas);
		updateCollection(usuario.getVeiculos(), veiculos);
	}
	
	private <T> void updateCollection(Set<T> target, List<? extends T> source) {
		target.clear();
		target.addAll(source);
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNomeSocial() {
		return nomeSocial;
	}
	
	public void setNomeSocial(String nomeSocial) {
		this.nomeSocial = nomeSocial;
	}
	
	public Set<Long> getDocumentos() {
		return documentos;
	}
	
	public void setDocumentos(Set<Long> documentos) {
		this.documentos = documentos;
	}
	
	public Set<Long> getTelefones() {
		return telefones;
	}
	
	public void setTelefones(Set<Long> telefones) {
		this.telefones = telefones;
	}
	
	public Long getEndereco() {
		return endereco;
	}
	
	public void setEndereco(Long endereco) {
		this.endereco = endereco;
	}
	
	public Set<Long> getEmails() {
		return emails;
	}
	
	public void setEmails(Set<Long> emails) {
		this.emails = emails;
	}
	
	public List<PerfilUsuario> getPerfis() {
		return perfis;
	}
	
	public void setPerfis(List<PerfilUsuario> perfis) {
		this.perfis = perfis;
	}
	
	public Set<Long> getCredenciais() {
		return credenciais;
	}
	
	public void setCredenciais(Set<Long> credenciais) {
		this.credenciais = credenciais;
	}
	
	public Set<Long> getMercadorias() {
		return mercadorias;
	}
	
	public void setMercadorias(Set<Long> mercadorias) {
		this.mercadorias = mercadorias;
	}
	
	public Set<Long> getVendas() {
		return vendas;
	}
	
	public void setVendas(Set<Long> vendas) {
		this.vendas = vendas;
	}
	
	public Set<Long> getVeiculos() {
		return veiculos;
	}
	
	public void setVeiculos(Set<Long> veiculos) {
		this.veiculos = veiculos;
	}
}
