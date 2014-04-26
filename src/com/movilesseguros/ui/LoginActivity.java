package com.movilesseguros.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.helper.OneClickListener;
import com.android.helper.fragment.AbstractFragmentActivity;
import com.android.helper.fragment.TaskDialogFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.movilesseguros.R;
import com.movilesseguros.entidades.Usuario;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.negocio.ControlConfiguracion;
import com.movilesseguros.negocio.task.LoginTask;
import com.movilesseguros.negocio.task.RegistrarUsuarioTask;
import com.movilesseguros.negocio.task.VerificarUsuarioTask;
import com.movilesseguros.ui.fragment.LoginFragment;
import com.movilesseguros.ui.fragment.LoginFragment.LoginListener;
import com.movilesseguros.ui.fragment.LoginRegisterDialogFragment;
import com.movilesseguros.ui.fragment.LoginRegisterDialogFragment.LoginRegisterDialogListener;

public class LoginActivity extends AbstractFragmentActivity implements
		LoginListener, LoginRegisterDialogListener {

	private LoginFragment mLoginFragment;
	private String mId;
	private String mPassword;
	private String mCorreo;
	private int mTipoUsuario;
	private ControlConfiguracion mControlConfiguracion;
	private boolean mRegistrar;
	private boolean mLogin;
	private boolean mVerificar;
	private boolean mRegistrarDialog;
	// ## Google+
	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress;
	private ConnectionResult mConnectionResult;
	private boolean mSignInClicked;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mControlConfiguracion = new ControlConfiguracion(
				getApplicationContext());

		mLoginFragment = (LoginFragment) getSupportFragmentManager()
				.findFragmentById(android.R.id.content);
		if (mLoginFragment == null) {
			mLoginFragment = new LoginFragment();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, mLoginFragment).commit();
		}

		if (savedInstanceState != null) {
			mId = savedInstanceState.getString("id");
			mPassword = savedInstanceState.getString("password");
			mCorreo = savedInstanceState.getString("correo");
			mTipoUsuario = savedInstanceState.getInt("tipo_usuario",
					Usuario.PROPIO);
			mSignInClicked = savedInstanceState.getBoolean("sign_in_clicked");
			mRegistrar = savedInstanceState.getBoolean("registrar");
			mLogin = savedInstanceState.getBoolean("login");
			mVerificar = savedInstanceState.getBoolean("verificar");
			mRegistrarDialog = savedInstanceState
					.getBoolean("registrar_dialog");
		}

		mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
				.addConnectionCallbacks(mConnectionCallbacks)
				.addOnConnectionFailedListener(mOnConnectionFailedListener)
				.addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constantes.REQUEST_CODE_GPLUS_SIGN_IN) {
			if (resultCode != Activity.RESULT_OK) {
				mSignInClicked = false;
				if (mProgressDialog != null && mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}

		mLoginFragment.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart() {

		super.onStart();

		if (mSignInClicked) {
			mGoogleApiClient.connect();
		}
	}

	@Override
	protected void onStop() {

		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	protected void onPostResume() {

		super.onPostResume();

		if (mRegistrar) {
			registrarUsuario();
		}

		if (mVerificar) {
			verificarUsuario();
		}

		if (mLogin) {
			iniciarSesion();
		}

		if (mRegistrarDialog) {
			showDialogRegistro();
		}
	}

	@Override
	public void onEntrarClick(OneClickListener oneClickListener) {

		LoginRegisterDialogFragment lrdf = LoginRegisterDialogFragment
				.newInstance(LoginRegisterDialogFragment.TIPO_LOGIN);
		lrdf.show(getSupportFragmentManager(), LoginRegisterDialogFragment.TAG);

		oneClickListener.reset();
	}

	@Override
	public void onFacebookLogin(String id, String password, String correo) {

		mId = id;
		mPassword = password;
		mCorreo = correo;
		mTipoUsuario = Usuario.FACEBOOK;

		if (mControlConfiguracion.getUsuario() == null) {
			verificarUsuario();
		}

	}

	@Override
	public void onGoogleLogin() {

		signInGooglePlus();

	}

	@Override
	public void onRegistrateClick(OneClickListener oneClickListener) {

		LoginRegisterDialogFragment lrdf = LoginRegisterDialogFragment
				.newInstance(LoginRegisterDialogFragment.TIPO_REGISTER);
		lrdf.show(getSupportFragmentManager(), LoginRegisterDialogFragment.TAG);

		oneClickListener.reset();
	}

	@Override
	public void onDialogLoginClick(String id, String password, int tipo,
			OneClickListener oneClickListener) {

		mId = id;
		mPassword = password;
		mCorreo = id;
		mTipoUsuario = Usuario.PROPIO;

		if (tipo == LoginRegisterDialogFragment.TIPO_LOGIN) {

			iniciarSesion();

		} else if (tipo == LoginRegisterDialogFragment.TIPO_REGISTER) {

			verificarUsuario();
		}

		oneClickListener.reset();
	}

	@Override
	public void onDialogFacebookLogin() {

		mLoginFragment.socialButtonClick(Usuario.FACEBOOK);

	}

	@Override
	public void onDialogGoogleLogin() {

		signInGooglePlus();
	}

	@Override
	public void onTaskFinished(int taskId, String message, Object data) {

		Intent intent = null;

		switch (taskId) {
		case LoginTask.ID:
			intent = new Intent(this, MenuActivity.class);

			startActivity(intent);

			finish();

			break;
		case RegistrarUsuarioTask.ID:
			intent = new Intent(this, MenuActivity.class);

			startActivity(intent);

			finish();
			break;
		case VerificarUsuarioTask.ID:
			registrarUsuario();
			break;

		default:
			super.onTaskFinished(taskId, message, data);
		}

	}

	@Override
	public void onTaskError(int taskId, String message) {

		switch (taskId) {
		case VerificarUsuarioTask.ID:
			if (mTipoUsuario == Usuario.PROPIO) {
				if (mIsActivityForeground) {
					showDialogRegistro();
				} else {
					mRegistrar = true;
				}
				showToast(message, Toast.LENGTH_SHORT);
			} else {
				iniciarSesion();
			}

			break;

		default:
			showToast(message, Toast.LENGTH_SHORT);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, R.id.menu_settings, Menu.NONE,
				R.string.configuracion_activity_label).setShowAsAction(
				MenuItem.SHOW_AS_ACTION_NEVER);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(this, ConfiguracionActivity.class);
			startActivity(intent);

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);

		outState.putString("id", mId);
		outState.putString("password", mPassword);
		outState.putString("correo", mCorreo);
		outState.putInt("tipo_usuario", mTipoUsuario);
		outState.putBoolean("sign_in_clicked", mSignInClicked);
		outState.putBoolean("registrar", mRegistrar);
		outState.putBoolean("login", mLogin);
		outState.putBoolean("verificar", mVerificar);
		outState.putBoolean("registrar_dialog", mRegistrarDialog);
	}

	private void showDialogRegistro() {

		LoginRegisterDialogFragment lrdf = LoginRegisterDialogFragment
				.newInstance(LoginRegisterDialogFragment.TIPO_REGISTER);
		lrdf.show(getSupportFragmentManager(), LoginRegisterDialogFragment.TAG);
	}

	private void verificarUsuario() {

		Log.i("LoginActivity", "verificarUsuario");

		if (mIsActivityForeground) {
			TaskDialogFragment tdf = TaskDialogFragment
					.newInstance(VerificarUsuarioTask.ID);
			tdf.setCancelable(false);
			if (mTipoUsuario == Usuario.PROPIO) {
				tdf.setMessage(getString(R.string.login_message_iniciando_sesion));
			} else {
				tdf.setMessage(getString(R.string.login_message_conectando));
			}

			VerificarUsuarioTask vut = new VerificarUsuarioTask(this);
			vut.setFragment(tdf);
			tdf.setTask(vut);

			tdf.show(getSupportFragmentManager(), TaskDialogFragment.TAG);

			vut.execute(mId);
		} else {
			mVerificar = true;
		}
	}

	private void registrarUsuario() {

		if (mIsActivityForeground) {
			TaskDialogFragment tdf = TaskDialogFragment
					.newInstance(RegistrarUsuarioTask.ID);
			tdf.setCancelable(false);
			if (mTipoUsuario == Usuario.PROPIO) {
				tdf.setMessage(getString(R.string.login_message_iniciando_sesion));
			} else {
				tdf.setMessage(getString(R.string.login_message_conectando));
			}

			RegistrarUsuarioTask rut = new RegistrarUsuarioTask(this);
			rut.setFragment(tdf);
			tdf.setTask(rut);

			tdf.show(getSupportFragmentManager(), TaskDialogFragment.TAG);

			rut.execute(new Object[] { mId, mPassword, mCorreo, mTipoUsuario });
		} else {
			mRegistrar = true;
		}
	}

	private void iniciarSesion() {

		if (mIsActivityForeground) {
			TaskDialogFragment tdf = TaskDialogFragment
					.newInstance(LoginTask.ID);
			if (mTipoUsuario == Usuario.PROPIO) {
				tdf.setMessage(getString(R.string.login_message_iniciando_sesion));
			} else {
				tdf.setMessage(getString(R.string.login_message_conectando));
			}
			LoginTask lt = new LoginTask(this);
			lt.setFragment(tdf);
			tdf.setTask(lt);

			tdf.show(getSupportFragmentManager(), TaskDialogFragment.TAG);

			lt.execute(new Object[] { mId, mPassword, mTipoUsuario });
		} else {
			mLogin = true;
		}
	}

	@Override
	public void onError(String message) {

		showToast(message, Toast.LENGTH_SHORT);
	}

	private void signInGooglePlus() {

		if (!mGoogleApiClient.isConnected()) {
			mSignInClicked = true;
			mProgressDialog = ProgressDialog.show(this, null,
					getString(R.string.login_message_conectando), true, false);

			if (mConnectionResult != null) {
				resolveSignInError();
			} else {
				mGoogleApiClient.connect();
			}
		}
	}

	private ConnectionCallbacks mConnectionCallbacks = new ConnectionCallbacks() {

		@Override
		public void onConnected(Bundle connectionHint) {

			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}

			if (mSignInClicked) {
				getProfileInformation();
			}

			mSignInClicked = false;
		}

		@Override
		public void onConnectionSuspended(int cause) {
			mGoogleApiClient.connect();
		}

	};

	private OnConnectionFailedListener mOnConnectionFailedListener = new OnConnectionFailedListener() {

		@Override
		public void onConnectionFailed(ConnectionResult result) {

			if (!result.hasResolution()) {
				GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
						LoginActivity.this,
						Constantes.REQUEST_CODE_GPLUS_SIGN_IN).show();
			}

			if (!mIntentInProgress) {
				mConnectionResult = result;

				if (mSignInClicked) {
					resolveSignInError();
				}
			}
		}
	};

	private void resolveSignInError() {

		if (mConnectionResult.hasResolution()) {
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

	private void getProfileInformation() {

		try {
			Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
			if (person != null) {
				String id = person.getId();
				String pass = person.getName().getGivenName() + id;
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

				Log.i("Login", "Id: " + id + " Pass: " + pass + " Email: "
						+ email);

				mId = id;
				mPassword = pass;
				mCorreo = email;
				mTipoUsuario = Usuario.GOOGLE;

				if (mControlConfiguracion.getUsuario() == null) {
					verificarUsuario();
				}
			} else {
				onError(getString(R.string.login_error_message_google));
			}
		} catch (Exception e) {
			Log.e("Login", "", e);
			onError(getString(R.string.login_error_message_login));
		}
	}

}
