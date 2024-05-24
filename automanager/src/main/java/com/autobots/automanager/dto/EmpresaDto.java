package com.autobots.automanager.dto;

import com.autobots.automanager.entidades.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class EmpresaDto {
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long id;
	
	private String razaoSocial;
	
	public String getRazaoSocial() {
		return razaoSocial;
	}
	
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	
	public String getNomeFantasia() {
		return nomeFantasia;
	}
	
	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
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
	
	public Date getCadastro() {
		return cadastro;
	}
	
	public void setCadastro(Date cadastro) {
		this.cadastro = cadastro;
	}
	
	public Set<Long> getUsuarios() {
		return usuarios;
	}
	
	public void setUsuarios(Set<Long> usuarios) {
		this.usuarios = usuarios;
	}
	
	public Set<Long> getMercadorias() {
		return mercadorias;
	}
	
	public void setMercadorias(Set<Long> mercadorias) {
		this.mercadorias = mercadorias;
	}
	
	public Set<Long> getServicos() {
		return servicos;
	}
	
	public void setServicos(Set<Long> servicos) {
		this.servicos = servicos;
	}
	
	public Set<Long> getVendas() {
		return vendas;
	}
	
	public void setVendas(Set<Long> vendas) {
		this.vendas = vendas;
	}
	
	private String nomeFantasia;
	private Set<Long> telefones;
	private Long endereco;
	private Date cadastro;
	private Set<Long> usuarios;
	private Set<Long> mercadorias;
	private Set<Long> servicos;
	private Set<Long> vendas;
	
	public Empresa toEntity(Set<Telefone> telefones, Endereco endereco, Set<Usuario> usuarios, Set<Mercadoria> mercadorias, Set<Servico> servicos, Set<Venda> vendas) {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial(this.razaoSocial);
		empresa.setNomeFantasia(this.nomeFantasia);
		empresa.setTelefones(telefones);
		empresa.setEndereco(endereco);
		empresa.setCadastro(this.cadastro);
		empresa.setUsuarios(usuarios);
		empresa.setMercadorias(mercadorias);
		empresa.setServicos(servicos);
		empresa.setVendas(vendas);
		return empresa;
	}
}
