package com.autobots.automanager.controles;

import com.autobots.automanager.dto.VendaDto;
import com.autobots.automanager.entidades.*;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkVenda;
import com.autobots.automanager.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/venda")
public class VendaControle {
	
	@Autowired
	private VendaRepositorio vendaRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private MercadoriaRepositorio mercadoriaRepositorio;
	
	@Autowired
	private ServicoRepositorio servicoRepositorio;
	
	@Autowired
	private VeiculoRepositorio veiculoRepositorio;
	
	@Autowired
	public EmpresaRepositorio empresaRepositorio;
	
	@Autowired
	private AdicionadorLinkVenda adicionadorLink;
	
	@GetMapping("/{id}")
	public Venda obterVendaPeloId(@PathVariable Long id) {
		Venda venda = vendaRepositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		adicionadorLink.adicionarLink(venda);
		return venda;
	}
	
	@GetMapping
	public List<Venda> obterVendas() {
		List<Venda> vendas = vendaRepositorio.findAll();
		if (vendas.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foram encontradas vendas.");
		}
		vendas.forEach(adicionadorLink::adicionarLink);
		return vendas;
	}
	
	@PostMapping("/cadastro/empresa/{idEmpresa}")
	public ResponseEntity<Venda> cadastrarVendaEmpresa(@RequestBody VendaDto vendaDto, @PathVariable Long idEmpresa) {
		Empresa empresa = empresaRepositorio.findById(idEmpresa)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrado"));
		Venda venda = vendaDto.cadastro();
		empresa.getVendas().add(venda);
		vendaRepositorio.save(venda);
		adicionadorLink.adicionarLink(venda);
		return new ResponseEntity<Venda>(venda,HttpStatus.OK);
	}
	
	@PostMapping("/cadastro/{idCliente}/{idFuncionario}")
	public ResponseEntity<Venda> cadastrarVenda(@RequestBody VendaDto vendaDto, @PathVariable Long idCliente, @PathVariable Long idFuncionario) {
		Usuario cliente = usuarioRepositorio.findById(idCliente)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
		Usuario funcionario = usuarioRepositorio.findById(idFuncionario)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));
		
		Venda venda = vendaDto.cadastro();
		venda.setCliente(cliente);
		venda.setFuncionario(funcionario);
		
		vendaRepositorio.save(venda);
		adicionadorLink.adicionarLink(venda);
		return new ResponseEntity<Venda>(venda,HttpStatus.OK);
	}
	
	@PutMapping("/atualizar")
	public void atualizarVenda(@RequestBody VendaDto vendaDto) {
		Venda vendaExistente = vendaRepositorio.findById(vendaDto.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		Usuario cliente = usuarioRepositorio.findById(vendaDto.getClienteId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado com ID: " + vendaDto.getClienteId()));
		Usuario funcionario = usuarioRepositorio.findById(vendaDto.getFuncionarioId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado com ID: " + vendaDto.getFuncionarioId()));
		Set<Mercadoria> mercadorias = vendaExistente.getMercadorias().stream()
				.map(venda -> mercadoriaRepositorio.findById(venda.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda não encontrada com ID: " + venda.getId())))
				.collect(Collectors.toSet());
		Set<Servico> servicos = vendaExistente.getServicos().stream()
				.map(venda -> servicoRepositorio.findById(venda.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda não encontrada com ID: " + venda.getId())))
				.collect(Collectors.toSet());
		Veiculo veiculo = veiculoRepositorio.findById(vendaDto.getVeiculoId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado com ID: " + vendaDto.getVeiculoId()));
		
		vendaExistente.setCadastro(vendaDto.getCadastro());
		vendaExistente.setIdentificacao(vendaDto.getIdentificacao());
		vendaExistente.setCliente(cliente);
		vendaExistente.setFuncionario(funcionario);
		vendaExistente.setMercadorias(mercadorias);
		vendaExistente.setServicos(servicos);
		vendaExistente.setVeiculo(veiculo);
		
		vendaRepositorio.save(vendaExistente);
	}

	@DeleteMapping("/excluir/{id}")
	public void excluirVenda(@PathVariable Long id) {
		Venda venda = vendaRepositorio.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		List<Empresa> empresas = empresaRepositorio.findAll();
		for(Empresa e : empresas){
			if(e.getVendas().contains(venda)){
				e.getVendas().remove(venda);
                empresaRepositorio.save(e);
			}
		}
		vendaRepositorio.delete(venda);
	}
}