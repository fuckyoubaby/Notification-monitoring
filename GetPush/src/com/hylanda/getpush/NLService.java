package com.hylanda.getpush;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.Notification.Action;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class NLService extends NotificationListenerService {
    public final static String COMMAND = "com.canking.COMMAND_NOTIFICATION_LISTENER_SERVICE";
    public final static String COMMAND_EXTRA = "command";
    public final static String CANCEL_ALL = "clearall";
    public final static String GET_LIST = "list";
    public final static String WEIXIN = "com.tencent.mm";

    private String TAG = "NLService";
   // private NLServiceReceiver nlservicereciver;

    @Override
    public void onCreate() {
    	
    	Log.e(TAG, "CREATE");
    	Toast.makeText(NLService.this, "服务启动完成", Toast.LENGTH_SHORT).show();
        super.onCreate();
       /* nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(COMMAND);
        registerReceiver(nlservicereciver, filter);*/
    }

    
    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(nlservicereciver);
    }

    
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
    	
    	Log.e("NLService posted", "package="+sbn.getPackageName()+" sbn = notification"+sbn.getNotification().toString());
    	
    	String packageName = null;
    	
    	
    	Uri uri = sbn.getNotification().sound;
    	
    	Log.e("uri---uri", "uri="+uri);
    	//PendingIntent pendingIntent = null;
    			// 当 API > 18 时，使用 extras 获取通知的详细信息
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    		Bundle extras = sbn.getNotification().extras;
    		
    		PendingIntent pendingIntent = sbn.getNotification().contentIntent;
    		Log.e("-------------pendingintent", "pendintIntent = "+pendingIntent.toString());
    		
    		
    		Class pdi = pendingIntent.getClass();
    		
    		Field[] fs = pdi.getDeclaredFields();
    		
    		StringBuffer sb = new StringBuffer();
    		sb.append(Modifier.toString(pdi.getModifiers())+" class "+pdi.getSimpleName()+"{\n");
    		for (Field field:fs) {
    			 sb.append("\t");//空格  
                 sb.append(Modifier.toString(field.getModifiers())+" ");//获得属性的修饰符，例如public，static等等  
                 sb.append(field.getType().getSimpleName() + " ");//属性的类型的名字  
                 sb.append(field.getName()+";\n");//属性的名字+回车
			}
    		sb.append("}");
    		
    		Method[] methods = pdi.getDeclaredMethods();
    		
    		for (Method method : methods) {
				Log.e(""+method.getName(), "returntype = "+method.getReturnType());
			}
    		
    		Log.e("result =====", "="+sb);
    		
    		String pdipgn = pendingIntent.getTargetPackage();
    		
    		try {
				Method method = pdi.getDeclaredMethod("getIntent");
				
				method.setAccessible(true);
				//Field field = Class.forName(PendingIntent.class.getName()).getDeclaredField("");
				//Object object = 
				/*Object object = new Object();
				method.invoke(object);*/
				
				Intent intent = (Intent) method.invoke(pendingIntent);
				Uri uri2 = intent.getData();
				Log.e("intent--intent", "intent = "+intent.toURI()+"  ---"+intent.toString());
				ComponentName  componentName = intent.getComponent();
				packageName = componentName.getPackageName();
				
				Log.e("pname 真是名字", packageName);
				if (uri2!=null) {
					Log.e("uri22222222222222222", "====="+uri2.toString());
				}
				
				
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		//Icon icon = sbn.getNotification().getSmallIcon();
    		String icon = extras.getString(Notification.EXTRA_LARGE_ICON, "");
    		Log.e("icon", "="+icon);
    		Log.e("pending packagename", "---="+pdipgn);
    		//packageName = pdipgn;
    		Log.e("pending to string", "="+pendingIntent.toString());
    		
    		if (extras != null) {
    			// 获取通知标题
    		    String title = extras.getString(Notification.EXTRA_TITLE, "");
    		    // 获取通知内容
    		    String content = extras.getString(Notification.EXTRA_TEXT, "");
    		    String url = extras.getString("url","");
    		            
    		    Log.e(" extras info title = "+title , "content = "+content+"     url ="+url);
    		}
    	}
    	if (packageName==null) {
			packageName = sbn.getPackageName();
		}
    	MainActivity.notifyReceive(packageName, sbn.getNotification());
       
    }

    

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    	Log.e("NLService close", "sbn = notification"+sbn.getNotification().toString());
        
    }


}
