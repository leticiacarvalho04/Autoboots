package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkCliente;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkEmpresa;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkTelefone;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkUsuario;
import com.autobots.automanager.modelo.atualizadores.TelefoneAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	
	@Autowired
	public TelefoneRepositorio repositorio;
	
	@Autowired
	public AdicionadorLinkTelefone adicionadorLink;
	
	@Autowired
	public EmpresaRepositorio empresaRepositorio;
	
	@Autowired
	private AdicionadorLinkEmpresa adicionadorLinkEmpresa;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private AdicionadorLinkUsuario adicionadorLinkUsuario;
	
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	@Autowired
	private AdicionadorLinkCliente adicionadorLinkCliente;
	
	@PostMapping("/cadastro/empresa/{idEmpresa}")
	public ResponseEntity<Telefone> cadastrarTelefoneEmpresa(@RequestBody Telefone telefone, @PathVariable Long idEmpresa) {
		Empresa empresa = empresaRepositorio.findById(idEmpresa).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		telefone = repositorio.save(telefone);
		empresa.getTelefones().add(telefone);
		empresaRepositorio.save(empresa);
		adicionadorLinkEmpresa.adicionarLink(empresa);
		adicionadorLink.adicionarLink(telefone);
		return new ResponseEntity<>(telefone, HttpStatus.CREATED);
	}
	
	@PostMapping("/cadastro/usuario/{idUsuario}")
	public ResponseEntity<Telefone> cadastrarTelefoneUsuario(@RequestBody Telefone telefone, @PathVariable Long idUsuario) {
		Usuario usuario = usuarioRepositorio.findById(idUsuario).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		telefone = repositorio.save(telefone);
		usuario.getTelefones().add(telefone);
		usuarioRepositorio.save(usuario);
		adicionadorLinkUsuario.adicionarLink(usuario);
		adicionadorLink.adicionarLink(telefone);
		return new ResponseEntity<>(telefone, HttpStatus.CREATED);
	}
	
	@PostMapping("/cadastro/cliente/{idCliente}")
	public ResponseEntity<Telefone> cadastrarTelefoneCliente(@RequestBody Telefone telefone, @PathVariable Long idCliente) {
		Cliente cliente = clienteRepositorio.getById(idCliente);
		telefone = repositorio.save(telefone);
		cliente.getTelefones().add(telefone);
		clienteRepositorio.save(cliente);
		adicionadorLinkCliente.adicionarLink(cliente);
		adicionadorLink.adicionarLink(telefone);
		return new ResponseEntity<>(telefone, HttpStatus.CREATED);
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
