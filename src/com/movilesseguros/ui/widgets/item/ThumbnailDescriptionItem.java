package com.movilesseguros.ui.widgets.item;

import greendroid.widget.item.Item;
import greendroid.widget.itemview.ItemView;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.movilesseguros.R;

public class ThumbnailDescriptionItem extends Item {

	public static final int TYPE_PHONE = 1;
	public static final int TYPE_EMAIL = 2;
	public static final int TYPE_IMAGE = 3;
	public static final int TYPE_DIRECTION = 4;
	public static final int TYPE_WEB = 5;

	public String text;

	/**
	 * The resource ID for the Drawable.
	 */
	public int drawableId;

	/**
	 * An optional URL that may be used to retrieve an image
	 */
	public String drawableURL;

	public int type;

	public double latitud;

	public double longitud;
	
	public int tipoImagen;
	
	public int id;

	/**
	 * @hide
	 */
	public ThumbnailDescriptionItem() {
	}

	/**
	 * Create a new ThumbnailItem.
	 * 
	 * @param text
	 *            The text to draw
	 * @param drawableId
	 *            The resource identifier to the Drawable
	 */
	public ThumbnailDescriptionItem(String text, int drawableId) {
		this(text, drawableId, null);
	}

	/**
	 * Create a new ThumbnailItem which will asynchronously load the image at
	 * the given URL.
	 * 
	 * @param text
	 *            The text to draw
	 * @param subtitle
	 *            The subtitle to use
	 * @param drawableId
	 *            The default image used when loading the image at the given
	 *            <em>drawableURL</em>
	 * @param drawableURL
	 *            The URL pointing to the image to load.
	 */
	public ThumbnailDescriptionItem(String text, int drawableId,
			String drawableURL) {
		super();
		this.drawableId = drawableId;
		this.drawableURL = drawableURL;
	}

	@Override
	public ItemView newView(Context context, ViewGroup parent) {
		return createCellFromXml(context,
				R.layout.thumbnail_description_item_view_layout, parent);
	}

	@Override
	public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs)
			throws XmlPullParserException, IOException {
		super.inflate(r, parser, attrs);

		TypedArray a = r.obtainAttributes(attrs, R.styleable.ThumbnailItem);
		drawableId = a.getResourceId(R.styleable.ThumbnailItem_thumbnail,
				drawableId);		
		drawableURL = a.getString(R.styleable.ThumbnailItem_thumbnailURL);
		
		a.recycle();
	}

}
