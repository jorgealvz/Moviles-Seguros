package com.movilesseguros.ui;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.android.helper.TabsAdapter;
import com.android.helper.fragment.AbstractFragmentActivity;
import com.movilesseguros.R;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.ui.fragment.ResultadoBusquedaFragment;
import com.movilesseguros.ui.fragment.ResultadoBusquedaFragment.ResultadoBusquedaListener;

public class ResultadoBusquedaActivity extends AbstractFragmentActivity
		implements ResultadoBusquedaListener {

	private TabHost mTabHost;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.resultado_busqueda_layout);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mViewPager = (ViewPager) findViewById(R.id.pager);

		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

		addTab(getString(R.string.busqueda_empresa_tag),
				Constantes.TIPO_EMPRESA);
		addTab(getString(R.string.busqueda_conductor_tag),
				Constantes.TIPO_CONDUCTOR);
		addTab(getString(R.string.busqueda_vehiculo_tag),
				Constantes.TIPO_VEHICULO);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void addTab(String tag, int tipo) {

		Bundle args = new Bundle();
		args.putInt("tipo", tipo);
		args.putString("placa", getIntent().getStringExtra("placa"));

		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.apptheme_tab_indicator_holo, mTabHost.getTabWidget(),
				false);
		TextView title = (TextView) tabIndicator
				.findViewById(android.R.id.title);
		title.setText(tag);

		mTabsAdapter.addTab(
				mTabHost.newTabSpec(tag).setIndicator(tabIndicator),
				ResultadoBusquedaFragment.class, args);

	}

	@Override
	public void onError(String message) {

		showToast(message, Toast.LENGTH_SHORT);
	}

	@Override
	public void onItemImageSelected(int id, int tipo) {
		Intent intent = new Intent(this, ImagenActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("tipo", tipo);

		startActivity(intent);
	}

	@Override
	public void onItemWebSelected(String url) {

		try {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
		} catch (ActivityNotFoundException e) {
			showToast(R.string.busqueda_error_message_activity_no_found,
					Toast.LENGTH_SHORT);
		}

	}

	@Override
	public void onItemPhoneSelected(String[] phones) {

		if (phones.length > 1) {
			mostrarDialogoTelefonos(phones);
		} else {
			realizarLlamada(phones[0]);
		}

	}

	@Override
	public void onItemEmailSelected(String email) {

		try {
			Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
					"mailto", email, null));

			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			showToast(R.string.busqueda_error_message_activity_no_found,
					Toast.LENGTH_SHORT);
		}

	}

	@Override
	public void onItemDirectionSelected(double latitud, double longitud,
			String label) {

		try {
			final String uri = "http://maps.google.com/maps?q=" + latitud + ','
					+ longitud + "(" + label + ")&z=15";
			// String uri = "geo:" + latitud + "," + longitud;
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(uri));

			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			showToast(R.string.busqueda_error_message_activity_no_found,
					Toast.LENGTH_SHORT);
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

}
