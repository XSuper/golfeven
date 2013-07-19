package com.golfeven.firstGolf.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.adapter.BallParkListAdapter;
import com.golfeven.firstGolf.adapter.BallTeamListAdapter;
import com.golfeven.firstGolf.base.BaseListActivity;
import com.golfeven.firstGolf.bean.BallPark;
import com.golfeven.firstGolf.bean.BallTeam;
import com.golfeven.firstGolf.ui.GallerysActivity.SpinnerListener;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.PullToRefreshListView;


public class BallTeamsActivity extends BaseListActivity {

	@ViewInject(id = R.id.activity_ballteam_list_spinner_city)
	Spinner city;
	@ViewInject(id = R.id.activity_ballteam_list_spinner_peoplecount)
	Spinner peoplecount;

	private ArrayAdapter cityAdapter;
	private ArrayAdapter peoplecountAdapter;
	/**
	 * 必须按照这个顺序排列
	 */
	@Override
	public void mustInit() {
		this.layoutResid = R.layout.activity_ballteam_list;
		setContentView(layoutResid);
		this.context = BallTeamsActivity.this;
		this.entityClass = BallTeam.class ;
		this.adapter = new BallTeamListAdapter(context, datas);
		this.headBack = (HeadBack)findViewById(R.id.activity_ballteam_list_headback);
		this.listView = (PullToRefreshListView)findViewById(R.id.activity_ballteam_list);
		this.params.put("cmd","Clubs");
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		cityAdapter = ArrayAdapter.createFromResource(appContext,
				R.array.spinner_city, android.R.layout.simple_spinner_item);
		peoplecountAdapter = ArrayAdapter.createFromResource(appContext,
				R.array.spinner_peoplecount, android.R.layout.simple_spinner_item);
		cityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		peoplecountAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		city.setAdapter(cityAdapter);
		peoplecount.setAdapter(peoplecountAdapter);
		OnItemSelectedListener onItemSelectedListener = new SpinnerListener();
		city.setOnItemSelectedListener(onItemSelectedListener);
		peoplecount.setOnItemSelectedListener(onItemSelectedListener);
	}

	class SpinnerListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if (parent == city) {
				String value = city.getItemAtPosition(position).toString();

			}
			if (parent == peoplecount) {
//				String Str = "day";
				String value = peoplecount.getItemAtPosition(position).toString();
				
			}
			reset();
			listView.setTag(LOAD);
			requestData();

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}
	
	

}
