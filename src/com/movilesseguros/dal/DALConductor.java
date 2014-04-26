package com.movilesseguros.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.movilesseguros.entidades.Conductor;

public class DALConductor {

	private DBManager mDbManager;

	public DALConductor(Context context) {
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
			ContentValues values = new ContentValues();
			values.put("Id", conductor.getId());
			values.put("Nombre", conductor.getNombre());
			values.put("Paterno", conductor.getPaterno());
			values.put("Materno", conductor.getMaterno());
			values.put("Nacionalidad", conductor.getNacionalidad());
			values.put("Edad", conductor.getEdad());
			values.put("Observacion", conductor.getObservacion());

			return mDbManager.insertar("TMP_Conductor", values);
		} catch (SQLException e) {
			throw new SQLException(
					"DALConductor::adicionarConductor: Error al adicionar. "
							+ e.getMessage());
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

		Conductor conductor = null;
		Cursor cursor = null;

		try {
			String consulta = "SELECT C.Id,C.Nombre,C.Paterno,C.Materno,C.Nacionalidad,"
					+ "C.Edad,C.Observacion,C.Foto "
					+ "FROM TMP_Conductor C INNER JOIN TMP_Vehiculo V ON "
					+ "C.Id = V.IdConductor " + "WHERE V.PlacaPta = ?";
			String[] parametros = new String[] { placa };
			cursor = mDbManager.ejecutarConsulta(consulta, parametros);
			if (cursor.moveToNext()) {
				conductor = new Conductor();
				conductor.setId(cursor.getInt(0));
				conductor.setNombre(cursor.getString(1));
				conductor.setPaterno(cursor.getString(2));
				conductor.setMaterno(cursor.getString(3));
				conductor.setNacionalidad(cursor.getString(4));
				conductor.setEdad(cursor.getInt(5));
				conductor.setObservacion(cursor.getString(6));
				conductor.setFoto(cursor.getString(7));
			}

			return conductor;
		} catch (SQLException e) {
			throw new SQLException(
					"DALConductor::getConductor: Error al obtener. "
							+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
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
			return mDbManager.eliminar("TMP_Conductor", "1", null);
		} catch (SQLException e) {
			throw new SQLException(
					"DALConductor::eliminarConductor: Error al eliminar. "
							+ e.getMessage());
		}
	}

	/**
	 * Actualiza un conductor en la base de datos
	 * 
	 * @param conductor
	 *            {@link Conductor} a actualizar
	 * @return 1 si se actuliza correctamente o 0 si no
	 * @throws SQLException
	 *             Si ocurre un error al actualizar
	 */
	public int actualizarConductor(Conductor conductor) throws SQLException {

		try {
			ContentValues values = new ContentValues();
			values.put("Nombre", conductor.getNombre());
			values.put("Paterno", conductor.getPaterno());
			values.put("Materno", conductor.getMaterno());
			values.put("Nacionalidad", conductor.getNacionalidad());
			values.put("Edad", conductor.getEdad());
			values.put("Observacion", conductor.getObservacion());
			String whereClause = "Id = ?";
			String[] whereArgs = new String[] { String.valueOf(conductor
					.getId()) };

			return mDbManager.modificar("TMP_Conductor", values, whereClause,
					whereArgs);
		} catch (SQLException e) {
			throw new SQLException(
					"DALConductor::actualizarConductor: Error al actualizar. "
							+ e.getMessage());
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
	public int actualizarFoto(int idConductor, String foto) throws SQLException {

		try {
			ContentValues values = new ContentValues();
			values.put("Foto", foto);
			String whereClause = "Id = ?";
			String[] whereArgs = new String[] { String.valueOf(idConductor) };

			return mDbManager.modificar("TMP_Conductor", values, whereClause,
					whereArgs);
		} catch (SQLException e) {
			throw new SQLException(
					"DALConductor::actualizarFoto: Error al actualizar. "
							+ e.getMessage());
		}
	}

	/**
	 * Actualiza la foto de un conductor
	 * 
	 * @param idConductor
	 *            Identificador del {@link Conductor}
	 * @param thumbnail
	 *            Nombre del thumbnail
	 * @return 1 si se actualizo correctamente o 0 si no
	 * @throws SQLException
	 *             Si ocurre un error al actualizar
	 */
	public int actualizarThumbnail(int idConductor, String thumbnail)
			throws SQLException {

		try {
			ContentValues values = new ContentValues();
			values.put("Foto", thumbnail);
			String whereClause = "Id = ?";
			String[] whereArgs = new String[] { String.valueOf(idConductor) };

			return mDbManager.modificar("TMP_Conductor", values, whereClause,
					whereArgs);
		} catch (SQLException e) {
			throw new SQLException(
					"DALConductor::actualizarFoto: Error al actualizar. "
							+ e.getMessage());
		}
	}

	/**
	 * Verifica si existe el conductor en la base de datos
	 * 
	 * @param idConductor
	 *            Identificador del {@link Conductor}
	 * @return True si existe o False si no
	 * @throws SQLException
	 *             Si ocurre un error al verificar
	 */
	public boolean existeConductor(int idConductor) throws SQLException {

		boolean existe = false;
		Cursor cursor = null;
		try {
			String consulta = "SELECT Id " + "FROM TMP_Conductor "
					+ "WHERE Id = ?";
			String[] parametros = new String[] { String.valueOf(idConductor) };
			cursor = mDbManager.ejecutarConsulta(consulta, parametros);
			if (cursor.moveToNext()) {
				existe = true;
			}
			return existe;

		} catch (SQLException e) {
			throw new SQLException(
					"DALConductor::existeConductor: Error al verificar. "
							+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Obtiene la foto de un conductor
	 * 
	 * @param idConductor
	 *            Identificador del {@link Conductor}
	 * @return Foto del conductor o null si no existe
	 * @throws SQLException
	 *             Si ocurre un error al obtener
	 */
	public String getFoto(int idConductor) throws SQLException {

		String foto = null;
		Cursor cursor = null;

		try {
			String consulta = "SELECT Foto " + "FROM TMP_Conductor "
					+ "WHERE Id = ?";
			String[] parametros = new String[] { String.valueOf(idConductor) };
			cursor = mDbManager.ejecutarConsulta(consulta, parametros);

			if (cursor.moveToNext()) {
				foto = cursor.getString(0);
			}

			return foto;
		} catch (SQLException e) {
			throw new SQLException("DALConductor::getFoto: Error al obtener. "
					+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Obtiene el thumbnail de un conductor
	 * 
	 * @param idConductor
	 *            Identificador del {@link Conductor}
	 * @return Thumbnail del conductor o null si no existe
	 * @throws SQLException
	 *             Si ocurre un error al obtener
	 */
	public String getThumbnail(int idConductor) throws SQLException {

		String foto = null;
		Cursor cursor = null;

		try {
			String consulta = "SELECT Thumbnail " + "FROM TMP_Conductor "
					+ "WHERE Id = ?";
			String[] parametros = new String[] { String.valueOf(idConductor) };
			cursor = mDbManager.ejecutarConsulta(consulta, parametros);

			if (cursor.moveToNext()) {
				foto = cursor.getString(0);
			}

			return foto;
		} catch (SQLException e) {
			throw new SQLException("DALConductor::getFoto: Error al obtener. "
					+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

}
