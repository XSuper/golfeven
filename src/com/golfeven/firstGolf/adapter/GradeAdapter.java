package com.golfeven.firstGolf.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.adapter.ScoreAdapter.ItemGrade;
import com.golfeven.firstGolf.base.MBaseAdapter;
import com.golfeven.firstGolf.bean.Grade;
import com.golfeven.firstGolf.bean.Score;

public class GradeAdapter extends MBaseAdapter{
	

	public GradeAdapter(Context context, List<Grade> datas) {
		this.context = context;
		this.datas = datas;
	}
	TextView name; 
	TextView time; 
	TextView totalputter; 
	TextView totalpole; 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_grade_totle, null);
			name = (TextView) convertView.findViewById(R.id.item_grade_totle_name);
			time = (TextView) convertView.findViewById(R.id.item_grade_totle_time);
			totalputter = (TextView) convertView.findViewById(R.id.item_grade_totle_totalputter);
			totalpole = (TextView) convertView.findViewById(R.id.item_grade_totle_totalpole);
			GradeView gradeView = new GradeView(name, time, totalpole, totalputter);
			convertView.setTag(gradeView);
		} else {
			GradeView gradeView = (GradeView) convertView.getTag();
			name = gradeView.name;
			time = gradeView.time;
			totalputter=gradeView.totalputter;
			totalpole = gradeView.totalpole;
		}
		Grade grade = (Grade)datas.get(position);
		if(grade!= null){
			name.setText(grade.getGname());
			time.setText(grade.getMarkdate().substring(0, 9));
			totalputter.setText(grade.getTotalputter());
			totalpole.setText(grade.getTotalpole());
		}
		
		return convertView;
	}
	
	/**
	 * 
	 * @author ISuper
	 *
	 */
	class GradeView {
		public  TextView name;
		public TextView time;
		public TextView totalpole;
		public TextView totalputter;
		public GradeView(TextView name, TextView time, TextView totalpole,
				TextView totalputter) {
			super();
			this.name = name;
			this.time = time;
			this.totalpole = totalpole;
			this.totalputter = totalputter;
		}
		
	}
	

}
