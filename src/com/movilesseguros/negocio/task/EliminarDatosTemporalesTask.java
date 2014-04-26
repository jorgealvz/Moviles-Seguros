package com.movilesseguros.negocio.task;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.android.helper.DeviceHelper;
import com.movilesseguros.helper.ImageHelper;
import com.movilesseguros.negocio.ControlConductor;
import com.movilesseguros.negocio.ControlEmpresa;
import com.movilesseguros.negocio.ControlVehiculo;

public class EliminarDatosTemporalesTask extends AsyncTask<Void, Void, Void> {

	private static final String TAG = EliminarDatosTemporalesTask.class
			.getSimpleName();

	private ControlVehiculo mControlVehiculo;
	private ControlEmpresa mControlEmpresa;
	private ControlConductor mControlConductor;
	private Context mContext;

	public EliminarDatosTemporalesTask(Context context) {
		mContext = context;
		mControlVehiculo = new ControlVehiculo(context);
		mControlEmpresa = new ControlEmpresa(context);
		mControlConductor = new ControlConductor(context);
	}

	@Override
	protected Void doInBackground(Void... params) {

		try {
			mControlConductor.eliminarConductores();
			mControlVehiculo.eliminarVehiculos();
			mControlEmpresa.eliminarEmpresas();
			DeviceHelper.deleteDirectory(ImageHelper
					.getImageDirectory(mContext));
		} catch (SQLException e) {
			Log.e(TAG, "", e);
		} catch (NullPointerException e) {
			Log.e(TAG, "", e);
		}

		return null;
	}

}
