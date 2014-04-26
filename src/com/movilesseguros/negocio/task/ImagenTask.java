package com.movilesseguros.negocio.task;

import greendroid.widget.AsyncImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.movilesseguros.R;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.helper.ImageHelper;
import com.movilesseguros.negocio.ControlConductor;
import com.movilesseguros.negocio.ControlEmpresa;
import com.movilesseguros.negocio.ControlVehiculo;
import com.movilesseguros.ws.MovilesSegurosWS;

public class ImagenTask extends AsyncTask<Object[], Void, Integer> {

	private static final String TAG = ImagenTask.class.getSimpleName();

	public static final int ID = 204;

	private MovilesSegurosWS mConsultasWS;
	private Context mContext;
	private AsyncImageView mAsyncImageView;
	private ControlEmpresa mControlEmpresa;
	private ControlVehiculo mControlVehiculo;
	private ControlConductor mControlConductor;
	private int result;
	private String message;
	private String mUrl;
	private String mName;
	private ImagenTaskListener mImagenListener;

	public ImagenTask(Context context, AsyncImageView asyncImageView,
			ImagenTaskListener imagenListener) {
		mContext = context;
		mConsultasWS = new MovilesSegurosWS(context);
		mAsyncImageView = asyncImageView;
		mControlConductor = new ControlConductor(context);
		mControlEmpresa = new ControlEmpresa(context);
		mControlVehiculo = new ControlVehiculo(context);
		mAsyncImageView = asyncImageView;
		mImagenListener = imagenListener;
	}

	@Override
	protected Integer doInBackground(Object[]... params) {

		result = Constantes.TAREA_CORRECTA;

		try {

			int id = (Integer) params[0][0];
			int tipo = (Integer) params[0][1];
			boolean thumbnail = (Boolean) params[0][2];

			String imagen = null;

			int escala = Constantes.ESCALA_FULL_SCREEN;
			if (thumbnail) {
				escala = ImageHelper.getImageScale(mContext);
			}

			Log.i(TAG, "Id: " + id);

			switch (tipo) {
			case Constantes.TIPO_EMPRESA:
				// ## Verificamos si no tenemos la imagen en la cache
				mName = mControlEmpresa.getLogo(id, thumbnail);
				if (TextUtils.isEmpty(mName)) {
					if (thumbnail) {
						mName = "EMP" + id;
					} else {
						mName = "_EMP" + id;
					}
					Log.i(TAG, "obteniendo logo...");
					imagen = mConsultasWS.getLogoEmpresa(id, escala);
				}
				break;
			case Constantes.TIPO_CONDUCTOR:
				// ## Verificamos si no tenemos la imagen en la cache
				mName = mControlConductor.getFoto(id, thumbnail);
				if (TextUtils.isEmpty(mName)) {
					if (thumbnail) {
						mName = "CON" + id;
					} else {
						mName = "_CON" + id;
					}

					imagen = mConsultasWS.getFotoConductor(id, escala);
				}
				break;
			case Constantes.TIPO_VEHICULO:
				// ## Verificamos si no tenemos la imagen en la cache
				mName = mControlVehiculo.getFoto(id, thumbnail);
				if (TextUtils.isEmpty(mName)) {
					if (thumbnail) {
						mName = "VEH" + id;
					} else {
						mName = "_VEH" + id;
					}

					imagen = mConsultasWS.getFotoVehiculo(id, escala);
				}
				break;

			default:
				break;
			}

			Log.i(TAG, "Nombre: " + mName);

			File mediaStorageDir = ImageHelper.getImageDirectory(mContext);

			mUrl = mediaStorageDir + File.separator + "img_" + mName + ".jpg";

			if (imagen != null) {
				// ## Convertimos a byte[] la imagen en Base64
				byte[] _imagen = Base64.decode(imagen, Base64.DEFAULT);

				if (!mediaStorageDir.exists()) {
					if (!mediaStorageDir.mkdirs()) {
						throw new NullPointerException(
								"No se puede crear el directorio.");
					}
				}

				FileOutputStream fos = new FileOutputStream(mUrl);
				fos.write(_imagen);
				fos.flush();
				fos.close();

				// ## Guardamos el nombre de la imagen en la base de datos segun
				// su tipo
				switch (tipo) {
				case Constantes.TIPO_EMPRESA:
					mControlEmpresa.actualizarLogo(id, mName, thumbnail);
					break;
				case Constantes.TIPO_CONDUCTOR:
					mControlConductor.actualizarFoto(id, mName, thumbnail);
					break;
				case Constantes.TIPO_VEHICULO:
					mControlVehiculo.actualizarFoto(id, mName, thumbnail);
					break;

				default:
					break;
				}

			}

		} catch (JsonSyntaxException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.busqueda_error_imagen_message),
					e);
			result = Constantes.TAREA_INCORRECTA;
		} catch (NullPointerException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.busqueda_error_imagen_message),
					e);
			result = Constantes.TAREA_INCORRECTA;
		} catch (IOException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.app_error_conexion_message), e);
			result = Constantes.TAREA_INCORRECTA;
		} catch (XmlPullParserException e) {
			Log.e(TAG, "", e);
			message = mConsultasWS.getErrorMessage(
					mContext.getString(R.string.busqueda_error_imagen_message),
					e);
			result = Constantes.TAREA_INCORRECTA;
		}

		return result;
	}

	@Override
	protected void onPostExecute(Integer result) {

		super.onPostExecute(result);

		if (result == Constantes.TAREA_CORRECTA) {
			mAsyncImageView.setUrl("file://" + mUrl);
			if (mImagenListener != null) {
				mImagenListener.onLoadFinished(mName);
			}
		} else {
			if (mImagenListener != null) {
				mImagenListener.onLoadError(message);
			} else {
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
			}
		}
	}

	public interface ImagenTaskListener {

		public void onLoadFinished(String name);

		public void onLoadError(String message);
	}
}
