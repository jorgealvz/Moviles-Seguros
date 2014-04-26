package com.movilesseguros.negocio;

import android.content.Context;
import android.database.SQLException;

import com.movilesseguros.dal.DALEmpresa;
import com.movilesseguros.dal.DBManager;
import com.movilesseguros.entidades.Empresa;

public class ControlEmpresa {

	private DALEmpresa mDalEmpresa;
	private DBManager mDbManager;

	public ControlEmpresa(Context context) {
		mDalEmpresa = new DALEmpresa(context);
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
			mDbManager.beginTransaction();

			long rowId = 0;
			if (!mDalEmpresa.existeEmpresa(empresa.getId())) {
				rowId = mDalEmpresa.adicionarEmpresa(empresa);
				if (rowId > 0) {
					mDbManager.commit();
				}
			} else {
				int result = mDalEmpresa.actualizarEmpresa(empresa);
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
	 * Obtiene los datos de una empresa
	 * 
	 * @param placa
	 *            Placa del radio movil al que pertenece la Empresa
	 * @return {@link Empresa} o null si no existe
	 * @throws SQLException
	 *             Si ocurre un error al obtener
	 */
	public Empresa getEmpresa(String placa) throws SQLException {

		return mDalEmpresa.getEmpresa(placa);
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
			mDbManager.beginTransaction();

			int result = mDalEmpresa.eliminarEmpresas();
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
	 * Actualiza el logo de una empresa
	 * 
	 * @param idEmpresa
	 *            Identificador de la {@link Empresa}
	 * @param logo
	 *            Nombre del logo
	 * @param thumbnail
	 *            True si es thumbnail o False si no
	 * @return 1 si se actualizo correctamente o 0 si no
	 * @throws SQLException
	 *             Si ocurre un error al actualizar
	 */
	public int actualizarLogo(int idEmpresa, String logo, boolean thumbnail)
			throws SQLException {

		try {
			mDbManager.beginTransaction();

			int result = 0;
			if (thumbnail) {
				result = mDalEmpresa.actualizarThumbnail(idEmpresa, logo);
			} else {
				result = mDalEmpresa.actualizarLogo(idEmpresa, logo);
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
	 * Obtiene el logo de una empresa
	 * 
	 * @param idEmpresa
	 *            Identificador de la {@link Empresa}
	 * @param thumbnail
	 *            True si se obtendra el thumbnail de la Empresa o False si no
	 * @return Logo de la empresa o null si no tiene
	 * @throws SQLException
	 *             Si ocurre un error al obtener
	 */
	public String getLogo(int idEmpresa, boolean thumbnail) throws SQLException {

		if (thumbnail) {
			return mDalEmpresa.getThumbnail(idEmpresa);
		}
		return mDalEmpresa.getLogo(idEmpresa);
	}

}
