package com.movilesseguros.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.movilesseguros.entidades.Empresa;

public class DALEmpresa {

	private DBManager mDbManager;

	public DALEmpresa(Context context) {
		mDbManager = DBManager.getInstancia(context);
	}

	/**
	 * Adiciona una empresa a la base de datos
	 * 
	 * @param empresa
	 *            {@link Empresa} a adicionar
	 * @return rowId o -1 si ocurre un error
	 * @throws SQLException
	 *             Si ocurre un error al adicionar
	 */
	public long adicionarEmpresa(Empresa empresa) throws SQLException {

		try {
			ContentValues values = new ContentValues();
			values.put("Id", empresa.getId());
			values.put("RazonSocial", empresa.getRazonSocial());
			values.put("Direccion", empresa.getDireccion());
			values.put("Telefono", empresa.getTelefono());
			values.put("Fax", empresa.getFax());
			values.put("Email", empresa.getEmail());
			values.put("PaginaWeb", empresa.getPaginaWeb());
			values.put("Latitud", empresa.getLatitud());
			values.put("Longitud", empresa.getLongitud());

			return mDbManager.insertar("TMP_Empresa", values);
		} catch (SQLException e) {
			throw new SQLException(
					"DALEmpresa::adicionarEmpresa: Error al adicionar. "
							+ e.getMessage());
		}
	}

	/**
	 * Obtiene los datos de una empresa
	 * 
	 * @param placa
	 *            Placa del radio movil al que pertenece la Empresa
	 * @return {@link Empresa} o null si no existe
	 * @throws SQLException
	 *             Si ocurre un error al obtener
	 */
	public Empresa getEmpresa(String placa) throws SQLException {

		Empresa empresa = null;
		Cursor cursor = null;

		try {
			String consulta = "SELECT E.Id,E.RazonSocial,E.Direccion,E.Telefono,E.Fax,"
					+ "E.Email,E.PaginaWeb,E.Latitud,E.Longitud,E.Logo "
					+ "FROM TMP_Empresa E INNER JOIN TMP_Vehiculo V ON "
					+ "E.Id = V.IdEmpresa " + "AND V.PlacaPta = ?";
			String[] parametros = new String[] { placa };
			cursor = mDbManager.ejecutarConsulta(consulta, parametros);
			if (cursor.moveToNext()) {
				empresa = new Empresa();
				empresa.setId(cursor.getInt(0));
				empresa.setRazonSocial(cursor.getString(1));
				empresa.setDireccion(cursor.getString(2));
				empresa.setTelefono(cursor.getString(3));
				empresa.setFax(cursor.getString(4));
				empresa.setEmail(cursor.getString(5));
				empresa.setPaginaWeb(cursor.getString(6));
				empresa.setLatitud(cursor.getDouble(7));
				empresa.setLongitud(cursor.getDouble(8));
				empresa.setLogo(cursor.getString(9));
			}

			return empresa;
		} catch (SQLException e) {
			throw new SQLException("DALEmpresa::getEmpresa:Error al obtener. "
					+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Elimina las empresas de la base de datos
	 * 
	 * @return La cantidad de registros eliminados
	 * @throws SQLException
	 *             Si ocurre un error al eliminar
	 */
	public int eliminarEmpresas() throws SQLException {

		try {
			return mDbManager.eliminar("TMP_Empresa", "1", null);
		} catch (SQLException e) {
			throw new SQLException(
					"DALEmpresa::eliminarEmpresa: Error al eliminar. "
							+ e.getMessage());
		}
	}

	/**
	 * Actualiza los datos de una empresa
	 * 
	 * @param empresa
	 *            {@link Empresa} a actualizar
	 * @return 1 si se actualizo correctamente o 0 si no
	 * @throws SQLException
	 *             Si ocurrio un error al actualizar
	 */
	public int actualizarEmpresa(Empresa empresa) throws SQLException {

		try {
			ContentValues values = new ContentValues();
			values.put("RazonSocial", empresa.getRazonSocial());
			values.put("Direccion", empresa.getDireccion());
			values.put("Telefono", empresa.getTelefono());
			values.put("Fax", empresa.getFax());
			values.put("Email", empresa.getEmail());
			values.put("PaginaWeb", empresa.getPaginaWeb());
			values.put("Latitud", empresa.getLatitud());
			values.put("Longitud", empresa.getLongitud());
			String whereClause = "Id = ?";
			String[] whereArgs = new String[] { String.valueOf(empresa.getId()) };
			return mDbManager.modificar("TMP_Empresa", values, whereClause,
					whereArgs);
		} catch (SQLException e) {
			throw new SQLException(
					"DALEmpresa::actualizarEmpresa: Error al actualizar. "
							+ e.getMessage());
		}
	}

	/**
	 * Actualiza el logo de una empresa
	 * 
	 * @param idEmpresa
	 *            Identificador de la {@link Empresa}
	 * @param logo
	 *            Nombre del logo
	 * @return 1 si se actualizo correctamente o 0 si no
	 * @throws SQLException
	 *             Si ocurre un error al actualizar
	 */
	public int actualizarLogo(int idEmpresa, String logo) throws SQLException {

		try {
			ContentValues values = new ContentValues();
			values.put("Logo", logo);
			String whereClause = "Id = ?";
			String[] whereArgs = new String[] { String.valueOf(idEmpresa) };

			return mDbManager.modificar("TMP_Empresa", values, whereClause,
					whereArgs);
		} catch (SQLException e) {
			throw new SQLException(
					"DALEmpresa::actualizarLogo: Error al actualizar. "
							+ e.getMessage());
		}
	}

	/**
	 * Actualiza el thumbnail de una empresa
	 * 
	 * @param idEmpresa
	 *            Identificador de la {@link Empresa}
	 * @param thumbnail
	 *            Nombre del thumbnail
	 * @return 1 si se actualizo correctamente o 0 si no
	 * @throws SQLException
	 *             Si ocurre un error al actualizar
	 */
	public int actualizarThumbnail(int idEmpresa, String thumbnail)
			throws SQLException {

		try {
			ContentValues values = new ContentValues();
			values.put("Thumbnail", thumbnail);
			String whereClause = "Id = ?";
			String[] whereArgs = new String[] { String.valueOf(idEmpresa) };

			return mDbManager.modificar("TMP_Empresa", values, whereClause,
					whereArgs);
		} catch (SQLException e) {
			throw new SQLException(
					"DALEmpresa::actualizarThumbnail: Error al actualizar. "
							+ e.getMessage());
		}
	}

	/**
	 * Verifica si existe una empresa en la base de datos
	 * 
	 * @param idEmpresa
	 *            Identificador de la {@link Empresa}
	 * @return True si existe o False si no
	 * @throws SQLException
	 *             Si ocurre un error al verificar
	 */
	public boolean existeEmpresa(int idEmpresa) throws SQLException {

		boolean existe = false;
		Cursor cursor = null;

		try {
			String consulta = "SELECT Id " + "FROM TMP_Empresa "
					+ "WHERE Id = ?";
			String[] parametros = new String[] { String.valueOf(idEmpresa) };
			cursor = mDbManager.ejecutarConsulta(consulta, parametros);
			if (cursor.moveToNext()) {
				existe = true;
			}

			return existe;
		} catch (SQLException e) {
			throw new SQLException(
					"DALEmpresa::existeEmpresa: Error al verificar"
							+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Obtiene el logo de una empresa
	 * 
	 * @param idEmpresa
	 *            Identificador de la {@link Empresa}
	 * @return Logo de la empresa o null si no tiene
	 * @throws SQLException
	 *             Si ocurre un error al obtener
	 */
	public String getLogo(int idEmpresa) throws SQLException {

		String logo = null;
		Cursor cursor = null;

		try {
			String consulta = "SELECT Logo " + "FROM TMP_Empresa "
					+ "WHERE Id = ?";
			String[] parametros = new String[] { String.valueOf(idEmpresa) };
			cursor = mDbManager.ejecutarConsulta(consulta, parametros);
			if (cursor.moveToNext()) {
				logo = cursor.getString(0);
			}

			return logo;
		} catch (SQLException e) {
			throw new SQLException("DALEmpresa::getLogo: Error al obtener. "
					+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Obtiene el thumbnail de una empresa
	 * 
	 * @param idEmpresa
	 *            Identificador de la {@link Empresa}
	 * @return Thumbnail de la empresa o null si no tiene
	 * @throws SQLException
	 *             Si ocurre un error al obtener
	 */
	public String getThumbnail(int idEmpresa) throws SQLException {

		String logo = null;
		Cursor cursor = null;

		try {
			String consulta = "SELECT Thumbnail " + "FROM TMP_Empresa "
					+ "WHERE Id = ?";
			String[] parametros = new String[] { String.valueOf(idEmpresa) };
			cursor = mDbManager.ejecutarConsulta(consulta, parametros);
			if (cursor.moveToNext()) {
				logo = cursor.getString(0);
			}

			return logo;
		} catch (SQLException e) {
			throw new SQLException("DALEmpresa::getLogo: Error al obtener. "
					+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
}
