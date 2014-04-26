package com.movilesseguros.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.movilesseguros.entidades.ClaseVehiculo;
import com.movilesseguros.entidades.MarcaVehiculo;
import com.movilesseguros.entidades.TipoVehiculo;
import com.movilesseguros.entidades.Vehiculo;

public class DALVehiculo {

	private DBManager mDbManager;

	public DALVehiculo(Context context) {
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
			ContentValues values = new ContentValues();
			values.put("Id", vehiculo.getId());
			values.put("Modelo", vehiculo.getModelo());
			values.put("PlacaPta", vehiculo.getPlacaPta());
			values.put("PlacaAnt", vehiculo.getPlacaAnt());
			values.put("Observaciones", vehiculo.getObservaciones());
			values.put("Tipo", vehiculo.getTipoVehiculo().getDescripcion());
			values.put("Marca", vehiculo.getMarcaVehiculo().getDescripcion());
			values.put("Clase", vehiculo.getClaseVehiculo().getDescripcion());
			values.put("NroMovil", vehiculo.getNroMovil());
			values.put("IdEmpresa", vehiculo.getEmpresa().getId());
			values.put("IdConductor", vehiculo.getConductor().getId());

			return mDbManager.insertar("TMP_Vehiculo", values);
		} catch (SQLException e) {
			throw new SQLException(
					"DALVehiculo::adicionarVehiculo: Error al adicionar. "
							+ e.getMessage());
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

		Vehiculo vehiculo = null;
		Cursor cursor = null;

		try {
			String consulta = "SELECT Id,Modelo,PlacaPta,PlacaAnt,Observaciones,NroMovil,"
					+ "Tipo,Clase,Marca,Foto "
					+ "FROM TMP_Vehiculo "
					+ "WHERE PlacaPta = ?";
			String[] parametros = new String[] { placa };
			cursor = mDbManager.ejecutarConsulta(consulta, parametros);
			if (cursor.moveToNext()) {
				vehiculo = new Vehiculo();
				vehiculo.setId(cursor.getInt(0));
				vehiculo.setModelo(cursor.getInt(1));
				vehiculo.setPlacaPta(cursor.getString(2));
				vehiculo.setPlacaAnt(cursor.getString(3));
				vehiculo.setObservaciones(cursor.getString(4));
				vehiculo.setNroMovil(cursor.getInt(5));

				TipoVehiculo tipoVehiculo = new TipoVehiculo();
				tipoVehiculo.setDescripcion(cursor.getString(6));
				vehiculo.setTipoVehiculo(tipoVehiculo);

				ClaseVehiculo claseVehiculo = new ClaseVehiculo();
				claseVehiculo.setDescripcion(cursor.getString(7));
				vehiculo.setClaseVehiculo(claseVehiculo);

				MarcaVehiculo marcaVehiculo = new MarcaVehiculo();
				marcaVehiculo.setDescripcion(cursor.getString(8));
				vehiculo.setMarcaVehiculo(marcaVehiculo);

				vehiculo.setFoto(cursor.getString(9));

			}

			return vehiculo;
		} catch (SQLException e) {
			throw new SQLException(
					"DALVehiculo::getVehiculo: Error al obtener. "
							+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
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
			return mDbManager.eliminar("TMP_Vehiculo", "1", null);
		} catch (SQLException e) {
			throw new SQLException(
					"DALVehiculo::eliminarVehiculo: Error al eliminar. "
							+ e.getMessage());
		}
	}

	/**
	 * Actualiza los datos de un vehiculo en la base de datos
	 * 
	 * @param vehiculo
	 *            {@link Vehiculo} a adicionar
	 * @return 1 si se actualizo correctamente o 0 si no
	 * @throws SQLException
	 *             Si ocurre un error al actualizar
	 */
	public int actualizarVehiculo(Vehiculo vehiculo) throws SQLException {

		try {
			ContentValues values = new ContentValues();
			values.put("Modelo", vehiculo.getModelo());
			values.put("PlacaPta", vehiculo.getPlacaPta());
			values.put("PlacaAnt", vehiculo.getPlacaAnt());
			values.put("Observaciones", vehiculo.getObservaciones());
			values.put("Tipo", vehiculo.getTipoVehiculo().getDescripcion());
			values.put("Marca", vehiculo.getMarcaVehiculo().getDescripcion());
			values.put("Clase", vehiculo.getClaseVehiculo().getDescripcion());
			values.put("NroMovil", vehiculo.getNroMovil());
			String whereClause = "Id = ?";
			String[] whereArgs = new String[] { String
					.valueOf(vehiculo.getId()) };

			return mDbManager.modificar("TMP_Vehiculo", values, whereClause,
					whereArgs);
		} catch (SQLException e) {
			throw new SQLException(
					"DALVehiculo::actualizarVehiculo: Error al actualizar. "
							+ e.getMessage());
		}
	}

	/**
	 * Actualiza la foto de un vehiculo
	 * 
	 * @param idVehiculo
	 *            Identificador del {@link Vehiculo}
	 * @param foto
	 *            Nombre de la foto
	 * @return 1 si se actualizo correctamente o 0 si no
	 * @throws SQLException
	 *             Si ocurre un error al actualizar
	 */
	public int actualizarFoto(int idVehiculo, String foto) throws SQLException {

		try {
			ContentValues values = new ContentValues();
			values.put("Foto", foto);
			String whereClause = "Id = ?";
			String[] whereArgs = new String[] { String.valueOf(idVehiculo) };

			return mDbManager.modificar("TMP_Vehiculo", values, whereClause,
					whereArgs);
		} catch (SQLException e) {
			throw new SQLException(
					"DALVehiculo::actualizarFoto: Error al actualizar. "
							+ e.getMessage());
		}
	}

	/**
	 * Actualiza el thumbnail de un vehiculo
	 * 
	 * @param idVehiculo
	 *            Identificador del {@link Vehiculo}
	 * @param thumbnail
	 *            Nombre del thumbnail
	 * @return 1 si se actualizo correctamente o 0 si no
	 * @throws SQLException
	 *             Si ocurre un error al actualizar
	 */
	public int actualizarThumbnail(int idVehiculo, String thumbnail)
			throws SQLException {

		try {
			ContentValues values = new ContentValues();
			values.put("Thumbnail", thumbnail);
			String whereClause = "Id = ?";
			String[] whereArgs = new String[] { String.valueOf(idVehiculo) };

			return mDbManager.modificar("TMP_Vehiculo", values, whereClause,
					whereArgs);
		} catch (SQLException e) {
			throw new SQLException(
					"DALVehiculo::actualizarFoto: Error al actualizar. "
							+ e.getMessage());
		}
	}

	/**
	 * Verifica si existe un vehiculo en la base de datos
	 * 
	 * @param idVehiculo
	 *            Identificador del {@link Vehiculo}
	 * @return True si existe o False si no
	 * @throws SQLException
	 *             Si ocurre un error al verificar
	 */
	public boolean existeVehiculo(int idVehiculo) throws SQLException {

		boolean existe = false;
		Cursor cursor = null;

		try {
			String consulta = "SELECT Id " + "FROM TMP_Vehiculo "
					+ "WHERE Id = ?";
			String[] parametros = new String[] { String.valueOf(idVehiculo) };

			cursor = mDbManager.ejecutarConsulta(consulta, parametros);
			if (cursor.moveToNext()) {
				existe = true;
			}

			return existe;
		} catch (SQLException e) {
			throw new SQLException(
					"DALVehiculo::existeVehiculo: Error al verificar. "
							+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Obtiene la foto de un vehiculo
	 * 
	 * @param idVehiculo
	 *            Identificador del {@link Vehiculo}
	 * @return Foto del vehiculo o null si no tiene
	 * @throws SQLException
	 *             Si ocurre un error al obtener
	 */
	public String getFoto(int idVehiculo) throws SQLException {

		String foto = null;
		Cursor cursor = null;

		try {
			String consulta = "SELECT Foto " + "FROM TMP_Vehiculo "
					+ "WHERE Id = ?";
			String[] parametros = new String[] { String.valueOf(idVehiculo) };

			cursor = mDbManager.ejecutarConsulta(consulta, parametros);
			if (cursor.moveToNext()) {
				foto = cursor.getString(0);
			}

			return foto;
		} catch (SQLException e) {
			throw new SQLException("DALVehiculo::getFoto: Error al obtener. "
					+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Obtiene el thumbnail de un vehiculo
	 * 
	 * @param idVehiculo
	 *            Identificador del {@link Vehiculo}
	 * @return Thumbnail del vehiculo o null si no tiene
	 * @throws SQLException
	 *             Si ocurre un error al obtener
	 */
	public String getThumbnail(int idVehiculo) throws SQLException {

		String foto = null;
		Cursor cursor = null;

		try {
			String consulta = "SELECT Thumbnail " + "FROM TMP_Vehiculo "
					+ "WHERE Id = ?";
			String[] parametros = new String[] { String.valueOf(idVehiculo) };

			cursor = mDbManager.ejecutarConsulta(consulta, parametros);
			if (cursor.moveToNext()) {
				foto = cursor.getString(0);
			}

			return foto;
		} catch (SQLException e) {
			throw new SQLException("DALVehiculo::getFoto: Error al obtener. "
					+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
}
