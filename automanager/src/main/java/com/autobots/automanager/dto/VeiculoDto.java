package com.autobots.automanager.dto;

import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.enumeracoes.TipoVeiculo;
import lombok.Data;

@Data
public class VeiculoDto {
	private TipoVeiculo tipo;
	private String modelo;
	private String placa;
	public Veiculo toEntity() {
		Veiculo veiculo = new Veiculo();
		veiculo.setTipo(this.tipo);
		veiculo.setModelo(this.modelo);
		veiculo.setPlaca(this.placa);
		return veiculo;
	}
}
