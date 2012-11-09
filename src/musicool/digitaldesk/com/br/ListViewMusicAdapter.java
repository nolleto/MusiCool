package musicool.digitaldesk.com.br;

import java.sql.SQLException;
import java.util.List;

import musicool.digitaldesk.com.br.R;

import database.digitaldesk.com.br.Artista;
import database.digitaldesk.com.br.DaoProvider;
import database.digitaldesk.com.br.Favoritos;
import database.digitaldesk.com.br.Musica;
import database.digitaldesk.com.br.TopArtista;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListViewMusicAdapter extends BaseAdapter {
	private Context context;
	private List<Musica> list;
	private Boolean[] listFavloritos;
	private DaoProvider daoProvider;
	
	public ListViewMusicAdapter(Context context, DaoProvider daoProvider, List<Musica> list) {
		super();
		this.context = context;
		this.list = list;
		this.daoProvider = daoProvider;
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
		Musica item = list.get(position);
		LinearLayout view = (LinearLayout)View.inflate(context, R.layout.list_item, null);
		
		ImageView img = (ImageView) view.findViewById(R.id.img_ListItem);
		img.setVisibility(LinearLayout.GONE);
		
		if (!item.nome.equals("Nenhuma mœsicas encontrada")) {
			Button btn = (Button) view.findViewById(R.id.btn_Favoritos);
			btn.setVisibility(LinearLayout.VISIBLE);
			
			if (daoProvider.getFavoritosRuntimeDao().queryForId(item.musicaId) == null) {
				btn.setBackgroundResource(R.drawable.star_0_large);
			} else {
				btn.setBackgroundResource(R.drawable.star_2_large);
			}
			
			final int index = position;
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					btn_Touch(index);
				}
			});
		}
		
		TextView name = (TextView) view.findViewById(R.id.text_ListItem);
		name.setText(item.nome);
		
		view.setTag(item.musicaId);
		
		return view;
	}
	
	public void btn_Touch(int position) {
		Musica musica = list.get(position);
		if (daoProvider.getFavoritosRuntimeDao().queryForId(musica.musicaId) == null) {
			try {
				Favoritos favoritos = new Favoritos(musica);
				daoProvider.getFavoritosDao().create(favoritos);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Favoritos favoritos = daoProvider.getFavoritosRuntimeDao().queryForId(musica.musicaId);
				daoProvider.getFavoritosDao().delete(favoritos);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		notifyDataSetChanged();
	}
	
}
