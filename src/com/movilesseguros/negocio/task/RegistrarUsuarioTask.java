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

public class RegistrarUsuarioTask extends AbstractTask<Object[], Void, Integer> {

	private static final String TAG = RegistrarUsuarioTask.class
			.getSimpleName();

	public static final int ID = 101;

	private ControlUsuario mControlUsuario;
	private MovilesSegurosWS mConsultasWS;
	private TaskDialogFragment mTaskDialogFragment;
	private Context mContext;
	private ControlConfiguracion mControlConfiguracion;

	public RegistrarUsuarioTask(Context context) {
		mContext = context;
		mControlUsuario = new ControlUsuario(context);
		mConsultasWS = new MovilesSegurosWS(context);
		mControlConfiguracion = new ControlConfiguracion(context);
	}

	@Override
	protected Integer doInBackground(Object[]... params) {

		result = Constantes.TAREA_CORRECTA;

		mTaskDialogFragment = (TaskDialogFragment) getFragment();

		String id = (String) params[0][0];
		String pass = (String) params[0][1];
		String email = (String) params[0][2];
		int tipo = (Integer) params[0][3];

		try {
			// ## Codificamos el password del usuario
			String _pass = HashHelper.encriptar(pass);

			String idUsuario = mConsultasWS.registrarUsuario(id, _pass, email,
					tipo);
			if (idUsuario != null) {
				// ## Almacenamos el usuario en la base de datos
				Usuario usuario = new Usuario();
				usuario.setId(idUsuario);
				usuario.setPass(_pass);
				usuario.setTipo(tipo);

				mControlUsuario.adicionarUsuario(usuario);

				// ## Registramos al usuario en la sesion
				mControlConfiguracion.setUsuario(idUsuario);
			} else {
				message = mContext
						.getString(R.string.login_error_message_registro);
				result = Constantes.TAREA_INCORRECTA;
			}

		} catch (JsonSyntaxException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.login_error_message_registro),
					e);
			result = Constantes.TAREA_INCORRECTA;
		} catch (NullPointerException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.login_error_message_registro),
					e);
			result = Constantes.TAREA_INCORRECTA;
		} catch (IOException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.app_error_conexion_message), e);
			result = Constantes.ERROR_CONEXION;
		} catch (XmlPullParserException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.login_error_message_registro),
					e);
			result = Constantes.TAREA_INCORRECTA;
		} catch (SQLException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.login_error_message_registro),
					e);
			result = Constantes.TAREA_INCORRECTA;
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.login_error_message_registro),
					e);
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
