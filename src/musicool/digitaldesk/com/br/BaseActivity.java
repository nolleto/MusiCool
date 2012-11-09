package musicool.digitaldesk.com.br;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import database.digitaldesk.com.br.DaoProvider;
import android.R.anim;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout.LayoutParams;

public class BaseActivity extends Activity{
	private DaoProvider databaseHelper = null;
	private ProgressDialog dialog;
	
	@Override
	protected void onDestroy() {
	        super.onDestroy();
	        if (databaseHelper != null) {
                OpenHelperManager.releaseHelper();
                databaseHelper = null;
            }
	        
//	        if (((MusiCoolApp) this.getApplication()).getHelper() != null) {
//	                OpenHelperManager.releaseHelper();
//	                ((MusiCoolApp) this.getApplication()).setNullHelper();
//	        }
	}

	public DaoProvider getHelper() {
		 if (databaseHelper == null) {
	          databaseHelper = OpenHelperManager.getHelper(this.getApplication(), DaoProvider.class);
	      }
	      return databaseHelper;
//	        return ((MusiCoolApp) this.getApplication()).getHelper();
	}
	
	public void ShowLoad(Context context, String title, String message) {
		
		dialog = new ProgressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.show();
	}
	
	public  void HideLoad() {
		dialog.hide();
	}
	
}
