package com.autobots.automanager.entidades;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Email {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String endereco;
}
