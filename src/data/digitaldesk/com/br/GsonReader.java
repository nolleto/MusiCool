package data.digitaldesk.com.br;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class GsonReader {
	/** Instancia do Gson */
    private Gson mGson;
    /** ? */
    private JsonParser mJsonParser;
    /** Array de JSON */
    private JsonArray mJsonArray;
    /** ? */
    private JsonElement mJsonElement;
    /** String onde esta armazenado o JSON */
    private String mJson;

    public GsonReader(String json) {
            this.mJson = json;
            this.mGson = new Gson();
            this.mJsonParser = new JsonParser();
    }
    
    public void createArrayJSON() {
        this.mJsonArray = this.mJsonParser.parse(mJson).getAsJsonArray();
}

    public void createElementJSON() {
        this.mJsonElement = this.mJsonParser.parse(mJson).getAsJsonObject();
    }
    
    
    //TOP
    
	private TopProfile extractDataFromTopArtistasArrayJSON(JsonElement element) {
		return mGson.fromJson(element, TopProfile.class);
	}
	
	public ArrayList<TopProfile> extractArrayListTopProfile(){
		ArrayList<TopProfile> topList = new ArrayList<TopProfile>();
		
		 for (JsonElement element:mJsonArray){
			 TopProfile noticia = extractDataFromTopArtistasArrayJSON(element);
			 topList.add(noticia);
     }
		
		return topList;
	}
}
