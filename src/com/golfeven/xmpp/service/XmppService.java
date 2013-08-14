package com.golfeven.xmpp.service;

import java.util.Collection;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.RosterPacket;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.ui.BallFriendDetailActivity;
import com.golfeven.firstGolf.ui.LoginActivity;
import com.golfeven.firstGolf.ui.MainActivity;
import com.golfeven.xmpp.activity.Chat;
import com.golfeven.xmpp.db.DbHelper;
import com.golfeven.xmpp.entity.ChatMsg;
import com.golfeven.xmpp.entity.FriendInfo;
import com.golfeven.xmpp.utils.Logs;
import com.golfeven.xmpp.utils.Utils;
import com.golfeven.xmpp.xmppmanager.XmppUtils;

public class XmppService extends Service {

	XMPPConnection connection;
	
	RemoteViews view;
	Notification notification;
	NotificationManager manager;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			ChatMsg chatMsg = (ChatMsg) msg.obj;
			Toast.makeText(XmppService.this, "用户【" + chatMsg.getUsername() + "】，body = " + chatMsg.getMsg(),Toast.LENGTH_SHORT).show();
			MyLog.v("xmpp","用户【" + chatMsg.getUsername() + "】，body = " + chatMsg.getMsg());
			
//			notification=new Notification(R.drawable.h091, "开始下载", System.currentTimeMillis());
//			view=new RemoteViews(getPackageName(),R.layout.notification_xmppnews );
//			notification.contentView=view;
//			view.setTextViewText(R.id.n_xmpp_text, "用户【" + chatMsg.getUsername() + "】，body = " + chatMsg.getMsg().toString());
//			notification.contentIntent=PendingIntent.getActivity(getBaseContext(), 1,new Intent(getBaseContext(),MainActivity.class),0);
//			notification.flags=Notification.FLAG_NO_CLEAR;
//			manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//			
			FriendInfo info = new FriendInfo();
			info.setNickname(chatMsg.getUsername());
			info.setUsername(chatMsg.getUsername());
			
			showNotification("新消息",chatMsg.getMsg(),info);
		};
	};
	@Override
	public void onCreate() {
		
		try {
			connection = XmppUtils.getInstance().getConnection();
			connection.addPacketListener(new PacketListener() {
				
				@Override
				public void processPacket(Packet packet) {
					if(packet instanceof Message){//信息监听
						Message msg = (Message) packet;
						if(Message.Type.chat.equals(msg.getType())){//是一对一聊天信息
							if(!TextUtils.isEmpty(msg.getBody())){
								Logs.i(XmppService.class, "收到一条信息 xml = " + msg.toXML());
								Intent intent = new Intent();
								intent.setAction("msg_in");
								ChatMsg chatMsg = new ChatMsg();
								chatMsg.setMsg(msg.getBody());
								chatMsg.setType(2);
								chatMsg.setUsername(Utils.getJidToUsername(msg.getFrom()));
								DbHelper.getInstance(XmppService.this).saveChatMsg(chatMsg);
								intent.putExtra("msg_in", chatMsg);
								android.os.Message message = new android.os.Message();
								message.obj = chatMsg;
								mHandler.sendMessage(message);
								sendBroadcast(intent);
								
							}
							
						}else if(Message.Type.groupchat.equals(msg.getType())){//多人聊天
							
						}
					}else if(packet instanceof Presence){
						Presence presence = (Presence) packet;
							Logs.i(XmppService.class, "收到一条状态 xml = " + presence.toXML());
							
					}
				}
			}, null);
		} catch (XMPPException e) {
			
			e.printStackTrace();
		}
		
		try {
			connection.getRoster().addRosterListener(new RosterListener() {
				
				//好友在线状态改变
				@Override
				public void presenceChanged(Presence presence) {
					
					Logs.i(XmppService.class, "presenceChanged  username = " + Utils.getJidToUsername(presence.getFrom())
												+ " ,在线状态 = " + presence.getMode());
					
				}
				
				//好友内容更新了
				@Override
				public void entriesUpdated(Collection<String> addresses) {
					
					for(String add : addresses){
						Logs.i(XmppService.class, " updaye = " + add);
					}
				}
				
				//删除好友
				@Override
				public void entriesDeleted(Collection<String> addresses) {
//					for(String add : addresses){
//						Logs.i(XmppService.class, " del = " + add);
//						if(FriendListActivity.adapter != null){
//							FriendListActivity.friendListActivity.deleteFriend(Utils.getJidToUsername(add));
//						}
//					}
					
				}
				//添加好友
				@Override
				public void entriesAdded(Collection<String> addresses) {
					for(String add : addresses){
						Logs.i(XmppService.class, " add = " + add);
						Presence response = new Presence(Presence.Type.subscribed);
						response.setTo(add);
						response.setMode(Mode.chat); // 用户状态
						connection.sendPacket(response);

						String[] mGroupName = { "Friends" };
						try {
							createEntry(add, Utils.getJidToUsername(add), mGroupName);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		connection.addConnectionListener(new ConnectionListener() {
			
			@Override
			public void reconnectionSuccessful() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void reconnectionFailed(Exception e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void reconnectingIn(int seconds) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void connectionClosedOnError(Exception e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void connectionClosed() {
				
			}
		});
		
	}
	
	@Override
	public void onDestroy() {
		Logs.i(XmppService.class, "onDestroy");
		super.onDestroy();
	}
	
	/**
	 * 邀请人向被邀请人发出一个添加好友的消息
	 * 
	 * @param user
	 *            被邀请人的email
	 * @param name
	 *            被邀请人的昵称
	 * @param groups
	 *            要添加的群组
	 * @param callMsg
	 *            招呼消息
	 * @param isInvaite
	 *            该数据是否为邀请
	 * @throws XMPPException
	 * @author by_wsc
	 * @email wscnydx@gmail.com
	 * @date 日期：2013-4-20 时间：上午3:54:13
	 */
	public void createEntry(String user, String name, String[] groups) throws XMPPException {
		if (TextUtils.isEmpty(user) || groups == null || groups.length == 0) {
			return;
		}
		// ----------- 加上昵称
		name = user.substring(0, user.indexOf("@"));
		// -------------
		RosterPacket rosterPacket = new RosterPacket();
		rosterPacket.setType(IQ.Type.SET);
		RosterPacket.Item item = new RosterPacket.Item(user, name);
		if (groups != null) {
			for (String group : groups) {
				if (!TextUtils.isEmpty(group)) {
					item.addGroupName(group);
				}
			}
		}
		rosterPacket.addRosterItem(item);
		PacketCollector collector = connection.createPacketCollector(
				new PacketIDFilter(rosterPacket.getPacketID()));
		connection.sendPacket(rosterPacket);
		IQ response = (IQ) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		collector.cancel();
		if (response == null) {
			throw new XMPPException("No response from the server.");
		} else if (response.getType() == IQ.Type.ERROR) {
			throw new XMPPException(response.getError());
		}

		Presence presencePacket = new Presence(Presence.Type.subscribe);
		presencePacket.setTo(user);
		connection.sendPacket(presencePacket);

		Presence response2 = new Presence(Presence.Type.available);
		response2.setTo(user);
		response2.setMode(Mode.chat);
		connection.sendPacket(response);
		FriendInfo fInfo = new FriendInfo();
		fInfo.setUsername(Utils.getJidToUsername(user));
		android.os.Message message = new android.os.Message();
//		message.what = FriendListActivity.ADD_FRIEND;
//		message.obj = fInfo;
		//FriendListActivity.friendListActivity.mHandler.sendMessage(message);
	}
	
	 private void showNotification(String title,String content,FriendInfo info){

	        // 创建一个NotificationManager的引用  

	        NotificationManager notificationManager = (NotificationManager)   
	            this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);  
	        // 定义Notification的各种属性  
	        Notification notification =new Notification(R.drawable.h091,  
	                "新消息", System.currentTimeMillis());

	        //FLAG_AUTO_CANCEL   该通知能被状态栏的清除按钮给清除掉

	        //FLAG_NO_CLEAR      该通知不能被状态栏的清除按钮给清除掉

	        //FLAG_ONGOING_EVENT 通知放置在正在运行

	        //FLAG_INSISTENT     是否一直进行，比如音乐一直播放，知道用户响应

	        //notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中  
	     //   notification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用  
	      //  notification.flags |= Notification.FLAG_SHOW_LIGHTS;  

	        //DEFAULT_ALL     使用所有默认值，比如声音，震动，闪屏等等
	        //DEFAULT_LIGHTS  使用默认闪光提示
	        //DEFAULT_SOUNDS  使用默认提示声音
	        //DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission android:name="android.permission.VIBRATE" />权限
	        notification.defaults = Notification.DEFAULT_LIGHTS;
	        //叠加效果常量
	        //notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
	        notification.ledARGB = Color.BLUE;  
	        notification.ledOnMS =5000; //闪光时间，毫秒
	     // 设置通知的事件消息  
	        CharSequence contentTitle =title; // 通知栏标题  
	        CharSequence contentText =content; // 通知栏内容  
	        Intent notificationIntent =new Intent(XmppService.this, Chat.class); // 点击该通知后要跳转的Activity  
	        notificationIntent.putExtra("info", info);
	        PendingIntent contentItent = PendingIntent.getActivity(this, 0, notificationIntent, 0);  
	        notification.setLatestEventInfo(this, contentTitle, contentText, contentItent);  
	        // 把Notification传递给NotificationManager  
	        notificationManager.notify(0, notification);  

	    }
	
}
