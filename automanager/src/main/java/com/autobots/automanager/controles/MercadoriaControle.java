package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.adicionadorLink.AdicionadorLinkMercadoria;
import com.autobots.automanager.modelo.atualizadores.MercadoriaAtualizadora;
import com.autobots.automanager.modelo.selecionadores.MercadoriaSelecionadora;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {
	
	@Autowired
	public MercadoriaRepositorio repositorio;
	
	@Autowired
	public MercadoriaSelecionadora selecionador;
	
	@Autowired
	public AdicionadorLinkMercadoria adicionadorLink;
	
	@Autowired
	public UsuarioRepositorio usuarioRepositorio;
	
	@GetMapping("/{id}")
	public Mercadoria buscarPorId(Long id) {
        Mercadoria mercadoria = repositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		adicionadorLink.adicionarLink(mercadoria);
		return mercadoria;
    }
	
	@GetMapping
	public List<Mercadoria> obterMercadoria(){
        List<Mercadoria> mercadoria = repositorio.findAll();
		adicionadorLink.adicionarLink(mercadoria);
        return mercadoria;
    }
	
	@PostMapping("/cadastro")
	public void cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
        repositorio.save(mercadoria);
		adicionadorLink.adicionarLink(mercadoria);
    }
	
	@PutMapping("/atualizar")
	public void atualizarMercadoria(@RequestBody Mercadoria mercadoria) {
        Mercadoria mercadoriaId = repositorio.getById(mercadoria.getId());
		MercadoriaAtualizadora atualizador = new MercadoriaAtualizadora();
        atualizador.atualizar(mercadoriaId, mercadoria);
        repositorio.save(mercadoria);
    }
	
	@DeleteMapping("/excluir/{id}")
	public void excluirMercadoria(@PathVariable Long id) {
		Mercadoria mercadoria = repositorio.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		List<Usuario> usuarios = usuarioRepositorio.findAll();
		for (Usuario usuario : usuarios) {
			if (usuario.getMercadorias().contains(mercadoria)) {
				usuario.getMercadorias().remove(mercadoria);
				usuarioRepositorio.save(usuario);
			}
		}

		repositorio.delete(mercadoria);
	}
}
