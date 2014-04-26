package com.movilesseguros.negocio;

import android.content.Context;
import android.database.SQLException;

import com.movilesseguros.dal.DALUsuario;
import com.movilesseguros.dal.DBManager;
import com.movilesseguros.entidades.Usuario;

public class ControlUsuario {

	private DBManager mDbManager;
	private DALUsuario mDalUsuario;

	public ControlUsuario(Context context) {
		mDbManager = DBManager.getInstancia(context);
		mDalUsuario = new DALUsuario(context);
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
			mDbManager.beginTransaction();
			long rowId = 0;
			if (!mDalUsuario.existeUsuario(usuario.getId())) {
				rowId = mDalUsuario.adicionarUsuario(usuario);
				if (rowId > 0) {
					mDbManager.commit();
				}
			} else {
				rowId = 1;
			}

			return rowId;
		} catch (SQLException e) {
			throw e;
		} finally {
			mDbManager.endTransaccion();
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

		return mDalUsuario.getUsuario(id);
	}
}
