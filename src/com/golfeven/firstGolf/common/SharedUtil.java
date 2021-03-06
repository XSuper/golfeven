package com.golfeven.firstGolf.common;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.sharesdk.BaiduShareException;
import com.baidu.sharesdk.BaiduSocialShare;
import com.baidu.sharesdk.ShareContent;
import com.baidu.sharesdk.ShareListener;
import com.baidu.sharesdk.Utility;
import com.baidu.sharesdk.ui.BaiduSocialShareUserInterface;

public class SharedUtil {
	private static String key = "szbxCLKXkqne7bte9nRGnmoC";
	private static BaiduSocialShare mSocialShare;
	private static BaiduSocialShareUserInterface face;
	private static ShareContent content;
	
	private static final String WEIBOKEY="2177641727";
	private static final String WEIXINKEY="wxbd13120806c0d64c";

	public static void share(Activity activity,String title,String contentStr,String url,String imageUrl) {

		mSocialShare = BaiduSocialShare.getInstance(activity, key);
		face = mSocialShare.getSocialShareUserInterfaceInstance();
		// mSocialShare.supportQQSso(arg0);
		mSocialShare.supportWeiBoSso(WEIBOKEY);
		mSocialShare.supportWeixin(WEIXINKEY);
		
		
		content = new ShareContent();
		content.setTitle(title);
		content.setContent(contentStr);
//		content.setUrl("http://www.golfeven.cn/");
		content.setUrl(url);
//		content.setImageUrl("http://www.golfeven.cn/skin/default/images/logo.jpg");
		content.setImageUrl(imageUrl);
		
		sharemenu(activity, content);
	}
	private static void sharemenu(final Activity activity,ShareContent content) {
		// TODO Auto-generated method stub
		face.showShareMenu(activity,content, Utility. SHARE_BOX_STYLE, new ShareListener(){

			@Override
			public void onApiComplete(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAuthComplete(Bundle arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(BaiduShareException arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, arg0.toString(), Toast.LENGTH_SHORT).show();
				
			}
			
			
		} );
	}

}
