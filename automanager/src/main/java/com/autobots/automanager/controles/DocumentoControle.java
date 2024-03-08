package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
public class DocumentoControle {
	
	@Autowired
	private DocumentoRepositorio repositorio;
	
	// cadastro documento
	@PostMapping("/cadastro/documento")
	public void cadastrarDocumento(@RequestBody Documento documento) {
		repositorio.save(documento);
	}
	
	// listagem dos documentos
	@GetMapping("/documento")
	public List<Documento> listar(){
		return repositorio.findAll();
	}
	
	// atualizar documentos
	@PutMapping("/atualizar/documento")
	public void atualizarDocumento(@RequestBody Documento doc) {
		Documento documento = repositorio.getById(doc.getId());
		DocumentoAtualizador atualizador = new DocumentoAtualizador();
		atualizador.atualizar(documento, documento);
		repositorio.save(documento);
	}
	
	// excluir documento
	@DeleteMapping("/excluir/documento")
	public void excluirCliente(@RequestBody Documento exclusao) {
		Documento documento = repositorio.getById(exclusao.getId());
		repositorio.delete(documento);
	}

}
