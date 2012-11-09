package database.digitaldesk.com.br;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TopArtista {

	@DatabaseField(columnName="artistaId", foreign=true, foreignAutoRefresh=true, canBeNull=false)
	public Artista artista;
	@DatabaseField(id=true)
	public int posicao;
	
	public TopArtista() { }

	public Artista getId() {
		return artista;
	}

	public void setId(Artista artista) {
		this.artista = artista;
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}
	
	
	
}
