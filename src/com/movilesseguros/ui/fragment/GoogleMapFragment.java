package com.movilesseguros.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.helper.FormatHelper;
import com.android.helper.HelpPopup;
import com.android.helper.HelpPopup.ShowListener;
import com.android.helper.google.map.Route;
import com.android.helper.gps.GpsHelper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.movilesseguros.R;
import com.movilesseguros.dal.PlaceProvider;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.negocio.ControlConfiguracion;

public class GoogleMapFragment extends SupportMapFragment implements
		OnInfoWindowClickListener, LoaderCallbacks<Cursor> {

	private static final int DEFAULT_ZOOM = 15;
	private static final LatLng DEFAULT_LAT_LNG = new LatLng(-17.7797768,
			-63.1699816);
	private static final int SEARCH_CURSOR_LOADER_ID = 0;
	private static final int DETAILS_CURSOR_LOADER_ID = 1;

	private Marker marker;
	private GoogleMap mGoogleMap;
	private GoogleMapListener mGoogleMapListener;
	private ControlConfiguracion mControlConfiguracion;
	private HelpPopup mHelpPopup;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mGoogleMapListener = (GoogleMapListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement GoogleMapListener.");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = super.onCreateView(inflater, container, savedInstanceState);
		initMap();
		return view;
	}

	private void initMap() {

		mGoogleMap = getMap();
		mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mGoogleMap.setMyLocationEnabled(true);

		Location location = GpsHelper.getInstancia().getLocation();

		mGoogleMap
				.moveCamera(CameraUpdateFactory.newLatLngZoom(
						location == null ? DEFAULT_LAT_LNG
								: new LatLng(location.getLatitude(), location
										.getLongitude()), DEFAULT_ZOOM));

		UiSettings settings = getMap().getUiSettings();
		settings.setMyLocationButtonEnabled(true);
		settings.setCompassEnabled(true);
		settings.setAllGesturesEnabled(true);

		mGoogleMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng point) {

				mGoogleMap.clear();

				marker = mGoogleMap.addMarker(new MarkerOptions()
						.position(point)
						.title("")
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

			}
		});
		mGoogleMap.setOnInfoWindowClickListener(this);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		mControlConfiguracion = new ControlConfiguracion(getActivity());

		if (!mControlConfiguracion.isShowHelpMapa() && getView() != null) {
			mHelpPopup = new HelpPopup(getActivity(),
					getString(R.string.mapa_message_help_punto_destino),
					R.layout.yellow_popup_layout);
			mHelpPopup.setShowListener(new ShowListener() {

				@Override
				public void onShow() {
					mControlConfiguracion.setShowHelpMapa();
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

	public Marker getMarker() {
		return marker;
	}

	public void drawRoute(Route route) {

		mGoogleMap.clear();
		marker = null;

		List<LatLng> listPoints = route.getPoints();

		if (!listPoints.isEmpty()) {
			PolylineOptions polylineOptions = new PolylineOptions();
			polylineOptions.color(0x7F0000FF);

			for (LatLng point : listPoints) {
				polylineOptions.add(point);
			}

			// ## Calculamos la tarifa
			double tarifa = calcularTarifa(route.getLength());

			// ## Adicionamos el punto inicial
			addMarker(mGoogleMap, route.getStartAddress(), null,
					R.drawable.ic_maps_indicator_startpoint_route,
					listPoints.get(0));

			// ## Adicionamos el punto final
			Marker markerDestino = addMarker(
					mGoogleMap,
					route.getEndAddress(),
					getString(R.string.mapa_tarifa_aproximada_label,
							FormatHelper.toDecimalFormat(tarifa, "Bs.")),
					R.drawable.ic_maps_indicator_endpoint_route,
					listPoints.get(listPoints.size() - 1));

			mGoogleMap.addPolyline(polylineOptions);

			showMarkerInfo(markerDestino);
		}

	}

	private Marker addMarker(GoogleMap map, String title, String snippet,
			int drawableId, LatLng position) {

		return map.addMarker(new MarkerOptions().title(title).snippet(snippet)
				.icon(BitmapDescriptorFactory.fromResource(drawableId))
				.position(position).anchor(0.5f, 1.0f));

	}

	private void showMarkerInfo(Marker marker) {

		marker.showInfoWindow();

		if (mGoogleMap.getCameraPosition().zoom < DEFAULT_ZOOM) {
			mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					marker.getPosition(), DEFAULT_ZOOM));
		} else {
			mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(marker
					.getPosition()));
		}
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

	@Override
	public void onInfoWindowClick(Marker marker) {

		mGoogleMapListener.onInfoWindowClick();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		switch (id) {
		case SEARCH_CURSOR_LOADER_ID:

			return new CursorLoader(getActivity(), PlaceProvider.SEARCH_URI,
					null, getString(R.string.mapa_google_api_browser_key),
					new String[] { args.getString("query") }, null);
		case DETAILS_CURSOR_LOADER_ID:

			return new CursorLoader(getActivity(), PlaceProvider.DETAILS_URI,
					null, getString(R.string.mapa_google_api_browser_key),
					new String[] { args.getString("query") }, null);

		default:
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		showLocations(cursor);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// do nothing

	}

	private void showLocations(Cursor cursor) {

		LatLng position = null;
		mGoogleMap.clear();

		while (cursor.moveToNext()) {
			MarkerOptions markerOptions = new MarkerOptions();
			position = new LatLng(Double.parseDouble(cursor.getString(1)),
					Double.parseDouble(cursor.getString(2)));
			markerOptions.position(position);
			mGoogleMap.addMarker(markerOptions);
		}

		if (position != null) {
			CameraUpdate cameraPosition = CameraUpdateFactory
					.newLatLng(position);
			mGoogleMap.animateCamera(cameraPosition);
		}
	}

	public void doSearch(String query) {

		Bundle data = new Bundle();
		data.putString("query", query);

		getLoaderManager().restartLoader(SEARCH_CURSOR_LOADER_ID, data, this);
	}

	public void getPlace(String query) {

		Bundle data = new Bundle();
		data.putString("query", query);

		getLoaderManager().restartLoader(DETAILS_CURSOR_LOADER_ID, data, this);
	}

	public void addMarker(LatLng position) {

		marker = mGoogleMap.addMarker(new MarkerOptions()
				.position(position)
				.title("")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

		if (position != null) {
			CameraUpdate cameraPosition = CameraUpdateFactory
					.newLatLng(position);
			mGoogleMap.animateCamera(cameraPosition);
		}
	}

	public interface GoogleMapListener {

		public void onInfoWindowClick();
	}
}
