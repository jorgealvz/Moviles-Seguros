package com.movilesseguros.entidades;

public class TipoVehiculo {

	private int id;
	private String descripcion;

	public TipoVehiculo() {

	}

	public TipoVehiculo(int id) {
		super();
		this.id = id;
	}

	public TipoVehiculo(int id, String descripcion) {
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
