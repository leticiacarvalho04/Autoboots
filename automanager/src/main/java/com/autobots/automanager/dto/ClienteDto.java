package com.autobots.automanager.dto;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClienteDto {
	private Long id;
	private String nome;
	private String nomeSocial;
	private Date dataNascimento;
	private Date dataCadastro;
	private List<Long> documentos;
	private Long endereco;
	private List<Long> telefones;
	
	public ClienteDto() {
		this.documentos = new ArrayList<>();
		this.telefones = new ArrayList<>();
	}
	
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	public String getNomeSocial() { return nomeSocial; }
	public void setNomeSocial(String nomeSocial) { this.nomeSocial = nomeSocial; }
	public Date getDataNascimento() { return dataNascimento; }
	public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }
	public Date getDataCadastro() { return dataCadastro; }
	public void setDataCadastro(Date dataCadastro) { this.dataCadastro = dataCadastro; }
	public List<Long> getDocumentos() { return documentos; }
	public void setDocumentos(List<Long> documentos) { this.documentos = documentos; }
	public Long getEndereco() { return endereco; }
	public void setEndereco(Long endereco) { this.endereco = endereco; }
	public List<Long> getTelefones() { return telefones; }
	public void setTelefones(List<Long> telefones) { this.telefones = telefones; }
	
	public Cliente toEntity(List<Documento> documentos, Endereco endereco, List<Telefone> telefones){
		Cliente cliente = new Cliente();
		cliente.setId(this.id);
		cliente.setNome(this.nome);
		cliente.setNomeSocial(this.nomeSocial);
		cliente.setDataNascimento(this.dataNascimento);
		cliente.setDataCadastro(this.dataCadastro);
		cliente.setDocumentos(documentos);
		cliente.setEndereco(endereco);
		cliente.setTelefones(telefones);
		return cliente;
	}
	
}