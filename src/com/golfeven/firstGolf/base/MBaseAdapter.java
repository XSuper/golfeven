package com.golfeven.firstGolf.base;

import java.util.List;

import com.golfeven.AppContext;
import com.golfeven.firstGolf.bean.GolfInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MBaseAdapter extends BaseAdapter {
	public Context context;
	public List datas;

	
	public AppContext getAppContext(){
		return (AppContext)context.getApplicationContext();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent) ;
	
	/**
	 * 刷新列表
	 * @param mdata
	 */
	public void refresh(List mdata) {
		this.datas = mdata;
		notifyDataSetChanged();
	}

}
