package database.digitaldesk.com.br;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Album {

	@DatabaseField(id=true)
	public String albumId;
	@DatabaseField(columnName="artistaId", foreign=true, foreignAutoRefresh=true, canBeNull=false)
	public Artista artista;
	@DatabaseField
	public String nome;
	@DatabaseField
	public String imagem;
	
	public Album() { }
	
	public Album(DaoProvider daoProvider , JSONObject obj) {
		try {
			this.albumId = obj.getString("mbid");
			this.nome = obj.getString("name");
			
			this.artista = new Artista();
			artista = daoProvider.getArtistaRuntimeDao().queryForId(obj.getJSONObject("artist").getString("mbid"));;
			
			JSONArray tempArray = obj.getJSONArray("image");
			this.imagem = tempArray.getJSONObject(tempArray.length() - 2).getString("#text");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getId() {
		return albumId;
	}
	public void setId(String albumId) {
		this.albumId = albumId;
	}
	public Artista getArtista() {
		return artista;
	}
	public void setArtistaId(Artista artistaId) {
		this.artista = artistaId;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getImagem() {
		return imagem;
	}
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

}
