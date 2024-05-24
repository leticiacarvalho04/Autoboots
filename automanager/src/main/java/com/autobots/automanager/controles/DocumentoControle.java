package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkDocumento;
import com.autobots.automanager.modelo.atualizadores.DocumentoAtualizador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
	
	@Autowired
	public DocumentoRepositorio repositorio;
	
	@Autowired
	public AdicionadorLinkDocumento adicionadorLink;
	
	@Autowired
	public UsuarioRepositorio usuarioRepositorio;
	
	@PostMapping("/cadastro")
	public void cadastrarDocumento(@RequestBody Documento documento) {
		adicionadorLink.adicionarLink(documento);
		repositorio.save(documento);
	}
	
	@GetMapping
	public List<Documento> obterDocumento(){
		List<Documento> documentos = repositorio.findAll();
		adicionadorLink.adicionarLink(documentos);
		return documentos;
	}

	@PutMapping("/atualizar")
	public void atualizarDocumento(@RequestBody Documento doc) {
		Documento documento = repositorio.getById(doc.getId());
		DocumentoAtualizador atualizador = new DocumentoAtualizador();
		atualizador.atualizar(documento, documento);
		repositorio.save(documento);
	}
	
	@DeleteMapping("/excluir/{id}")
	public void excluirCliente(@PathVariable Long id) {
		Documento documento = repositorio.getById(id);
		List<Usuario> usuarios = usuarioRepositorio.findAll();
		for(Usuario usuario : usuarios){
			if(usuario.getDocumentos().contains(documento)){
				usuario.getDocumentos().remove(documento);
                usuarioRepositorio.save(usuario);
			}
		}
		repositorio.delete(documento);
	}

}
