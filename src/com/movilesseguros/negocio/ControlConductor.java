package com.movilesseguros.negocio;

import android.content.Context;
import android.database.SQLException;

import com.movilesseguros.dal.DALConductor;
import com.movilesseguros.dal.DBManager;
import com.movilesseguros.entidades.Conductor;

public class ControlConductor {

	private DALConductor mDalConductor;
	private DBManager mDbManager;

	public ControlConductor(Context context) {
		mDalConductor = new DALConductor(context);
		mDbManager = DBManager.getInstancia(context);
	}

	/**
	 * Adiciona un conductor a la base de datos
	 * 
	 * @param conductor
	 *            {@link Conductor} a adicionar
	 * @return rowId o -1 si ocurre un error
	 * @throws SQLException
	 *             Si ocurre un error al adicionar
	 */
	public long adicionarConductor(Conductor conductor) throws SQLException {

		try {
			mDbManager.beginTransaction();

			long rowId = 0;
			if (!mDalConductor.existeConductor(conductor.getId())) {
				rowId = mDalConductor.adicionarConductor(conductor);
				if (rowId > 0) {
					mDbManager.commit();
				}
			} else {
				int result = mDalConductor.actualizarConductor(conductor);
				if (result > 0) {
					mDbManager.commit();
				}
			}

			return rowId;
		} catch (SQLException e) {
			throw e;
		} finally {
			mDbManager.endTransaccion();
		}
	}

	/**
	 * Obtiene los datos de un conductor
	 * 
	 * @param placa
	 *            Placa del radio movil asociado al conductor
	 * @return {@link Conductor} o null si no existe
	 * @throws SQLException
	 *             Si ocurre un error al obtener
	 */
	public Conductor getConductor(String placa) throws SQLException {

		return mDalConductor.getConductor(placa);
	}

	/**
	 * Elimina los conductores de la base de datos
	 * 
	 * @return La cantidad de registros eliminados
	 * @throws SQLException
	 *             Si ocurre un error al eliminar
	 */
	public int eliminarConductores() throws SQLException {

		try {
			mDbManager.beginTransaction();

			int result = mDalConductor.eliminarConductores();
			if (result > 0) {
				mDbManager.commit();
			}

			return result;
		} catch (SQLException e) {
			throw e;
		} finally {
			mDbManager.endTransaccion();
		}
	}

	/**
	 * Actualiza la foto de un conductor
	 * 
	 * @param idConductor
	 *            Identificador del {@link Conductor}
	 * @param foto
	 *            Nombre de la foto
	 * @return 1 si se actualizo correctamente o 0 si no
	 * @throws SQLException
	 *             Si ocurre un error al actualizar
	 */
	public int actualizarFoto(int idConductor, String foto, boolean thumbnail)
			throws SQLException {

		try {
			mDbManager.beginTransaction();

			int result = 0;
			if (thumbnail) {
				result = mDalConductor.actualizarThumbnail(idConductor, foto);
			} else {
				result = mDalConductor.actualizarFoto(idConductor, foto);
			}
			if (result > 0) {
				mDbManager.commit();
			}

			return result;
		} catch (SQLException e) {
			throw e;
		} finally {
			mDbManager.endTransaccion();
		}
	}

	/**
	 * Obtiene la foto de un conductor
	 * 
	 * @param idConductor
	 *            Identificador del {@link Conductor}
	 * @param thumbnail
	 *            True si se obtendra el thumbnail del conductor o False si no
	 * @return Foto del conductor o null si no existe
	 * @throws SQLException
	 *             Si ocurre un error al obtener
	 */
	public String getFoto(int idConductor, boolean thumbnail)
			throws SQLException {

		if (thumbnail) {
			return mDalConductor.getThumbnail(idConductor);
		}
		return mDalConductor.getFoto(idConductor);
	}
}
