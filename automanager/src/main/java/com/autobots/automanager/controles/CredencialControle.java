package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkCredencial;
import com.autobots.automanager.modelo.selecionadores.CredencialSelecionador;
import com.autobots.automanager.repositorios.CredencialRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/credencial")
public class CredencialControle {
	
	@Autowired
	public CredencialRepositorio repositorio;
	
	@Autowired
	public CredencialSelecionador selecionador;
	
	@Autowired
	public AdicionadorLinkCredencial adicionadorLink;
	
	@GetMapping("/{id}")
	public Credencial getCredencial(@PathVariable long id) {
		Credencial credencial = repositorio.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credencial not found"));
		adicionadorLink.adicionarLink(credencial);
		return credencial;
	}
	
	@GetMapping
	public List<CredencialUsuarioSenha> obterCredenciais() {
		List<CredencialUsuarioSenha> credenciais = repositorio.findAll();
		for (CredencialUsuarioSenha credencial : credenciais) {
			adicionadorLink.adicionarLink(credencial);
		}
		return credenciais;
	}
	
	@PostMapping("/cadastro")
	public void cadastrarCredencial(@RequestBody CredencialUsuarioSenha credencial) {
		adicionadorLink.adicionarLink(credencial);
		repositorio.save(credencial);
	}
	
	
	@PutMapping("/atualizar")
	public void atualizarCredencial(@RequestBody CredencialUsuarioSenha credencialAtualizada) {
		CredencialUsuarioSenha credencialExistente = repositorio.findById(credencialAtualizada.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credencial not found"));

		credencialExistente.setCriacao(credencialAtualizada.getCriacao());
		credencialExistente.setUltimoAcesso(credencialAtualizada.getUltimoAcesso());
		credencialExistente.setInativo(credencialAtualizada.isInativo());
		credencialExistente.setNomeUsuario(credencialAtualizada.getNomeUsuario());
		credencialExistente.setSenha(credencialAtualizada.getSenha());

		repositorio.save(credencialExistente);
	}
	
	
	@DeleteMapping("/excluir/{id}")
	public void excluirCredencial(@PathVariable Long id) {
		CredencialUsuarioSenha credencial = repositorio.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credencial not found"));
		repositorio.delete(credencial);
	}
}