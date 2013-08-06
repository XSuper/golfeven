package com.golfeven.firstGolf.ui;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
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
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.AppContext;
import com.golfeven.AppManager;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.Photo;
import com.golfeven.firstGolf.bean.User;
import com.golfeven.firstGolf.bean.WrongResponse;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.NetUtil;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.common.ValidateUtil;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.MyToast;

public class MyDetailActivity extends BaseActivity {
	@ViewInject(id = R.id.activity_my_detail_headback)
	HeadBack headback;
	@ViewInject(id = R.id.activity_my_detail_photo)
	ImageView mPhoto;
	@ViewInject(id = R.id.activity_my_detail_face)
	ImageView face;
	@ViewInject(id = R.id.activity_my_detail_mcount)
	TextView imagecount;

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

	ProgressDialog dialog;

	// private void getView(){
	// headback = (HeadBack)findViewById(R.id.activity_my_detail_headback);
	// mPhoto = (ImageView)findViewById(R.id.activity_my_detail_photo);
	// face = (ImageView)findViewById(R.id.activity_my_detail_face);
	// imagecount = (TextView)findViewById(R.id.activity_my_detail_mcount);
	// tname = (EditText)findViewById(R.id.activity_my_detail_name);
	// tage = (TextView)findViewById(R.id.activity_my_detail_age);
	// tsex = (ImageView)findViewById(R.id.activity_my_detail_sex_icon);
	// txsex = (TextView)findViewById(R.id.activity_my_detail_sex_text);
	// tplace = (EditText)findViewById(R.id.activity_my_detail_place);
	// tlovemsg = (EditText)findViewById(R.id.activity_my_detail_lovemsg);
	// ttags = (EditText)findViewById(R.id.activity_my_detail_tags);
	// btn = (Button)findViewById(R.id.activity_my_detail_btn);
	//
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_my_detail);
		// getView();

		fb = appContext.getFB();
		user = appContext.user;
		load();
		initValue();
		tname.setEnabled(false);
		tplace.setEnabled(false);
		tlovemsg.setEnabled(false);
		ttags.setEnabled(false);

		android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
				Utils.getScreenWith(MyDetailActivity.this) / 4,
				Utils.getScreenWith(MyDetailActivity.this) / 4);
		params.setMargins(10, 10, 10, 10);
		face.setLayoutParams(params);

		int width = Utils.getScreenWith(MyDetailActivity.this);
		LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT,
				width / 2);
		mPhoto.setLayoutParams(params2);

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

					HashMap<String, String> hashMap = new HashMap<String, String>();
					Api api = Api.getInstance();
					Set<Integer> typeSet = new HashSet<Integer>();
					if (!oldName.equals(tname.getText().toString())) {
					}
					if (!oldAge.equals(tage.getText().toString())) {
						hashMap.put("birthday", tage.getText().toString());

					}
					if (!oldSex.equals(txsex.getText().toString())) {
						hashMap.put("sex", txsex.getText().toString());
					}
					if (!oldPlace.equals(tplace.getText().toString())) {
						hashMap.put("commplace", tplace.getText().toString());
						typeSet.add(5);
						//
					}
					if (!oldTags.equals(ttags.getText().toString())) {
						hashMap.put("label", ttags.getText().toString());
						typeSet.add(7);
						//
					}
					if (!oldLovemsg.equals(tlovemsg.getText().toString())) {
						hashMap.put("lovemsg", tlovemsg.getText().toString());
						typeSet.add(6);
						//
					}
					dialog = Utils.initWaitingDialog(MyDetailActivity.this,
							"正在更新资料");
					api.updateInfo(MyDetailActivity.this, user, hashMap,
							typeSet, new AjaxCallBack<String>() {

								@Override
								public void onSuccess(String t) {
									// TODO Auto-generated method stub
									super.onSuccess(t);
									dialog.dismiss();
									WrongResponse response = null;

									try {
										response = JSON.parseObject(t,
												WrongResponse.class);
									} catch (Exception e) {
										// TODO: handle exception
									}
									if (response != null && response.code == 0) {

										MyToast.customToast(
												MyDetailActivity.this,
												Toast.LENGTH_SHORT,
												MyToast.TOAST_MSG_SUCCESS_TITLE,
												"资料更新成功",
												Constant.TOAST_IMG_SUCCESS);
										tname.setEnabled(false);
										tplace.setEnabled(false);
										tlovemsg.setEnabled(false);
										ttags.setEnabled(false);
										MyDetailActivity.this.btn
												.setText("完善资料");
										MyDetailActivity.this.btn.setTag(null);

										appContext.user.setUname(tname
												.getText().toString());
										appContext.user.setCommplace(tplace
												.getText().toString());
										appContext.user.setLovemsg(tlovemsg
												.getText().toString());
										appContext.user.setLabel(ttags
												.getText().toString());
										appContext.user.setSex(txsex.getText()
												.toString());

										// finish();
										// MainActivity m =
										// (MainActivity)AppManager.getAppManager().getActivity(MainActivity.class);
										// m.mScrollLayout.scrollToScreen(3);

									} else {
										WrongResponse wrongResponse = ValidateUtil
												.wrongResponse(t);
										if (wrongResponse.show) {
											MyToast.customToast(
													MyDetailActivity.this,
													Toast.LENGTH_SHORT,
													MyToast.TOAST_MSG_ERROR_TITLE,
													wrongResponse.msg,
													Constant.TOAST_IMG_ERROR);

										} else {
											MyToast.customToast(
													MyDetailActivity.this,
													Toast.LENGTH_SHORT,
													MyToast.TOAST_MSG_ERROR_TITLE,
													"资料更新失败",
													Constant.TOAST_IMG_ERROR);
											MyLog.v("资料更新失败", wrongResponse.msg);
										}

									}
								}

								@Override
								public void onFailure(Throwable t, String strMsg) {
									// TODO Auto-generated method stub
									super.onFailure(t, strMsg);
									dialog.dismiss();
									NetUtil.requestError(appContext, null);
									MyDetailActivity.this.btn.setText("点击重新提交");
								}

							});

				}
			}
		});
	}

	private void initValue() {

		if (!StringUtils.isEmpty(user.getFace())) {

			fb.isSquare = true;
			fb.display(face, Constant.URL_IMG_BASE + user.getFace());
		}

		tname.setText(user.getUname());
		tplace.setText(user.getCommplace());

		txsex.setText(user.getSex());
		if ("男".equals(user.getSex().trim())) {
			tsex.setBackgroundResource(R.drawable.qy_man);
		} else if ("女".equals(user.getSex().trim())) {
			tsex.setBackgroundResource(R.drawable.qy_girl);
		} else {
			tsex.setBackgroundResource(R.drawable.qy_sec);
		}

		tlovemsg.setText(user.getLovemsg());
		tage.setText(user.getBirthday());
		ttags.setText(user.getLabel());

		tage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MyDetailActivity.this);
				builder.setIcon(android.R.drawable.ic_dialog_info);
				final View view = LayoutInflater.from(MyDetailActivity.this)
						.inflate(R.layout.dialog_choice_age, null);
				builder.setView(view);
				builder.setTitle("请选择年龄段");
				final RadioGroup rg = (RadioGroup) view
						.findViewById(R.id.choice_age);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								int id = rg.getCheckedRadioButtonId();
								if (id != -1) {

									RadioButton btn = (RadioButton) view
											.findViewById(id);
									if (btn != null) {

										tage.setText(btn.getText());
									}
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
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				mPhoto.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MyDetailActivity.this,
								PhotosActivity.class);
						intent.putExtra("isMe", true);
						intent.putParcelableArrayListExtra("photos",
								(ArrayList<Photo>) photos);
						startActivityForResult(intent, 003);
					}
				});
			}

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				photos = JSON.parseArray(t, Photo.class);
				if (photos != null) {
					imagecount.setText(photos.size() + "");
				}
				mPhoto.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MyDetailActivity.this,
								PhotosActivity.class);
						intent.putExtra("isMe", true);
						intent.putParcelableArrayListExtra("photos",
								(ArrayList<Photo>) photos);
						startActivityForResult(intent, 003);
					}
				});
				if (photos != null && photos.size() != 0) {
					FinalBitmap fbi = FinalBitmap.createNew(appContext,
							Constant.IMG_CACHEPATH);
					fbi.proportion = 2;
					fbi.display(mPhoto, Constant.URL_IMG_BASE
							+ photos.get(0).getPic());

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
			InputStream in = null;
			try {
				in = cr.openInputStream(data.getData());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Api.getInstance().uploadFace(appContext.user, in,
					new AjaxCallBack<String>() {
						ProgressDialog progressDialog = Utils
								.initWaitingDialog(MyDetailActivity.this,
										"正在上传头像...");

						@Override
						public void onSuccess(String t) {
							// TODO Auto-generated method stub
							MyToast.customToast(MyDetailActivity.this,
									Toast.LENGTH_SHORT, "成功", "头像更换成功",
									Constant.TOAST_IMG_SUCCESS);
							Api.getInstance().addCredits(MyDetailActivity.this, appContext.user, 2, "成功上传头像");
							super.onSuccess(t);
							progressDialog.dismiss();

							MyLog.v("face", t);
							Bitmap map = BitmapFactory
									.decodeFile(Constant.IMG_CACHEPATH
											+ "/face.jpg");
							face.setImageBitmap(map);
							// 更新图片缓存，头像显示为新上传头像
							appContext.getFB().mImageCache
									.clearCache(Constant.URL_IMG_BASE
											+ user.getFace());
							appContext.getFB().mImageCache
									.clearMemoryCache(Constant.URL_IMG_BASE
											+ user.getFace());
							appContext.getFB().mImageCache.addBitmapToCache(
									Constant.URL_IMG_BASE + user.getFace(), map);
						}

						@Override
						public void onFailure(Throwable t, String strMsg) {
							// TODO Auto-generated method stub
							super.onFailure(t, strMsg);
							MyLog.v("face", strMsg);

							progressDialog.dismiss();
							MyToast.customToast(MyDetailActivity.this,
									Toast.LENGTH_SHORT, "失败", "头像更换失败",
									Constant.TOAST_IMG_SUCCESS);
						}

					});

		}
		if (requestCode == resultCode && resultCode == 003) {
			photos = data.getParcelableArrayListExtra("photos");

		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (photos != null && photos.size() >= 1) {
			imagecount.setText(photos.size() + "");
		}
	}
}
