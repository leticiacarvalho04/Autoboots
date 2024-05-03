package com.autobots.automanager.entidades;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Inheritance(strategy= InheritanceType.JOINED)
public class Credencial {
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Date criacao;

	@Column()
	private Date ultimoAcesso;

	@Column(nullable = false)
	private boolean inativo;
}
