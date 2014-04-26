package com.movilesseguros.entidades;

public class Empresa {

	private int id;
	private String razonSocial;
	private String direccion;
	private String telefono;
	private String fax;
	private String paginaWeb;
	private String email;
	private double latitud;
	private double longitud;
	private String logo;

	public Empresa() {

	}

	public Empresa(int id) {
		super();
		this.id = id;
	}

	public Empresa(int id, String razonSocial, String direccion,
			String telefono, String fax, String paginaWeb, String email,
			double latitud, double longitud, String logo) {
		super();
		this.id = id;
		this.razonSocial = razonSocial;
		this.direccion = direccion;
		this.telefono = telefono;
		this.fax = fax;
		this.paginaWeb = paginaWeb;
		this.email = email;
		this.latitud = latitud;
		this.longitud = longitud;
		this.logo = logo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPaginaWeb() {
		return paginaWeb;
	}

	public void setPaginaWeb(String paginaWeb) {
		this.paginaWeb = paginaWeb;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

}
