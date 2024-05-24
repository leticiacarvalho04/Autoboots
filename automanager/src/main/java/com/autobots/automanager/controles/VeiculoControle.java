package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkVeiculo;
import com.autobots.automanager.modelo.atualizadores.VeiculoAtualizador;
import com.autobots.automanager.modelo.selecionadores.VeiculoSelecionador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {
	
	@Autowired
	private VeiculoRepositorio veiculoRepositorio;
	
	@Autowired
	private VeiculoSelecionador veiculoSelecionador;
	
	@Autowired
	public VendaRepositorio vendaRepositorio;
	
	@Autowired
	public UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private AdicionadorLinkVeiculo adicionadorLink;
	
	@GetMapping("/{id}")
	public Veiculo obterVeiculoPorId(@PathVariable Long id) {
		Veiculo veiculo = veiculoRepositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		adicionadorLink.adicionarLink(veiculo);
		return veiculo;
	}
	
	@GetMapping
	public List<Veiculo> obterVeiculo() {
		List<Veiculo> veiculos = veiculoRepositorio.findAll();
		adicionadorLink.adicionarLink(veiculos);
		return veiculos;
	}
	
	@PostMapping("/cadastro")
	public void cadastrarVeiculo(@RequestBody Veiculo veiculo) {
		Set<Venda> vendas = veiculo.getVendas().stream()
				.map(venda -> vendaRepositorio.findById(venda.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda não encontrada com ID: " + venda.getId())))
				.collect(Collectors.toSet());
		veiculo.setVendas(vendas);
		veiculoRepositorio.save(veiculo);
		adicionadorLink.adicionarLink(veiculo);
	}
	
	@PutMapping("/atualizar")
	public void atualizarVeiculo(@RequestBody Veiculo veiculo) {
		Veiculo veiculoExistente = veiculoRepositorio.findById(veiculo.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado com ID: " + veiculo.getId()));
		
		Set<Venda> vendas = veiculo.getVendas() != null ? veiculo.getVendas().stream()
				.map(venda -> vendaRepositorio.findById(venda.getId())
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda não encontrada com ID: " + venda.getId())))
				.collect(Collectors.toSet()) : veiculoExistente.getVendas();
		
		Usuario proprietario = veiculo.getProprietario() != null ?
				usuarioRepositorio.findById(veiculo.getProprietario().getId())
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com ID: " + veiculo.getProprietario().getId()))
				: veiculoExistente.getProprietario();
		
		veiculoExistente.setVendas(vendas);
		veiculoExistente.setProprietario(proprietario);
		veiculoExistente.setTipo(veiculo.getTipo());
		veiculoExistente.setModelo(veiculo.getModelo());
		veiculoExistente.setPlaca(veiculo.getPlaca());
		
		veiculoRepositorio.save(veiculoExistente);
	}
	
	@DeleteMapping("/excluir/{id}")
	public void excluirVeiculo(@PathVariable Long id) {
		Veiculo veiculo = veiculoRepositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		veiculoRepositorio.delete(veiculo);
	}
}
