package com.autobots.automanager.controles;

import com.autobots.automanager.dto.ClienteDto;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkCliente;
import com.autobots.automanager.modelo.atualizadores.ClienteAtualizador;
import com.autobots.automanager.modelo.selecionadores.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteControle {
	
	@Autowired
	public ClienteRepositorio repositorio;
	
	@Autowired
	public DocumentoRepositorio documentoRepositorio;
	
	@Autowired
	public EnderecoRepositorio enderecoRepositorio;
	
	@Autowired
	public TelefoneRepositorio telefoneRepositorio;
	
	@Autowired
	public ClienteSelecionador selecionador;
	
	@Autowired
	public AdicionadorLinkCliente adicionadorLink;
	
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> listarPorId(@PathVariable long id) {
		Cliente cliente = repositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		adicionadorLink.adicionarLink(cliente);
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	@GetMapping
	public List<Cliente> obterCliente() {
		List<Cliente> clientes = repositorio.findAll();
		adicionadorLink.adicionarLink(clientes);
		return clientes;
	}
	
	@PostMapping("/cadastro")
	public ResponseEntity<Cliente> cadastrarCliente(@Validated @RequestBody Cliente cliente) {
		try {
			adicionadorLink.adicionarLink(cliente);
			Cliente clienteSalvo = repositorio.save(cliente);
			return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	@PutMapping("/atualizar")
	public void atualizarCliente(@RequestBody ClienteDto clienteDto) {
		Optional<Cliente> optionalCliente = repositorio.findById(clienteDto.getId());
		if (!optionalCliente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente not found");
		}
		Cliente cliente = optionalCliente.get();
		
		List<Documento> documentos = documentoRepositorio.findAllById(clienteDto.getDocumentos());
		Endereco endereco = enderecoRepositorio.findById(clienteDto.getEndereco()).orElse(null);
		List<Telefone> telefones = telefoneRepositorio.findAllById(clienteDto.getTelefones());
		
		ClienteAtualizador atualizador = new ClienteAtualizador();
		Cliente clienteAtualizado = clienteDto.toEntity(documentos, endereco, telefones);
		atualizador.atualizar(cliente, clienteAtualizado);
		
		repositorio.save(cliente);
	}
	
	@DeleteMapping("/excluir/{id}")
	public void excluirCliente(@PathVariable Long id) {
		Cliente cliente = repositorio.getById(id);
		repositorio.delete(cliente);
	}
	
}
