package com.example.datamonitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import com.example.datamonitor.jsonlib.*;
import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
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
	
    private JSONObject jsonParams=null;
    private String method=null;
    private String URL=null;
    private InputStream is = null;
	private Reader reader = null;
	private Class<?> obj,rlt;
	private Handler mhttpHandler;
	
	private static final String TAG = "HttpClient";
    
    public wikiUpload(String _url, String _method, JSONObject _jsonObj, Class<?>_obj) {
        URL=_url;
        method=_method;
        jsonParams=_jsonObj;
        obj=_obj;
    }
    @Override
    protected InputStream doInBackground(String... params) {

        try {
        	if(method.equals("POST")){

        		DefaultHttpClient httpclient = new DefaultHttpClient();
        		HttpPost httpPostRequest = new HttpPost(URL);

        		StringEntity se = new StringEntity(jsonParams.toString());

        		// Set HTTP parameters
        		httpPostRequest.setEntity(se);
        		httpPostRequest.setHeader("Accept", "application/json");
        		httpPostRequest.setHeader("Content-type", "application/json");
		
        		HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
        		is = response.getEntity().getContent();

        	}else if(method == "GET"){
            // request method is GET
            /*DefaultHttpClient httpClient = new DefaultHttpClient();
            String paramString = URLEncodedUtils.format(postparams, "utf-8");
            url += "?" + paramString;
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();*/
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


