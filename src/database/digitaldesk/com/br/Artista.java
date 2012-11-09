package database.digitaldesk.com.br;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import data.digitaldesk.com.br.TopProfile;

@DatabaseTable
public class Artista {

	@DatabaseField(id=true)
	public String artistaId;
	@DatabaseField(canBeNull=false)
	public String nome;
	@DatabaseField
	public String imagem;
	@DatabaseField
	public String descricao;
	
	public Artista() { }
	public Artista(TopProfile top) {
		this.artistaId = top.mbid;
		this.nome = top.name;
		this.imagem = top.image;
	}
	
	
	public String getName() {
		return nome;
	}
	
	public void setName(String nome) {
		this.nome = nome;
	}
	
	public String getID() {
		return artistaId;
	}
	
	public void setID(String artistaId) {
		this.artistaId = artistaId;
	}
	
	public String getImagem() {
		return imagem;
	}
	
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
