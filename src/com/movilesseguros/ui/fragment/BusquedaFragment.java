package com.movilesseguros.ui.fragment;

import greendroid.widget.ItemAdapter;
import greendroid.widget.item.SeparatorItem;
import greendroid.widget.item.TextItem;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.android.helper.DeviceHelper;
import com.movilesseguros.R;
import com.movilesseguros.entidades.Busqueda;
import com.movilesseguros.negocio.loader.BusquedaLoader;

public class BusquedaFragment extends SherlockListFragment implements
		LoaderCallbacks<Object> {

	private AutoCompleteTextView mTxtSearch;
	private Button mBtnSearch;
	private BusquedaListener mBusquedaListener;
	private ItemAdapter mItemAdapter;

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

		try {
			mBusquedaListener = (BusquedaListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement BusquedaListener. ");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mItemAdapter = new ItemAdapter(getActivity());
		mItemAdapter.setNotifyOnChange(false);

		setListAdapter(mItemAdapter);

		getSherlockActivity().getSupportActionBar()
				.setDisplayShowCustomEnabled(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.busqueda_layout, null);

		mTxtSearch = (AutoCompleteTextView) view.findViewById(R.id.actv_search);
		// mTxtSearch.requestFocus();
		mTxtSearch
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {

						switch (actionId) {
						case EditorInfo.IME_ACTION_SEARCH:

							DeviceHelper.hideKeyboard(mTxtSearch);
							mBusquedaListener.onSearch(mTxtSearch.getText()
									.toString());

							return true;

						case EditorInfo.IME_ACTION_UNSPECIFIED:

							DeviceHelper.hideKeyboard(mTxtSearch);
							mBusquedaListener.onSearch(mTxtSearch.getText()
									.toString());

							return true;

						default:
							break;
						}

						return false;
					}
				});
		mTxtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					DeviceHelper.showKeyboard(mTxtSearch);
				}
			}
		});

		mBtnSearch = (Button) view.findViewById(R.id.btn_search);
		mBtnSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				DeviceHelper.hideKeyboard(mTxtSearch);
				mBusquedaListener.onSearch(mTxtSearch.getText().toString());

			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		// SharedPreferences showCasePreferences = getActivity()
		// .getSharedPreferences(SHOWCASE_PREFERENCES,
		// Context.MODE_PRIVATE);
		// if (!showCasePreferences.contains(PLACA_SHOT_TYPE_KEY)) {
		// animate(getListView()).alpha(0.0f);
		// }
		//
		// ShowcaseViews showcaseViews = new ShowcaseViews(getActivity(),
		// new ShowcaseViews.OnShowcaseAcknowledged() {
		//
		// @Override
		// public void onShowCaseAcknowledged(ShowcaseView showcaseView) {
		// animate(getListView()).alpha(1.0f);
		// DeviceHelper.showKeyboard(mTxtSearch);
		// }
		// });
		//
		// ShowcaseView.ConfigOptions coPlaca = new
		// ShowcaseView.ConfigOptions();
		// coPlaca.shotType = ShowcaseView.TYPE_ONE_SHOT;
		// coPlaca.showcaseId = PLACA_SHOT_TYPE_ID;
		//
		// showcaseViews.addView(new ItemViewProperties(R.id.actv_search,
		// R.string.busqueda_help_nro_placa_title_label,
		// R.string.busqueda_help_nro_placa_descripcion_label, coPlaca));
		//
		// ShowcaseView.ConfigOptions coBuscar = new
		// ShowcaseView.ConfigOptions();
		// coBuscar.shotType = ShowcaseView.TYPE_ONE_SHOT;
		// coBuscar.showcaseId = BUSQUEDA_SHOT_TYPE_ID;
		//
		// showcaseViews.addView(new ItemViewProperties(R.id.search,
		// R.string.busqueda_help_buscar_title_label,
		// R.string.busqueda_help_buscar_descripcion_label, coBuscar));
		//
		// showcaseViews.show();

		getLoaderManager().initLoader(BusquedaLoader.ID, null, this);
	}

	@Override
	public Loader<Object> onCreateLoader(int id, Bundle args) {

		getSherlockActivity()
				.setSupportProgressBarIndeterminateVisibility(true);

		return new BusquedaLoader(getActivity());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoadFinished(Loader<Object> loader, Object data) {

		if (data != null) {
			List<Busqueda> listBusqueda = (List<Busqueda>) data;
			cargarBusquedas(listBusqueda);
		}

		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(
				false);
	}

	@Override
	public void onLoaderReset(Loader<Object> loader) {
		// do nothing

	}

	private void cargarBusquedas(List<Busqueda> listBusquedas) {

		if (listBusquedas.isEmpty()) {
			return;
		}

		mItemAdapter.clear();

		SeparatorItem separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_busqueda_recientes_label);

		mItemAdapter.add(separatorItem);

		for (Busqueda busqueda : listBusquedas) {
			TextItem textItem = new TextItem();
			textItem.text = busqueda.getDescripcion();

			mItemAdapter.add(textItem);
		}

		mItemAdapter.notifyDataSetChanged();
	}

	public void refreshBusquedas() {

		getLoaderManager().restartLoader(BusquedaLoader.ID, null, this);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		DeviceHelper.hideKeyboard(mTxtSearch);

		TextItem textItem = (TextItem) mItemAdapter.getItem(position);

		mBusquedaListener.onSearch(textItem.text);
	}

	public void hideKeyboard() {

		if (mTxtSearch != null) {
			DeviceHelper.hideKeyboard(mTxtSearch);
		}
	}

	public interface BusquedaListener {

		public void onSearch(String text);

	}

}
