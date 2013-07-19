package com.golfeven.firstGolf.ui;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.Photo;
import com.golfeven.firstGolf.bean.User;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.MyToast;

public class MyDetailActivity extends BaseActivity {
	@ViewInject(id = R.id.activity_my_detail_headback)
	HeadBack headback;
	@ViewInject(id = R.id.activity_my_detail_photo)
	ImageView mPhoto;
	@ViewInject(id = R.id.activity_my_detail_face)
	ImageView face;

	@ViewInject(id = R.id.activity_my_detail_name)
	EditText tname;
	@ViewInject(id = R.id.activity_my_detail_sex)
	ImageView tsex;
	@ViewInject(id = R.id.activity_my_detail_place)
	EditText tplace;
	@ViewInject(id = R.id.activity_my_detail_lovemsg)
	EditText tlovemsg;
	@ViewInject(id = R.id.activity_my_detail_tags)
	EditText ttags;

	@ViewInject(id = R.id.activity_my_detail_btn)
	Button btn;

	private User user;

	private FinalBitmap fb;
	private List<Photo> photos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_my_detail);
		fb = appContext.getFB();
		user = appContext.user;
		load();
		initValue();
		tname.setEnabled(false);
		tplace.setEnabled(false);
		tlovemsg.setEnabled(false);
		ttags.setEnabled(false);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Button btn = (Button) v;
				if (btn.getTag() == null) {
					tname.setEnabled(true);
					tplace.setEnabled(true);
					tlovemsg.setEnabled(true);
					ttags.setEnabled(true);
					btn.setText("提交");
					btn.setTag("按下了");
				} else {
					tname.setEnabled(false);
					tplace.setEnabled(false);
					tlovemsg.setEnabled(false);
					ttags.setEnabled(false);
					btn.setText("完善资料");
					btn.setTag(null);
				}
			}
		});
	}

	private void initValue() {
		fb.display(face, Constant.URL_IMG_BASE + user.getFace());
		tname.setText(user.getUname());
		tplace.setText(user.getCommplace());
		tlovemsg.setText(user.getLovemsg());

		ttags.setText(user.getLabel());
	}

	private void load() {
		// TODO Auto-generated method stub
		Api api = Api.getInstance();

		// 加载详细信息
//		api.getBallFriendDetail(user.getMid(), appContext.user.getMid(),
//				appContext.latitude, appContext.longitude,
//				new AjaxCallBack<String>() {
//
//					@Override
//					public void onSuccess(String t) {
//						// TODO Auto-generated method stub
//						super.onSuccess(t);
//						headback.setProgressVisible(false);
//						user = JSON.parseObject(t, BallFriend.class);
//						initValue();
//					}
//
//					@Override
//					public void onFailure(Throwable t, String strMsg) {
//						// TODO Auto-generated method stub
//						super.onFailure(t, strMsg);
//						headback.setProgressVisible(false);
//					}
//
//				});
		// 加载个人相册
		api.getPhoto(user.getMid(), new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				photos = JSON.parseArray(t, Photo.class);
				if (photos != null && photos.size() != 0) {
					fb.display(mPhoto, Constant.URL_IMG_BASE
							+ photos.get(0).getPic());
					
					mPhoto.setOnClickListener(new OnClickListener() {
						
						@Override 
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(MyDetailActivity.this, PhotosActivity.class);
							intent.putExtra("isMe", true);
							intent.putParcelableArrayListExtra("photos", (ArrayList<Photo>)photos);
							startActivityForResult(intent, 003);
						}
					});
				}
			}
		});

		face.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MyDetailActivity.this,
						ChoicePhotoActivity.class);
				startActivityForResult(intent, 001);

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == resultCode && resultCode == 001) {
			ContentResolver cr = getContentResolver();
	         InputStream in=null;
			try {
				in = cr.openInputStream(data.getData());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			Api.getInstance().uploadFace(appContext.user, in, new AjaxCallBack<String>() {
				ProgressDialog progressDialog = Utils.initWaitingDialog(MyDetailActivity.this,"正在上传头像...");
				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					MyToast.customToast(MyDetailActivity.this, Toast.LENGTH_SHORT, "成功","头像更换成功",Constant.TOAST_IMG_SUCCESS);
					super.onSuccess(t);
					progressDialog.dismiss();
					
					MyLog.v("face", t);
					Bitmap map = BitmapFactory.decodeFile(Constant.IMG_CACHEPATH
							+ "/face.jpg");
					face.setImageBitmap(map);
					//更新图片缓存，头像显示为新上传头像
					appContext.getFB().mImageCache.clearCache(Constant.URL_IMG_BASE+user.getFace());
					appContext.getFB().mImageCache.clearMemoryCache(Constant.URL_IMG_BASE+user.getFace());
					appContext.getFB().mImageCache.addBitmapToCache(Constant.URL_IMG_BASE+user.getFace(), map);
				}

				@Override
				public void onFailure(Throwable t, String strMsg) {
					// TODO Auto-generated method stub
					super.onFailure(t, strMsg);
					MyLog.v("face", strMsg);
					progressDialog.dismiss();
					MyToast.customToast(MyDetailActivity.this, Toast.LENGTH_SHORT, "失败","头像更换失败",Constant.TOAST_IMG_SUCCESS);
				}
				
			});
			
			
		}
		if (requestCode == resultCode && resultCode == 003) {
			photos = data.getParcelableArrayListExtra("photos");
			Toast.makeText(appContext, "baochunle004 ", Toast.LENGTH_SHORT).show();
			
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

}
