package com.movilesseguros.negocio.task;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.android.helper.fragment.AbstractTask;
import com.android.helper.fragment.TaskDialogFragment;
import com.android.helper.google.map.GMapDirection;
import com.android.helper.google.map.Route;
import com.google.android.gms.maps.model.LatLng;
import com.movilesseguros.R;
import com.movilesseguros.helper.Constantes;

public class RouteTask extends AbstractTask<Object[], Void, Integer> {

	public static final int ID = 2;
	private static final String TAG = RouteTask.class.getSimpleName();
	private TaskDialogFragment mTaskDialogFragment;
	private Route mRoute;
	private Context mContext;

	public RouteTask(Context context) {
		this.mContext = context;
	}

	@Override
	protected Integer doInBackground(Object[]... params) {

		result = Constantes.TAREA_CORRECTA;

		try {
			mTaskDialogFragment = (TaskDialogFragment) getFragment();

			LatLng src = (LatLng) params[0][0];
			LatLng dst = (LatLng) params[0][1];
			String mode = (String) params[0][2];

			final GMapDirection gMapDirection = new GMapDirection(mContext);

			gMapDirection.clearRouteData();

			mRoute = gMapDirection.getRoute(src.latitude, src.longitude,
					dst.latitude, dst.longitude, mode);

			gMapDirection.saveRouteData(mRoute);

		} catch (JSONException e) {
			Log.e(TAG, "", e);
			mContext.getString(R.string.mapa_error_message_ruta);
			result = Constantes.TAREA_INCORRECTA;
		} catch (MalformedURLException e) {
			Log.e(TAG, "", e);
			mContext.getString(R.string.mapa_error_message_ruta);
			result = Constantes.TAREA_INCORRECTA;
		} catch (IOException e) {
			Log.e(TAG, "", e);
			mContext.getString(R.string.app_error_conexion_message);
			result = Constantes.ERROR_CONEXION;
		}
		return result;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);

		if (result == Constantes.TAREA_CORRECTA) {
			mTaskDialogFragment.taskFinished(ID, message, mRoute);
		} else {
			mTaskDialogFragment.taskError(ID, message);
		}
	}

}
