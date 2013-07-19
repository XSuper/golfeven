package com.golfeven.firstGolf.widget.frame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.bean.BallPark;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.ui.BallParksActivity;
import com.golfeven.firstGolf.ui.MainActivity;
import com.golfeven.firstGolf.ui.PlayBallScoreActivity;
import com.golfeven.firstGolf.widget.MyToast;

public class PlayBallFrame extends LinearLayout{
	private Spinner hole;
	private  TextView ballPark;
	private Button btn;
	
	private BallPark mBallPark;
	/**
	 * 选择球场后得到球场
	 * @param mBallPark
	 */
	public void setBallPark(BallPark mBallPark){
		this.mBallPark = mBallPark;
		ballPark.setText(mBallPark.getTitle());
	}
	
	
	private Context context;

	public PlayBallFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initViews(context);
	}


	private void initViews(final Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.frame_playball, this);
		hole = (Spinner)view.findViewById(R.id.frame_playball_hole);
		btn = (Button)view.findViewById(R.id.frame_playball_btn);
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mBallPark==null){
					MyToast.customToast(context, Toast.LENGTH_SHORT, "请选择", "请选择球场！", Constant.TOAST_IMG_WARNING);
					return;
				}
				Intent intent = new Intent(getContext(),PlayBallScoreActivity.class);
				intent.putExtra("ballpark", mBallPark);
				intent.putExtra("hole", hole.getSelectedItem().toString());
				getContext().startActivity(intent);
				
			}
		});
		
		ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.spinner_hole, android.R.layout.simple_spinner_item);
		hole.setAdapter(adapter);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		
		ballPark = (TextView)view.findViewById(R.id.frame_playball_ballpark);
		ballPark.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, BallParksActivity.class);
				intent.putExtra("isChoice", true);
				((Activity)context).startActivityForResult(intent, MainActivity.PLAYBALLFRAMEGETBALLPARKCODE);
			}
		});
		
		
		
	}
	

}
