package com.movilesseguros.ui.widgets.item;

import com.movilesseguros.R;

import android.content.Context;
import android.view.ViewGroup;
import greendroid.widget.item.Item;
import greendroid.widget.itemview.ItemView;

public class EmpresaItem extends Item {

	public int id;
	public String nombre;
	public String telefono;
	public String drawableURL;

	@Override
	public ItemView newView(Context context, ViewGroup parent) {

		return createCellFromXml(context, R.layout.empresa_item_view_layout,
				parent);
	}

}
