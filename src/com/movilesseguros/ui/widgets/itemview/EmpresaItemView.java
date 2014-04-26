package com.movilesseguros.ui.widgets.itemview;

import greendroid.widget.AsyncImageView;
import greendroid.widget.item.Item;
import greendroid.widget.itemview.ItemView;

import java.io.File;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movilesseguros.R;
import com.movilesseguros.helper.Constantes;
import com.movilesseguros.helper.ImageHelper;
import com.movilesseguros.negocio.task.ImagenTask;
import com.movilesseguros.negocio.task.ImagenTask.ImagenTaskListener;
import com.movilesseguros.ui.widgets.item.EmpresaItem;

public class EmpresaItemView extends RelativeLayout implements ItemView {

	private TextView mNombre;
	private TextView mTelefono;
	private AsyncImageView mAsyncImageView;
	private File mImageDirectory;

	public EmpresaItemView(Context context) {
		super(context, null, 0);
		mImageDirectory = ImageHelper.getImageDirectory(context);
	}

	public EmpresaItemView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		mImageDirectory = ImageHelper.getImageDirectory(context);
	}

	public EmpresaItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mImageDirectory = ImageHelper.getImageDirectory(context);
	}

	@Override
	public void prepareItemView() {

		mNombre = (TextView) findViewById(R.id.tv_nombre);
		mTelefono = (TextView) findViewById(R.id.tv_telefono);
		mAsyncImageView = (AsyncImageView) findViewById(R.id.gd_thumbnail);
	}

	@Override
	public void setObject(Item item) {

		final EmpresaItem empresaItem = (EmpresaItem) item;

		mNombre.setText(empresaItem.nombre);
		mTelefono
				.setText(!TextUtils.isEmpty(empresaItem.telefono) ? empresaItem.telefono
						: "");
		String url = empresaItem.drawableURL;
		if (!TextUtils.isEmpty(url)) {
			url = "file://" + mImageDirectory + File.separator + "img_" + url
					+ ".jpg";
			mAsyncImageView.setUrl(url);
		} else {
			ImagenTask it = new ImagenTask(getContext(), mAsyncImageView,
					new ImagenTaskListener() {

						@Override
						public void onLoadFinished(String name) {
							
							empresaItem.drawableURL = name;
						}

						@Override
						public void onLoadError(String message) {

						}
					});
			it.execute(new Object[] { empresaItem.id, Constantes.TIPO_EMPRESA,
					true });
		}

	}

}
