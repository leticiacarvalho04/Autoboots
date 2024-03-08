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
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
public class EnderecoControle {

	@Autowired
	public EnderecoRepositorio repositorio;
	
	// cadastrar endereço
	@PostMapping("/cadastro/endereco")
	public void cadastrarEndereco(@RequestBody Endereco endereco) {
		repositorio.save(endereco);
	}
	
	// listar endereços
	@GetMapping("/enderecos")
	public List<Endereco> listar(){
		return repositorio.findAll();
	}
	
	//editar endereços
	@PutMapping("/editar/endereco")
	public void atualizarEndereco(@RequestBody Endereco e) {
		Endereco endereco = repositorio.getById(e.getId());
		EnderecoAtualizador atualizador = new EnderecoAtualizador();
		atualizador.atualizar(endereco, e);
		repositorio.save(endereco);
	}
	
	// excluir endereço
	@DeleteMapping("/excluir/endereco")
	public void excluirCliente(@RequestBody Endereco exclusao) {
		Endereco endereco = repositorio.getById(exclusao.getId());
		repositorio.delete(endereco);
	}
	
}
