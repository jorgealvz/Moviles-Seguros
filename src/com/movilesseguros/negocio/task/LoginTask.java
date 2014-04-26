package com.movilesseguros.negocio.task;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.android.helper.HashHelper;
import com.android.helper.fragment.AbstractTask;
import com.android.helper.fragment.TaskDialogFragment;
import com.google.gson.JsonSyntaxException;
import com.movilesseguros.R;
import com.movilesseguros.entidades.Usuario;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.negocio.ControlConfiguracion;
import com.movilesseguros.negocio.ControlUsuario;
import com.movilesseguros.ws.MovilesSegurosWS;

public class LoginTask extends AbstractTask<Object[], Void, Integer> {

	private static final String TAG = LoginTask.class.getSimpleName();

	public static final int ID = 100;

	private ControlUsuario mControlUsuario;
	private MovilesSegurosWS mConsultasWS;
	private TaskDialogFragment mTaskDialogFragment;
	private ControlConfiguracion mControlConfiguracion;
	private Context mContext;

	public LoginTask(Context context) {
		mContext = context;
		mControlUsuario = new ControlUsuario(context);
		mConsultasWS = new MovilesSegurosWS(context);
		mControlConfiguracion = new ControlConfiguracion(context);
	}

	@Override
	protected Integer doInBackground(Object[]... params) {

		result = Constantes.TAREA_CORRECTA;

		try {
			mTaskDialogFragment = (TaskDialogFragment) getFragment();

			String id = (String) params[0][0];
			String pass = (String) params[0][1];
			int tipo = (Integer) params[0][2];

			// ## Codificamos el password del usuario
			String _pass = HashHelper.encriptar(pass);

			Usuario usuario = mConsultasWS.getUsuario(id, _pass, tipo);

			if (!isCancelled()) {
				if (usuario != null) {
					// ## Adicionamos el usuario a la base de datos
					usuario.setPass(_pass);
					usuario.setTipo(tipo);
					mControlUsuario.adicionarUsuario(usuario);

					// ## Almacenamos el usuario que inicio sesion
					mControlConfiguracion.setUsuario(id);
					// ## Almacenamos el tipo de usuario
					mControlConfiguracion.setTipoUsuario(tipo);
				} else {
					message = mContext
							.getString(R.string.login_error_message_login_incorrecto);
					result = Constantes.TAREA_INCORRECTA;
				}
			}
		} catch (JsonSyntaxException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.login_error_message_login), e);
			result = Constantes.TAREA_INCORRECTA;
		} catch (NullPointerException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.login_error_message_login), e);
			result = Constantes.TAREA_INCORRECTA;
		} catch (IOException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.app_error_conexion_message), e);
			result = Constantes.ERROR_CONEXION;
		} catch (XmlPullParserException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.login_error_message_login), e);
			result = Constantes.TAREA_INCORRECTA;
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.login_error_message_login), e);
			result = Constantes.TAREA_INCORRECTA;
		} catch (SQLException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.login_error_message_login), e);
			result = Constantes.TAREA_INCORRECTA;
		}

		return result;
	}

	@Override
	protected void onPostExecute(Integer result) {

		super.onPostExecute(result);

		if (result == Constantes.TAREA_CORRECTA) {
			mTaskDialogFragment.taskFinished(ID, message, null);
		} else {
			mTaskDialogFragment.taskError(ID, message);
		}
	}

}
