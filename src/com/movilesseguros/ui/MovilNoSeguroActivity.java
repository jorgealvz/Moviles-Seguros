package com.movilesseguros.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.helper.OneClickListener;
import com.android.helper.fragment.AbstractFragmentActivity;
import com.movilesseguros.R;
import com.movilesseguros.negocio.ControlConfiguracion;
import com.movilesseguros.ui.fragment.MovilNoSeguroFragment;
import com.movilesseguros.ui.fragment.MovilNoSeguroFragment.MovilNoSeguroListener;

public class MovilNoSeguroActivity extends AbstractFragmentActivity implements
		MovilNoSeguroListener {

	private ControlConfiguracion mControlConfiguracion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mControlConfiguracion = new ControlConfiguracion(
				getApplicationContext());

		MovilNoSeguroFragment mnsf = (MovilNoSeguroFragment) getSupportFragmentManager()
				.findFragmentById(android.R.id.content);
		if (mnsf == null) {
			mnsf = new MovilNoSeguroFragment();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, mnsf).commit();
		}
	}

	@Override
	public void onReportarClick(OneClickListener oneClickListener) {

		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL,
				new String[] { getString(R.string.app_correo_reporte) });
		i.putExtra(
				Intent.EXTRA_SUBJECT,
				getString(R.string.app_asunto_correo_reporte, getIntent()
						.getStringExtra("placa")));
		i.putExtra(Intent.EXTRA_TEXT, getContenidoEmail());
		try {
			startActivity(Intent.createChooser(i,
					getString(R.string.app_enviar_correo_label)));
		} catch (android.content.ActivityNotFoundException ex) {
			showToast(R.string.app_message_cliente_correo_no_instalado,
					Toast.LENGTH_SHORT);
		}

		oneClickListener.reset();
	}

	private String getContenidoEmail() {

		StringBuilder builder = new StringBuilder();
		builder.append("====================");
		builder.append("\n");
		builder.append("User: " + mControlConfiguracion.getUsuario());
		builder.append("\n");
		builder.append("====================");
		builder.append("\n");
		builder.append("\n");

		return builder.toString();
	}
}
