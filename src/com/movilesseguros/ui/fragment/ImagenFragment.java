package com.movilesseguros.ui.fragment;

import greendroid.widget.AsyncImageView;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragment;
import com.movilesseguros.R;
import com.movilesseguros.negocio.task.ImagenTask;
import com.movilesseguros.negocio.task.ImagenTask.ImagenTaskListener;

public class ImagenFragment extends SherlockFragment implements
		ImagenTaskListener {

	private AsyncImageView mAsyncView;
	private ProgressBar mProgressBar;
	private int mId;
	private int mTipo;
	private ImagenListener mImagenListener;

	public static ImagenFragment newInstance(int id, int tipo) {

		ImagenFragment imgf = new ImagenFragment();
		Bundle args = new Bundle();
		args.putInt("id", id);
		args.putInt("tipo", tipo);

		imgf.setArguments(args);

		return imgf;
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

		try {
			mImagenListener = (ImagenListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ImagenListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.image_layout, null);
		mAsyncView = (AsyncImageView) view.findViewById(R.id.gd_thumbnail);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progress);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		mId = getArguments().getInt("id");
		mTipo = getArguments().getInt("tipo");

		ImagenTask it = new ImagenTask(getActivity(), mAsyncView, this);
		it.execute(new Object[] { mId, mTipo, false });

	}

	public interface ImagenListener {

		public void onError(String message);
	}

	@Override
	public void onLoadFinished(String name) {

		mProgressBar.setVisibility(View.GONE);
		mAsyncView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoadError(String message) {

		mImagenListener.onError(message);
	}
}
