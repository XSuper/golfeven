package com.golfeven.firstGolf.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.base.MBaseAdapter;
import com.golfeven.firstGolf.bean.Score;

public class ScoreAdapter extends MBaseAdapter {
	AlertDialog.Builder builder;

	public ScoreAdapter(Context context, List<Score> datas) {
		this.context = context;
		this.datas = datas;
	}

	TextView id = null;
	TextView score = null;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_grade, null);
			id = (TextView) convertView.findViewById(R.id.item_grade_id);
			score = (TextView) convertView.findViewById(R.id.item_grade_score);
			ItemGrade grade = new ItemGrade(id, score);
			convertView.setTag(grade);
		} else {
			ItemGrade grade = (ItemGrade) convertView.getTag();
			id = grade.id;
			score = grade.score;
		}
		final Score mscore = (Score) datas.get(position);
		id.setText(mscore.getId() + "");
		score.setText(mscore.getTotleCount() + "||" + mscore.getTuiCount());

		score.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				builder = null;
				System.gc();
				totleCount = 4;
				tuiCount = 3;
				builder = new AlertDialog.Builder(context);
				builder.setIcon(android.R.drawable.ic_dialog_info);
				View view = getView();
				builder.setView(view);
				builder.setTitle("请选择");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								mscore.setTotleCount(totleCount + "");
								mscore.setTuiCount(tuiCount + "");
								score.setText(mscore.getTotleCount() + "||"
										+ mscore.getTuiCount());
								refresh(datas);

							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder.show();

			}
		});
		return convertView;
	}

	/**
	 * 保存view
	 * 
	 * @author ISuper
	 * 
	 */
	class ItemGrade {
		TextView id;
		TextView score;

		public ItemGrade(TextView id, TextView score) {
			super();
			this.id = id;
			this.score = score;
		}

	}

	int totleCount = 4;
	int tuiCount = 3;

	public View getView() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_playcount, null);
		final View totleUp = (View) view
				.findViewById(R.id.dialog_playcount_totlecount_add);
		final View totleDown = (View) view
				.findViewById(R.id.dialog_playcount_totlecount_subtract);
		final View tuiUp = (View) view
				.findViewById(R.id.dialog_playcount_tuicount_add);
		final View tuiDown = (View) view
				.findViewById(R.id.dialog_playcount_tuicount_subtract);
		final TextView totle = (TextView) view
				.findViewById(R.id.dialog_playcount_totlecount);
		final TextView tui = (TextView) view
				.findViewById(R.id.dialog_playcount_tuicount);

		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == totleUp) {
					++totleCount;
				} else if (v == totleDown) {
					if (totleCount <= 1) {
						return;
					}
					--totleCount;
					if (tuiCount >= totleCount) {
						tuiCount = totleCount - 1;
					}
				} else if (v == tuiUp) {
					if (tuiCount + 1 < totleCount) {
						++tuiCount;
					}

				} else if (v == tuiDown) {
					if (tuiCount >= 1) {
						--tuiCount;
					}

				}
				totle.setText("" + totleCount);
				tui.setText("" + tuiCount);

			}
		};
		totleUp.setOnClickListener(listener);
		totleDown.setOnClickListener(listener);
		tuiUp.setOnClickListener(listener);
		tuiDown.setOnClickListener(listener);
		return view;
	}

}
