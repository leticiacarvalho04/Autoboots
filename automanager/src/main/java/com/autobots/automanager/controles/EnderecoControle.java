package com.autobots.automanager.controles;

import java.util.List;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.AdicionadorLinkEndereco;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
public class EnderecoControle {

	@Autowired
	public EnderecoRepositorio repositorio;

	@Autowired
	private AdicionadorLinkEndereco adicionadorLink;
	
	// cadastrar endereço
	@PostMapping("/cadastro/endereco")
	public ResponseEntity<?> cadastrarEndereco(@RequestBody Endereco endereco) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (endereco.getId() == null) {
			repositorio.save(endereco);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);

	}
	
	// listar endereços
	@GetMapping("/enderecos")
	public ResponseEntity<List<Endereco>> obterEndereco() {
		List<Endereco> enderecos = repositorio.findAll();
		if (enderecos.isEmpty()) {
			ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(enderecos);
			ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(enderecos, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	//atualizar endereços
	@PutMapping("/atualizar/endereco")
	public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Endereco endereco = repositorio.getById(atualizacao.getId());
		if (endereco != null) {
			EnderecoAtualizador atualizador = new EnderecoAtualizador();
			atualizador.atualizar(endereco, atualizacao);
			repositorio.save(endereco);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}
	
	// excluir endereço
	@DeleteMapping("/excluir/endereco")
	public ResponseEntity<?> excluirEndereco(@RequestBody Endereco exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Endereco endereco = repositorio.getById(exclusao.getId());
		if (endereco != null) {
			repositorio.delete(endereco);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
	
}
