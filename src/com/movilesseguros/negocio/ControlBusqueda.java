package com.movilesseguros.negocio;

import java.util.List;

import android.content.Context;
import android.database.SQLException;

import com.movilesseguros.dal.DALBusqueda;
import com.movilesseguros.dal.DBManager;
import com.movilesseguros.entidades.Busqueda;
import com.movilesseguros.entidades.Usuario;

public class ControlBusqueda {

	private DALBusqueda mDalBusqueda;
	private DBManager mDbManager;

	public ControlBusqueda(Context context) {
		mDalBusqueda = new DALBusqueda(context);
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
			mDbManager.beginTransaction();

			long rowId = 0;
			if (!mDalBusqueda.existeBusqueda(busqueda.getDescripcion())) {
				rowId = mDalBusqueda.adicionarBusqueda(busqueda, idUsuario);
				if (rowId > 0) {
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
	 * Obtiene las busquedas de un usuario
	 * 
	 * @param idUsuario
	 *            Identificador del {@link Usuario}
	 * @return List<Busqueda>
	 * @throws SQLException
	 *             Si ocurre un error al buscar
	 */
	public List<Busqueda> getBusquedas(String idUsuario) throws SQLException {

		return mDalBusqueda.getBusquedas(idUsuario);
	}
}
