package com.movilesseguros.helper;

public class Constantes {

	/**
	 * Indica que una tarea finalizo correctamente
	 */
	public static final int TAREA_CORRECTA = 1;

	/**
	 * Indica que una tarea finalizo con errores
	 */
	public static final int TAREA_INCORRECTA = 0;

	/**
	 * Indica un error de conexion con el servidor
	 */
	public static final int ERROR_CONEXION = -1;

	/**
	 * Indica que el tipo de informacion a consultar se refiere a la empresa
	 */
	public static final int TIPO_EMPRESA = 1;
	/**
	 * Indica que el tipo de informacion a consultar se refiere al conductor
	 */
	public static final int TIPO_CONDUCTOR = 2;
	/**
	 * Indica que el tipo de informacion a consultar se refiere al vehiculo
	 */
	public static final int TIPO_VEHICULO = 3;
	/**
	 * Indica el tamanio de la escala de una imagen para una pantalla ldpi
	 */
	public static final int ESCALA_LDPI = 96;
	/**
	 * Indica el tamanio de la escala de una imagen para una pantalla mdpi
	 */
	public static final int ESCALA_MDPI = 128;
	/**
	 * Indica el tamanio de la escala de una imagen para una pantalla hdpi
	 */
	public static final int ESCALA_HDPI = 196;
	/**
	 * Indica el tamanio de la escala de una imagen para una pantalla xhdpi
	 */
	public static final int ESCALA_XHDPI = 256;
	/**
	 * Indica el tamanio de la escala de la imagen cuando se esta a pantalla
	 * completa
	 */
	public static final int ESCALA_FULL_SCREEN = 512;

	/**
	 * Indica la cantidad de registros por pagina
	 */
	public static final int TAMANIO_PAGINACION = 30;
	
	/**
	 * Request code google plus login
	 */
	public static final int REQUEST_CODE_GPLUS_SIGN_IN = 100;
	
	/**
	 * Costo de la tarifa basica = 10 Bs
	 */
	public static final double TARIFA_BASICA = 10;
	
	/**
	 * Costo de la tarifa por cada kilometro = 3 Bs
	 */
	public static final double TARIFA_X_KILOMETRO = 3;
	
	/**
	 * Distancia minima para tarifa basica
	 */
	public static final double KILOMETROS_MINIMOS = 3;
}
