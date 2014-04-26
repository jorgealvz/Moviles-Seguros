package com.movilesseguros.ui;

import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.android.helper.fragment.AbstractFragmentActivity;
import com.movilesseguros.ui.fragment.ImagenFragment;
import com.movilesseguros.ui.fragment.ImagenFragment.ImagenListener;

public class ImagenActivity extends AbstractFragmentActivity implements
		ImagenListener {

	private ImagenFragment mImagenFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mImagenFragment = (ImagenFragment) getSupportFragmentManager()
				.findFragmentById(android.R.id.content);
		if (mImagenFragment == null) {
			mImagenFragment = ImagenFragment.newInstance(getIntent()
					.getIntExtra("id", 0), getIntent().getIntExtra("tipo", 0));
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, mImagenFragment).commit();
		}
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
	
	@Override
	public void onError(String message) {

		showToast(message, Toast.LENGTH_SHORT);
	}
}
