package com.autobots.automanager.dto;

import com.autobots.automanager.enumeracoes.TipoVeiculo;

public class VeiculoDto {
		private TipoVeiculo tipo;
		private String modelo;
		private String placa;

		public TipoVeiculo getTipo() {
			return tipo;
		}
		
		public void setTipo(TipoVeiculo tipo) {
			this.tipo = tipo;
		}
		
		public String getModelo() {
			return modelo;
		}
		
		public void setModelo(String modelo) {
			this.modelo = modelo;
		}
		
		public String getPlaca() {
			return placa;
		}
		
		public void setPlaca(String placa) {
			this.placa = placa;
		}
	
}
