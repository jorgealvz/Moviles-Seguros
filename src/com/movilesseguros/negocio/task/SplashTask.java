package com.movilesseguros.negocio.task;

import android.content.Context;
import android.util.Log;

import com.android.helper.fragment.AbstractTask;
import com.android.helper.fragment.BackgroundTaskFragment;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.negocio.ControlConfiguracion;

public class SplashTask extends AbstractTask<Void, Void, Integer> {

	private static final int SPLASH_TIME = 3 * 1000;
	private static final String TAG = SplashTask.class.getSimpleName();

	public static final int ID = 1;

	private ControlConfiguracion mControlConfiguracion;
	private String mUsuario;
	private BackgroundTaskFragment mBackgroundTaskFragment;

	public SplashTask(Context context) {
		mControlConfiguracion = new ControlConfiguracion(context);
	}

	@Override
	protected Integer doInBackground(Void... params) {

		result = Constantes.TAREA_CORRECTA;

		try {

			mBackgroundTaskFragment = (BackgroundTaskFragment) getFragment();

			mUsuario = mControlConfiguracion.getUsuario();

			Thread.sleep(SPLASH_TIME);
		} catch (InterruptedException e) {
			Log.e(TAG, "", e);
			message = e.getMessage();
			result = Constantes.TAREA_INCORRECTA;
		}

		return result;
	}

	@Override
	protected void onPostExecute(Integer result) {

		super.onPostExecute(result);

		if (result == Constantes.TAREA_CORRECTA) {
			mBackgroundTaskFragment.taskFinished(ID, message, mUsuario);
		} else {
			mBackgroundTaskFragment.taskError(ID, message);
		}
	}
}
