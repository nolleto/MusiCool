package data.digitaldesk.com.br;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;

public class TopProfile {
    

	public String mbid, name, image;
	
	public TopProfile() {
		// TODO Auto-generated constructor stub
	}
	
	public TopProfile(JSONObject obj) {
		try {
			this.name = obj.getString("name");
			this.mbid = obj.getString("mbid");
			JSONArray array = obj.getJSONArray("image");
			this.image = array.getJSONObject(array.length()-2).getString("#text");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public TopProfile(String name, String mbid) {
		this.name = name;
		this.mbid = mbid;
		
	}
}
