package com.movilesseguros.negocio.loader;

import android.content.Context;
import android.database.SQLException;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.movilesseguros.entidades.Vehiculo;
import com.movilesseguros.negocio.ControlVehiculo;

public class VehiculoLoader extends AsyncTaskLoader<Object> {

	private static final String TAG = VehiculoLoader.class.getSimpleName();

	public static final int ID = 203;

	private ControlVehiculo mControlVehiculo;
	private String mPlaca;
	private Vehiculo mData;

	public VehiculoLoader(Context context, String placa) {
		super(context);
		mControlVehiculo = new ControlVehiculo(context);
		mPlaca = placa;
	}

	@Override
	public Object loadInBackground() {

		try {
			mData = mControlVehiculo.getVehiculo(mPlaca);
		} catch (SQLException e) {
			Log.e(TAG, "", e);
		}

		return mData;
	}

	@Override
	protected void onStartLoading() {

		if (mData != null) {
			deliverResult(mData);
		} else {
			forceLoad();
		}

	}
}
