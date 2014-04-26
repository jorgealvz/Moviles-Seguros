package com.movilesseguros.dal;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.movilesseguros.entidades.Busqueda;
import com.movilesseguros.entidades.Usuario;

public class DALBusqueda {

	private DBManager mDbManager;

	public DALBusqueda(Context context) {
		mDbManager = DBManager.getInstancia(context);
	}

	/**
	 * Adiciona una busqueda a la base de datos
	 * 
	 * @param busqueda
	 *            {@link Busqueda} a adicionar
	 * @param idUsuario
	 *            Identificador del {@link Usuario}
	 * @return rowId o -1 si ocurre un error
	 * @throws SQLException
	 *             Si ocurre un error al adicionar
	 */
	public long adicionarBusqueda(Busqueda busqueda, String idUsuario)
			throws SQLException {

		try {
			ContentValues values = new ContentValues();
			values.putNull("Id");
			values.put("Descripcion", busqueda.getDescripcion());
			values.put("IdUsuario", idUsuario);

			return mDbManager.insertar("Busqueda", values);
		} catch (SQLException e) {
			throw new SQLException(
					"DALBusqueda::adicionarBusqueda: Error al adicionar. "
							+ e.getMessage());
		}
	}

	/**
	 * Obtiene las busquedas de un usuario
	 * 
	 * @param idUsuario
	 *            Identificador del {@link Usuario}
	 * @return List<Busqueda>
	 * @throws SQLException
	 *             Si ocurre un error al buscar
	 */
	public List<Busqueda> getBusquedas(String idUsuario) throws SQLException {

		List<Busqueda> listBusquedas = new ArrayList<Busqueda>();
		Cursor cursor = null;

		try {
			String consulta = "SELECT Id,Descripcion " + "FROM Busqueda "
					+ "WHERE IdUsuario = ? " + "ORDER BY Id DESC";
			String[] parametros = new String[] { idUsuario };

			cursor = mDbManager.ejecutarConsulta(consulta, parametros);
			while (cursor.moveToNext()) {
				Busqueda busqueda = new Busqueda();
				busqueda.setId(cursor.getInt(0));
				busqueda.setDescripcion(cursor.getString(1));

				listBusquedas.add(busqueda);
			}

			return listBusquedas;
		} catch (SQLException e) {
			throw new SQLException(
					"DALUsuario::getBusquedas: Error al obtener. "
							+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Verifica si existe una busqueda en la base de datos
	 * 
	 * @param descripcion
	 *            Descripcion a buscar
	 * @return True si existe o False si no
	 * @throws SQLException
	 *             Si ocurre un error al verificar
	 */
	public boolean existeBusqueda(String descripcion) throws SQLException {

		boolean existe = false;
		Cursor cursor = null;

		try {
			String consulta = "SELECT Id " + "FROM Busqueda "
					+ "WHERE UPPER(Descripcion) = UPPER(?)";
			String[] parametros = new String[] { descripcion };

			cursor = mDbManager.ejecutarConsulta(consulta, parametros);
			if (cursor.moveToNext()) {
				existe = true;
			}

			return existe;
		} catch (SQLException e) {
			throw new SQLException(
					"DALBusqueda::existeBusqueda: Error al verificar. "
							+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
}
