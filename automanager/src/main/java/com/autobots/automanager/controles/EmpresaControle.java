package com.autobots.automanager.controles;

import com.autobots.automanager.dto.EmpresaDto;
import com.autobots.automanager.entidades.*;
import com.autobots.automanager.modelo.adicionadorLink.*;
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
	
	@Autowired
	private AdicionadorLinkEmpresa adicionadorLinkEmpresa;
	
	@Autowired
	private AdicionadorLinkTelefone adicionadorLinkTelefone;
	
	@Autowired
	private AdicionadorLinkUsuario adicionadorLinkUsuario;
	
	@Autowired
	private AdicionadorLinkMercadoria adicionadorLinkMercadoria;
	
	@Autowired
	private AdicionadorLinkServico adicionadorLinkServico;
	
	@Autowired
	private AdicionadorLinkVenda adicionadorLinkVenda;
	
	@Autowired
	private AdicionadorLinkEndereco adicionadorLinkEndereco;
	
	@Autowired
	private AdicionadorLinkDocumento adicionadorLinkDocumento;
	
	@Autowired
	private AdicionadorLinkEmail adicionadorLinkEmail;
	
	@Autowired
	private AdicionadorLinkCredencial adicionadorLinkCredencial;
	
	
	@GetMapping("/{id}")
	public Empresa obterEmpresaPorId(@PathVariable Long id) {
		Empresa empresa = empresaRepositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		adicionadorLink.adicionarLink(empresa);
		return empresa;
	}
	
	@GetMapping
	public List<Empresa> obterEmpresa() {
		List<Empresa> empresas = empresaRepositorio.findAll();
		for (Empresa empresa : empresas) {
			adicionadorLinkEmpresa.adicionarLink(empresa);
			
			for (Telefone telefone : empresa.getTelefones()) {
				adicionadorLinkTelefone.adicionarLink(telefone);
			}
			
			if (empresa.getEndereco() != null) {
				adicionadorLinkEndereco.adicionarLink(empresa.getEndereco());
			}
			
			for (Usuario usuario : empresa.getUsuarios()) {
				adicionadorLinkUsuario.adicionarLink(usuario);
				
				// Adicionar links para telefones de cada usuário
				for (Telefone telefone : usuario.getTelefones()) {
					adicionadorLinkTelefone.adicionarLink(telefone);
				}
				
				// Adicionar links para documentos de cada usuário, se houver
				for (Documento documento : usuario.getDocumentos()) {
					adicionadorLinkDocumento.adicionarLink(documento);
				}
				
				// Adicionar links para emails de cada usuário, se houver
				for (Email email : usuario.getEmails()) {
					adicionadorLinkEmail.adicionarLink(email);
				}

				for (Credencial credencial : usuario.getCredenciais()) {
					adicionadorLinkCredencial.adicionarLink(credencial);
				}

				for (Mercadoria mercadoria : usuario.getMercadorias()) {
					adicionadorLinkMercadoria.adicionarLink(mercadoria);
				}

				for (Venda venda : usuario.getVendas()) {
					adicionadorLinkVenda.adicionarLink(venda);
				}
			}
			
			for (Mercadoria mercadoria : empresa.getMercadorias()) {
				adicionadorLinkMercadoria.adicionarLink(mercadoria);
			}
			
			for (Servico servico : empresa.getServicos()) {
				adicionadorLinkServico.adicionarLink(servico);
			}
			
			for (Venda venda : empresa.getVendas()) {
				adicionadorLinkVenda.adicionarLink(venda);
			}
		}
		return empresas;
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
		Empresa empresa = empresaRepositorio.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		empresa.setUsuarios(null);
		empresaRepositorio.save(empresa);
		empresaRepositorio.delete(empresa);
	}
}