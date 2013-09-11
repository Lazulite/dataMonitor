package com.example.datamonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.datamonitor.jsonlib.*;
import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


//private String baseURL = "http://wikihealth.bigdatapro.org/api/#!";

public class wikiUpload extends AsyncTask<String, Void, InputStream>{
	
    private String method=null;
    private String URL=null;
    private InputStream is = null;
	private Reader reader = null;
	private Class<?> obj,rlt;
	private Handler mhttpHandler;
	private List<NameValuePair> paras;
	private String jobj=null;
	
	private static final String TAG = "HttpClient";
	
    public wikiUpload(String _url, String _method, String _jobj , List<NameValuePair> _paras, Class<?>_obj) {
        URL=_url;
        method=_method;
        jobj = _jobj;
        obj=_obj;
        paras=_paras;
    }
    @Override
    protected InputStream doInBackground(String... param) {

        try {
        	if(method.equals("POST")){

        		DefaultHttpClient httpclient = new DefaultHttpClient();
        		HttpPost httpPostRequest = new HttpPost(URL);
        		httpPostRequest.setHeader("Accept", "application/json");
        		httpPostRequest.setHeader("Content-type", "application/json");
        		// Set HTTP parameters
        		StringEntity se = new StringEntity(jobj);
      			httpPostRequest.setEntity(se);
        		//Reader reader = new InputStreamReader(httpPostRequest.getEntity().getContent());
        		
//        		BufferedReader r = new BufferedReader(new InputStreamReader(httpPostRequest.getEntity().getContent()));
//        		StringBuilder total = new StringBuilder();
//        		String line;
//        		while ((line = r.readLine()) != null) {
//        		    total.append(line);
//        		}   		
//        		Log.e("In post", total.toString());
		
        		HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
        		is = response.getEntity().getContent();

        	}else if(method == "GET"){
            // request method is GET
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            if(paras!=null)
	            {	String paramString = URLEncodedUtils.format(paras, "utf-8");
	            	URL += "?" + paramString;
	            }
	            HttpGet httpGet = new HttpGet(URL);
	
	            HttpResponse httpResponse = httpClient.execute(httpGet);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            is = httpEntity.getContent();
        	}           

        } catch (UnsupportedEncodingException e) {
        	e.printStackTrace();
        } catch (ClientProtocolException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
    }

    return is;

   }
    
    protected void onPostExecute(Reader result) {
        // TODO: check this.exception 
        // TODO: do something with the feed
    	/*Message msg=new Message();
 		msg.obj=result;
 		//Log.e("wiki",result.toString());
 		mhttpHandler.sendMessage(msg);*/
    }
    
 }

/*class A{
	
}

class B extends A{
	void dosomething(){
		
	}
	
}

class wikiup{
	static A getjson(A bb){
		return (A)bb;
	}
}

class C{
	void test(){
		B b = new B();
		A bb = wikiup.getjson(b);
		bb.dosomething();
	}
}*/


