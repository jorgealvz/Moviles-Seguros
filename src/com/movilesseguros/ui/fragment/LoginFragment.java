package com.movilesseguros.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.android.helper.OneClickListener;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.movilesseguros.R;
import com.movilesseguros.entidades.Usuario;

public class LoginFragment extends SherlockFragment {

	private Button mBtnEntrar;
	private Button mBtnRegistrate;
	private LoginListener mLoginListener;
	// ## Facebook
	private LoginButton mBtnFacebook;
	private UiLifecycleHelper mUiLifecycleHelper;
	private Session mSession;
	// ## Google
	private SignInButton mBtnGoogle;

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

		try {
			mLoginListener = (LoginListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement LoginListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.login_layout, null);

		mBtnEntrar = (Button) view.findViewById(R.id.btn_entrar);
		mBtnEntrar.setOnClickListener(new OneClickListener() {

			@Override
			public void onOneClick(View v) {
				mLoginListener.onEntrarClick(this);
			}
		});
		mBtnFacebook = (LoginButton) view.findViewById(R.id.btn_facebook);
		mBtnFacebook.setReadPermissions("email");
		mBtnFacebook.setFragment(this);

		mBtnGoogle = (SignInButton) view.findViewById(R.id.btn_google);
		mBtnGoogle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mLoginListener.onGoogleLogin();
			}
		});
		setGooglePlusButtonText(mBtnGoogle);

		mBtnRegistrate = (Button) view.findViewById(R.id.btn_registrate);
		mBtnRegistrate.setOnClickListener(new OneClickListener() {

			@Override
			public void onOneClick(View v) {
				mLoginListener.onRegistrateClick(this);
			}
		});

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mUiLifecycleHelper = new UiLifecycleHelper(getActivity(), mCallback);
		mUiLifecycleHelper.onCreate(savedInstanceState);

	}

	@Override
	public void onResume() {

		super.onResume();

		mUiLifecycleHelper.onResume();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		mUiLifecycleHelper.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void onPause() {

		super.onPause();

		mUiLifecycleHelper.onPause();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();

		mUiLifecycleHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);

		mUiLifecycleHelper.onSaveInstanceState(outState);

	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			if (mSession == null || mSession.getState() != session.getState()) {
				mSession = session;
				Request request = Request.newMeRequest(session,
						new Request.GraphUserCallback() {

							@Override
							public void onCompleted(GraphUser user,
									Response response) {

								if (user != null) {
									String id = user.getId();
									String pass = user.getFirstName()
											+ user.getId();
									String email = (String) user
											.getProperty("email");

									Log.i("Login",
											user.getFirstName()
													+ user.getName()
													+ user.getId());
									Log.i("Login", email != null ? email
											: "no email");

									mLoginListener.onFacebookLogin(id, pass,
											email);

								}
							}
						});

				request.executeAsync();
			}
		}
	}

	private Session.StatusCallback mCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			onSessionStateChange(session, state, exception);
		}
	};

	private void setGooglePlusButtonText(SignInButton signInButton) {

		for (int i = 0; i < signInButton.getChildCount(); i++) {
			View view = signInButton.getChildAt(i);
			if (view instanceof TextView) {
				TextView textView = (TextView) view;
				textView.setText(R.string.login_google_label);
				textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			}
		}
	}

	public void socialButtonClick(int tipo) {

		switch (tipo) {
		case Usuario.FACEBOOK:
			mBtnFacebook.performClick();
			break;

		default:
			break;
		}
	}

	public interface LoginListener {

		public void onEntrarClick(OneClickListener oneClickListener);

		public void onFacebookLogin(String id, String password, String correo);

		public void onGoogleLogin();

		public void onRegistrateClick(OneClickListener oneClickListener);

		public void onError(String message);
	}
}
