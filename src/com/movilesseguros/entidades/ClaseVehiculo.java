package com.movilesseguros.entidades;

public class ClaseVehiculo {

	private int id;
	private String descripcion;

	public ClaseVehiculo() {

	}

	public ClaseVehiculo(int id) {
		super();
		this.id = id;
	}

	public ClaseVehiculo(int id, String descripcion) {
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
