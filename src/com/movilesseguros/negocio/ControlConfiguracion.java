package com.movilesseguros.negocio;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.movilesseguros.R;
import com.movilesseguros.entidades.Usuario;

public class ControlConfiguracion {

	private static final String KEY_ID_USUARIO = "usuario";
	private static final String KEY_TIPO_USUARIO = "tipo_usuario";
	private static final String KEY_HELP_RESULTADO_BUSQUEDA = "help_resultado_busqueda";
	private static final String KEY_HELP_MAPA = "help_mapa";

	private SharedPreferences mPreferences;
	private Context mContext;

	public ControlConfiguracion(Context context) {
		mContext = context;
		mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * Obtiene la url del webservice
	 * 
	 * @return URL del servicioweb
	 */
	public String getUrl() {

		return mPreferences.getString(mContext
				.getString(R.string.config_url_web_service_key), mContext
				.getString(R.string.config_url_web_service_default_value));
	}

	/**
	 * Establece el usuario q ha iniciado sesion
	 * 
	 * @param id
	 */
	public void setUsuario(String id) {

		mPreferences.edit().putString(KEY_ID_USUARIO, id).commit();
	}

	/**
	 * Obtiene el usuario q ha iniciado sesion
	 * 
	 * @return Identificador del usuario que ha iniciado sesion o null si no hay
	 *         una sesion activa
	 */
	public String getUsuario() {

		return mPreferences.getString(KEY_ID_USUARIO, null);
	}

	/**
	 * Estable el tipo de usuario que inicio sesion en la aplicacion
	 * 
	 * @param tipo
	 *            Tipo de usuario {@link Usuario#PROPIO},
	 *            {@link Usuario#FACEBOOK}, {@link Usuario#GOOGLE}
	 */
	public void setTipoUsuario(int tipo) {

		mPreferences.edit().putInt(KEY_TIPO_USUARIO, tipo).commit();
	}

	/**
	 * Retorna el tipo de usuario que inicio sesion en la aplicacion
	 * 
	 * @return Tipo de usuario
	 */
	public int getTipoUsuario() {

		return mPreferences.getInt(KEY_TIPO_USUARIO, Usuario.PROPIO);
	}

	/**
	 * Elimina todos los datos de la sesion
	 */
	public void clearPreferences() {

		mPreferences.edit().clear().commit();
	}

	/**
	 * Marca como mostrada la ayuda del resultado de la busqueda
	 */
	public void setShowHelpResultadoBusqueda() {

		mPreferences.edit().putBoolean(KEY_HELP_RESULTADO_BUSQUEDA, true)
				.commit();
	}

	/**
	 * Indica si la ayuda del resultado de la busqueda ha sido mostrada o no
	 * 
	 * @return True si ha sido mostrada o False si no
	 */
	public boolean isShowHelpResultadoBusqueda() {

		return mPreferences.getBoolean(KEY_HELP_RESULTADO_BUSQUEDA, false);
	}

	/**
	 * Marca como mostrada la ayuda del mapa
	 */
	public void setShowHelpMapa() {

		mPreferences.edit().putBoolean(KEY_HELP_MAPA, true).commit();
	}
	
	/**
	 * Indica si la ayuda del mapa ha sido mostrada o no
	 * 
	 * @return True si ha sido mostrada o False si no
	 */
	public boolean isShowHelpMapa() {

		return mPreferences.getBoolean(KEY_HELP_MAPA, false);
	}
}
