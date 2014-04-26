package com.movilesseguros.ui.fragment;

import greendroid.widget.ItemAdapter;
import greendroid.widget.item.SeparatorItem;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListFragment;
import com.android.helper.FormatHelper;
import com.android.helper.google.map.GMapDirection;
import com.android.helper.google.map.Route;
import com.movilesseguros.R;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.ui.widgets.item.ThumbnailDescriptionItem;

public class TarifaFragment extends SherlockListFragment {

	private ItemAdapter mItemAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		mItemAdapter = new ItemAdapter(getActivity());
		mItemAdapter.setNotifyOnChange(false);

		cargarDatosTarifa();
	}

	private void cargarDatosTarifa() {

		GMapDirection gMapDirection = new GMapDirection(getActivity());

		Route route = gMapDirection.getSavedRoute();

		// ## Costo
		SeparatorItem separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.tarifa_costo_label);

		mItemAdapter.add(separatorItem);

		ThumbnailDescriptionItem item = new ThumbnailDescriptionItem();
		item.text = FormatHelper.toDecimalFormat(
				calcularTarifa(route.getLength()), "Bs.");
		item.enabled = false;

		mItemAdapter.add(item);

		// ## Desde
		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.tarifa_punto_inicial_label);

		mItemAdapter.add(separatorItem);

		item = new ThumbnailDescriptionItem();
		item.text = route.getStartAddress();
		item.enabled = false;

		mItemAdapter.add(item);

		// ## Hasta
		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.tarifa_punto_final_label);

		mItemAdapter.add(separatorItem);

		item = new ThumbnailDescriptionItem();
		item.text = route.getEndAddress();
		item.enabled = false;

		mItemAdapter.add(item);

		// ## Distancia
		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.tarifa_distancia_label);

		mItemAdapter.add(separatorItem);

		item = new ThumbnailDescriptionItem();
		item.text = (route.getLength() / 1000) + " Km.";
		item.enabled = false;

		mItemAdapter.add(item);

		// ## Tiempo
		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.tarifa_tiempo_label);

		mItemAdapter.add(separatorItem);

		item = new ThumbnailDescriptionItem();
		item.text = (route.getDuration() / 60) + " min.";
		item.enabled = false;

		mItemAdapter.add(item);

		setListAdapter(mItemAdapter);
	}

	private double calcularTarifa(int routeLength) {

		double kilometers = routeLength / 1000;

		double tarifa = 0;
		if (kilometers <= Constantes.KILOMETROS_MINIMOS) {
			tarifa = Constantes.TARIFA_BASICA;
		} else {
			tarifa = Constantes.TARIFA_BASICA
					+ ((kilometers - Constantes.KILOMETROS_MINIMOS) * Constantes.TARIFA_X_KILOMETRO);
		}
		return tarifa;

	}
}
