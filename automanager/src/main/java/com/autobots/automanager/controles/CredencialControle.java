package com.autobots.automanager.controles;

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
	public CredencialUsuarioSenha getCredencial(@PathVariable long id) {
		List<CredencialUsuarioSenha> credenciais = repositorio.findAll();
		CredencialUsuarioSenha credencial = selecionador.selecionar(credenciais, id);
		adicionadorLink.adicionarLink(credencial);
        return credencial;
    }
	
	@GetMapping
    public List<CredencialUsuarioSenha> obterCredenciais() {
        List<CredencialUsuarioSenha> credenciais = repositorio.findAll();
		adicionadorLink.adicionarLink(credenciais);
        return credenciais;
    }
	
	@PostMapping("/cadastro")
	public void cadastrarCredencial(@RequestBody CredencialUsuarioSenha credencial) {
        adicionadorLink.adicionarLink(credencial);
		repositorio.save(credencial);
    }
	
	@PutMapping("/atualizar")
    public void atualizarCredencial(@RequestBody CredencialUsuarioSenha credencial) {
        repositorio.save(credencial);
    }
	
	@DeleteMapping("/excluir/{id}")
	public void excluirCredencial(@PathVariable Long id) {
		CredencialUsuarioSenha credencial = repositorio.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credencial not found"));
		repositorio.delete(credencial);
	}
}
