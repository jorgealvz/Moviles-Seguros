package com.movilesseguros.helper;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;

public class ImageHelper {

	/**
	 * Obtiene el directorio temporal donde se almacenan las imagenes de los
	 * vehiculos, conductores y empresas
	 * 
	 * @param context
	 *            Contexto de la aplicacion
	 * @return <code>File</code> conteniendo la ruta del directorio de las
	 *         imagenes
	 */
	public static File getImageDirectory(Context context) {

		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "Android" + File.separator + "data"
				+ File.separator + context.getPackageName() + File.separator
				+ "Files" + File.separator + "Images");

		return file;
	}

	/**
	 * Obtiene el tamano de escalacion en pixeles para una imagen basandose en
	 * los dpi de la pantalla del dispositivo
	 * 
	 * @param context
	 *            Contexto de la aplicacion
	 * @return La escala en pixeles. Ya sea {@link Constantes#ESCALA_LDPI}
	 *         {@link Constantes#ESCALA_MDPI} {@link Constantes#ESCALA_HDPI}
	 *         {@link Constantes#ESCALA_XHDPI}
	 * @see DisplayMetrics#densityDpi
	 */
	public static int getImageScale(Context context) {

		// ## Obtenemos la densidad en dpi de la pantalla
		int density = context.getResources().getDisplayMetrics().densityDpi;

		switch (density) {
		case DisplayMetrics.DENSITY_LOW:

			return Constantes.ESCALA_LDPI;
		case DisplayMetrics.DENSITY_MEDIUM:

			return Constantes.ESCALA_MDPI;

		case DisplayMetrics.DENSITY_HIGH:

			return Constantes.ESCALA_HDPI;

		case DisplayMetrics.DENSITY_XHIGH:

			return Constantes.ESCALA_XHDPI;
		default:
			return Constantes.ESCALA_MDPI;
		}
	}
}
