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
		            httpURLConnection.setConnectTimeout(5000);     //�������ӳ�ʱʱ��
		            httpURLConnection.setDoInput(true);                  //�����������Ա�ӷ�������ȡ����
		            httpURLConnection.setDoOutput(true);                 //����������Ա���������ύ����
		            httpURLConnection.setRequestMethod("POST");     //������Post��ʽ�ύ����
		            httpURLConnection.setUseCaches(false);               //ʹ��Post��ʽ����ʹ�û���
		            //������������������ı�����
		            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		            //����������ĳ���
		            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(json.length));
		            //�����������������д������
		            OutputStream outputStream = httpURLConnection.getOutputStream();
		            outputStream.write(json);
		            
		            int response = httpURLConnection.getResponseCode();            //��÷���������Ӧ��
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
