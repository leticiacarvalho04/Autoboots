package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkTelefone;
import com.autobots.automanager.modelo.atualizadores.TelefoneAtualizador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	
	@Autowired
	public TelefoneRepositorio repositorio;
	
	@Autowired
	public AdicionadorLinkTelefone adicionadorLink;
	
	@Autowired
	public EmpresaRepositorio empresaRepositorio;

	@PostMapping("/cadastro")
	public void cadastrarTelefone(@RequestBody Telefone telefone) {
		adicionadorLink.adicionarLink(telefone);
		repositorio.save(telefone);
	}

	@GetMapping
	public List<Telefone> obterTelefone(){
		List<Telefone> telefones = repositorio.findAll();
		adicionadorLink.adicionarLink(telefones);
		return repositorio.findAll();
	}

	@PutMapping("/atualizar")
	public void atualizarTelefone(@RequestBody Telefone t) {
		Telefone telefone = repositorio.getById(t.getId());
		TelefoneAtualizador atualizador = new TelefoneAtualizador();
		atualizador.atualizar(telefone, t);
		repositorio.save(t);
	}

	@DeleteMapping("/excluir/{id}")
	public void excluirCliente(@PathVariable Long id) {
		Telefone telefone = repositorio.getById(id);
		List<Empresa> empresas = empresaRepositorio.findAll();
		for(Empresa e : empresas){
			if(e.getTelefones().contains(telefone)){
				e.getTelefones().remove(telefone);
				empresaRepositorio.save(e);
			}
		}
		repositorio.delete(telefone);
	}
}
