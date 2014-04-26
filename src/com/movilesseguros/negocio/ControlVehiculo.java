package com.movilesseguros.negocio;

import android.content.Context;
import android.database.SQLException;

import com.movilesseguros.dal.DALVehiculo;
import com.movilesseguros.dal.DBManager;
import com.movilesseguros.entidades.Vehiculo;

public class ControlVehiculo {

	private DALVehiculo mDalVehiculo;
	private DBManager mDbManager;

	public ControlVehiculo(Context context) {
		mDalVehiculo = new DALVehiculo(context);
		mDbManager = DBManager.getInstancia(context);
	}

	/**
	 * Adiciona un vehiculo a la base de datos
	 * 
	 * @param vehiculo
	 *            {@link Vehiculo} a adicionar
	 * @return rowId o -1 si ocurre un error
	 * @throws SQLException
	 *             Si ocurre un error al adicionar
	 */
	public long adicionarVehiculo(Vehiculo vehiculo) throws SQLException {

		try {
			mDbManager.beginTransaction();

			long rowId = 0;
			if (!mDalVehiculo.existeVehiculo(vehiculo.getId())) {
				rowId = mDalVehiculo.adicionarVehiculo(vehiculo);
				if (rowId > 0) {
					mDbManager.commit();
				}
			} else {
				int result = mDalVehiculo.actualizarVehiculo(vehiculo);
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
	 * Obtiene los datos de un vehiculo
	 * 
	 * @param placa
	 *            Placa del vehiculo
	 * @return {@link Vehiculo} o null si no existe
	 * @throws SQLException
	 *             Si ocurre un error al obtener
	 */
	public Vehiculo getVehiculo(String placa) throws SQLException {

		return mDalVehiculo.getVehiculo(placa);
	}

	/**
	 * Elimina los vehiculo de la base de datos
	 * 
	 * @return La cantidad de vehiculos eliminados
	 * @throws SQLException
	 *             Si ocurre un error al eliminar
	 */
	public int eliminarVehiculos() throws SQLException {

		try {
			mDbManager.beginTransaction();
			int result = mDalVehiculo.eliminarVehiculos();
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
	 * Actualiza la foto de un vehiculo
	 * 
	 * @param idVehiculo
	 *            Identificador del {@link Vehiculo}
	 * @param foto
	 *            Nombre de la foto
	 * @param thumbnail
	 *            True si es thumbnail o False si no
	 * @return 1 si se actualizo correctamente o 0 si no
	 * @throws SQLException
	 *             Si ocurre un error al actualizar
	 */
	public int actualizarFoto(int idVehiculo, String foto, boolean thumbnail)
			throws SQLException {

		try {
			mDbManager.beginTransaction();

			int result = 0;
			if (thumbnail) {
				result = mDalVehiculo.actualizarThumbnail(idVehiculo, foto);
			} else {
				result = mDalVehiculo.actualizarFoto(idVehiculo, foto);
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
	 * Obtiene la foto de un vehiculo
	 * 
	 * @param idVehiculo
	 *            Identificador del {@link Vehiculo}
	 * @param thumbnail
	 *            True si se obtendra el thumbnail del vehiculo o False si no
	 * @return Foto del vehiculo o null si no tiene
	 * @throws SQLException
	 *             Si ocurre un error al obtener
	 */
	public String getFoto(int idVehiculo, boolean thumbnail)
			throws SQLException {

		if (thumbnail) {
			return mDalVehiculo.getThumbnail(idVehiculo);
		}
		return mDalVehiculo.getFoto(idVehiculo);
	}
}
