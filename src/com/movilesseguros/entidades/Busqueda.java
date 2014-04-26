package com.movilesseguros.entidades;

public class Busqueda {

	private int id;
	private String descripcion;

	public Busqueda() {

	}

	public Busqueda(int id) {
		super();
		this.id = id;
	}

	public Busqueda(int id, String descripcion) {
		super();
		this.id = id;
		this.descripcion = descripcion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
