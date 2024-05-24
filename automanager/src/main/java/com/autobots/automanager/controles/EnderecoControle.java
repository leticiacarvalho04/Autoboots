package com.autobots.automanager.controles;

import java.util.List;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkEndereco;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.atualizadores.EnderecoAtualizador;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {

	@Autowired
	public EnderecoRepositorio repositorio;
	
	@Autowired
	public AdicionadorLinkEndereco adicionadorLink;
	
	@Autowired
	public EmpresaRepositorio empresaRepositorio;
	
	@PostMapping("/cadastro")
	public void cadastrarEndereco(@RequestBody Endereco endereco) {
		adicionadorLink.adicionarLink(endereco);
		repositorio.save(endereco);
	}

	@GetMapping
	public List<Endereco> obterEndereco(){
		List<Endereco> enderecos = repositorio.findAll();
		adicionadorLink.adicionarLink(enderecos);
		return enderecos;
	}

	@PutMapping("/atualizar")
	public void atualizarEndereco(@RequestBody Endereco e) {
		Endereco endereco = repositorio.getById(e.getId());
		EnderecoAtualizador atualizador = new EnderecoAtualizador();
		atualizador.atualizar(endereco, e);
		repositorio.save(endereco);
	}

	@DeleteMapping("/excluir/{id}")
	public void excluirCliente(@PathVariable Long id) {
		Endereco endereco = repositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		List<Empresa> empresas = empresaRepositorio.findAll();
		for(Empresa e : empresas){
			if(e.getEndereco().getId().equals(endereco.getId())){
                e.setEndereco(null);
                empresaRepositorio.save(e);
            }
		}
		repositorio.delete(endereco);
	}
	
}
