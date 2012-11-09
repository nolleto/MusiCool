package musicool.digitaldesk.com.br;

import java.util.List;

import org.w3c.dom.Text;

import database.digitaldesk.com.br.TopArtista;

import musicool.digitaldesk.com.br.ImageThreadLoader.ImageLoadedListener;
import musicool.digitaldesk.com.br.R;

import android.R.array;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ListViewTopAdapter extends BaseAdapter {

	public Bitmap[] imagens;
	public List<TopArtista> list;
	public Context currentContext;
	
	public ListViewTopAdapter(Context context, List<TopArtista> list) {
		super();
		this.currentContext = context;
		this.list = list;
		this.imagens = new Bitmap[list.size()];
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		
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
			convertView = (LinearLayout)View.inflate(currentContext, R.layout.list_item, null);
		}
		TopArtista item = list.get(position);
		LinearLayout view = (LinearLayout)View.inflate(currentContext, R.layout.list_item, null);
		
		final ImageView img = (ImageView) view.findViewById(R.id.img_ListItem);
		
		if (imagens[position] == null) {
			img.setImageResource(android.R.drawable.arrow_down_float);
			ShowImage(img, position);
		}
		else {
			img.setImageBitmap(imagens[position]);
		}
		
		TextView name = (TextView) view.findViewById(R.id.text_ListItem);
		name.setText((position + 1) + " " + item.artista.nome);
		
		view.setTag(item.artista.artistaId);
		
		return view;
	}
	
	public void ShowImage(final ImageView imgThumb, final int position) {
		ImageLoadedListener imgListener = new ImageLoadedListener() {
			public void imageLoaded(Bitmap imageBitmap) {
				imgThumb.setImageBitmap(imageBitmap);
				imgThumb.setScaleType(ImageView.ScaleType.FIT_XY);
				imgThumb.setVisibility(ImageView.VISIBLE);
				imagens[position] = imageBitmap;
				//noticeBox.removeViewInLayout(noticeBox.findViewById(R.idHome.pv_noticia_progressBar));
			}
		};
		
		String URL = list.get(position).artista.imagem;
		String[] cutURL = URL.split("/");
        String imageName = cutURL[cutURL.length - 1];
		
		ImageRequester.DownloadIfNotExistImage(imageName, URL, imgListener);
	}
	
	
} 
