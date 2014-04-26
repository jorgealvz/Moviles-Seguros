package com.movilesseguros.ui.widgets.itemview;

import greendroid.widget.AsyncImageView;
import greendroid.widget.item.Item;
import greendroid.widget.itemview.ItemView;

import java.io.File;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyrilmottier.android.greendroid.R;
import com.movilesseguros.helper.ImageHelper;
import com.movilesseguros.negocio.task.ImagenTask;
import com.movilesseguros.negocio.task.ImagenTask.ImagenTaskListener;
import com.movilesseguros.ui.widgets.item.ThumbnailDescriptionItem;

public class ThumbnailDescriptionItemView extends RelativeLayout implements
		ItemView {

	private TextView mTextView;
	private AsyncImageView mThumbnailView;
	private File mImageDirectory;

	public ThumbnailDescriptionItemView(Context context) {
		this(context, null);
		mImageDirectory = ImageHelper.getImageDirectory(context);
	}

	public ThumbnailDescriptionItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mImageDirectory = ImageHelper.getImageDirectory(context);
	}

	public ThumbnailDescriptionItemView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mImageDirectory = ImageHelper.getImageDirectory(context);
	}

	@Override
	public void prepareItemView() {
		mTextView = (TextView) findViewById(R.id.gd_text);
		mThumbnailView = (AsyncImageView) findViewById(R.id.gd_thumbnail);
	}

	@Override
	public void setObject(Item object) {

		final ThumbnailDescriptionItem item = (ThumbnailDescriptionItem) object;
		mTextView.setText(item.text);
		mThumbnailView.setDefaultImageResource(item.drawableId);
		String url = item.drawableURL;
		if (!TextUtils.isEmpty(url)) {
			url = "file://" + mImageDirectory + File.separator + "img_"
					+ item.drawableURL + ".jpg";
			mThumbnailView.setUrl(url);			
		} else if (item.type == ThumbnailDescriptionItem.TYPE_IMAGE) {
			ImagenTask it = new ImagenTask(getContext(), mThumbnailView,
					new ImagenTaskListener() {

						@Override
						public void onLoadFinished(String name) {
							item.enabled = true;

						}

						@Override
						public void onLoadError(String message) {
							item.enabled = false;

						}
					});
			it.execute(new Object[] { item.id, item.tipoImagen, true });
		}

		if (item.drawableId == 0 && TextUtils.isEmpty(item.drawableURL)) {
			mThumbnailView.setVisibility(View.GONE);
		} else {
			mThumbnailView.setVisibility(View.VISIBLE);
		}

	}

}
