package com.autobots.automanager.controles;

import com.autobots.automanager.dto.EmpresaDto;
import com.autobots.automanager.entidades.*;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkEmpresa;
import com.autobots.automanager.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {
	
	@Autowired
	public EmpresaRepositorio empresaRepositorio;
	
	@Autowired
	public TelefoneRepositorio telefoneRepositorio;
	
	@Autowired
	public EnderecoRepositorio enderecoRepositorio;
	
	@Autowired
	public UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	public MercadoriaRepositorio mercadoriaRepositorio;
	
	@Autowired
	public ServicoRepositorio servicoRepositorio;
	
	@Autowired
	public VendaRepositorio vendaRepositorio;
	
	@Autowired
	public AdicionadorLinkEmpresa adicionadorLink;
	
	@GetMapping("/{id}")
	public Empresa obterEmpresaPorId(@PathVariable Long id) {
		Empresa empresa = empresaRepositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		adicionadorLink.adicionarLink(empresa);
		return empresa;
	}
	
	@GetMapping
	public List<Empresa> obterEmpresa() {
		List<Empresa> empresa = empresaRepositorio.findAll();
		adicionadorLink.adicionarLink(empresa);
		return empresa;
	}
	
	@PostMapping("/cadastro")
	public void cadastrarEmpresa(@RequestBody EmpresaDto empresaDto) {
		Set<Telefone> telefones = telefoneRepositorio.findAllById(empresaDto.getTelefones()).stream().collect(Collectors.toSet());
		Endereco endereco = enderecoRepositorio.findById(empresaDto.getEndereco()).orElse(null);
		Set<Usuario> usuarios = usuarioRepositorio.findAllById(empresaDto.getUsuarios()).stream().collect(Collectors.toSet());
		Set<Mercadoria> mercadorias = mercadoriaRepositorio.findAllById(empresaDto.getMercadorias()).stream().collect(Collectors.toSet());
		Set<Servico> servicos = servicoRepositorio.findAllById(empresaDto.getServicos()).stream().collect(Collectors.toSet());
		Set<Venda> vendas = vendaRepositorio.findAllById(empresaDto.getVendas()).stream().collect(Collectors.toSet());
		
		Empresa empresa = empresaDto.toEntity(telefones, endereco, usuarios, mercadorias, servicos, vendas);

		empresaRepositorio.save(empresa);
		adicionadorLink.adicionarLink(empresa);
	}
	
	@PutMapping("/atualizar")
	public void atualizarEmpresa(@RequestBody EmpresaDto empresaDto) {
		Empresa empresaExistente = empresaRepositorio.findById(empresaDto.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		Set<Telefone> telefonesAtualizados = telefoneRepositorio.findAllById(empresaDto.getTelefones())
				.stream().collect(Collectors.toSet());
		Endereco endereco = enderecoRepositorio.findById(empresaDto.getEndereco()).orElse(null);
		Set<Usuario> usuarios = usuarioRepositorio.findAllById(empresaDto.getUsuarios())
				.stream().collect(Collectors.toSet());
		Set<Mercadoria> mercadorias = mercadoriaRepositorio.findAllById(empresaDto.getMercadorias())
				.stream().collect(Collectors.toSet());
		Set<Servico> servicos = servicoRepositorio.findAllById(empresaDto.getServicos())
				.stream().collect(Collectors.toSet());
		Set<Venda> vendas = vendaRepositorio.findAllById(empresaDto.getVendas())
				.stream().collect(Collectors.toSet());

		empresaExistente.setRazaoSocial(empresaDto.getRazaoSocial());
		empresaExistente.setNomeFantasia(empresaDto.getNomeFantasia());
		empresaExistente.getTelefones().clear();
		empresaExistente.getTelefones().addAll(telefonesAtualizados);
		empresaExistente.setEndereco(endereco);
		empresaExistente.setCadastro(empresaDto.getCadastro());
		empresaExistente.setUsuarios(usuarios);
		empresaExistente.setMercadorias(mercadorias);
		empresaExistente.setServicos(servicos);
		empresaExistente.setVendas(vendas);

		empresaRepositorio.save(empresaExistente);
		adicionadorLink.adicionarLink(empresaExistente);
	}
	
	
	@DeleteMapping("/excluir/{id}")
	public void excluirEmpresa(@PathVariable Long id) {
		Empresa empresa = empresaRepositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		empresaRepositorio.delete(empresa);
	}
}