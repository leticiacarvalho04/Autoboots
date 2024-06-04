package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkEmpresa;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkServico;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkVenda;
import com.autobots.automanager.modelo.atualizadores.ServicoAtualizador;
import com.autobots.automanager.modelo.selecionadores.ServicoSelecionador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.ServicoRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/servico")
public class ServicoControle {
	
	@Autowired
	public ServicoRepositorio repositorio;
	
	@Autowired
	public ServicoSelecionador selecionador;
	
	@Autowired
	public AdicionadorLinkServico adicionadorLink;
	
	@Autowired
	public VendaRepositorio vendaRepositorio;
	
	@Autowired
	public EmpresaRepositorio empresaRepositorio;
	
	@Autowired
	private AdicionadorLinkEmpresa adicionadorLinkEmpresa;
	
	@Autowired
	private AdicionadorLinkVenda adicionadorLinkVenda;
	
	@GetMapping("/{id}")
	public Servico buscarServicoPeloId(Long id){
		Servico servico = repositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return servico;
	}
	
	@GetMapping
	public List<Servico> obterServico(){
        List<Servico> servico = repositorio.findAll();
		adicionadorLink.adicionarLink(servico);
        return servico;
    }
	
	@PostMapping("/cadastro/venda/{idVenda}")
	public ResponseEntity<Servico> cadastrarServico(@RequestBody Servico servico, @PathVariable Long idVenda) {
		Venda venda = vendaRepositorio.getById(idVenda);
		servico = repositorio.save(servico);
		venda.getServicos().add(servico);
		adicionadorLinkVenda.adicionarLink(venda);
		repositorio.save(servico);
		adicionadorLink.adicionarLink(servico);
		return new ResponseEntity<Servico>(servico, HttpStatus.CREATED);
	}
	
	@PostMapping("/cadastro/empresa/{idEmpresa}")
	public ResponseEntity<Servico> cadastrarServicoEmpresa(@RequestBody Servico servico, @PathVariable Long idEmpresa) {
		Empresa empresa = empresaRepositorio.getById(idEmpresa);
		servico = repositorio.save(servico);
		empresa.getServicos().add(servico);
		adicionadorLinkEmpresa.adicionarLink(empresa);
		repositorio.save(servico);
		adicionadorLink.adicionarLink(servico);
		return new ResponseEntity<Servico>(servico, HttpStatus.CREATED);
	}
	
	@PutMapping("/atualizar")
	public void atualizarServico(@RequestBody Servico servico) {
		Servico servicoId = repositorio.getById(servico.getId());
        ServicoAtualizador atualizador = new ServicoAtualizador();
        atualizador.atualizar(servicoId, servico);
        repositorio.save(servico);
	}
	
	@DeleteMapping("/excluir/{id}")
	public void excluirServico(@PathVariable Long id) {
		Servico servico = repositorio.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		List<Venda> vendas = vendaRepositorio.findAll();
		List<Empresa> empresas = empresaRepositorio.findAll();
		for (Empresa empresa : empresas) {
			empresa.getServicos().removeIf(s -> s.getId().equals(id));
			empresaRepositorio.save(empresa);
		}
		for (Venda venda : vendas) {
			if (venda.getServicos().contains(servico)) {
				venda.getServicos().remove(servico);
				vendaRepositorio.save(venda);
			}
		}
		repositorio.delete(servico);
	}
}
