package musicool.digitaldesk.com.br;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import musicool.digitaldesk.com.br.ImageThreadLoader.ImageLoadedListener;

import database.digitaldesk.com.br.Album;
import database.digitaldesk.com.br.DaoProvider;
import database.digitaldesk.com.br.Favoritos;
import database.digitaldesk.com.br.Musica;

import android.R.array;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListViewFavoritesAdapter extends BaseAdapter {

	private List<Favoritos> list;
	private Context context;
	private DaoProvider daoProvider;
	public HashMap<String, Bitmap> listaImagens;
	private ArrayList<String> listaImagensDownloading;
	private HashMap<String, View> listCell;
	
	public ListViewFavoritesAdapter(Context context, DaoProvider daoProvider, List<Favoritos> list) {
		super();
		this.list = list;
		this.context = context;
		this.daoProvider = daoProvider;
		listaImagens = new HashMap<String, Bitmap>();
		listaImagensDownloading = new ArrayList<String>();
		listCell = new HashMap<String, View>();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = (LinearLayout)View.inflate(context, R.layout.list_item, null);
		}
		
		Favoritos item = list.get(position);
		
		if (listCell.containsKey(item.mbid)) {
			return listCell.get(item.mbid);
		} 
		
		LinearLayout view = (LinearLayout) View.inflate(context, R.layout.list_item, null);
		
		ImageView img = (ImageView) view.findViewById(R.id.img_ListItem);
		
		if (item.name.equals("Sem musicas adicionadas em Favoritos")) {
			img.setVisibility(LinearLayout.GONE);
		} else {
			if (listaImagens.containsKey(item.albumId)) {
				img.setImageBitmap(listaImagens.get(item.albumId));
			} else {
				Album album = daoProvider.getAlbumRuntimeDao().queryForId(item.albumId);
				if (CheckIsDownloadingImage(item.albumId)) {
					ShowImage(img, album.imagem, album.albumId);
				} else {
					img.setImageBitmap(listaImagens.get(item.albumId));
				}
				
			}
		}
		
		TextView txt = (TextView) view.findViewById(R.id.text_ListItem);
		txt.setText(item.name);
		
		listCell.put(item.mbid, view);
		
		return view;
	}

	public boolean CheckIsDownloadingImage(String albumId) {
		if (listaImagensDownloading.contains(albumId)) {
			return false;
		} else {
			listaImagensDownloading.add(albumId);
			return true;
		}
	}
	
	public void ShowImage(final ImageView imgThumb, final String url, final String albumId) {
		ImageLoadedListener imgListener = new ImageLoadedListener() {
			public void imageLoaded(Bitmap imageBitmap) {
				imgThumb.setImageBitmap(imageBitmap);
				imgThumb.setScaleType(ImageView.ScaleType.FIT_XY);
				imgThumb.setVisibility(ImageView.VISIBLE);
				listaImagens.put(albumId, imageBitmap);
				//noticeBox.removeViewInLayout(noticeBox.findVi.ewById(R.idHome.pv_noticia_progressBar));
			}
		};
		
		String[] cutURL = url.split("/");
        String imageName = cutURL[cutURL.length - 1];
		
        ImageRequester.DownloadIfNotExistImage(imageName, url, imgListener);
	}
	
}
