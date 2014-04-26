package com.movilesseguros.dal;

import java.util.List;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.android.helper.google.places.GooglePlaces;
import com.android.helper.google.places.Place;
import com.android.helper.google.places.PlaceDetails;

public class PlaceProvider extends ContentProvider {

	public static final String AUTHORITY = "com.movilesseguros.dal.PlaceProvider";

	public static final Uri SEARCH_URI = Uri.parse("content://" + AUTHORITY
			+ "/search");

	public static final Uri DETAILS_URI = Uri.parse("content://" + AUTHORITY
			+ "/details");

	private static final int SEARCH = 1;
	private static final int SUGGESTIONS = 2;
	private static final int DETAILS = 3;

	String mKey = "key=AIzaSyBRavGtloIAZu5WSCXOcEHaLW4Sm1mS06o";

	// Defines a set of uris allowed with this content provider
	private static final UriMatcher mUriMatcher = buildUriMatcher();

	private static UriMatcher buildUriMatcher() {

		UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		// URI for "Go" button
		uriMatcher.addURI(AUTHORITY, "search", SEARCH);

		// URI for suggestions in Search Dialog
		uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY,
				SUGGESTIONS);

		// URI for Details
		uriMatcher.addURI(AUTHORITY, "details", DETAILS);

		return uriMatcher;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor c = null;

		GooglePlaces googlePlaces = new GooglePlaces(mKey);

		List<Place> listPlaces = null;

		MatrixCursor mCursor = null;

		String criteria = selectionArgs[0];

		switch (mUriMatcher.match(uri)) {
		case SEARCH:
			// Defining a cursor object with columns description, lat and lng
			mCursor = new MatrixCursor(new String[] { "description", "lat",
					"lng" });

			listPlaces = googlePlaces.getPlaces(criteria);
			for (Place place : listPlaces) {
				// ## Obtenemos el detalle del lugar
				PlaceDetails placeDetails = googlePlaces.getPlaceDetails(place
						.getReference());

				mCursor.addRow(new String[] { place.getDescription(),
						String.valueOf(placeDetails.getLatitud()),
						String.valueOf(placeDetails.getLongitud()) });
			}

			c = mCursor;
			break;

		case SUGGESTIONS:

			// Defining a cursor object with columns id, SUGGEST_COLUMN_TEXT_1,
			// SUGGEST_COLUMN_INTENT_EXTRA_DATA
			mCursor = new MatrixCursor(new String[] { "_id",
					SearchManager.SUGGEST_COLUMN_TEXT_1,
					SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA });

			listPlaces = googlePlaces.getPlaces(criteria);

			for (int i = 0; i < listPlaces.size(); i++) {
				Place place = listPlaces.get(i);
				mCursor.addRow(new String[] { Integer.toString(i),
						place.getDescription(), place.getReference() });
			}

			c = mCursor;
			break;

		case DETAILS:
			// Defining a cursor object with columns description, lat and lng
			mCursor = new MatrixCursor(new String[] { "description", "lat",
					"lng" });

			listPlaces = googlePlaces.getPlaces(criteria);
			for (Place place : listPlaces) {
				// ## Obtenemos el detalle del lugar
				PlaceDetails placeDetails = googlePlaces.getPlaceDetails(place
						.getReference());

				mCursor.addRow(new String[] { place.getDescription(),
						String.valueOf(placeDetails.getLatitud()),
						String.valueOf(placeDetails.getLongitud()) });
			}

			c = mCursor;
			break;

		}

		return c;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public boolean onCreate() {
		return false;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
