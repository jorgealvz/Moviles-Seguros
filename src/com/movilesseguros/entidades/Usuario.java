package com.movilesseguros.entidades;

public class Usuario {

	public static final int PROPIO = 1;
	public static final int FACEBOOK = 2;
	public static final int GOOGLE = 3;

	private String id;
	private String pass;
	private int tipo;
	private int estado;

	public Usuario() {

	}

	public Usuario(String id) {
		super();
		this.id = id;
	}

	public Usuario(String id, String pass, int tipo, int estado) {
		super();
		this.id = id;
		this.pass = pass;
		this.tipo = tipo;
		this.estado = estado;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

}
