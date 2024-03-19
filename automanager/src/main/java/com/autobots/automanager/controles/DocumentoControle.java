package com.autobots.automanager.controles;

import java.util.List;

import com.autobots.automanager.modelo.AdicionadorLinkDocumento;
import com.autobots.automanager.modelo.ClienteAtualizador;
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
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

import javax.print.Doc;

@RestController
public class DocumentoControle {
	
	@Autowired
	private DocumentoRepositorio repositorio;

	@Autowired
	private AdicionadorLinkDocumento adicionadorLink;
	
	// cadastro documento
	@PostMapping("/cadastro/documento")
	public ResponseEntity<?> cadastrarDocumento(@RequestBody Documento documento) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (documento.getId() == null) {
			repositorio.save(documento);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}
	
	// listagem dos documentos
	@GetMapping("/documento")
	public ResponseEntity<List<Documento>> obterDocumento() {
		List<Documento> documentos = repositorio.findAll();
		if (documentos.isEmpty()) {
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(documentos);
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(documentos, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	// atualizar documentos
	@PutMapping("/atualizar/documento")
	public ResponseEntity<?> atualizarDocumento(@RequestBody Documento atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Documento documento = repositorio.getById(atualizacao.getId());
		if (documento != null) {
			DocumentoAtualizador atualizador = new DocumentoAtualizador();
			atualizador.atualizar(documento, atualizacao);
			repositorio.save(documento);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}
	
	// excluir documento
	@DeleteMapping("/excluir/documento")
	public ResponseEntity<?> excluirDocumento(@RequestBody Documento exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Documento documento = repositorio.getById(exclusao.getId());
		if (documento != null) {
			repositorio.delete(documento);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

}
