package com.movilesseguros.ui.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.android.helper.OneClickListener;
import com.facebook.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.SignInButton;
import com.movilesseguros.R;

public class LoginRegisterDialogFragment extends SherlockDialogFragment {

	public static final String TAG = "LoginRegisterDialog";

	public static final int TIPO_LOGIN = 1;
	public static final int TIPO_REGISTER = 2;

	private Button mBtnLogin;
	private EditText mTxtCorreo;
	private EditText mTxtPassword;
	private TextView mTvRegistrate;
	private AlertDialog mAlertDialog;
	private LoginRegisterDialogListener mLoginRegisterDialogListener;
	private int mTipo;
	private AccountManager mAccountManager;
	// ## Facebook
	private LoginButton mBtnFacebook;
	// ## Google
	private SignInButton mBtnGoogle;

	public static LoginRegisterDialogFragment newInstance(int tipo) {

		LoginRegisterDialogFragment lrdf = new LoginRegisterDialogFragment();
		Bundle args = new Bundle();
		args.putInt("tipo", tipo);
		lrdf.setArguments(args);

		return lrdf;
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

		try {
			mLoginRegisterDialogListener = (LoginRegisterDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement LoginRegisterDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		mTipo = getArguments().getInt("tipo");

		View view = getActivity().getLayoutInflater().inflate(
				R.layout.login_resgister_dialog_layout, null);
		builder.setView(view);

		mBtnFacebook = (LoginButton) view.findViewById(R.id.btn_facebook);
		mBtnFacebook.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		mBtnFacebook.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				dismiss();

				mLoginRegisterDialogListener.onDialogFacebookLogin();

			}
		});

		mBtnGoogle = (SignInButton) view.findViewById(R.id.btn_google);
		mBtnGoogle.setOnClickListener(new OneClickListener() {

			@Override
			public void onOneClick(View v) {

				dismiss();

				mLoginRegisterDialogListener.onDialogGoogleLogin();
			}
		});
		setGooglePlusButtonText(mBtnGoogle);

		mTxtCorreo = (EditText) view.findViewById(R.id.txt_correo);
		mTxtPassword = (EditText) view.findViewById(R.id.txt_password);
		mTvRegistrate = (TextView) view.findViewById(R.id.tv_registrate);

		if (mTipo == TIPO_LOGIN) {
			mBtnLogin = (Button) view.findViewById(R.id.btn_login);
			mBtnLogin.setText(R.string.login_entrar_label);
			mTvRegistrate.setVisibility(View.GONE);
			view.findViewById(R.id.btn_registrate).setVisibility(View.GONE);

		} else if (mTipo == TIPO_REGISTER) {
			mBtnLogin = (Button) view.findViewById(R.id.btn_registrate);
			mBtnLogin.setText(R.string.login_registrate_label);
			view.findViewById(R.id.btn_login).setVisibility(View.GONE);
			mBtnLogin.setVisibility(View.VISIBLE);
		}

		mBtnLogin.setOnClickListener(new OneClickListener() {

			@Override
			public void onOneClick(View v) {
				if (TextUtils.isEmpty(mTxtCorreo.getText())) {
					Toast.makeText(getActivity(),
							R.string.login_message_warning_correo,
							Toast.LENGTH_SHORT).show();
					this.reset();
					return;
				}
				if (TextUtils.isEmpty(mTxtPassword.getText())) {
					Toast.makeText(getActivity(),
							R.string.login_message_warning_password,
							Toast.LENGTH_SHORT).show();
					this.reset();
					return;
				}
				if (!isValidEmail(mTxtCorreo.getText())) {
					Toast.makeText(getActivity(),
							R.string.login_message_warning_correo_incorrecto,
							Toast.LENGTH_SHORT).show();
					this.reset();
					return;
				} else {
					mLoginRegisterDialogListener.onDialogLoginClick(mTxtCorreo
							.getText().toString(), mTxtPassword.getText()
							.toString(), mTipo, this);

					mAlertDialog.dismiss();
				}

			}
		});

		mAlertDialog = builder.create();

		return mAlertDialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mTxtCorreo.setText(getAccount());
	}

	private String getAccount() {
		mAccountManager = AccountManager.get(getActivity());
		Account[] accounts = mAccountManager
				.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
		if (accounts.length > 0) {
			return accounts[0].name;
		} else {
			return null;
		}

	}

	private boolean isValidEmail(CharSequence target) {

		if (TextUtils.isEmpty(target)) {
			return false;
		}

		return Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}

	private void setGooglePlusButtonText(SignInButton signInButton) {

		for (int i = 0; i < signInButton.getChildCount(); i++) {
			View view = signInButton.getChildAt(i);
			if (view instanceof TextView) {
				TextView textView = (TextView) view;
				textView.setText(R.string.login_google_label);
			}
		}
	}

	public interface LoginRegisterDialogListener {

		public void onDialogFacebookLogin();

		public void onDialogGoogleLogin();

		public void onDialogLoginClick(String id, String password, int tipo,
				OneClickListener oneClickListener);

	}
}
