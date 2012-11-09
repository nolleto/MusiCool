package musicool.digitaldesk.com.br;

import java.util.List;

import org.w3c.dom.Text;

import data.digitaldesk.com.br.WSArtista;
import database.digitaldesk.com.br.Album;
import database.digitaldesk.com.br.Artista;
import database.digitaldesk.com.br.Musica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import musicool.digitaldesk.com.br.ImageThreadLoader.ImageLoadedListener;
import musicool.digitaldesk.com.br.R;

public class ArtistInfoActivity extends BaseActivity {
	private TextView txtInfo, txtDescricao;
	private ImageView imgArtist;
	private String mbid;
	private Button btnMais;
	private LinearLayout linearScrollAlbum;
	Artista artista;
	public List<Album> listAlbum;
	public WSArtista ws;
	int larguraTxtDescricao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.artistinfoactivity);
		
		ws = new WSArtista(getHelper());
		mbid = getIntent().getExtras().getString("mbid");
		larguraTxtDescricao = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, getResources().getDisplayMetrics());
		artista = getHelper().getArtistaRuntimeDao().queryForId(mbid);
		
		listAlbum = getHelper().getAlbumRuntimeDao().queryForEq("artistaId", artista.artistaId);
		
		txtInfo = (TextView) findViewById(R.id.txt_Info_ArtistInfoActivity);
		txtInfo.setText(artista.nome);
		
		imgArtist = (ImageView) findViewById(R.id.img_ArtistInfoActivity);
		ShowImageArtista();
		
		txtDescricao = (TextView) findViewById(R.id.txt_Descricao_ArtistInfoActivity);
		txtDescricao.setText(Html.fromHtml(artista.descricao));
		
		btnMais = (Button) findViewById(R.id.btn_Show_Descricao);
		btnMais.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (btnMais.getText().toString().equals("More")) {
					txtDescricao.setLayoutParams(new LinearLayout.LayoutParams(larguraTxtDescricao, ViewGroup.LayoutParams.WRAP_CONTENT));
					btnMais.setText("Less");
				} else {
					float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
					txtDescricao.setLayoutParams(new LinearLayout.LayoutParams(larguraTxtDescricao, (int)px));
					btnMais.setText("More");
				}
				
			}
		});
		
		linearScrollAlbum = (LinearLayout) findViewById(R.id.linear_Albums_ArtistInfoActivity);
		
		CheckAlbum();
	}
	
	private void CheckAlbum() {
		int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
		for (int i = 0; i < listAlbum.size(); i++) {
			ImageView image = new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(px, px);
			params.setMargins(10, 0, 10, 0);
			image.setLayoutParams(params);
			image.setImageResource(android.R.drawable.alert_light_frame);
			image.layout(10, 0, 0, 0);
			
			final int index = i;
			image.setOnClickListener(new OnClickListener() {
				
				final ProgressDialog dialolg = new ProgressDialog(ArtistInfoActivity.this);
				@Override
				public void onClick(View v) {
					
					AsyncTask<String, Void, Void> asyncTask = new AsyncTask<String, Void, Void>() {
						
						@Override
						protected void onPreExecute() {
							dialolg.setTitle("Carregando ...");
							dialolg.setMessage("Carregando musicas do ambum " + listAlbum.get(index).nome);
							dialolg.setCancelable(false);
							dialolg.show();
						};
						
						@Override
						protected Void doInBackground(String... params) {
							List<Musica> listMusicas = getHelper().getMusicaRuntimeDao().queryForEq("albumId", params[0]);
							
							if (listMusicas.size() < 1) {
								ws.callGetAlbumInfo(params[0]);
								listMusicas = getHelper().getMusicaRuntimeDao().queryForEq("albumId", params[0]);
							
							 }
							
							Intent intent = new Intent(ArtistInfoActivity.this, MusicInfoActivity.class);
							Bundle extras = new Bundle();
							extras.putString("mbid", listAlbum.get(index).albumId);
							intent.putExtras(extras);
							startActivity(intent);
							return null;
						}
						
						@Override
						protected void onPostExecute(Void result) {
							dialolg.dismiss();
							super.onPostExecute(result);
						}
					};
					
					asyncTask.execute(listAlbum.get(index).albumId);
				}
			});
			
			linearScrollAlbum.addView(image);
			String url = listAlbum.get(i).imagem;
			ShowImageAlbum(image, url);
			
//			if (i == 3) { //DEBUG
//				break;
//			}
		}
	}
	
	public void ShowImageAlbum(final ImageView image, String url) {
		ImageLoadedListener imgListener = new ImageLoadedListener() {
			public void imageLoaded(Bitmap imageBitmap) {
				image.setImageBitmap(imageBitmap);
//				imgArtist.setScaleType(ImageView.ScaleType.FIT_CENTER);
				image.setVisibility(ImageView.VISIBLE);
				//noticeBox.removeViewInLayout(noticeBox.findViewById(R.idHome.pv_noticia_progressBar));
			}
		};
		
		String[] cutURL = url.split("/");
        String imageName = cutURL[cutURL.length - 1];
		
		ImageRequester.DownloadIfNotExistImage(imageName, url, imgListener);
	}
	
	public void ShowImageArtista() {
		ImageLoadedListener imgListener = new ImageLoadedListener() {
			public void imageLoaded(Bitmap imageBitmap) {
				imgArtist.setImageBitmap(imageBitmap);
//				imgArtist.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imgArtist.setVisibility(ImageView.VISIBLE);
				//noticeBox.removeViewInLayout(noticeBox.findViewById(R.idHome.pv_noticia_progressBar));
			}
		};
		
		String URL = artista.imagem;
		String[] cutURL = URL.split("/");
        String imageName = cutURL[cutURL.length - 1];
		
		ImageRequester.DownloadIfNotExistImage(imageName, URL, imgListener);
	}
	
}
