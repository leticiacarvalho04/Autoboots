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
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
public class TelefoneControle {
	
	@Autowired
	public TelefoneRepositorio repositorio;
	
	// cadastrar telefone
	@PostMapping("/cadastro/telefone")
	public void cadastrarTelefone(@RequestBody Telefone telefone) {
		repositorio.save(telefone);
	}
	
	// listar telefones
	@GetMapping("/telefone")
	public List<Telefone> listar(){
		return repositorio.findAll();
	}
	
	// atualizar telefone
	@PutMapping("/atualizar/telefone")
	public void atualizarTelefone(@RequestBody Telefone t) {
		Telefone telefone = repositorio.getById(t.getId());
		TelefoneAtualizador atualizador = new TelefoneAtualizador();
		atualizador.atualizar(telefone, t);
		repositorio.save(t);
	}
	
	// excluir telefone
	@DeleteMapping("/excluir/telefone")
	public void excluirCliente(@RequestBody Telefone exclusao) {
		Telefone telefone = repositorio.getById(exclusao.getId());
		repositorio.delete(telefone);
	}
}
