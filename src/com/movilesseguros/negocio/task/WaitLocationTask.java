package com.movilesseguros.negocio.task;

import android.content.Context;
import android.util.Log;

import com.android.helper.fragment.AbstractTask;
import com.android.helper.fragment.TaskDialogFragment;
import com.android.helper.gps.GpsHelper;
import com.movilesseguros.R;
import com.movilesseguros.helper.Constantes;

public class WaitLocationTask extends AbstractTask<Void, Void, Integer> {

	public static final int ID = 0;
	private static final String TAG = WaitLocationTask.class.getSimpleName();

	private static final int ONE_SECOND = 1 * 1000;
	private static final int WAIT_TIME = 60 * 1000;
	private TaskDialogFragment mTaskDialogFragment;
	private Context mContext;

	public WaitLocationTask(Context context) {
		this.mContext = context;
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();

		GpsHelper.getInstancia()
				.start(mContext, GpsHelper.GPS_NETWORK_PROVIDER);
	}

	@Override
	protected Integer doInBackground(Void... params) {

		result = Constantes.TAREA_CORRECTA;

		try {
			mTaskDialogFragment = (TaskDialogFragment) getFragment();
			int count = 0;

			do {
				Thread.sleep(ONE_SECOND);
				count++;
			} while (GpsHelper.getInstancia().getLocation() == null
					&& (count <= WAIT_TIME));

			if (GpsHelper.getInstancia().getLocation() == null) {
				message = mContext
						.getString(R.string.mapa_message_ubicacion_no_disponible);
				result = Constantes.TAREA_INCORRECTA;
			}

		} catch (InterruptedException e) {
			Log.e(TAG, "", e);
		}

		return result;
	}

	@Override
	protected void onPostExecute(Integer result) {

		super.onPostExecute(result);

		GpsHelper.getInstancia().stop();

		if (result == Constantes.TAREA_CORRECTA) {
			mTaskDialogFragment.taskFinished(ID, message, null);
		} else {
			mTaskDialogFragment.taskError(ID, message);
		}
	}
}
