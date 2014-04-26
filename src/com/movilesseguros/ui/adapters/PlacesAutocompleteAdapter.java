package com.movilesseguros.ui.adapters;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.android.helper.google.places.GooglePlaces;
import com.android.helper.google.places.Place;
import com.android.helper.google.places.PlaceDetails;
import com.movilesseguros.R;

public class PlacesAutocompleteAdapter extends ArrayAdapter<Place> implements
		Filterable {

	private List<Place> mResultList;
	private GooglePlaces mGooglePlaces;

	public PlacesAutocompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		mGooglePlaces = new GooglePlaces(
				context.getString(R.string.mapa_google_api_browser_key));
	}

	@Override
	public int getCount() {
		return mResultList.size();
	}

	@Override
	public Place getItem(int position) {

		if (position >= mResultList.size()) {
			return null;
		}
		return mResultList.get(position);
	}

	@Override
	public Filter getFilter() {

		Filter filter = new Filter() {

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					// ## Obtenemos los resultados del autocompletado
					mResultList = mGooglePlaces
							.getPlaces(constraint.toString());
					for (Place place : mResultList) {
						// ## Obtenemos el detalle del lugar
						PlaceDetails placeDetails = mGooglePlaces
								.getPlaceDetails(place.getReference());
						place.setPlaceDetails(placeDetails);
					}

					filterResults.values = mResultList;
					filterResults.count = mResultList.size();
				}

				return filterResults;
			}
		};

		return filter;
	}

}
