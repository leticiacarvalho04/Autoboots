package com.autobots.automanager.dto;

import com.autobots.automanager.entidades.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class VendaDto {
	private Long id;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getCadastro() {
		return cadastro;
	}
	
	public void setCadastro(Date cadastro) {
		this.cadastro = cadastro;
	}
	
	public String getIdentificacao() {
		return identificacao;
	}
	
	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}
	
	public Long getClienteId() {
		return clienteId;
	}
	
	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}
	
	public Long getFuncionarioId() {
		return funcionarioId;
	}
	
	public void setFuncionarioId(Long funcionarioId) {
		this.funcionarioId = funcionarioId;
	}
	
	public List<Long> getMercadorias() {
		return mercadorias;
	}
	
	public void setMercadorias(List<Long> mercadorias) {
		this.mercadorias = mercadorias;
	}
	
	public List<Long> getServicos() {
		return servicos;
	}
	
	public void setServicos(List<Long> servicos) {
		this.servicos = servicos;
	}
	
	public Long getVeiculoId() {
		return veiculoId;
	}
	
	public void setVeiculoId(Long veiculoId) {
		this.veiculoId = veiculoId;
	}
	
	private Date cadastro;
	private String identificacao;
	private Long clienteId;
	private Long funcionarioId;
	private List<Long> mercadorias;
	private List<Long> servicos;
	private Long veiculoId;
	
	public VendaDto() {
		this.mercadorias = new ArrayList<>();
		this.servicos = new ArrayList<>();
	}
	
	public Venda toEntity(Usuario cliente, Usuario funcionario, List<Mercadoria> mercadorias, List<Servico> servicos, Veiculo veiculo) {
		Venda venda = new Venda();
		venda.setId(this.id);
		venda.setCadastro(this.cadastro);
		venda.setIdentificacao(this.identificacao);
		venda.setCliente(cliente);
		venda.setFuncionario(funcionario);
		venda.setMercadorias(new HashSet<>(mercadorias));
		venda.setServicos(new HashSet<>(servicos));
		venda.setVeiculo(veiculo);
		return venda;
	}
}
