package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.ClienteDto;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.modelo.ClienteAtualizador;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/clientes")
public class ClienteControle {
	
	@Autowired
	private ClienteRepositorio repositorio;
	
	@Autowired
	private DocumentoRepositorio documentoRepositorio;
	
	@Autowired
	private EnderecoRepositorio enderecoRepositorio;
	
	@Autowired
	private TelefoneRepositorio telefoneRepositorio;
	
	@Autowired
	private ClienteSelecionador selecionador;
	
	@GetMapping("/{id}")
	public Cliente obterCliente(@PathVariable long id) {
		List<Cliente> clientes = repositorio.findAll();
		Cliente cliente = selecionador.selecionar(clientes, id);
		return cliente ;
	}
	
	@GetMapping
	public List<Cliente> obterClientes() {
		List<Cliente> clientes = repositorio.findAll();
		return clientes;
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
	
	@DeleteMapping("/excluir")
	public void excluirCliente(@RequestBody ClienteDto clienteDto) {
		Cliente cliente = repositorio.getById(clienteDto.getId());
		repositorio.delete(cliente);
	}
	
}
