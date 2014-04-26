package com.movilesseguros.negocio.task;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.android.helper.fragment.AbstractTask;
import com.android.helper.fragment.TaskDialogFragment;
import com.google.gson.JsonSyntaxException;
import com.movilesseguros.R;
import com.movilesseguros.entidades.Busqueda;
import com.movilesseguros.entidades.Conductor;
import com.movilesseguros.entidades.Empresa;
import com.movilesseguros.entidades.Vehiculo;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.negocio.ControlBusqueda;
import com.movilesseguros.negocio.ControlConductor;
import com.movilesseguros.negocio.ControlConfiguracion;
import com.movilesseguros.negocio.ControlEmpresa;
import com.movilesseguros.negocio.ControlVehiculo;
import com.movilesseguros.ws.MovilesSegurosWS;

public class BusquedaTask extends AbstractTask<String, Void, Integer> {

	private static final String TAG = BusquedaTask.class.getSimpleName();

	public static final int ID = 200;

	private ControlEmpresa mControlEmpresa;
	private ControlConductor mControlConductor;
	private ControlVehiculo mControlVehiculo;
	private MovilesSegurosWS mConsultasWS;
	private TaskDialogFragment mTaskDialogFragment;
	private Context mContext;
	private Vehiculo mVehiculo;
	private ControlBusqueda mControlBusqueda;
	private ControlConfiguracion mControlConfiguracion;
	private String mPlaca;

	public BusquedaTask(Context context) {
		mContext = context;
		mControlEmpresa = new ControlEmpresa(context);
		mControlConductor = new ControlConductor(context);
		mControlVehiculo = new ControlVehiculo(context);
		mConsultasWS = new MovilesSegurosWS(context);
		mControlBusqueda = new ControlBusqueda(context);
		mControlConfiguracion = new ControlConfiguracion(context);
	}

	@Override
	protected Integer doInBackground(String... params) {

		result = Constantes.TAREA_CORRECTA;

		try {
			mTaskDialogFragment = (TaskDialogFragment) getFragment();

			mPlaca = params[0];

			// ## Realizamos la busqueda en el servidor
			mVehiculo = mConsultasWS.getVehiculo(mPlaca);
			if (mVehiculo != null) {
				// ## Adicionamos los datos de busqueda en las tablas temporales
				// ## Adicionamos la empresa
				Empresa empresa = mVehiculo.getEmpresa();
				mControlEmpresa.adicionarEmpresa(empresa);
				// ## Adicionamos el conductor
				Conductor conductor = mVehiculo.getConductor();
				mControlConductor.adicionarConductor(conductor);
				// ## Adicionamos el vehiculo
				mControlVehiculo.adicionarVehiculo(mVehiculo);

				// ## Adicionamos la busqueda a la base de datos
				Busqueda busqueda = new Busqueda();
				busqueda.setDescripcion(mPlaca);

				String idUsuario = mControlConfiguracion.getUsuario();

				mControlBusqueda.adicionarBusqueda(busqueda, idUsuario);

			}

		} catch (JsonSyntaxException e) {
			Log.e(TAG, "", e);
			message = mContext.getString(R.string.busqueda_error_message);
			result = Constantes.TAREA_INCORRECTA;
		} catch (NullPointerException e) {
			Log.e(TAG, "", e);
			message = mContext.getString(R.string.busqueda_error_message);
			result = Constantes.TAREA_INCORRECTA;
		} catch (IOException e) {
			Log.e(TAG, "", e);
			message = mContext.getString(R.string.app_error_conexion_message);
			result = Constantes.ERROR_CONEXION;
		} catch (XmlPullParserException e) {
			Log.e(TAG, "", e);
			message = mContext.getString(R.string.busqueda_error_message);
			result = Constantes.TAREA_INCORRECTA;
		} catch (SQLException e) {
			Log.e(TAG, "", e);
			message = mContext.getString(R.string.busqueda_error_message);
			result = Constantes.TAREA_INCORRECTA;
		}

		return result;
	}

	@Override
	protected void onPostExecute(Integer result) {

		super.onPostExecute(result);

		if (result == Constantes.TAREA_CORRECTA) {
			mTaskDialogFragment.taskFinished(ID, message, new Object[] {
					mVehiculo != null, mPlaca });
		} else {
			mTaskDialogFragment.taskError(ID, message);
		}
	}

}
