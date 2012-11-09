package musicool.digitaldesk.com.br;

import java.util.List;

import data.digitaldesk.com.br.WSArtista;
import database.digitaldesk.com.br.Album;
import database.digitaldesk.com.br.Artista;
import database.digitaldesk.com.br.Favoritos;
import database.digitaldesk.com.br.Musica;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import musicool.digitaldesk.com.br.R.raw;

public class MusicInfoActivity extends BaseActivity {
	private ListView listView;
	private List<Musica> listMusicas;
	private TextView txtInfo;
	private WSArtista ws;
	private String mbid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musicinfoactivity);
		
		mbid = getIntent().getExtras().getString("mbid");
		ws = new WSArtista(getHelper());
		
		Album album = getHelper().getAlbumRuntimeDao().queryForId(mbid);
		listMusicas = getHelper().getMusicaRuntimeDao().queryForEq("albumId", mbid);
		
		if (listMusicas.size() < 1) {
			Musica semMusica = new Musica();
			semMusica.nome = "Nenhuma mœsicas encontrada";
			listMusicas.add(semMusica);
		 }
		
		txtInfo = (TextView) findViewById(R.id.txt_Info_MusicInfoActivity);
		txtInfo.setText(album.nome);
		
		listView = (ListView) findViewById(R.id.list_Contente_MusicInfoActivity);
		ListViewMusicAdapter adapter = new ListViewMusicAdapter(this, getHelper(), listMusicas);
		listView.setAdapter(adapter);
		
		
	}
	
}
