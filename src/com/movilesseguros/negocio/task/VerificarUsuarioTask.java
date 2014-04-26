package com.movilesseguros.negocio.task;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;

import com.android.helper.fragment.AbstractTask;
import com.android.helper.fragment.TaskDialogFragment;
import com.google.gson.JsonSyntaxException;
import com.movilesseguros.R;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.ws.MovilesSegurosWS;

public class VerificarUsuarioTask extends AbstractTask<String, Void, Integer> {

	private static final String TAG = VerificarUsuarioTask.class
			.getSimpleName();

	public static final int ID = 103;

	private MovilesSegurosWS mConsultasWS;
	private Context mContext;
	private TaskDialogFragment mTaskDialogFragment;

	public VerificarUsuarioTask(Context context) {
		mContext = context;
		mConsultasWS = new MovilesSegurosWS(context);
	}

	@Override
	protected Integer doInBackground(String... params) {

		result = Constantes.TAREA_CORRECTA;

		mTaskDialogFragment = (TaskDialogFragment) getFragment();

		String id = params[0];

		try {
			String idUsuario = mConsultasWS.verificarIdUsuario(id);
			if (idUsuario != null) {
				// ## El usuario se encuentra registrado
				message = mContext
						.getString(R.string.login_message_usuario_registrado);
				result = Constantes.TAREA_INCORRECTA;
			}
		} catch (JsonSyntaxException e) {
			Log.e(TAG, "", e);
			message = mContext
					.getString(R.string.login_error_message_verificacion);
			result = Constantes.TAREA_INCORRECTA;
		} catch (NullPointerException e) {
			Log.e(TAG, "", e);
			message = mContext
					.getString(R.string.login_error_message_verificacion);
			result = Constantes.TAREA_INCORRECTA;
		} catch (IOException e) {
			Log.e(TAG, "", e);
			message = mContext.getString(R.string.app_error_conexion_message);
			result = Constantes.ERROR_CONEXION;
		} catch (XmlPullParserException e) {
			Log.e(TAG, "", e);
			message = mContext
					.getString(R.string.login_error_message_verificacion);
			result = Constantes.TAREA_INCORRECTA;
		}

		return result;
	}

	@Override
	protected void onPostExecute(Integer result) {

		super.onPostExecute(result);

		if (result == Constantes.TAREA_CORRECTA) {
			mTaskDialogFragment.taskFinished(ID, message, null);
		} else if (result == Constantes.ERROR_CONEXION) {
			mTaskDialogFragment.taskError(Constantes.ERROR_CONEXION, message);
		} else {
			mTaskDialogFragment.taskError(ID, message);
		}
	}
}
