package data.digitaldesk.com.br;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;

public class WebServiceClient {

	public String WebService() {
	try {
		URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=track.getInfo&format=json&api_key=3d1f1a4373ac304e4db0832fe4e349e8&artist=cher&track=believe");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		return readStream(con.getInputStream());
		} catch (Exception e) {
		e.printStackTrace();
	}
	return "";
	}



	private String readStream(InputStream in) {
		BufferedReader reader = null;
		String retorno = "";
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
			  retorno += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
			  try {
			    reader.close();
			  } catch (IOException e) {
			    e.printStackTrace();
		      }
			}
		}
		return retorno;
	} 
	
	public boolean isNetworkAvailable(Context content) {
	    ConnectivityManager cm = (ConnectivityManager) 
	    	content.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    // if no network is available networkInfo will be null
	    // otherwise check if we are connected
	    if (networkInfo != null && networkInfo.isConnected()) {
	        return true;
	    }
	    return false;
	} 
	
	
}
