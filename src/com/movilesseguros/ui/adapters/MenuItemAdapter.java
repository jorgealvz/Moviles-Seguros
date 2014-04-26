package com.movilesseguros.ui.adapters;

import com.movilesseguros.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenuItemAdapter extends BaseAdapter {

	private String[] mTitles;
	private int[] mIcons;
	private Context mContext;

	public MenuItemAdapter(Context context, String[] titles, int[] icons) {
		mContext = context;
		mTitles = titles;
		mIcons = icons;
	}

	static class ViewHolder {
		TextView tvTitle;
	}

	@Override
	public int getCount() {
		return mTitles.length;
	}

	@Override
	public Object getItem(int position) {
		return mTitles[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.menu_item_view_layout, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
			view.setTag(viewHolder);
		}

		ViewHolder viewHolder = (ViewHolder) view.getTag();

		viewHolder.tvTitle.setText(mTitles[position]);
		viewHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(
				mIcons[position], 0, 0, 0);

		return view;
	}

}
