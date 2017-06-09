package com.hylanda.getpush;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.hylanda.util.Constant;
import com.hylanda.util.HttpUtil;
import com.hylanda.util.SystemUtil;
import com.hylanda.util.ThreadPoolHttpUtil;
import com.hylanda.util.ThreadPoolHttpUtil.HttpCallback;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Action;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
	private Button openService,closeService;
	private LinearLayout rootLayout;
	//private String ip = "";
	private static MainActivity activity = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		activity = this;
		
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		//判断wifi是否开启  
        if (!wifiManager.isWifiEnabled()) {  
        wifiManager.setWifiEnabled(true);    
        }  
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
        int ipAddress = wifiInfo.getIpAddress();   
        Constant.ip = SystemUtil.intToIp(ipAddress); 
		
		initView();
		
		
		openService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 if (!isEnabled()) {
		                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
		                startActivity(intent);
		         } else {
		        	 	//toggleNotificationListenerService(MainActivity.this);
		        	 	toggleNotificationListenerService();
		                Toast.makeText(MainActivity.this, "正在启动服务", Toast.LENGTH_LONG).show();
		         }
			}
		});
		closeService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent s = new Intent(MainActivity.this, NLService.class);
	            stopService(s);
	            Toast.makeText(MainActivity.this, "已关闭服务权限", Toast.LENGTH_LONG).show();
			}
		});
		
	}

	
	private void getIp()
	{
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		//判断wifi是否开启  
        if (!wifiManager.isWifiEnabled()) {  
        wifiManager.setWifiEnabled(true);    
        }  
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
        int ipAddress = wifiInfo.getIpAddress();   
        Constant.ip = SystemUtil.intToIp(ipAddress); 
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onStart() {
		
		super.onStart();
	}
	
	@Override
	protected void onRestart() {
		
		super.onRestart();
	}
	@Override
	protected void onResume() {
		toggleNotificationListenerService();
        Toast.makeText(MainActivity.this, "正在启动服务", Toast.LENGTH_LONG).show();
		super.onResume();
	}
	
	
	private void initView(){
		openService = (Button) findViewById(R.id.openservice);
		closeService = (Button) findViewById(R.id.closeservice);
		rootLayout = (LinearLayout) findViewById(R.id.root_layout);
	}
	
	private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
		
		
		
        return false;
    }
	
	private void EnumGroupViews(View v1, Map<String, String> map)
	{
		
		if(v1 instanceof ViewGroup)
		{
			ViewGroup lav = (ViewGroup)v1;
			int lcCnt = lav.getChildCount();
			for(int i = 0; i < lcCnt; i++)
			{
				
				View c1 = lav.getChildAt(i);
				if(c1 instanceof ViewGroup)
					EnumGroupViews(c1,map);
				else if(c1 instanceof TextView)
				{
					TextView txt = (TextView)c1;
					
					int id = txt.getId();
					String name = getResources().getResourceEntryName(id).trim();
					
					
					String str = txt.getText().toString().trim();
					
					if (str.length() > 0 ) {
						if (name.equals("title")) {
							map.put("title", str);
						}else if (name.equals("time")) {
							Date date = new Date();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							String time = format.format(date);
							if (str.contains("下午")) {
								String result = getTime(str);
								if (result!=null) {
									map.put("time",time+" "+result+":00");
								}else {
									SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									String tempTime = format1.format(date);
									map.put("time", tempTime);
								}
							}else {
								map.put("time", time+" "+str+":00");
							}
						}else if (name.equals("text")) {
							map.put("text", str);
						}
					}
				}
			}
		}
		
		
	}
	
	private void addToUi(Notification notification,RemoteViews remoteView, String packName) {
		
		Log.e("MainActivity", "addToUi");
		try {
			View v1 = remoteView.apply(this, rootLayout);
			Log.e("addToUi ", "v1="+v1.toString());
			Map<String, String> map = new HashMap<String, String>();
			
			EnumGroupViews(v1,map);
			
			if (isNull(map.get("title"))||isNull(map.get("text"))) { //如果通过上面的方法获取不到text或者title,则调用extras中的值获取该参数.
				Log.e("map没有获取到值", "map没有获取到值");
				getDateByExtras(notification,packName);
			}else {
				sendData(map,packName);
			}
			
			
			
			if(rootLayout.getChildCount() > 100)
			{
				rootLayout.removeViews(0, 50);
				
			}
			rootLayout.addView(v1);
			//data.isChina = PackName.isChina(packName);
		} catch (Exception e) {
		}
		
	}
	
	private void sendData(Map<String, String> map,String packName)
	{
		try {
			//String urlPath = "http://192.168.12.188:8080/NewsTest/NewsUploadServlet";
			//String urlPath = "http://192.168.12.188:8080/NewsTest/NewsPostFormTest";
			String urlPath = "http://wly.hylanda.com/server/api/datareceiver.php";
			
			Log.e("addtoui   ------------------", "map.text="+map.get("text")+"----time="+map.get("time")+"----map.title="+map.get("title"));
			if (map.containsKey("text")&&map.containsKey("title")) {
				if (Constant.ip == null) {
					getIp();
				}
				map.put("ip", Constant.ip);
				if (!isNull(map.get("text"))&&!isNull(map.get("title"))&&!isContainsText(map.get("title"))&&!isContainsText(map.get("text"))) {
					ThreadPoolHttpUtil.doPostForm(urlPath, map, packName, new HttpCallback<String>() {
						
						@Override
						public void onSuccess(String response) {
							System.out.println("success="+response);
						}
						
						@Override
						public void onError(String error) {
							System.out.println("error = "+error);
						}
					});
				}
			}
		
		} catch (Exception e) {
			Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
		}
	}
	@SuppressLint("NewApi")
	public static void notifyReceive(String packageName, Notification notification)
	{
		if (notification != null) {
			
			RemoteViews remoteV = notification.contentView;
			//String packageString = remoteV.getPackage();
			remoteV.toString();
			
			//Log.e("package = "+packageString, "remotev.toString = "+remoteV.toString());
			
			if(null!=remoteV&&activity != null){
				activity.addToUi(notification,remoteV, packageName);
			}else {
				Log.e("未进入addtoview中", "activity是null");
			}
		}
		
	}
	
	@SuppressLint("NewApi")
	private void getDateByExtras(Notification notification,String packageName){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    		Bundle extras = notification.extras;
    		
    		
    		if (extras != null) {
    			Map<String, String> map = new HashMap<>();
    			// 获取通知标题
    		    String title = extras.getString(Notification.EXTRA_TITLE, "");
    		    // 获取通知内容
    		    String content = extras.getString(Notification.EXTRA_TEXT, "");
    		    
    		    
    		            
    		    Log.e(" extras info title = "+title , "content = "+content+" extras="+extras.toString());
    		    
    		    if (title!=null&&content!=null) {
					map.put("title", title);
					map.put("text", content);
					Date date = new Date();
					SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String tempTime = format1.format(date);
					map.put("time", tempTime);
					activity.sendData(map, packageName);
				}
    		    
    		}
    		
    	}
	}
	private String getTime(String date){
		String result = null;
		try {
			String[] strings = date.split(":");
			int time = 0;
			String string = null;
			if (strings.length>0) {
				string = strings[0].trim();
			}
			if (string.contains("下午")&&string.length()>2) {
				time = Integer.parseInt(string.substring(2).trim());
				time = time + 12;
			}else if (string.length()>2) {
				time = Integer.parseInt(string.substring(2).trim());
			}
			if (strings.length>1) {
				result = time+":"+strings[1];
			}
			else {
				result = time+":00";
			}
		} catch (NumberFormatException e) {
			result = null;
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	/**
	 * 判断text中是否包含某些关键词
	 * @param text
	 * @return
	 */
	private boolean isContainsText(String text)
	{
		boolean flag = false;
		
		String loading = "正在下载";
		if (text.indexOf(loading)!=-1) {
			flag = true;
		}
		if (text.indexOf("逍遥市场")!=-1) {
			flag = true;
		}
		if (text.indexOf("正在运行")!=-1) {
			flag = true;
		}
		if (text.indexOf("正在充电")!=-1) {
			flag = true;
		}
		if (text.indexOf("手机电池已充满")!=-1) {
			flag = true;
		}
		if (text.indexOf("豌豆荚")!=-1) {
			flag = true;
		}
		if (text.indexOf("USB调试")!=-1) {
			flag = true;
		}
		if (text.indexOf("发现新版本")!=-1) {
			flag = true;
		}
		if (text.indexOf("SD卡")!=-1) {
			flag = true;
		}
		if (text.indexOf("正在安装")!=-1) {
			flag = true;
		}
		if (text.indexOf("完成安装")!=-1) {
			flag = true;
		}
		if (text.indexOf("正在搜索")!=-1) {
			flag = true;
		}
		if (text.indexOf("应用下载")!=-1) {
			flag = true;
		}
		
		return flag;
	}
	
	/*public static void toggleNotificationListenerService(Context context) {  
        Log.e("MainActivity","toggleNLS");  
        PackageManager pm = context.getPackageManager();  
        pm.setComponentEnabledSetting(  
                new ComponentName(context, com.hylanda.getpush.NLService.class),  
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);  
  
        pm.setComponentEnabledSetting(  
                new ComponentName(context, com.hylanda.getpush.NLService.class),  
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);  
    }*/
	
	private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, com.hylanda.getpush.NLService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(this, com.hylanda.getpush.NLService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }
	private boolean isNull(String text){
		
		if (text==null) {
			return true;
		}
		if (text.length()==0) {
			return true;
		}
		if (text.trim().length()==0) {
			return true;
		}
		if (text.trim().equals("")) {
			return true;
		}
		return false ;
	}
}
