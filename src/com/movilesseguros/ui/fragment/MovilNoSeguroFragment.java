package com.movilesseguros.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.android.helper.OneClickListener;
import com.movilesseguros.R;

public class MovilNoSeguroFragment extends SherlockFragment {

	public static final String TAG = "movil_no_seguro";

	private MovilNoSeguroListener movilNoSeguroListener;

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

		try {
			movilNoSeguroListener = (MovilNoSeguroListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement MovilNoSeguroListener.");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.movil_no_seguro_layout, null);

		Button btnReportar = (Button) view.findViewById(R.id.btn_reportar);
		btnReportar.setOnClickListener(new OneClickListener() {

			@Override
			public void onOneClick(View v) {

				movilNoSeguroListener.onReportarClick(this);
			}
		});

		return view;
	}

	public interface MovilNoSeguroListener {

		public void onReportarClick(OneClickListener oneClickListener);
	}

}
