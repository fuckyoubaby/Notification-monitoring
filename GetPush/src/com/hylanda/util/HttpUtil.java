package com.hylanda.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.hylanda.util.ThreadPoolHttpUtil.HttpCallback;

public class HttpUtil {
	
	public static void doPost(final String urlPath, final Map<String, String> map,final String packageName,final HttpCallback<String> callback)
	{
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				JSONObject jsonObject = new JSONObject();
		    	try {
		    		if (map.containsKey("title")) {
		    			jsonObject.put("title", map.get("title"));
					}
					if (map.containsKey("text")) {
						jsonObject.put("text", map.get("text"));
					}
					if (map.containsKey("time")) {
						jsonObject.put("time", map.get("time"));
					}
					if (map.containsKey("ip")) {
						jsonObject.put("ip", map.get("ip"));
					}
			    	jsonObject.put("mediaName", packageName);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        byte[] json = jsonObject.toString().getBytes();
		        BufferedReader bufferedReader = null;
		        StringBuffer result = new StringBuffer();
		        try {            
		            
		            URL url = new URL(urlPath);  
		             
		            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
		            httpURLConnection.setConnectTimeout(5000);     //设置连接超时时间
		            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
		            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
		            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
		            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
		            //设置请求体的类型是文本类型
		            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		            //设置请求体的长度
		            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(json.length));
		            //获得输出流，向服务器写入数据
		            OutputStream outputStream = httpURLConnection.getOutputStream();
		            outputStream.write(json);
		            
		            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
		            if(response == HttpURLConnection.HTTP_OK) {
		            	bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));  
                        String line = null;  
                        while ((line = bufferedReader.readLine()) != null) {  
                        	result.append(line);  
                        } 
		                callback.onSuccess(result.toString());            
		            }
		        } catch (IOException e) {
		            callback.onError(e.getMessage());
		        }finally{
		        	if (bufferedReader != null) {  
                        try {  
                            bufferedReader.close();  
                        } catch (IOException e) {  
                            e.printStackTrace();  
                        }  
                    }  
		        }
				
			
			}
		});
		
		thread.start();
	}

}
