package musicool.digitaldesk.com.br;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import data.digitaldesk.com.br.TopProfile;
import data.digitaldesk.com.br.WSArtista;
import data.digitaldesk.com.br.WebServiceClient;
import database.digitaldesk.com.br.Album;
import database.digitaldesk.com.br.Artista;
import database.digitaldesk.com.br.DaoProvider;
import database.digitaldesk.com.br.Favoritos;
import database.digitaldesk.com.br.Musica;
import database.digitaldesk.com.br.TopArtista;

import musicool.digitaldesk.com.br.R;
import android.R.layout;
import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MusiCoolActivity extends BaseActivity {
    /** Called when the activity is first created. */
	
	String infoTab;
	ListView listMainContent;
	Button btnSearch;
	TextView txtView;
	List<TopArtista> listTabTop;
	List<TopProfile> listTabSearch;
	List<Favoritos> listTabFavorites;
	WSArtista ws;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        infoTab = getIntent().getExtras().getString("tab");
        ws = new WSArtista(getHelper());
        listMainContent = (ListView) findViewById(R.id.list_Content);
        txtView = (TextView) findViewById(R.id.txt_Top);
        btnSearch = (Button) findViewById(R.id.btn_Search);
        
        if (infoTab.equals("Search")) {
			Search_TAB();
		} else if (infoTab.equals("Top")) {
			ws.callTopArtistsThread();
			
			Top_TAB();

		} else {
			Favorites_TAB();
		}
       
        EditText txtSearch = (EditText) findViewById(R.id.txt_Search_Editable);
        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == btnSearch.getImeActionId()) {
					btnSearch_Touch();
		            return true;
		        }
				return false;
			}
		});
        
        btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnSearch_Touch();
				
			}
		});
        
        listMainContent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if (!infoTab.equals("Favorites")) {
					if ((infoTab.equals("Search")  && (listTabSearch.get(0).name.equals("Procure por um artista.") == true || listTabSearch.get(0).name.equals("Sem Resutados") == true)) == false || infoTab.equals("Top") ) {
						CellSelect(view);
					}
				} 
				
			}

			
		});
        
    }
    
    public void Search_TAB () {
    	((LinearLayout) findViewById(R.id.linear_Search)).setVisibility(LinearLayout.VISIBLE);
		txtView.setVisibility(LinearLayout.GONE);
    	
    	if (listTabSearch == null) {
    		listTabSearch = new ArrayList<TopProfile>();
    		listTabSearch.add(new TopProfile("Procure por um artista.", "00000"));
    	}
    		ListViewSearchAdapter adapter = new ListViewSearchAdapter(MusiCoolActivity.this, listTabSearch);
        	listMainContent.setAdapter(adapter);
    }
    
    public void Top_TAB() {
    	txtView.setText("Top Artists");
    	
		listTabTop = getHelper().getTopArtistaRuntimeDao().queryForAll();
	        
	    ListViewTopAdapter adapter = new ListViewTopAdapter(MusiCoolActivity.this, listTabTop);
	    listMainContent.setAdapter(adapter);
	   
    }
    
    public void Favorites_TAB() {
    	txtView.setText("Favorites");
    	
		listTabFavorites = getHelper().getFavoritosRuntimeDao().queryForAll();
		
		if (listTabFavorites.size() == 0) {
			Favoritos favoritos = new Favoritos();
			favoritos.name = "Sem musicas adicionadas em Favoritos";
			listTabFavorites.add(favoritos);
		}
		
		ListViewFavoritesAdapter adapter = new ListViewFavoritesAdapter(MusiCoolActivity.this, getHelper(), listTabFavorites);
		listMainContent.setAdapter(adapter);
    }
    

    private void btnSearch_Touch() {
    	Thread thread = new Thread(new Runnable() {
    		ProgressDialog dialog = new ProgressDialog(MusiCoolActivity.this);
    		@Override
			public void run() {
    			runOnUiThread(new Runnable() {
                    
                    @Override
                    public void run() {
                    	dialog.setTitle("Procurando . . .");
        				dialog.setMessage("Tentando achar " + ((EditText) findViewById(R.id.txt_Search_Editable)).getText().toString());
        				dialog.setCancelable(false);
        				dialog.show();
        	
        		        			}
    			});
    			listTabSearch = ws.callSearchArtist(((EditText) findViewById(R.id.txt_Search_Editable)).getText().toString());
    			
    			if (listTabSearch == null) {
    				listTabSearch = new ArrayList<TopProfile>();
					listTabSearch.add(new TopProfile("Sem Resutados", "000000000"));
				}
    			
    			runOnUiThread(new  Runnable() {
					public void run() {
				    	ListViewSearchAdapter adapter = new ListViewSearchAdapter(MusiCoolActivity.this, listTabSearch);
        		    	listMainContent.setAdapter(adapter);
        		    	
        		    	dialog.dismiss();
					}
				});	
			}
		});
    	
    	thread.start();
    }
    
    public void CellSelect(final View view) {
    	final String mbid = (String) view.getTag();
    		
    	AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
    		ProgressDialog dialog;
    		
    		@Override
    		protected void onPreExecute() {
    			dialog = new ProgressDialog(MusiCoolActivity.this);
    			dialog.setTitle("Carregando . . .");
				dialog.setMessage("Aguarde enquando as informação do artista são baixadas");
				dialog.setCancelable(false);
				dialog.show();
    		};
    		
			@Override
			protected Void doInBackground(Void... params) {
				Artista artista = new Artista();
				artista = getHelper().getArtistaRuntimeDao().queryForId(mbid);
				
				if (artista == null) {
					ws.callInfoArtista(mbid);
					artista = getHelper().getArtistaRuntimeDao().queryForId(mbid);
				} else {
					if (artista.descricao == null) {
						ws.callInfoArtista(mbid);
						artista = getHelper().getArtistaRuntimeDao().queryForId(mbid);
					}
				}
				
				long countAlbum = 0;
				try {
					countAlbum = getHelper().getAlbumDao().countOf(getHelper().getAlbumDao().queryBuilder().setCountOf(true).where().eq("artistaId", mbid).prepare());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (countAlbum < 10) {
					ws.callGetAlbumArtist(mbid);
				}
				
				Intent i = new Intent(getApplicationContext(), ArtistInfoActivity.class);
				Bundle extras = new Bundle();
				extras.putString("mbid", mbid);
				i.putExtras(extras);
				startActivity(i);
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				dialog.dismiss();
				super.onPostExecute(result);
			}
    		
    	};
    	
    	asyncTask.execute();
    	
    	
    	
    }

     @Override
    protected void onResume() {
    	if (infoTab.equals("Favorites")) {
    		listTabFavorites = getHelper().getFavoritosRuntimeDao().queryForAll();
    		
    		if (listTabFavorites.size() == 0) {
    			Favoritos favoritos = new Favoritos();
    			favoritos.name = "Sem musicas adicionadas em Favoritos";
    			listTabFavorites.add(favoritos);
    		}
    		
    		ListViewFavoritesAdapter adapter = new ListViewFavoritesAdapter(MusiCoolActivity.this, getHelper(), listTabFavorites);
    		listMainContent.setAdapter(adapter);
		}
    	super.onResume();
    }
	
     @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
    	 
    	 
    	return super.onTouchEvent(event);
    }
     
}