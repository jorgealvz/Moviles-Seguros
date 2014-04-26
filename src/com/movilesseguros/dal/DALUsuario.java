package com.movilesseguros.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.movilesseguros.entidades.Usuario;

public class DALUsuario {

	private DBManager mDbManager;

	public DALUsuario(Context context) {
		mDbManager = DBManager.getInstancia(context);
	}

	/**
	 * Adiciona un usuario a la base de datos
	 * 
	 * @param usuario
	 *            {@link Usuario} a adicionar
	 * @return rowId o -1 si ocurre un error
	 * @throws SQLException
	 *             Si ocurre un error al adicionar
	 */
	public long adicionarUsuario(Usuario usuario) throws SQLException {

		try {
			ContentValues values = new ContentValues();
			values.put("Id", usuario.getId());
			values.put("Pass", usuario.getPass());
			values.put("Tipo", usuario.getTipo());
			values.put("Estado", usuario.getEstado());

			return mDbManager.insertar("Usuario", values);
		} catch (SQLException e) {
			throw new SQLException(
					"DALUsuario::adicionarUsuario: Error al adicionar. "
							+ e.getMessage());
		}
	}

	/**
	 * Obtiene un usuario de la base de datos
	 * 
	 * @param id
	 *            Identificador del {@link Usuario}
	 * @return Usuario o null si no existe
	 * @throws SQLException
	 *             Si ocurre un error al obtener
	 */
	public Usuario getUsuario(String id) throws SQLException {

		Usuario usuario = null;
		Cursor cursor = null;

		try {
			String consulta = "SELECT Tipo " + "FROM Usuario " + "WHERE Id = ?";
			String[] parametros = new String[] { id };
			cursor = mDbManager.ejecutarConsulta(consulta, parametros);
			if (cursor.moveToNext()) {
				usuario = new Usuario();
				usuario.setId(id);
				usuario.setTipo(cursor.getInt(0));
			}

			return usuario;
		} catch (SQLException e) {
			throw new SQLException("DALUsuario::getUsuario: ERror al obtener. "
					+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Verifica si existe un usuario en la base de datos
	 * 
	 * @param idUsuario
	 *            Identificador del {@link Usuario}
	 * @return True si existe o False si no
	 * @throws SQLException
	 *             Si ocurre un error al verificar
	 */
	public boolean existeUsuario(String idUsuario) throws SQLException {

		boolean existe = false;
		Cursor cursor = null;

		try {
			String consulta = "SELECT Id " + "FROM Usuario " + "WHERE Id = ?";
			String[] parametros = new String[] { idUsuario };

			cursor = mDbManager.ejecutarConsulta(consulta, parametros);
			if (cursor.moveToNext()) {
				existe = true;
			}

			return existe;
		} catch (SQLException e) {
			throw new SQLException(
					"DALUsuario::existeUsuario: Error al verificar. "
							+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
}
