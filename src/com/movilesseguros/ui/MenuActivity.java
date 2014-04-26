package com.movilesseguros.ui;

import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.android.helper.fragment.AbstractFragmentActivity;
import com.android.helper.fragment.TaskDialogFragment;
import com.android.helper.google.map.GMapDirection;
import com.android.helper.google.map.Route;
import com.android.helper.google.places.Place;
import com.android.helper.gps.GpsHelper;
import com.android.helper.widget.CollapsibleSearchMenu;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.plus.Plus;
import com.movilesseguros.R;
import com.movilesseguros.entidades.Usuario;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.negocio.ControlConfiguracion;
import com.movilesseguros.negocio.task.BusquedaTask;
import com.movilesseguros.negocio.task.EliminarDatosTemporalesTask;
import com.movilesseguros.negocio.task.RouteTask;
import com.movilesseguros.negocio.task.WaitLocationTask;
import com.movilesseguros.ui.adapters.MenuItemAdapter;
import com.movilesseguros.ui.adapters.PlacesAutocompleteAdapter;
import com.movilesseguros.ui.fragment.BusquedaFragment;
import com.movilesseguros.ui.fragment.BusquedaFragment.BusquedaListener;
import com.movilesseguros.ui.fragment.GoogleMapFragment;
import com.movilesseguros.ui.fragment.GoogleMapFragment.GoogleMapListener;
import com.movilesseguros.ui.fragment.MovilesSegurosFragment;
import com.movilesseguros.ui.fragment.MovilesSegurosFragment.MovilesSegurosListener;

public class MenuActivity extends AbstractFragmentActivity implements
		BusquedaListener, MovilesSegurosListener, GoogleMapListener {

	private static final int BACK_COUNT = 2;
	private static final int BUSQUEDA_FRAGMENT = 0;
	private static final int MOVILES_SEGUROS_FRAGMENT = 1;
	private static final int MAPA_FRAGMENT = 2;

	private BusquedaFragment mBusquedaFragment;
	private MovilesSegurosFragment movilesSegurosFragment;
	private GoogleMapFragment mGoogleMapFragment;
	private int mBackCount;
	private DrawerLayout mDrawerLayout;
	private CharSequence mDrawerTitle;
	private ListView mDrawerList;
	private MenuItemAdapter menuItemAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
	private String[] mTitles;
	private ControlConfiguracion mControlConfiguracion;
	private int mTipoUsuario;
	private int mSelectedPosition;
	private MenuItem mSearchView;
	// ## Facebook
	private UiLifecycleHelper mUiLifecycleHelper;
	// ## Google+
	private GoogleApiClient mGoogleApiClient;
	private ConnectionResult mConnectionResult;
	private boolean mIntentInProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.drawer_main_layout);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mDrawerTitle = getTitle();

		mControlConfiguracion = new ControlConfiguracion(
				getApplicationContext());

		mTipoUsuario = mControlConfiguracion.getTipoUsuario();

		if (mTipoUsuario == Usuario.FACEBOOK) {
			mUiLifecycleHelper = new UiLifecycleHelper(this, mCallback);
			mUiLifecycleHelper.onCreate(savedInstanceState);
		} else if (mTipoUsuario == Usuario.GOOGLE) {
			mGoogleApiClient = new GoogleApiClient.Builder(
					getApplicationContext())
					.addConnectionCallbacks(mConnectionCallbacks)
					.addOnConnectionFailedListener(mOnConnectionFailedListener)
					.addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();
		}

		mBusquedaFragment = new BusquedaFragment();
		movilesSegurosFragment = new MovilesSegurosFragment();
		mGoogleMapFragment = new GoogleMapFragment();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerList = (ListView) findViewById(R.id.listview_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mTitles = getResources().getStringArray(R.array.title_array);

		menuItemAdapter = new MenuItemAdapter(getApplicationContext(), mTitles,
				new int[] { R.drawable.ic_search, R.drawable.ic_secure,
						R.drawable.ic_map });

		mDrawerList.setAdapter(menuItemAdapter);

		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				selectItem(position);
			}
		});

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_navigation_drawer, R.string.drawer_open_label,
				R.string.drawer_close_label) {

			@Override
			public void onDrawerClosed(View drawerView) {

				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {

				getSupportActionBar().setTitle(mDrawerTitle);

				mBusquedaFragment.hideKeyboard();

				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		} else {
			mSelectedPosition = savedInstanceState.getInt("selected_position");
			selectItem(mSelectedPosition);
		}

		if (mGoogleMapFragment.isAdded()) {
			handleIntent(getIntent());
		}
	}

	@Override
	protected void onResume() {

		super.onResume();

		if (mUiLifecycleHelper != null) {
			mUiLifecycleHelper.onResume();
		}

		GpsHelper.getInstancia().start(getApplicationContext(),
				GpsHelper.GPS_NETWORK_PROVIDER);

		mBackCount = 1;
	}

	@Override
	protected void onPause() {

		super.onPause();

		if (mUiLifecycleHelper != null) {
			mUiLifecycleHelper.onPause();
		}

		GpsHelper.getInstancia().stop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (mUiLifecycleHelper == null || mGoogleApiClient == null) {
			return;
		}

		mUiLifecycleHelper.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constantes.REQUEST_CODE_GPLUS_SIGN_IN) {

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		if (mUiLifecycleHelper != null) {
			mUiLifecycleHelper.onDestroy();
		}
	}

	@Override
	protected void onStart() {

		super.onStart();

		if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}
	}

	@Override
	protected void onStop() {

		super.onStop();

		if (mGoogleApiClient != null) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);

		outState.putInt("selected_position", mSelectedPosition);
	}

	@Override
	public void onSearch(String text) {

		if (!TextUtils.isEmpty(text)) {

			TaskDialogFragment tdf = TaskDialogFragment
					.newInstance(BusquedaTask.ID);
			tdf.setMessage(getString(R.string.busqueda_message_buscando));

			BusquedaTask bt = new BusquedaTask(this);
			bt.setFragment(tdf);
			tdf.setTask(bt);

			tdf.show(getSupportFragmentManager(), TaskDialogFragment.TAG);

			bt.execute(text.toUpperCase(Locale.getDefault()).trim());

		} else {
			showToast(R.string.busqueda_message_warning, Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onTaskFinished(int taskId, String message, Object data) {

		switch (taskId) {
		case BusquedaTask.ID:
			Object[] params = (Object[]) data;
			boolean result = (Boolean) params[0];
			String placa = (String) params[1];
			if (result) {
				mBusquedaFragment.refreshBusquedas();

				Intent intent = new Intent(this,
						ResultadoBusquedaActivity.class);
				intent.putExtra("placa", placa);
				startActivity(intent);
			} else {
				Intent intent = new Intent(getApplicationContext(),
						MovilNoSeguroActivity.class);
				intent.putExtra("placa", placa);

				startActivity(intent);
			}

			break;
		case WaitLocationTask.ID:
			calcularTarifa();
			break;
		case RouteTask.ID:
			Route route = (Route) data;
			drawRoute(route);
			break;

		default:
			super.onTaskFinished(taskId, message, data);
		}

	}

	@Override
	public void onTaskError(int taskId, String message) {

		showToast(message, Toast.LENGTH_LONG);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, R.id.menu_settings, Menu.NONE,
				R.string.configuracion_activity_label).setShowAsAction(
				MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(Menu.NONE, R.id.menu_cerrar_session, Menu.NONE,
				R.string.app_cerrar_sesion_label).setShowAsAction(
				MenuItem.SHOW_AS_ACTION_NEVER);

		if (mSelectedPosition == MAPA_FRAGMENT) {
			mSearchView = CollapsibleSearchMenu.addSearchMenuItem(menu, false,
					getString(R.string.mapa_buscar_label));
			final View searchView = mSearchView.getActionView();
			final AutoCompleteTextView editText = (AutoCompleteTextView) searchView
					.findViewById(R.id.search_src_text);
			editText.setAdapter(new PlacesAutocompleteAdapter(
					getApplicationContext(), R.layout.list_item));
			editText.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					Place place = (Place) parent.getItemAtPosition(position);
					mGoogleMapFragment.addMarker(new LatLng(place
							.getPlaceDetails().getLatitud(), place
							.getPlaceDetails().getLongitud()));
				}
			});

			menu.add(Menu.NONE, R.id.action_menu_tarifa, Menu.NONE,
					R.string.mapa_tarifa_label)
					.setIcon(R.drawable.ic_action_tarifa)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
			return true;
		case R.id.menu_settings:
			Intent intent = new Intent(this, ConfiguracionActivity.class);
			startActivity(intent);

			return true;
		case R.id.menu_cerrar_session:
			logout();
			return true;
		case R.id.action_menu_tarifa:
			calcularTarifa();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onBackPressed() {

		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
			return;
		}

		if (mBackCount == BACK_COUNT) {
			eliminarDatosTemporales();

			super.onBackPressed();
		} else {
			mBackCount++;
			showToast(R.string.app_message_salir, Toast.LENGTH_SHORT);
		}
	}

	private void selectItem(int position) {

		mSelectedPosition = position;
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		switch (position) {
		case BUSQUEDA_FRAGMENT:
			ft.replace(R.id.content_frame, mBusquedaFragment);
			break;
		case MOVILES_SEGUROS_FRAGMENT:
			ft.replace(R.id.content_frame, movilesSegurosFragment);
			break;
		case MAPA_FRAGMENT:
			ft.replace(R.id.content_frame, mGoogleMapFragment);
			break;

		default:
			break;
		}

		ft.commit();
		mDrawerList.setItemChecked(position, true);

		setTitle(mTitles[position]);

		mDrawerLayout.closeDrawer(mDrawerList);

		supportInvalidateOptionsMenu();

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {

		super.onPostCreate(savedInstanceState);

		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void setTitle(CharSequence title) {

		mDrawerTitle = title;
		getSupportActionBar().setTitle(title);
	}

	@Override
	public void onError(String message) {

		showToast(message, Toast.LENGTH_SHORT);
	}

	private StatusCallback mCallback = new StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isClosed()) {
			mControlConfiguracion.clearPreferences();

			goToLogin();
		}
	}

	private ConnectionCallbacks mConnectionCallbacks = new ConnectionCallbacks() {

		@Override
		public void onConnected(Bundle connectionHint) {
			Log.i("Menu", "onConnected");
		}

		@Override
		public void onConnectionSuspended(int cause) {

		}
	};

	private OnConnectionFailedListener mOnConnectionFailedListener = new OnConnectionFailedListener() {

		@Override
		public void onConnectionFailed(ConnectionResult result) {

			if (!result.hasResolution()) {
				GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
						MenuActivity.this,
						Constantes.REQUEST_CODE_GPLUS_SIGN_IN).show();
			}

			if (!mIntentInProgress) {
				mConnectionResult = result;

				resolveSignInError();

			}
		}
	};

	private void resolveSignInError() {

		if (mConnectionResult != null && mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this,
						Constantes.REQUEST_CODE_GPLUS_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	private void logout() {

		int tipoUsuario = mControlConfiguracion.getTipoUsuario();

		if (tipoUsuario == Usuario.FACEBOOK) {
			Session session = Session.getActiveSession();
			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
			}
		} else if (tipoUsuario == Usuario.GOOGLE) {
			if (mGoogleApiClient.isConnected()) {
				Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
				mGoogleApiClient.disconnect();
				mGoogleApiClient.connect();

				mControlConfiguracion.clearPreferences();

				goToLogin();
			} else {
				resolveSignInError();
				showToast(R.string.login_error_message_google,
						Toast.LENGTH_SHORT);
			}
		} else {
			mControlConfiguracion.clearPreferences();
			goToLogin();
		}
	}

	private void goToLogin() {

		Intent intent = new Intent(this, LoginActivity.class);

		startActivity(intent);

		finish();
	}

	private void calcularTarifa() {

		Location location = GpsHelper.getInstancia().getLocation();
		if (location != null) {
			getRoute(location.getLatitude(), location.getLongitude());
		} else {
			// ## Esperamos para intentar la ubicacion del dispositivo
			TaskDialogFragment tdf = new TaskDialogFragment();
			tdf.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			tdf.setMessage(getString(R.string.mapa_message_obteniendo_ubicacion));
			tdf.setCancelable(false);

			WaitLocationTask wlt = new WaitLocationTask(this);
			wlt.setFragment(tdf);
			tdf.setTask(wlt);

			tdf.show(getSupportFragmentManager(), TaskDialogFragment.TAG);

			wlt.execute();
		}
	}

	private void getRoute(double latOrigen, double lngOrigen) {

		Marker marker = mGoogleMapFragment.getMarker();
		if (marker != null) {
			// ## Obtenemos las coordenadas de la marca adicionada por el
			// usuario
			LatLng latLngDestino = marker.getPosition();

			TaskDialogFragment tdf = new TaskDialogFragment();
			tdf.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			tdf.setMessage(getString(R.string.mapa_message_obteniendo_ruta));
			tdf.setCancelable(false);

			RouteTask rt = new RouteTask(this);
			rt.setFragment(tdf);
			tdf.setTask(rt);

			tdf.show(getSupportFragmentManager(), TaskDialogFragment.TAG);

			rt.execute(new Object[] { new LatLng(latOrigen, lngOrigen),
					latLngDestino, GMapDirection.MODE_DRIVING });
		} else {
			showToast(R.string.mapa_message_destino_no_especificado,
					Toast.LENGTH_LONG);
		}
	}

	private void drawRoute(Route route) {

		if (route != null) {
			mGoogleMapFragment.drawRoute(route);
		} else {
			showToast(R.string.mapa_message_ruta_no_disponible,
					Toast.LENGTH_LONG);
		}
	}

	@Override
	public void onInfoWindowClick() {

		Intent intent = new Intent(this, TarifaActivity.class);

		startActivity(intent);
	}

	private void eliminarDatosTemporales() {

		EliminarDatosTemporalesTask edtt = new EliminarDatosTemporalesTask(
				getApplicationContext());
		edtt.execute();
	}

	@Override
	public void onEmpresaSelected(String[] telefonos) {

		if (telefonos.length > 1) {
			mostrarDialogoTelefonos(telefonos);
		} else {
			realizarLlamada(telefonos[0]);
		}
	}

	private void mostrarDialogoTelefonos(final String[] telefonos) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.empresas_dialog_telefonos_title_label);
		builder.setItems(telefonos, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();

				String telefono = telefonos[which];

				realizarLlamada(telefono);
			}
		});
		builder.create().show();
	}

	private void realizarLlamada(String telefono) {

		try {
			String uri = "tel:" + telefono.trim();
			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse(uri));

			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			showToast(R.string.busqueda_error_message_activity_no_found,
					Toast.LENGTH_SHORT);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {

		super.onNewIntent(intent);

		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {

		if (mGoogleMapFragment.isAdded()) {
			if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
				mGoogleMapFragment.doSearch(intent
						.getStringExtra(SearchManager.QUERY));
			} else if (intent.getAction().equals(Intent.ACTION_VIEW)) {
				mGoogleMapFragment.doSearch(intent
						.getStringExtra(SearchManager.EXTRA_DATA_KEY));
			}
		}
	}
}
