package com.movilesseguros.entidades;

public class MarcaVehiculo {

	private int id;
	private String descripcion;

	public MarcaVehiculo() {

	}

	public MarcaVehiculo(int id) {
		super();
		this.id = id;
	}

	public MarcaVehiculo(int id, String descripcion) {
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
