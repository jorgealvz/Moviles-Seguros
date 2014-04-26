package com.movilesseguros.ui.fragment;

import greendroid.widget.ItemAdapter;
import greendroid.widget.item.SeparatorItem;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.android.helper.HelpPopup;
import com.android.helper.HelpPopup.ShowListener;
import com.movilesseguros.R;
import com.movilesseguros.entidades.Conductor;
import com.movilesseguros.entidades.Empresa;
import com.movilesseguros.entidades.Vehiculo;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.negocio.ControlConfiguracion;
import com.movilesseguros.negocio.loader.ConductorLoader;
import com.movilesseguros.negocio.loader.EmpresaLoader;
import com.movilesseguros.negocio.loader.VehiculoLoader;
import com.movilesseguros.ui.widgets.item.ThumbnailDescriptionItem;

public class ResultadoBusquedaFragment extends SherlockListFragment implements
		LoaderCallbacks<Object> {

	private ThumbnailDescriptionItem mThumbnailItem;
	private ItemAdapter mItemAdapter;
	private int mTipo;
	private String mPlaca;
	private ResultadoBusquedaListener mResultadoBusquedaListener;
	private ControlConfiguracion mControlConfiguracion;
	private HelpPopup mHelpPopup;

	public static ResultadoBusquedaFragment newInstance(Bundle args) {
		ResultadoBusquedaFragment rbf = new ResultadoBusquedaFragment();
		rbf.setArguments(args);

		return rbf;
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

		try {
			mResultadoBusquedaListener = (ResultadoBusquedaListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ResultadoBusquedaListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(
				R.layout.resultado_busqueda_content_layout, null);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		mControlConfiguracion = new ControlConfiguracion(getActivity());
		mItemAdapter = new ItemAdapter(getActivity());
		mItemAdapter.setNotifyOnChange(false);
		setListAdapter(mItemAdapter);

		mTipo = getArguments().getInt("tipo");
		mPlaca = getArguments().getString("placa");

		switch (mTipo) {
		case Constantes.TIPO_EMPRESA:
			getLoaderManager().initLoader(EmpresaLoader.ID, null, this);
			break;
		case Constantes.TIPO_CONDUCTOR:
			getLoaderManager().initLoader(ConductorLoader.ID, null, this);
			break;
		case Constantes.TIPO_VEHICULO:
			getLoaderManager().initLoader(VehiculoLoader.ID, null, this);
			break;

		default:
			break;
		}

		if (!mControlConfiguracion.isShowHelpResultadoBusqueda()
				&& mTipo == Constantes.TIPO_EMPRESA && getView() != null) {
			mHelpPopup = new HelpPopup(getActivity(),
					getString(R.string.busqueda_message_help_informacion_item),
					R.layout.yellow_popup_layout);
			mHelpPopup.setShowListener(new ShowListener() {

				@Override
				public void onShow() {
					mControlConfiguracion.setShowHelpResultadoBusqueda();
				}

				@Override
				public void onPreShow() {

				}

				@Override
				public void onDismiss() {

				}
			});
			mHelpPopup.show(getView());

		}
	}

	@Override
	public void onDestroy() {

		super.onDestroy();

		if (mHelpPopup != null) {
			mHelpPopup.dismiss();
		}
	}

	@Override
	public Loader<Object> onCreateLoader(int id, Bundle args) {

		getSherlockActivity()
				.setSupportProgressBarIndeterminateVisibility(true);

		switch (id) {
		case EmpresaLoader.ID:

			return new EmpresaLoader(getActivity(), mPlaca);

		case ConductorLoader.ID:

			return new ConductorLoader(getActivity(), mPlaca);

		case VehiculoLoader.ID:

			return new VehiculoLoader(getActivity(), mPlaca);

		default:
			return null;
		}

	}

	@Override
	public void onLoadFinished(Loader<Object> loader, Object data) {

		switch (loader.getId()) {
		case EmpresaLoader.ID:
			if (data != null) {
				Empresa empresa = (Empresa) data;
				cargarDatosEmpresa(empresa);
			} else {
				mResultadoBusquedaListener
						.onError(getString(R.string.busqueda_empresa_error_message));
			}
			break;

		case ConductorLoader.ID:
			if (data != null) {
				Conductor conductor = (Conductor) data;
				cargarDatosConductor(conductor);
			} else {
				mResultadoBusquedaListener
						.onError(getString(R.string.busqueda_conductor_error_message));
			}
			break;

		case VehiculoLoader.ID:
			if (data != null) {
				Vehiculo vehiculo = (Vehiculo) data;
				cargarDatosVehiculo(vehiculo);
			} else {
				mResultadoBusquedaListener
						.onError(getString(R.string.busqueda_vehiculo_error_message));
			}
			break;

		default:
			break;
		}

		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(
				false);

	}

	@Override
	public void onLoaderReset(Loader<Object> loader) {
		// do nothing

	}

	private void cargarDatosEmpresa(Empresa empresa) {

		mItemAdapter.clear();

		SeparatorItem separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_empresa_razon_social);
		mItemAdapter.add(separatorItem);

		mThumbnailItem = new ThumbnailDescriptionItem();
		mThumbnailItem.id = empresa.getId();
		mThumbnailItem.text = empresa.getRazonSocial();
		if (TextUtils.isEmpty(mThumbnailItem.text)) {
			mThumbnailItem.text = getString(R.string.busqueda_no_disponible_label);
		}
		mThumbnailItem.type = ThumbnailDescriptionItem.TYPE_IMAGE;
		mThumbnailItem.tipoImagen = Constantes.TIPO_EMPRESA;
		if (TextUtils.isEmpty(empresa.getLogo())) {
			mThumbnailItem.drawableId = R.drawable.ic_no_logo;
			mThumbnailItem.enabled = false;
		} else {
			mThumbnailItem.drawableURL = empresa.getLogo();
			mThumbnailItem.enabled = true;
		}

		mItemAdapter.add(mThumbnailItem);

		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_empresa_direccion);
		mItemAdapter.add(separatorItem);

		ThumbnailDescriptionItem item = new ThumbnailDescriptionItem();
		item.text = empresa.getDireccion();
		item.latitud = empresa.getLatitud();
		item.longitud = empresa.getLongitud();
		item.enabled = true;
		if (TextUtils.isEmpty(item.text)) {
			item.text = getString(R.string.busqueda_no_disponible_label);
			item.enabled = false;
		}
		if (empresa.getLatitud() == 0 && empresa.getLongitud() == 0) {
			item.enabled = false;
		}
		item.type = ThumbnailDescriptionItem.TYPE_DIRECTION;
		mItemAdapter.add(item);

		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_empresa_telefono);
		mItemAdapter.add(separatorItem);

		item = new ThumbnailDescriptionItem();
		item.text = empresa.getTelefono();
		item.enabled = true;
		if (TextUtils.isEmpty(item.text)) {
			item.text = getString(R.string.busqueda_no_disponible_label);
			item.enabled = false;
		}
		item.type = ThumbnailDescriptionItem.TYPE_PHONE;
		mItemAdapter.add(item);

		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_empresa_fax);
		mItemAdapter.add(separatorItem);

		item = new ThumbnailDescriptionItem();
		item.text = empresa.getFax();
		if (TextUtils.isEmpty(item.text)) {
			item.text = getString(R.string.busqueda_no_disponible_label);
		}
		item.enabled = false;
		mItemAdapter.add(item);

		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_empresa_pagina_web);
		mItemAdapter.add(separatorItem);

		item = new ThumbnailDescriptionItem();
		item.text = empresa.getPaginaWeb();
		item.enabled = true;
		if (TextUtils.isEmpty(item.text)) {
			item.text = getString(R.string.busqueda_no_disponible_label);
			item.enabled = false;
		}
		item.type = ThumbnailDescriptionItem.TYPE_WEB;
		mItemAdapter.add(item);

		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_empresa_email);
		mItemAdapter.add(separatorItem);

		item = new ThumbnailDescriptionItem();
		item.text = empresa.getEmail();
		item.enabled = true;
		if (TextUtils.isEmpty(item.text)) {
			item.text = getString(R.string.busqueda_no_disponible_label);
			item.enabled = false;
		}
		item.type = ThumbnailDescriptionItem.TYPE_EMAIL;
		mItemAdapter.add(item);

		mItemAdapter.notifyDataSetChanged();

	}

	private void cargarDatosConductor(Conductor conductor) {

		mItemAdapter.clear();

		SeparatorItem separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_conductor_nombre);
		mItemAdapter.add(separatorItem);

		mThumbnailItem = new ThumbnailDescriptionItem();
		mThumbnailItem.id = conductor.getId();
		mThumbnailItem.text = conductor.getNombre()
				+ (conductor.getPaterno() != null ? " "
						+ conductor.getPaterno() : "")
				+ (conductor.getMaterno() != null ? " "
						+ conductor.getMaterno() : "");
		mThumbnailItem.type = ThumbnailDescriptionItem.TYPE_IMAGE;
		mThumbnailItem.tipoImagen = Constantes.TIPO_CONDUCTOR;
		if (TextUtils.isEmpty(conductor.getFoto())) {
			mThumbnailItem.drawableId = R.drawable.ic_contact_picture;
			mThumbnailItem.enabled = false;
		} else {
			mThumbnailItem.drawableURL = conductor.getFoto();
			mThumbnailItem.enabled = true;
		}
		if (TextUtils.isEmpty(mThumbnailItem.text)) {
			mThumbnailItem.text = getString(R.string.busqueda_no_disponible_label);
		}

		mItemAdapter.add(mThumbnailItem);

		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_conductor_nacionalidad);
		mItemAdapter.add(separatorItem);

		ThumbnailDescriptionItem item = new ThumbnailDescriptionItem();
		item.text = conductor.getNacionalidad();
		if (TextUtils.isEmpty(item.text)) {
			item.text = getString(R.string.busqueda_no_disponible_label);
		}
		item.enabled = false;
		mItemAdapter.add(item);

		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_conductor_observaciones);
		mItemAdapter.add(separatorItem);

		item = new ThumbnailDescriptionItem();
		item.text = conductor.getObservacion();
		if (TextUtils.isEmpty(item.text)) {
			item.text = getString(R.string.busqueda_ninguna_label);
		}
		item.enabled = false;
		mItemAdapter.add(item);

		mItemAdapter.notifyDataSetChanged();

	}

	private void cargarDatosVehiculo(Vehiculo vehiculo) {

		mItemAdapter.clear();

		SeparatorItem separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_vehiculo_placa);
		mItemAdapter.add(separatorItem);

		mThumbnailItem = new ThumbnailDescriptionItem();
		mThumbnailItem.id = vehiculo.getId();
		mThumbnailItem.text = vehiculo.getPlacaPta();
		if (TextUtils.isEmpty(mThumbnailItem.text)) {
			mThumbnailItem.text = getString(R.string.busqueda_no_disponible_label);
		}
		if (TextUtils.isEmpty(vehiculo.getFoto())) {
			mThumbnailItem.drawableId = R.drawable.ic_no_car;
			mThumbnailItem.enabled = false;
		} else {
			mThumbnailItem.drawableURL = vehiculo.getFoto();
			mThumbnailItem.enabled = true;
		}
		mThumbnailItem.type = ThumbnailDescriptionItem.TYPE_IMAGE;
		mThumbnailItem.tipoImagen = Constantes.TIPO_VEHICULO;

		mItemAdapter.add(mThumbnailItem);

		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_vehiculo_nro_movil);
		mItemAdapter.add(separatorItem);

		ThumbnailDescriptionItem item = new ThumbnailDescriptionItem();
		item.text = String.valueOf(vehiculo.getNroMovil());
		if (TextUtils.isEmpty(item.text)) {
			item.text = getString(R.string.busqueda_no_disponible_label);
		}
		item.enabled = false;
		mItemAdapter.add(item);

		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_vehiculo_observaciones);
		mItemAdapter.add(separatorItem);

		item = new ThumbnailDescriptionItem();
		item.text = vehiculo.getObservaciones();
		if (TextUtils.isEmpty(item.text)) {
			item.text = getString(R.string.busqueda_ninguna_label);
		}
		item.enabled = false;
		mItemAdapter.add(item);

		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_vehiculo_clase);
		mItemAdapter.add(separatorItem);

		item = new ThumbnailDescriptionItem();
		item.text = vehiculo.getClaseVehiculo().getDescripcion();
		if (TextUtils.isEmpty(item.text)) {
			item.text = getString(R.string.busqueda_no_disponible_label);
		}
		item.enabled = false;
		mItemAdapter.add(item);

		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_vehiculo_tipo);
		mItemAdapter.add(separatorItem);

		item = new ThumbnailDescriptionItem();
		item.text = vehiculo.getTipoVehiculo().getDescripcion();
		if (TextUtils.isEmpty(item.text)) {
			item.text = getString(R.string.busqueda_no_disponible_label);
		}
		item.enabled = false;
		mItemAdapter.add(item);

		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_vehiculo_marca);
		mItemAdapter.add(separatorItem);

		item = new ThumbnailDescriptionItem();
		item.text = vehiculo.getMarcaVehiculo().getDescripcion();
		if (TextUtils.isEmpty(item.text)) {
			item.text = getString(R.string.busqueda_no_disponible_label);
		}
		item.enabled = false;
		mItemAdapter.add(item);

		separatorItem = new SeparatorItem();
		separatorItem.text = getString(R.string.busqueda_vehiculo_modelo);
		mItemAdapter.add(separatorItem);

		item = new ThumbnailDescriptionItem();
		item.text = String.valueOf(vehiculo.getModelo());
		if (TextUtils.isEmpty(item.text)) {
			item.text = getString(R.string.busqueda_no_disponible_label);
		}
		item.enabled = false;
		mItemAdapter.add(item);

		mItemAdapter.notifyDataSetChanged();

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		ThumbnailDescriptionItem item = (ThumbnailDescriptionItem) mItemAdapter
				.getItem(position);

		switch (item.type) {
		case ThumbnailDescriptionItem.TYPE_IMAGE:
			mResultadoBusquedaListener.onItemImageSelected(item.id, mTipo);
			break;
		case ThumbnailDescriptionItem.TYPE_WEB:
			mResultadoBusquedaListener.onItemWebSelected(item.text);
			break;
		case ThumbnailDescriptionItem.TYPE_PHONE:
			
			String[] telefonos = item.text.split("-");
			
			mResultadoBusquedaListener.onItemPhoneSelected(telefonos);
			break;
		case ThumbnailDescriptionItem.TYPE_EMAIL:
			mResultadoBusquedaListener.onItemEmailSelected(item.text);
			break;
		case ThumbnailDescriptionItem.TYPE_DIRECTION:
			mResultadoBusquedaListener.onItemDirectionSelected(item.latitud,
					item.longitud, item.text);
			break;

		default:
			break;
		}

	}

	public interface ResultadoBusquedaListener {

		public void onError(String message);

		public void onItemImageSelected(int id, int tipo);

		public void onItemWebSelected(String url);

		public void onItemPhoneSelected(String[] phones);

		public void onItemEmailSelected(String email);

		public void onItemDirectionSelected(double latitud, double longitud,
				String label);
	}
}
