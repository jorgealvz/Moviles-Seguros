package com.movilesseguros.negocio.loader;

import android.content.Context;
import android.database.SQLException;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.movilesseguros.entidades.Conductor;
import com.movilesseguros.negocio.ControlConductor;

public class ConductorLoader extends AsyncTaskLoader<Object> {

	private static final String TAG = ConductorLoader.class.getSimpleName();

	public static final int ID = 202;

	private ControlConductor mControlConductor;
	private String mPlaca;
	private Conductor mData;

	public ConductorLoader(Context context, String placa) {
		super(context);
		mControlConductor = new ControlConductor(context);
		mPlaca = placa;
	}

	@Override
	public Object loadInBackground() {

		try {
			mData = mControlConductor.getConductor(mPlaca);
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
