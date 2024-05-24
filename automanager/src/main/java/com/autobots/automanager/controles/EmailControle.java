package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkEmail;
import com.autobots.automanager.modelo.atualizadores.EmailAtualizador;
import com.autobots.automanager.modelo.selecionadores.EmailSelecionador;
import com.autobots.automanager.repositorios.EmailRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emails")
public class EmailControle {
	
	@Autowired
	public EmailRepositorio repositorio;
	
	@Autowired
	public EmailSelecionador selecionador;
	
	@Autowired
	public AdicionadorLinkEmail adicionadorLink;
	
	@Autowired
	public UsuarioRepositorio usuarioRepositorio;
	
	@GetMapping("/{id}")
	public Email buscarPorId(Long id) {
        Email email = repositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		adicionadorLink.adicionarLink(email);
		return email;
    }
	
	@GetMapping
	public List<Email> obterEmail(){
		List<Email> emails = repositorio.findAll();
		adicionadorLink.adicionarLink(emails);
        return emails;
	}
	
	@PostMapping("/cadastro")
	public void cadastrarEmail(@RequestBody Email email) {
		adicionadorLink.adicionarLink(email);
        repositorio.save(email);
    }
	
	@PutMapping("/atualizar")
	public void atualizarEmail(@RequestBody Email email) {
		Email emailAtual = repositorio.findById(email.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		EmailAtualizador atualizador = new EmailAtualizador();
		atualizador.atualizar(emailAtual, email);
        repositorio.save(email);
    }
	
	@DeleteMapping("/excluir/{id}")
    public void excluirEmail(@PathVariable Long id) {
        Email email = repositorio.findById(id)
		        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		List<Usuario> usuarios = usuarioRepositorio.findAll();
		for(Usuario usuario : usuarios) {
			if(usuario.getEmails().contains(email)){
				usuario.getEmails().remove(email);
                usuarioRepositorio.save(usuario);
			}
		}
		repositorio.delete(email);
    }
}
