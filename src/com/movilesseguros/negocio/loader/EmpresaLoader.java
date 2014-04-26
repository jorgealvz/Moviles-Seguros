package com.movilesseguros.negocio.loader;

import android.content.Context;
import android.database.SQLException;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.movilesseguros.entidades.Empresa;
import com.movilesseguros.negocio.ControlEmpresa;

public class EmpresaLoader extends AsyncTaskLoader<Object> {

	private static final String TAG = EmpresaLoader.class.getSimpleName();

	public static final int ID = 201;

	private ControlEmpresa mControlEmpresa;
	private String mPlaca;
	private Empresa mData;

	public EmpresaLoader(Context context, String placa) {
		super(context);
		mControlEmpresa = new ControlEmpresa(context);
		mPlaca = placa;
	}

	@Override
	public Object loadInBackground() {

		try {
			mData = mControlEmpresa.getEmpresa(mPlaca);
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
