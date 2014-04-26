package com.movilesseguros.ui.fragment;

import greendroid.widget.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.android.helper.EndlessScrollListener;
import com.movilesseguros.R;
import com.movilesseguros.entidades.Empresa;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.negocio.loader.CantidadEmpresasSegurasLoader;
import com.movilesseguros.negocio.loader.EmpresasSegurasLoader;
import com.movilesseguros.ui.widgets.item.EmpresaItem;

public class MovilesSegurosFragment extends SherlockListFragment implements
		LoaderCallbacks<Object> {

	private ItemAdapter mItemAdapter;
	private MovilesSegurosListener movilesSegurosListener;
	private int mPagina;
	private int mCantidadDatos;
	private List<Empresa> mListEmpresas;
	private boolean mIsEndless;
	private View mFooterView;

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

		try {
			movilesSegurosListener = (MovilesSegurosListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement MovilesSegurosListener.");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		mListEmpresas = new ArrayList<Empresa>();
		mPagina = 1;

		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterView = inflater.inflate(R.layout.wait_item_view_layout, null);

		getListView().addFooterView(mFooterView);

		getListView().setOnScrollListener(new EndlessScrollListener(0, 0) {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {

				Log.i("MovilesSeguros", "TotalItemsCount: " + totalItemsCount);
				Log.i("MovilesSeguros", "CantidadDatos: " + mCantidadDatos);
				if ((totalItemsCount - 1) < mCantidadDatos) {
					toogleFooterView(mFooterView, View.VISIBLE);
					mPagina = page;

					Log.i("MovilesSeguros", "Pagina: " + mPagina);
					mIsEndless = true;
					getLoaderManager().restartLoader(EmpresasSegurasLoader.ID,
							null, MovilesSegurosFragment.this);
				} else {
					toogleFooterView(mFooterView, View.GONE);
				}

			}
		});

		mItemAdapter = new ItemAdapter(getActivity());
		mItemAdapter.setNotifyOnChange(false);
		mItemAdapter.clear();

		setListAdapter(mItemAdapter);

		getLoaderManager().initLoader(CantidadEmpresasSegurasLoader.ID, null,
				this);
	}

	@Override
	public Loader<Object> onCreateLoader(int id, Bundle args) {

		switch (id) {
		case CantidadEmpresasSegurasLoader.ID:

			return new CantidadEmpresasSegurasLoader(getActivity());
		case EmpresasSegurasLoader.ID:

			return new EmpresasSegurasLoader(getActivity(), mPagina);

		default:
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoadFinished(Loader<Object> loader, Object data) {

		switch (loader.getId()) {
		case CantidadEmpresasSegurasLoader.ID:
			if (data != null) {
				Object[] params = (Object[]) data;
				int result = (Integer) params[0];
				if (result == Constantes.TAREA_CORRECTA) {
					mCantidadDatos = (Integer) params[1];
					if (mCantidadDatos > 0) {
						mIsEndless = false;
						getLoaderManager().initLoader(EmpresasSegurasLoader.ID,
								null, this);
					}
				} else if (result == Constantes.ERROR_CONEXION) {
					movilesSegurosListener
							.onError(getString(R.string.app_error_conexion_message));
				} else {
					movilesSegurosListener
							.onError(getString(R.string.empresas_error_message_cargar_empresas));
				}
			}
			break;
		case EmpresasSegurasLoader.ID:
			if (data != null) {
				Object[] params = (Object[]) data;
				int result = (Integer) params[0];
				List<Empresa> listEmpresas = (List<Empresa>) params[1];
				if (result == Constantes.TAREA_CORRECTA) {
					cargarEmpresas(listEmpresas);
				} else if (result == Constantes.ERROR_CONEXION) {
					movilesSegurosListener
							.onError(getString(R.string.app_error_conexion_message));
				} else {
					movilesSegurosListener
							.onError(getString(R.string.empresas_error_message_cargar_empresas));
				}
			}

			toogleFooterView(mFooterView, View.GONE);

			break;

		default:
			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Object> loader) {
		// do nothing
	}

	private void cargarEmpresas(List<Empresa> listEmpresas) {

		if (!mIsEndless) {
			mItemAdapter.clear();
			mListEmpresas.clear();
		}

		for (Empresa empresa : listEmpresas) {
			EmpresaItem item = new EmpresaItem();
			item.id = empresa.getId();
			item.nombre = empresa.getRazonSocial();
			item.telefono = empresa.getTelefono();
			item.drawableURL = empresa.getLogo();

			if (TextUtils.isEmpty(item.telefono)) {
				item.enabled = false;
			}

			mItemAdapter.add(item);
			mListEmpresas.add(empresa);

		}

		mItemAdapter.notifyDataSetChanged();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		EmpresaItem empresaItem = (EmpresaItem) mItemAdapter.getItem(position);

		if (!TextUtils.isEmpty(empresaItem.telefono)) {
			String[] telefonos = empresaItem.telefono.split("-");

			movilesSegurosListener.onEmpresaSelected(telefonos);
		}

	}

	private void toogleFooterView(View footerView, int visibility) {

		footerView.findViewById(R.id.progress).setVisibility(visibility);
		footerView.findViewById(R.id.tv_progress).setVisibility(visibility);
	}

	public interface MovilesSegurosListener {

		public void onError(String message);

		public void onEmpresaSelected(String[] telefonos);
	}

}
