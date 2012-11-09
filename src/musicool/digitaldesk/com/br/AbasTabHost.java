package musicool.digitaldesk.com.br;

import musicool.digitaldesk.com.br.R;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost.TabSpec;
import android.widget.TabHost;
import android.widget.TextView;

public class AbasTabHost extends TabActivity {

	private TabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_tab_host);
		
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		setupTab(new TextView(this), "Search");
		setupTab(new TextView(this), "Top");
		setupTab(new TextView(this), "Favorites");
		
		mTabHost.setCurrentTab(1);
	}
	private void setupTab(final View view, final String tag) {
		Intent intent = null;
		
		intent = new Intent().setClass(AbasTabHost.this, MusiCoolActivity.class);
		intent.putExtra("tab", tag);
		View tabview = createTabView(mTabHost.getContext(), tag);

		tabview.setBackgroundDrawable(getResources().getDrawable(R.layout.tab_bg_selector));

		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(intent);
		
		mTabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}
	
	
}
