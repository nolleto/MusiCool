package musicool.digitaldesk.com.br;

import java.util.ArrayList;
import java.util.List;

import musicool.digitaldesk.com.br.ImageThreadLoader.ImageLoadedListener;

import android.R.anim;
import android.app.LauncherActivity.ListItem;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import data.digitaldesk.com.br.TopProfile;

public class ListViewSearchAdapter extends BaseAdapter {

	private List<TopProfile> list;
	private Bitmap[] imagens;
	private ArrayList<String> listaImagensDownloading;
	private Context context;
	
	public ListViewSearchAdapter(Context context, List<TopProfile> list) {
		super();
		this.list =list;
		this.context = context;
		imagens = new Bitmap[list.size()];
		listaImagensDownloading = new ArrayList<String>();
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = (LinearLayout)View.inflate(context, R.layout.list_item, null);
		}
			TopProfile listItem = list.get(position);
			
			LinearLayout view = (LinearLayout) View.inflate(context, R.layout.list_item, null);
			
			TextView text = (TextView) view.findViewById(R.id.text_ListItem);
			text.setText(listItem.name);
			ImageView img = (ImageView) view.findViewById(R.id.img_ListItem);

			if (text.getText().toString().equals("Procure por um artista.") || text.getText().toString().equals("Sem Resutados")) {
				img.setVisibility(LinearLayout.GONE);
			} else {
				if (imagens[position] == null) {
					img.setImageResource(android.R.drawable.alert_light_frame);
					if (CheckIsDownloadingImage(listItem.mbid)) {
						ShowImage(img, position);
					}
					
				} else {
					img.setImageBitmap(imagens[position]);
				}
			}
			
			view.setTag(listItem.mbid);
			
			return view;
		
	}
	
	public boolean CheckIsDownloadingImage(String Id) {
		if (listaImagensDownloading.contains(Id)) {
			return false;
		} else {
			listaImagensDownloading.add(Id);
			return true;
		}
	}
	
	public void ShowImage(final ImageView imgThumb, final int position) {
		ImageLoadedListener imgListener = new ImageLoadedListener() {
			public void imageLoaded(Bitmap imageBitmap) {
				imgThumb.setImageBitmap(imageBitmap);
				imgThumb.setScaleType(ImageView.ScaleType.FIT_XY);
				imgThumb.setVisibility(ImageView.VISIBLE);
				imagens[position] = imageBitmap;
				notifyDataSetChanged();
				//noticeBox.removeViewInLayout(noticeBox.findVi.ewById(R.idHome.pv_noticia_progressBar));
			}
		};
		
		String URL = list.get(position).image;
		String[] cutURL = URL.split("/");
        String imageName = cutURL[cutURL.length - 1];
		
        ImageRequester.DownloadIfNotExistImage(imageName, URL, imgListener);
	}
	
}
