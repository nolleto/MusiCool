package data.digitaldesk.com.br;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import musicool.digitaldesk.com.br.BaseActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.Where;

import database.digitaldesk.com.br.Album;
import database.digitaldesk.com.br.Artista;
import database.digitaldesk.com.br.DaoProvider;
import database.digitaldesk.com.br.Musica;
import database.digitaldesk.com.br.TopArtista;


public class WSArtista {
	 private static final String KEY_LAST_FM = "&api_key=3d1f1a4373ac304e4db0832fe4e349e8";
	 private DaoProvider daoProvider;
	 
	 public WSArtista(DaoProvider daoProvider) {
		this.daoProvider = daoProvider;
	}
	 
	private boolean callTopArtists() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(new URI("http://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&format=json" + KEY_LAST_FM)); 
			HttpResponse response = client.execute(get);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
			    builder.append(line).append("\n");
			}
			
			JSONObject json = new JSONObject(builder.toString());
			JSONArray jArray = json.getJSONObject("artists").getJSONArray("artist");
			
			RuntimeExceptionDao<Artista, String> artistaDao = daoProvider.getArtistaRuntimeDao();
			RuntimeExceptionDao<TopArtista, Integer> topArtistaDao = daoProvider.getTopArtistaRuntimeDao();
			
			for(int i=0; i < jArray.length(); i++){
				JSONObject obj = jArray.getJSONObject(i);
				TopProfile top = new TopProfile(obj);
				TopArtista topArtista = new TopArtista();
				Artista artista = artistaDao.queryForId(top.mbid);
				
				if (artista == null) {
					artista =  new Artista(top);
					artistaDao.create(artista);
				}
				
				topArtista.artista = artista;
				topArtista.posicao = i+1;
				
				topArtistaDao.createOrUpdate(topArtista);
				
				if (i == 9) {
					break;
				}
			}
			
			return true;
		} catch (Exception e) {
				e.printStackTrace();
		}
		return false;
	}
	
	public void callInfoArtista(String mbid) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(new URI("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&mbid=" + mbid + "&format=json" + KEY_LAST_FM));
			HttpResponse response = client.execute(get);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
			    builder.append(line).append("\n");
			}
			
			JSONObject json = new JSONObject(builder.toString());
			JSONObject jObj = json.getJSONObject("artist");
			
			Artista artista = daoProvider.getArtistaRuntimeDao().queryForId(mbid);
			if (artista == null) {
				String nome = jObj.getString("name");
				JSONArray arrayTemp = jObj.getJSONArray("image");
				String image = arrayTemp.getJSONObject(arrayTemp.length() - 2).getString("#text");
				
				artista = new Artista();
				artista.nome = nome;
				artista.imagem = image;
				artista.artistaId = mbid;
				daoProvider.getArtistaDao().create(artista);
				
				artista = daoProvider.getArtistaRuntimeDao().queryForId(mbid);
			}
			
			if (artista.descricao == null) {
				String descricao = jObj.getJSONObject("bio").getString("content");
				artista.setDescricao(descricao);
				daoProvider.getArtistaDao().update(artista);
			}
			
			
		} catch (Exception e) {
			e.fillInStackTrace();
		}
	}
	
	public void callGetAlbumArtist(String mbid) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(new URI("http://ws.audioscrobbler.com/2.0/?method=artist.gettopalbums&format=json&mbid=" + mbid + KEY_LAST_FM));
			HttpResponse response = client.execute(get);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
			    builder.append(line).append("\n");
			}
			
			JSONObject obj = new JSONObject(builder.toString());
			JSONArray jArray = obj.getJSONObject("topalbums").getJSONArray("album");
			
			for (int i = 0; i < jArray.length(); i++) {
				long countAlbum = daoProvider.getAlbumDao().countOf(daoProvider.getAlbumDao().queryBuilder().setCountOf(true).where().eq("artistaId", mbid).prepare());
				if (jArray.length() != countAlbum) {
					Album album = daoProvider.getAlbumRuntimeDao().queryForId(jArray.getJSONObject(i).getString("mbid"));
					if (album == null) {
						album = new Album(daoProvider, jArray.getJSONObject(i));
						daoProvider.getAlbumDao().createOrUpdate(album);
					}
				}
				
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public ArrayList<TopProfile> callSearchArtist(String artist) {
		try {
			artist = artist.replace(" ", "%20");
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(new URI("http://ws.audioscrobbler.com/2.0/?method=artist.search&artist=" + artist + "&format=json" + KEY_LAST_FM)); 
			HttpResponse response = client.execute(get);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
			    builder.append(line).append("\n");
			}
			
			JSONObject json = new JSONObject(builder.toString());
			JSONArray jArray = json.getJSONObject("results").getJSONObject("artistmatches").getJSONArray("artist");
			
			ArrayList<TopProfile> list = new ArrayList<TopProfile>();
			
			for(int i=0; i < jArray.length(); i++){
				if (jArray.getJSONObject(i).getString("mbid") != null) {
					JSONObject obj = jArray.getJSONObject(i);
					TopProfile top = new TopProfile(obj);
					
					if (top.image.contains("http")) {
						list.add(top);
					}
				}
			}
			
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public void callGetAlbumInfo(String mbid) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(new URI("http://ws.audioscrobbler.com/2.0/?method=album.getinfo&format=json" + KEY_LAST_FM + "&artist=Cher&mbid=" + mbid));
			HttpResponse response = client.execute(get);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
			    builder.append(line).append("\n");
			}
			
			JSONObject json = new JSONObject(builder.toString());
			JSONArray jArray = json.getJSONObject("album").getJSONObject("tracks").getJSONArray("track");
			
			Album album = daoProvider.getAlbumRuntimeDao().queryForId(mbid);
			
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject obj = jArray.getJSONObject(i);
				Musica musica = daoProvider.getMusicaRuntimeDao().queryForId(obj.getString("mbid"));
				if (musica == null) {
					musica = new Musica(album, obj);
					daoProvider.getMusicaDao().create(musica);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean callTopArtistsThread () {
		AsyncTask<Void, Void, Boolean> taskGet = new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				
				return callTopArtists();
			}
		};
		taskGet.execute();
		boolean resposta = false;
		try {
			resposta = taskGet.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return resposta;
	}
	
	public void callGetAlbumArtistThread(String mbid) {
		AsyncTask<String, Void, Void> asyncTask = new AsyncTask<String, Void, Void>() {
			
			@Override
			protected Void doInBackground(String... params) {
				callGetAlbumArtist(params[0]);
				return null;
			}
			
		};
		
		asyncTask.execute(mbid);
	}
	
	public ArrayList<TopProfile> callSearchArtistThread (Context context, final String artist) {
		
		AsyncTask<String, Void, ArrayList<TopProfile>> taskGet = new AsyncTask<String, Void, ArrayList<TopProfile>>() {
			
			
			@Override
			protected ArrayList<TopProfile> doInBackground(String... params) {
				return callSearchArtist(params[0]);
			};
			

		};
		
		taskGet.execute(artist);
		ArrayList<TopProfile> resposta = null;
		try {
			resposta = taskGet.get();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return resposta;
		
	}
}
