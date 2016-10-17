package br.com.thesource.mobshop.wservices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.JsonReader;

import br.com.thesource.mobshop.util.ConnectionUtil;
import br.com.thesource.mobshop.util.LogUtil;

import org.apache.http.NameValuePair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

class RESTConnection {
	
	private Reader reader = null;
	private JsonReader jsonReader = null;
	
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_DELETE = "DELETE";
	public static final String METHOD_PUT = "PUT";
	public static final String METHOD_HEAD = "HEAD";
	public static final String METHOD_TRACE = "TRACE";
	
	
	/*---METODOS VISIVEIS-------------------------------------------------------------------------*/
	
	public JsonReader request(String resourceName, String httpMethod, Activity activity) throws IOException {
		return processRequestWithoutParams(resourceName,httpMethod, activity);
	}
	
	public JsonReader request(String resourceName, List<NameValuePair> params, String httpMethod, Activity activity) throws IOException {
		return processRequestWithParams(resourceName, params, httpMethod, activity);
	}
	
	/*------------------------------------------------------------------------------------------*/
	
	
	private JsonReader processRequestWithoutParams(String resourceName, String httpMethod, Activity activity) throws IOException {
		return connect(resourceName, httpMethod, null, activity);
	}
	
	
	private JsonReader processRequestWithParams(String resourceName, List<NameValuePair> params, String httpMethod, Activity activity) throws IOException {
		return connect(resourceName, httpMethod, params, activity);
	}
	
	@SuppressLint("NewApi")
	private JsonReader connect(String resourceName, String httpMethod, List<NameValuePair> params, Activity activity) throws IOException {
        ConnectionUtil.verificaConexaoWebService(activity);
        //SE TEM PARAMETROS EXECUTA
        if(params != null){
        	resourceName+="?"+getQuery(params);
        	LogUtil.printInfo(resourceName);
        }

        URL url = new URL(resourceName);
        HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
        httpUrlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        httpUrlConn.setRequestMethod(httpMethod);
        httpUrlConn.setDoInput(true);
        httpUrlConn.setReadTimeout(10000);
        //httpUrlConn.setDoOutput(true);
        httpUrlConn.setUseCaches(false);
        
     
        httpUrlConn.connect();		
		
		reader =  new InputStreamReader(httpUrlConn.getInputStream(), "UTF-8");
		jsonReader = new JsonReader(reader);

		return jsonReader;
	}
	
	
	private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;

	    for (NameValuePair pair : params){
	        if (first)
	            first = false;
	        else
	            result.append("&");

	        result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
	        result.append("=");
	        result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
	    }
	    return result.toString();
	}	
	
	public void clear(){
		reader = null;
		jsonReader = null;
	}
	
}