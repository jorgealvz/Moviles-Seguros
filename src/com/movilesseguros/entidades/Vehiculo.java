package com.movilesseguros.entidades;

public class Vehiculo {

	private int id;
	private int modelo;
	private String placaPta;
	private String placaAnt;
	private String foto;
	private String observaciones;
	private int nroMovil;
	private Empresa empresa;
	private Conductor conductor;
	private ClaseVehiculo claseVehiculo;
	private TipoVehiculo tipoVehiculo;
	private MarcaVehiculo marcaVehiculo;

	public Vehiculo() {

	}

	public Vehiculo(int id) {
		super();
		this.id = id;
	}

	public Vehiculo(int id, int modelo, String placaPta, String placaAnt,
			String foto, String observaciones, int nroMovil, Empresa empresa,
			Conductor conductor, ClaseVehiculo claseVehiculo,
			TipoVehiculo tipoVehiculo, MarcaVehiculo marcaVehiculo) {
		super();
		this.id = id;
		this.modelo = modelo;
		this.placaPta = placaPta;
		this.placaAnt = placaAnt;
		this.foto = foto;
		this.observaciones = observaciones;
		this.nroMovil = nroMovil;
		this.empresa = empresa;
		this.conductor = conductor;
		this.claseVehiculo = claseVehiculo;
		this.tipoVehiculo = tipoVehiculo;
		this.marcaVehiculo = marcaVehiculo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getModelo() {
		return modelo;
	}

	public void setModelo(int modelo) {
		this.modelo = modelo;
	}

	public String getPlacaPta() {
		return placaPta;
	}

	public void setPlacaPta(String placaPta) {
		this.placaPta = placaPta;
	}

	public String getPlacaAnt() {
		return placaAnt;
	}

	public void setPlacaAnt(String placaAnt) {
		this.placaAnt = placaAnt;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getNroMovil() {
		return nroMovil;
	}

	public void setNroMovil(int nroMovil) {
		this.nroMovil = nroMovil;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Conductor getConductor() {
		return conductor;
	}

	public void setConductor(Conductor conductor) {
		this.conductor = conductor;
	}

	public ClaseVehiculo getClaseVehiculo() {
		return claseVehiculo;
	}

	public void setClaseVehiculo(ClaseVehiculo claseVehiculo) {
		this.claseVehiculo = claseVehiculo;
	}

	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public MarcaVehiculo getMarcaVehiculo() {
		return marcaVehiculo;
	}

	public void setMarcaVehiculo(MarcaVehiculo marcaVehiculo) {
		this.marcaVehiculo = marcaVehiculo;
	}

}
