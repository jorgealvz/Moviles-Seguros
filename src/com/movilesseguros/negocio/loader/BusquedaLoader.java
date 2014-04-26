package com.movilesseguros.negocio.loader;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.movilesseguros.entidades.Busqueda;
import com.movilesseguros.negocio.ControlBusqueda;
import com.movilesseguros.negocio.ControlConfiguracion;

public class BusquedaLoader extends AsyncTaskLoader<Object> {

	private static final String TAG = BusquedaLoader.class.getSimpleName();

	public static final int ID = 100;

	private ControlBusqueda mControlBusqueda;
	private ControlConfiguracion mControlConfiguracion;
	private List<Busqueda> mData;

	public BusquedaLoader(Context context) {
		super(context);
		mControlBusqueda = new ControlBusqueda(context);
		mControlConfiguracion = new ControlConfiguracion(context);
	}

	@Override
	public Object loadInBackground() {

		try {
			String idUsuario = mControlConfiguracion.getUsuario();

			mData = mControlBusqueda.getBusquedas(idUsuario);
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
