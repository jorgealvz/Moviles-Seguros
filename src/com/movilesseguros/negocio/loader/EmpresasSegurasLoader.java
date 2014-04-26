package com.movilesseguros.negocio.loader;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.movilesseguros.entidades.Empresa;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.negocio.ControlEmpresa;
import com.movilesseguros.ws.MovilesSegurosWS;

public class EmpresasSegurasLoader extends AsyncTaskLoader<Object> {

	public static final String TAG = EmpresasSegurasLoader.class
			.getSimpleName();

	public static final int ID = 301;

	private MovilesSegurosWS movilesSegurosWS;
	private int mPagina;
	private Object[] mData;
	private ControlEmpresa mControlEmpresa;

	public EmpresasSegurasLoader(Context context, int pagina) {
		super(context);
		movilesSegurosWS = new MovilesSegurosWS(context);
		mPagina = pagina;
		mControlEmpresa = new ControlEmpresa(context);
	}

	@Override
	public Object loadInBackground() {

		try {
			List<Empresa> listEmpresas = movilesSegurosWS.getEmpresas(mPagina,
					Constantes.TAMANIO_PAGINACION);
			if (listEmpresas != null) {
				for (Empresa empresa : listEmpresas) {
					// ## Adicionamos la empresa a la temporal
					mControlEmpresa.adicionarEmpresa(empresa);
					empresa.setLogo(mControlEmpresa.getLogo(empresa.getId(),
							true));
				}
			}

			mData = new Object[] { Constantes.TAREA_CORRECTA, listEmpresas };
		} catch (JsonSyntaxException e) {
			Log.e(TAG, "", e);
			mData = new Object[] { Constantes.TAREA_INCORRECTA, null };
		} catch (NullPointerException e) {
			Log.e(TAG, "", e);
			mData = new Object[] { Constantes.TAREA_INCORRECTA, null };
		} catch (IOException e) {
			Log.e(TAG, "", e);
			mData = new Object[] { Constantes.ERROR_CONEXION, null };
		} catch (XmlPullParserException e) {
			Log.e(TAG, "", e);
			mData = new Object[] { Constantes.ERROR_CONEXION, null };
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
