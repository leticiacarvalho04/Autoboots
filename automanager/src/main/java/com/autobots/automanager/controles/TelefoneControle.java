package com.autobots.automanager.controles;

import java.util.List;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.AdicionadorLinkTelefone;
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
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
public class TelefoneControle {
	
	@Autowired
	public TelefoneRepositorio repositorio;

	@Autowired
	private AdicionadorLinkTelefone adicionadorLink;
	
	// cadastrar telefone
	@PostMapping("/cadastro/telefone")
	public ResponseEntity<?> cadastrarTelefone(@RequestBody Telefone telefone) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (telefone.getId() == null) {
			repositorio.save(telefone);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}
	
	// listar telefones
	@GetMapping("/telefones")
	public ResponseEntity<List<Telefone>> obterTelefone() {
		List<Telefone> telefone = repositorio.findAll();
		if (telefone.isEmpty()) {
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(telefone);
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(telefone, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	// atualizar telefone
	@PutMapping("/atualizar/telefone")
	public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Telefone telefone = repositorio.getById(atualizacao.getId());
		if (telefone != null) {
			TelefoneAtualizador atualizador = new TelefoneAtualizador();
			atualizador.atualizar(telefone, atualizacao);
			repositorio.save(telefone);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}
	
	// excluir telefone
	@DeleteMapping("/excluir/telefone")
	public ResponseEntity<?> excluirTelefone(@RequestBody Telefone exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Telefone telefone = repositorio.getById(exclusao.getId());
		if (telefone != null) {
			repositorio.delete(telefone);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}
