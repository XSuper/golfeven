package com.golfeven.firstGolf.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.Photo;
import com.golfeven.firstGolf.bean.WrongResponse;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.NetUtil;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.common.ValidateUtil;
import com.golfeven.firstGolf.widget.MyToast;

public class PhotosActivity extends BaseActivity {

	@ViewInject(id = R.id.activity_photos_mytable)
	TableLayout mtable;
	private List<Photo> photos;

	private boolean isMe = false;
	TableRow row = null;
	int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photos);
		Intent intent = getIntent();
		photos = intent.getParcelableArrayListExtra("photos");
		isMe = intent.getBooleanExtra("isMe", false);
		if (photos != null && photos.size() > 0) {
			
			init();
		}

	}

	private void init() {

		while (index < photos.size()) {

			ImageView img = new ImageView(appContext);
			
			img.setTag(photos.get(index).getId());
			img.setTag(R.layout.activity_photos,index);
			appContext.getFB().display(img,
					Constant.URL_IMG_BASE + photos.get(index).getPic());
			img.setOnLongClickListener(mLong);
			img.setOnClickListener(onClick);
			rowAddView(img);

		}
		if (isMe) {

			Button btn = new Button(appContext);
			btn.setText("添加照片");
			rowAddView(btn);
			
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(PhotosActivity.this,
							ChoicePhotoActivity.class);
					intent.putExtra("dontCut", true);
					startActivityForResult(intent, 002);
					
				}
			});

		}

	}

	public void rowAddView(View view) {
		if (index % 3 == 0) {
			LayoutParams params = new LayoutParams(3);
			row = new TableRow(appContext);
			row.setLayoutParams(params);
			mtable.addView(row);
		}
		
		//view.setLayoutParams(params )
		LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1);
		view.setLayoutParams(param);
		row.addView(view);
		index++;
	}

	InputStream in=null;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == resultCode && resultCode == 002) {
			
			ContentResolver cr = getContentResolver();
			try {
				in = cr.openInputStream(data.getData());
				
			} catch (Exception e) {
				MyLog.e("input-file not found", e.toString());
			}
			Api.getInstance().uploadPhoto(appContext.user,  in, new AjaxCallBack<String>() {
				ProgressDialog progressDialog = Utils.initWaitingDialog(PhotosActivity.this,"正在上传照片...");
				
				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					super.onSuccess(t);
					progressDialog.dismiss();
					if(t.trim().startsWith("{")){
						Toast.makeText(appContext, t, Toast.LENGTH_LONG).show();
						Photo p = JSON.parseObject(t, Photo.class);
						p.setPic(p.getFile());
						photos.add(p);
						//ImageView img = new ImageView(PhotosActivity.this);
						//img.setTag(p.getId());
						//Bitmap bitmap = BitmapFactory.decodeStream(in);
						//img.setImageBitmap(bitmap);
						//将图片缓存到缓存区
						//appContext.getFB().mImageCache.addBitmapToCache(Constant.URL_IMG_BASE + p.getPic(), bitmap);
//						rowAddView(img);
//						PhotosActivity.this.postInvalidate();
//						
						mtable.removeAllViews();
						index = 0;
						init();
						
						Intent intent = new Intent();
						intent.putParcelableArrayListExtra("photos",(ArrayList<Photo>) photos);
						Toast.makeText(appContext, "baochunle001 ", Toast.LENGTH_SHORT).show();
						setResult(003,intent);

						
					}else{
						WrongResponse wrongResponse = ValidateUtil
								.wrongResponse(t);
						if(wrongResponse.show){
							MyToast.centerToast(appContext, wrongResponse.msg, Toast.LENGTH_SHORT);
						}else{
							MyToast.centerToast(appContext, "上传失败", Toast.LENGTH_SHORT);
							MyLog.v("上传失败",wrongResponse.msg );
						}
					}
				}

				@Override
				public void onFailure(Throwable t, String strMsg) {
					// TODO Auto-generated method stub
					super.onFailure(t, strMsg);
					progressDialog.dismiss();
					NetUtil.requestError(appContext, "网络连接失败");
				}
				
			});
		}
		super.onActivityResult(requestCode, resultCode, data);

	}
	OnLongClickListener mLong = new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(final View v) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(PhotosActivity.this);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setTitle("确定删除该照片吗？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					Api.getInstance().deletPhoto(appContext.user, v.getTag()+"", new AjaxCallBack<String>() {

						@Override
						public void onSuccess(String t) {
							// TODO Auto-generated method stub
							super.onSuccess(t);
							Toast.makeText(appContext, t, Toast.LENGTH_LONG).show();
						}

						@Override
						public void onFailure(Throwable t, String strMsg) {
							// TODO Auto-generated method stub
							super.onFailure(t, strMsg);
							Toast.makeText(appContext, "======error=="+strMsg, Toast.LENGTH_LONG).show();
						}
						
					});
					
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.show();
			return false;
		}
	};
	OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(PhotosActivity.this,PhotoShowActivity.class);
			intent.putExtra("index",(Integer)v.getTag(R.layout.activity_photos));
			intent.putParcelableArrayListExtra("photos",(ArrayList<Photo>) photos);
			startActivity(intent);
			
		}
	};
	
}
