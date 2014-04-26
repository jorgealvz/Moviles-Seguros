package com.movilesseguros.ui;

import android.os.Bundle;

import com.actionbarsherlock.view.MenuItem;
import com.android.helper.fragment.AbstractFragmentActivity;
import com.movilesseguros.ui.fragment.TarifaFragment;

public class TarifaActivity extends AbstractFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		TarifaFragment tf = (TarifaFragment) getSupportFragmentManager()
				.findFragmentById(android.R.id.content);

		if (tf == null) {
			tf = new TarifaFragment();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, tf).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
