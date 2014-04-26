package com.movilesseguros;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.helper.fragment.AbstractFragmentActivity;
import com.android.helper.fragment.BackgroundTaskFragment;
import com.movilesseguros.negocio.task.SplashTask;
import com.movilesseguros.ui.MenuActivity;
import com.movilesseguros.ui.LoginActivity;

public class MainActivity extends AbstractFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_layout);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		BackgroundTaskFragment btf = new BackgroundTaskFragment();
		SplashTask st = new SplashTask(this);
		st.setFragment(btf);
		btf.setTask(st);

		getSupportFragmentManager().beginTransaction()
				.add(btf, BackgroundTaskFragment.TAG).commit();

		st.execute();

	}

	@Override
	public void onTaskFinished(int taskId, String message, Object data) {

		Intent intent = new Intent();

		if (data != null) {
			intent.setClass(this, MenuActivity.class);
		} else {
			intent.setClass(this, LoginActivity.class);
		}

		startActivity(intent);

		finish();
	}

	@Override
	public void onTaskError(int taskId, String message) {

		showToast(message, Toast.LENGTH_SHORT);

		Intent intent = new Intent(this, LoginActivity.class);

		startActivity(intent);

		finish();
	}

}
