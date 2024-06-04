package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkCliente;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkDocumento;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkUsuario;
import com.autobots.automanager.modelo.atualizadores.DocumentoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
	
	@Autowired
	private AdicionadorLinkUsuario adicionadorLinkUsuario;
	
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	@Autowired
	private AdicionadorLinkCliente adicionadorLinkCliente;
	
	
	@PostMapping("/cadastro/usuario/{idUsuario}")
	public ResponseEntity<Documento> cadastrarDocumentoUsuario(@RequestBody Documento documento, @PathVariable Long idUsuario) {
		Usuario usuario = usuarioRepositorio.getById(idUsuario);
		usuario.getDocumentos().add(documento);
		usuarioRepositorio.save(usuario);
		adicionadorLinkUsuario.adicionarLink(usuario);
		adicionadorLink.adicionarLink(documento);
		repositorio.save(documento);
		return new ResponseEntity<>(documento, HttpStatus.CREATED);
	}
	
	@PostMapping("/cadastro/cliente/{idCliente}")
	public ResponseEntity<Documento> cadastrarDocumentoCliente(@RequestBody Documento documento, @PathVariable Long idCliente) {
		Cliente cliente = clienteRepositorio.findById(idCliente)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado com ID: " + idCliente));
		
		cliente.getDocumentos().add(documento);
		clienteRepositorio.save(cliente);
		adicionadorLinkCliente.adicionarLink(cliente);

		Documento documentoPersistido = cliente.getDocumentos().stream()
				.filter(doc -> doc.getNumero().equals(documento.getNumero()) && doc.getTipo().equals(documento.getTipo()))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar o documento"));
		
		return new ResponseEntity<>(documentoPersistido, HttpStatus.CREATED);
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
