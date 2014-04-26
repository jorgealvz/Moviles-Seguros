package com.movilesseguros.negocio.loader;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.google.gson.JsonSyntaxException;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.ws.MovilesSegurosWS;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class CantidadEmpresasSegurasLoader extends AsyncTaskLoader<Object> {

	private static final String TAG = CantidadEmpresasSegurasLoader.class
			.getSimpleName();

	public static final int ID = 300;

	private MovilesSegurosWS movilesSegurosWS;
	private Object[] mData;

	public CantidadEmpresasSegurasLoader(Context context) {
		super(context);
		movilesSegurosWS = new MovilesSegurosWS(context);
	}

	@Override
	public Object loadInBackground() {

		try {
			int cantidad = movilesSegurosWS.getCantidadEmpresas();

			mData = new Object[] { Constantes.TAREA_CORRECTA, cantidad };
		} catch (JsonSyntaxException e) {
			Log.e(TAG, "", e);
			mData = new Object[] { Constantes.TAREA_INCORRECTA, 0 };
		} catch (NullPointerException e) {
			Log.e(TAG, "", e);
			mData = new Object[] { Constantes.TAREA_INCORRECTA, 0 };
		} catch (IOException e) {
			Log.e(TAG, "", e);
			mData = new Object[] { Constantes.ERROR_CONEXION, 0 };
		} catch (XmlPullParserException e) {
			Log.e(TAG, "", e);
			mData = new Object[] { Constantes.ERROR_CONEXION, 0 };
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
