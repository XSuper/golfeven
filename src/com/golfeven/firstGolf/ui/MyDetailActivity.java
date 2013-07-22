package com.golfeven.firstGolf.ui;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.Inflater;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.Photo;
import com.golfeven.firstGolf.bean.User;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.StringUtils;
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
	@ViewInject(id = R.id.activity_my_detail_age)
	TextView tage;
	@ViewInject(id = R.id.activity_my_detail_sex_icon)
	ImageView tsex;
	@ViewInject(id = R.id.activity_my_detail_sex_text)
	TextView txsex;
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
	
	private String oldName;
	private String oldAge;
	private String oldSex;
	private String oldPlace;
	private String oldLovemsg;
	private String oldTags;
	

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
					
					oldName = tname.getText().toString();
					oldAge = tage.getText().toString();
					oldSex = txsex.getText().toString();
					oldPlace = tplace.getText().toString();
					oldLovemsg = tlovemsg.getText().toString();
					oldTags = ttags.getText().toString();
				} else {
					tname.setEnabled(false);
					tplace.setEnabled(false);
					tlovemsg.setEnabled(false);
					ttags.setEnabled(false);
					btn.setText("完善资料");
					btn.setTag(null);
					
					HashMap<String,String> hashMap = new HashMap<String, String>();
					Api api = Api.getInstance();
					Set<Integer> typeSet = new HashSet<Integer>();
					if(!oldName.equals(tname.getText().toString())){
						//hashMap.put("mid",tname.getText().toString());
					}
					if(!oldAge.equals(tage.getText().toString())){
						hashMap.put("birthday",tage.getText().toString());
						
					}
					if(!oldSex.equals(txsex.getText().toString())){
						hashMap.put("sex",txsex.getText().toString());
					}
					if(!oldPlace.equals(tplace.getText().toString())){
						hashMap.put("commplace",tplace.getText().toString());
						typeSet.add(5);
//						api.addCredits(appContext, user, 5, new AjaxCallBack<String>() {
//
//							@Override
//							public void onSuccess(String t) {
//								// TODO Auto-generated method stub
//								super.onSuccess(t);
//								MyToast.customToast(appContext, Toast.LENGTH_SHORT,MyToast.TOAST_MSG_SUCCESS_TITLE, "更新常出没地获得100积分", Constant.TOAST_IMG_SUCCESS);
//							}
//							
//						});
					}
					if(!oldTags.equals(ttags.getText().toString())&&StringUtils.isEmpty(ttags.getText().toString())){
						hashMap.put("label",ttags.getText().toString());
						typeSet.add(7);
//						api.addCredits(appContext, user, 7, new AjaxCallBack<String>() {
//							@Override
//							public void onSuccess(String t) {
//								// TODO Auto-generated method stub
//								super.onSuccess(t);
//								MyToast.customToast(appContext, Toast.LENGTH_SHORT,MyToast.TOAST_MSG_SUCCESS_TITLE, "完成标签获得50积分", Constant.TOAST_IMG_SUCCESS);
//							}
//						});
					}
					if(!oldLovemsg.equals(tlovemsg.getText().toString())&&StringUtils.isEmpty(tlovemsg.getText().toString())){
						hashMap.put("lovemsg",tlovemsg.getText().toString());
						typeSet.add(6);
//						api.addCredits(appContext, user, 6, new AjaxCallBack<String>() {
//							@Override
//							public void onSuccess(String t) {
//								// TODO Auto-generated method stub
//								super.onSuccess(t);
//								MyToast.customToast(appContext, Toast.LENGTH_SHORT,MyToast.TOAST_MSG_SUCCESS_TITLE, "完成签名获得100积分", Constant.TOAST_IMG_SUCCESS);
//							}
//						});
					}
					api.updateInfo(MyDetailActivity.this, user, hashMap, typeSet, new AjaxCallBack<String>() {

						@Override
						public void onSuccess(String t) {
							// TODO Auto-generated method stub
							super.onSuccess(t);
							Toast.makeText(appContext, t,Toast.LENGTH_LONG).show();
						}

						@Override
						public void onFailure(Throwable t, String strMsg) {
							// TODO Auto-generated method stub
							super.onFailure(t, strMsg);
							Toast.makeText(appContext, strMsg,Toast.LENGTH_LONG).show();
							MyLog.v("erroe", t.toString());
						}
						
					});
					
					
					
					
					
				}
			}
		});
	}

	private void initValue() {
		fb.display(face, Constant.URL_IMG_BASE + user.getFace());
		tname.setText(user.getUname());
		tplace.setText(user.getCommplace());
		txsex.setText(user.getSex());
		tlovemsg.setText(user.getLovemsg());
		tage.setText(user.getBirthday());
		ttags.setText(user.getLabel());
		
		tage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MyDetailActivity.this);
				builder.setIcon(android.R.drawable.ic_dialog_info);
				final View view =LayoutInflater.from(MyDetailActivity.this).inflate(
						R.layout.dialog_choice_age, null);
				builder.setView(view);
				builder.setTitle("请选择年龄段");
				final RadioGroup rg  = (RadioGroup)view.findViewById(R.id.choice_age);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
					
							public void onClick(DialogInterface dialog,
									int which) {
								int id = rg.getCheckedRadioButtonId();
								if(id!=-1){
									
									RadioButton btn = (RadioButton)view.findViewById(id);
									tage.setText(btn.getText());
								}
								
								dialog.dismiss();
								

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
	}

	private void load() {
		// TODO Auto-generated method stub
		Api api = Api.getInstance();
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
