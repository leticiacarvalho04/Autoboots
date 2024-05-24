package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkCliente;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.ClienteDto;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.modelo.atualizadores.ClienteAtualizador;
import com.autobots.automanager.modelo.selecionadores.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import org.springframework.web.server.ResponseStatusException;

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
		Optional<Cliente> cliente = repositorio.findById(id);
		if (cliente.isPresent()) {
			Cliente clienteEncontrado = cliente.get();
			adicionadorLink.adicionarLink(clienteEncontrado);
			return new ResponseEntity<>(clienteEncontrado, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping
	public ResponseEntity<List<Cliente>> obterCliente() {
		List<Cliente> clientes = repositorio.findAll();
		if (clientes.isEmpty()) {
			ResponseEntity<List<Cliente>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(clientes);
			ResponseEntity<List<Cliente>> resposta = new ResponseEntity<>(clientes, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@PostMapping("/cadastro")
	public void cadastrarCliente(@RequestBody ClienteDto clienteDto) {
		List<Documento> documentos = documentoRepositorio.findAllById(clienteDto.getDocumentos());
		Endereco endereco = enderecoRepositorio.findById(clienteDto.getEndereco()).orElse(null);
		List<Telefone> telefones = telefoneRepositorio.findAllById(clienteDto.getTelefones());
		
		Cliente cliente = clienteDto.toEntity(documentos, endereco, telefones);
		cliente.setDocumentos(documentos);
		cliente.setEndereco(endereco);
		cliente.setTelefones(telefones);
		repositorio.save(cliente);
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
