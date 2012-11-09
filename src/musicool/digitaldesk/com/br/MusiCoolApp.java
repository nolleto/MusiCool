package musicool.digitaldesk.com.br;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import database.digitaldesk.com.br.DaoProvider;
import android.app.Application;

public class MusiCoolApp extends Application{
	
	private DaoProvider databaseHelper = null;
	
	public DaoProvider getHelper() {
		if (databaseHelper == null) {
			databaseHelper =
					OpenHelperManager.getHelper(this, DaoProvider.class);
		}
		return databaseHelper;
	}

	public void setNullHelper() {
		databaseHelper = null;
	}
	
}
