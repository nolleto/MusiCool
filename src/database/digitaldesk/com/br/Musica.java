package database.digitaldesk.com.br;

import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Musica {

	@DatabaseField(id=true)
	public String musicaId;
	@DatabaseField(columnName="albumId", foreign=true, foreignAutoRefresh=true, canBeNull=false)
	public Album album;
	@DatabaseField
	public String nome;
	
	public Musica() { }
	
	public Musica(Album album, JSONObject obj) {
		try {
			this.musicaId = obj.getString("mbid");
			this.nome = obj.getString("name");
			this.album = album;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public String getMusicaId() {
		return musicaId;
	}

	public void setMusicaId(String musicaId) {
		this.musicaId = musicaId;
	}

	public String getName() {
		return nome;
	}
	
	public void setName(String nome) {
		this.nome = nome;
	}
	
	public Album getAlbum() {
		return album;
	}
	
	public void setAlbum(Album album) {
		this.album = album;
	}
}
