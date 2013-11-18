package br.com.bradesco.lelis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;



public class TrendAdapter extends ArrayAdapter<Trend> {
	public TrendAdapter(Context context) {
		super(context, R.layout.trend_adapter);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Trend t = getItem(position);
		
		LinearLayout layout = null;
		
		if(convertView == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = (LinearLayout) li.inflate(R.layout.trend_adapter, null);
		} else {
			layout = (LinearLayout) convertView;
		}
		
		TextView trendname = (TextView)layout.findViewById(R.id.tv_trendname);
		TextView rule = (TextView)layout.findViewById(R.id.tv_rule);
		
		trendname.setText(t.trendname);
		rule.setText(t.rulesUrl);
		
		return layout;
	}
}
