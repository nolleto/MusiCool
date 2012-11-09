package database.digitaldesk.com.br;

import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Favoritos {

	@DatabaseField(columnName="artistaId", foreign=true, foreignAutoRefresh=true, canBeNull=false)
	public Artista artista;
	@DatabaseField(id=true)
	public String mbid;
	@DatabaseField
	public String albumId;
	@DatabaseField
	public String name;
	@DatabaseField
	public String image;
	
	public Favoritos() {
		// TODO Auto-generated constructor stub
	}
	
	public Favoritos(Musica musica) {
		this.artista = musica.album.artista;
		this.mbid = musica.musicaId;
		this.name = musica.nome;
		this.albumId = musica.album.albumId;
		this.image = musica.album.imagem;
	}

	public Artista getArtista() {
		return artista;
	}

	public void setArtista(Artista artista) {
		this.artista = artista;
	}

	public String getTrack() {
		return albumId;
	}

	public void setTrack(String track) {
		this.albumId = track;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMbid() {
		return mbid;
	}

	public void setMbid(String mbid) {
		this.mbid = mbid;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
	
}
