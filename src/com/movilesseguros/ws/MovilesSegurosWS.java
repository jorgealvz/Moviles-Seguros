package com.movilesseguros.ws;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.android.helper.ws.AbstractWebService;
import com.android.helper.ws.Respuesta;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.movilesseguros.entidades.Conductor;
import com.movilesseguros.entidades.Empresa;
import com.movilesseguros.entidades.Usuario;
import com.movilesseguros.entidades.Vehiculo;
import com.movilesseguros.negocio.ControlConfiguracion;

public class MovilesSegurosWS extends AbstractWebService {

	private static final String NAMESPACE = "http://movilesseguros.com/";
	private static final int TIME_OUT = 120 * 1000;

	// ## SOAP ACTIONS
	private static final String SA_GET_VEHICULO = NAMESPACE + "GetVehiculo";
	private static final String SA_GET_FOTO_VEHICULO = NAMESPACE
			+ "GetFotoVehiculo";
	private static final String SA_GET_FOTO_CONDUCTOR = NAMESPACE
			+ "GetFotoConductor";
	private static final String SA_GET_LOGO_EMPRESA = NAMESPACE
			+ "GetLogoEmpresa";
	private static final String SA_REGISTRAR_USUARIO = NAMESPACE
			+ "RegistrarUsuario";
	private static final String SA_GET_USUARIO = NAMESPACE + "GetUsuario";
	private static final String SA_VERIFICAR_ID_USUARIO = NAMESPACE
			+ "VerificarIdUsuario";
	private static final String SA_GET_EMPRESAS = NAMESPACE + "GetEmpresas";
	private static final String SA_GET_CANTIDAD_EMPRESAS = NAMESPACE
			+ "GetCantidadEmpresas";

	// ## METODOS
	private static final String M_GET_VEHICULO = "GetVehiculo";
	private static final String M_GET_FOTO_VEHICULO = "GetFotoVehiculo";
	private static final String M_GET_FOTO_CONDUCTOR = "GetFotoConductor";
	private static final String M_GET_LOGO_EMPRESA = "GetLogoEmpresa";
	private static final String M_REGISTRAR_USUARIO = "RegistrarUsuario";
	private static final String M_GET_USUARIO = "GetUsuario";
	private static final String M_VERIFICAR_ID_USUARIO = "VerificarIdUsuario";
	private static final String M_GET_EMPRESAS = "GetEmpresas";
	private static final String M_GET_CANTIDAD_EMPRESAS = "GetCantidadEmpresas";

	private ControlConfiguracion mControlConfiguracion;
	private Gson mGson;

	public MovilesSegurosWS(Context context) {
		mControlConfiguracion = new ControlConfiguracion(context);
		mGson = new Gson();
	}

	@Override
	protected String getNameSpace() {
		return NAMESPACE;
	}

	@Override
	protected int getTimeOut() {
		return TIME_OUT;
	}

	@Override
	protected List<String> getUrlList() {

		List<String> listUrls = new ArrayList<String>();
		listUrls.add(mControlConfiguracion.getUrl());

		return listUrls;
	}

	@Override
	protected boolean isDataCompressed() {
		return false;
	}

	/**
	 * Obtiene los datos de un vehiculo junto con la {@link Empresa} de radio
	 * movil a la que pertenece y su {@link Conductor}
	 * 
	 * @param placa
	 *            Placa del vehiculo a obtener
	 * @return Vehiculo o null si no existe
	 * @throws JsonSyntaxException
	 * @throws IOException
	 * @throws XmlPullParserException
	 * @throws NullPointerException
	 */
	public Vehiculo getVehiculo(String placa) throws JsonSyntaxException,
			IOException, XmlPullParserException, NullPointerException {

		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("placa", placa);

		Respuesta respuesta = consumir(SA_GET_VEHICULO, M_GET_VEHICULO,
				parametros);

		if (respuesta.getValue() != null) {
			Vehiculo vehiculo = mGson.fromJson(respuesta.getValue().toString(),
					Vehiculo.class);

			return vehiculo;
		}

		return null;
	}

	/**
	 * Obtiene la fotografia de un vehiculo
	 * 
	 * @param idVehiculo
	 *            Identificador del {@link Vehiculo}
	 * @param escala
	 *            Escala de la imagen
	 * 
	 * @return Imagen codificada a Base64 o null si el vehiculo no tiene
	 *         fotografia
	 * @throws JsonSyntaxException
	 * @throws IOException
	 * @throws XmlPullParserException
	 * @throws NullPointerException
	 */
	public String getFotoVehiculo(int idVehiculo, int escala)
			throws JsonSyntaxException, IOException, XmlPullParserException,
			NullPointerException {

		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idVehiculo", idVehiculo);
		parametros.put("escala", escala);

		Respuesta respuesta = consumir(SA_GET_FOTO_VEHICULO,
				M_GET_FOTO_VEHICULO, parametros);

		String foto = respuesta.getValue() != null ? respuesta.getValue()
				.toString() : null;

		return foto;
	}

	/**
	 * Obtiene la fotografia de un conductor
	 * 
	 * @param idConductor
	 *            Identificador del {@link Conductor}
	 * @param escala
	 *            Escala de la imagen
	 * @return Imagen codificada a Base64 o null si el conductor no tiene
	 *         fotografia
	 * @throws JsonSyntaxException
	 * @throws IOException
	 * @throws XmlPullParserException
	 * @throws NullPointerException
	 */
	public String getFotoConductor(int idConductor, int escala)
			throws JsonSyntaxException, IOException, XmlPullParserException,
			NullPointerException {

		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idConductor", idConductor);
		parametros.put("escala", escala);

		Respuesta respuesta = consumir(SA_GET_FOTO_CONDUCTOR,
				M_GET_FOTO_CONDUCTOR, parametros);

		String foto = respuesta.getValue() != null ? respuesta.getValue()
				.toString() : null;

		return foto;
	}

	/**
	 * Obtiene el logotipo de una empresa
	 * 
	 * @param idEmpresa
	 *            Identificador de la {@link Empresa}
	 * @param escala
	 *            Escala de la imagen
	 * @return Imagen codificada a Base64 o null si la empresa no tiene logotipo
	 * @throws JsonSyntaxException
	 * @throws IOException
	 * @throws XmlPullParserException
	 * @throws NullPointerException
	 */
	public String getLogoEmpresa(int idEmpresa, int escala)
			throws JsonSyntaxException, IOException, XmlPullParserException,
			NullPointerException {

		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idEmpresa", idEmpresa);
		parametros.put("escala", escala);

		Respuesta respuesta = consumir(SA_GET_LOGO_EMPRESA, M_GET_LOGO_EMPRESA,
				parametros);

		String foto = respuesta.getValue() != null ? respuesta.getValue()
				.toString() : null;

		return foto;
	}

	/**
	 * Registra a un usuario en el sistema
	 * 
	 * @param id
	 *            Identificador del {@link Usuario}
	 * @param pass
	 *            Password del {@link Usuario}
	 * @param email
	 *            Email del {@link Usuario}
	 * @param tipo
	 *            Tipo de Usuario {@link Usuario#PROPIO},
	 *            {@link Usuario#FACEBOOK}, {@link Usuario#GOOGLE}
	 * @return Identificador del usuario si se registro correctamente o null si
	 *         no
	 * @throws JsonSyntaxException
	 * @throws IOException
	 * @throws XmlPullParserException
	 * @throws NullPointerException
	 */
	public String registrarUsuario(String id, String pass, String email,
			int tipo) throws JsonSyntaxException, IOException,
			XmlPullParserException, NullPointerException {

		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("id", id);
		parametros.put("pass", pass);
		parametros.put("email", email);
		parametros.put("tipo", tipo);

		Respuesta respuesta = consumir(SA_REGISTRAR_USUARIO,
				M_REGISTRAR_USUARIO, parametros);

		String result = respuesta.getValue() != null ? respuesta.getValue()
				.toString() : null;

		return result;
	}

	/**
	 * Obtiene un usuario registrado en el sistema
	 * 
	 * @param id
	 *            Identificador del {@link Usuario}
	 * @param pass
	 *            Password del {@link Usuario}
	 * @param tipo
	 *            Tipo de Usuario {@link Usuario#PROPIO},
	 *            {@link Usuario#FACEBOOK}, {@link Usuario#GOOGLE}
	 * @return Usuario o null si no existe
	 * @throws JsonSyntaxException
	 * @throws IOException
	 * @throws XmlPullParserException
	 * @throws NullPointerException
	 */
	public Usuario getUsuario(String id, String pass, int tipo)
			throws JsonSyntaxException, IOException, XmlPullParserException,
			NullPointerException {

		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("id", id);
		parametros.put("pass", pass);
		parametros.put("tipo", tipo);

		Respuesta respuesta = consumir(SA_GET_USUARIO, M_GET_USUARIO,
				parametros);

		Usuario usuario = null;
		if (respuesta.getValue() != null) {
			usuario = mGson.fromJson(respuesta.getValue().toString(),
					Usuario.class);
		}

		return usuario;
	}

	/**
	 * Verifica si un usuario no se encuentra registrado en el sistema
	 * 
	 * @param id
	 *            Identificador del {@link Usuario}
	 * @return Identificador del usuario o null si no se encuentra registrado
	 * @throws JsonSyntaxException
	 * @throws IOException
	 * @throws XmlPullParserException
	 * @throws NullPointerException
	 */
	public String verificarIdUsuario(String id) throws JsonSyntaxException,
			IOException, XmlPullParserException, NullPointerException {

		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("id", id);

		Respuesta respuesta = consumir(SA_VERIFICAR_ID_USUARIO,
				M_VERIFICAR_ID_USUARIO, parametros);

		String result = respuesta.getValue() != null ? respuesta.getValue()
				.toString() : null;

		return result;
	}

	/**
	 * Obtiene las empresas registradas en el sistema que se encuentran activas
	 * 
	 * @param pagina
	 *            Numero de pagina
	 * @param tamanio
	 *            Cantidad de datos por pagina
	 * @return List<Empresa>
	 * @throws JsonSyntaxException
	 * @throws IOException
	 * @throws XmlPullParserException
	 * @throws NullPointerException
	 */
	public List<Empresa> getEmpresas(int pagina, int tamanio)
			throws JsonSyntaxException, IOException, XmlPullParserException,
			NullPointerException {

		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("pagina", pagina);
		parametros.put("tamanio", tamanio);

		Respuesta respuesta = consumir(SA_GET_EMPRESAS, M_GET_EMPRESAS,
				parametros);

		Type typeListEmpresa = new TypeToken<List<Empresa>>() {
		}.getType();

		List<Empresa> listEmpresas = mGson.fromJson(respuesta.getValue()
				.toString(), typeListEmpresa);

		return listEmpresas;
	}

	/**
	 * Obtiene la cantidad de empresas registradas en el servidor que se
	 * encuentran activas
	 * 
	 * @return Cantidad de empresas registradas
	 * @throws JsonSyntaxException
	 * @throws IOException
	 * @throws XmlPullParserException
	 * @throws NullPointerException
	 */
	public int getCantidadEmpresas() throws JsonSyntaxException, IOException,
			XmlPullParserException, NullPointerException {

		Respuesta respuesta = consumir(SA_GET_CANTIDAD_EMPRESAS,
				M_GET_CANTIDAD_EMPRESAS, null);

		double cantidad = (Double) respuesta.getValue();
		
		return (int) cantidad;
	}
}
