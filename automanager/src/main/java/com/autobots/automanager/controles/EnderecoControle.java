package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkCliente;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkEmpresa;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkEndereco;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkUsuario;
import com.autobots.automanager.modelo.atualizadores.EnderecoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {

	@Autowired
	public EnderecoRepositorio repositorio;
	
	@Autowired
	public AdicionadorLinkEndereco adicionadorLink;
	
	@Autowired
	public EmpresaRepositorio empresaRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private AdicionadorLinkUsuario adicionadorLinkUsuario;
	
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	@Autowired
	private AdicionadorLinkCliente adicionadorLinkCliente;
	
	@Autowired
	private AdicionadorLinkEmpresa adicionadorLinkEmpresa;
	
	@PostMapping("/cadastro/usuario/{idUsuario}")
	public ResponseEntity<Endereco> cadastrarEndereco(@RequestBody Endereco endereco, @PathVariable Long idUsuario) {
		Usuario usuario = usuarioRepositorio.findById(idUsuario).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		endereco = repositorio.save(endereco);
		usuario.setEndereco(endereco);
		usuarioRepositorio.save(usuario);
		adicionadorLinkUsuario.adicionarLink(usuario);
		return new ResponseEntity<>(endereco, HttpStatus.CREATED);
	}
	
	@PostMapping("/cadastro/cliente/{idCliente}")
	public ResponseEntity<Endereco> cadastrarEnderecoCliente(@RequestBody Endereco endereco, @PathVariable Long idCliente) {
		Cliente cliente = clienteRepositorio.findById(idCliente).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		endereco = repositorio.save(endereco);
		cliente.setEndereco(endereco);
		clienteRepositorio.save(cliente);
		adicionadorLinkCliente.adicionarLink(cliente);
		adicionadorLink.adicionarLink(endereco);
		return new ResponseEntity<>(endereco, HttpStatus.CREATED);
	}
	
	@PostMapping("/cadastro/empresa/{idEmpresa}")
	public ResponseEntity<Endereco>  cadastrarEnderecoEmpresa(@RequestBody Endereco endereco, @PathVariable Long idEmpresa) {
		Empresa empresa = empresaRepositorio.findById(idEmpresa).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));;
		endereco = repositorio.save(endereco);
		empresa.setEndereco(endereco);
		empresaRepositorio.save(empresa);
		adicionadorLinkEmpresa.adicionarLink(empresa);
		repositorio.save(endereco);
		adicionadorLink.adicionarLink(endereco);
		return new ResponseEntity<>(endereco, HttpStatus.CREATED);
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
