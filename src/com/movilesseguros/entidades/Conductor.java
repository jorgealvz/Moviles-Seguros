package com.movilesseguros.entidades;

public class Conductor {

	private int id;
	private String nombre;
	private String paterno;
	private String materno;
	private String nacionalidad;
	private int edad;
	private String foto;
	private String observacion;

	public Conductor() {

	}

	public Conductor(int id) {
		super();
		this.id = id;
	}

	public Conductor(int id, String nombre, String paterno, String materno,
			String nacionalidad, int edad, String foto, String observacion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.paterno = paterno;
		this.materno = materno;
		this.nacionalidad = nacionalidad;
		this.edad = edad;
		this.foto = foto;
		this.observacion = observacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPaterno() {
		return paterno;
	}

	public void setPaterno(String paterno) {
		this.paterno = paterno;
	}

	public String getMaterno() {
		return materno;
	}

	public void setMaterno(String materno) {
		this.materno = materno;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

}
