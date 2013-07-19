package com.golfeven.firstGolf.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.golfeven.firstGolf.R;

public class HeadBack extends RelativeLayout {
	private ImageButton back, rbtn1, rbtn2;
	private TextView title;
	private ProgressBar progress;
	

	public ProgressBar getProgress() {
		return progress;
	}

	public void setProgress(ProgressBar progress) {
		this.progress = progress;
	}

	public ImageButton getBack() {
		return back;
	}

	public void setBack(ImageButton back) {
		this.back = back;
	}

	public ImageButton getRbtn1() {
		return rbtn1;
	}

	public void setRbtn1(ImageButton rbtn1) {
		this.rbtn1 = rbtn1;
	}

	public ImageButton getRbtn2() {
		return rbtn2;
	}

	public void setRbtn2(ImageButton rbtn2) {
		this.rbtn2 = rbtn2;
	}

	public TextView getTitle() {
		return title;
	}

	public void setTitle(TextView title) {
		this.title = title;
	}
	public void setTitle(String title) {
		this.title.setText(title);
	}
	

	public HeadBack(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
		init(attrs);
	}

	public HeadBack(Context context) {
		super(context);
		initView(context);
	}

	public HeadBack(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
		init(attrs);
	}
	public void setProgressVisible(boolean isvisible){
		progress.setVisibility(isvisible?VISIBLE:INVISIBLE);
	}
	public int getProgressVisibleState(){
		return progress.getVisibility();
	}

	private void init(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.head_back);
		try {
			String title = a.getString(R.styleable.head_back_htitle);

			boolean btn1_Visible = a.getBoolean(
					R.styleable.head_back_rbtn1_visible, false);
			boolean btn2_Visible = a.getBoolean(
					R.styleable.head_back_rbtn2_visible, false);
			boolean pro_visible = a.getBoolean(R.styleable.head_back_progress_visible, false);
			Drawable btn1_src = a.getDrawable(R.styleable.head_back_rbtn1_src);
			Drawable btn2_src = a.getDrawable(R.styleable.head_back_rbtn2_src);

			if (title != null) {
				this.title.setText(title);
			}

			if (btn1_Visible) {
				rbtn1.setVisibility(VISIBLE);
				rbtn1.setImageDrawable(btn1_src);
			}
			if (btn2_Visible) {
				rbtn2.setVisibility(VISIBLE);
				rbtn2.setImageDrawable(btn2_src);
			}
			if(pro_visible){
				progress.setVisibility(VISIBLE);
			}

		} finally {
			a.recycle();
		}
	}

	private void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.head_back, this);
		title =(TextView)view.findViewById(R.id.head_back_title);
		progress = (ProgressBar)view.findViewById(R.id.head_back_progress);
		back = (ImageButton)view.findViewById(R.id.btn_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((Activity)getContext()).finish();
			}
		});
		rbtn1 = (ImageButton)view.findViewById(R.id.head_back_rbtn1);
		rbtn2 = (ImageButton)view.findViewById(R.id.head_back_rbtn2);
	}
	public void setRbtn1ClickListener(OnClickListener clickListener){
		rbtn1.setOnClickListener(clickListener);
	}
	public void setRbtn2ClickListener(OnClickListener clickListener){
		rbtn2.setOnClickListener(clickListener);
	}
}
