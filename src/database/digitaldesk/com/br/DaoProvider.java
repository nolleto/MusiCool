package database.digitaldesk.com.br;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import musicool.digitaldesk.com.br.R;

public class DaoProvider extends OrmLiteSqliteOpenHelper {
	
	// Nome do arquivo do banco de dados
	private static final String DATABASE_NAME = "musiCool.db";
	// Versão do banco de dados. Incrementar sempre que alterações forem feitas no banco.
	// Modificado dia 20/07/2012
	private static final int DATABASE_VERSION = 1;
	
	private Context context;
	
	public DaoProvider(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
		this.context = context;
	}
	
	public DaoProvider(Context context, String databaseName,
			CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			android.util.Log.i(DaoProvider.class.getName(), "onCreate");
			TableUtils.createTableIfNotExists(connectionSource, Musica.class);
			TableUtils.createTableIfNotExists(connectionSource, Album.class);
			TableUtils.createTableIfNotExists(connectionSource, Artista.class);
			TableUtils.createTableIfNotExists(connectionSource, TopArtista.class);
			TableUtils.createTableIfNotExists(connectionSource, Favoritos.class);
		} catch (SQLException e) {
			android.util.Log.e(DaoProvider.class.getName(), "Erro ao criar banco de dados", e);
			throw new RuntimeException(e);
		}
	}
	
	/*public void dumpDatabaseToRoot() {
		
		File sd = Environment.getExternalStoragePublicDirectory("dump");
        File data = Environment.getDataDirectory();

		String currentDBPath = "/data/com.sanemob/databases/saneMob.db";
        String backupDBPath = "saneMob.db";
        
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        
        try {
	        if (currentDB.exists()) {
	            FileChannel src = new FileInputStream(currentDB).getChannel();
	            FileChannel dst = new FileOutputStream(backupDB).getChannel();
	            src.transferTo(0, src.size(), dst);
	            src.close();
	            dst.close();
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}*/
	
	public void clearDatabase(final ConnectionSource connectionSource) {
		try {
			TransactionManager.callInTransaction(connectionSource, new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					android.util.Log.i(DaoProvider.class.getName(), "cleanDatabase");
					TableUtils.clearTable(connectionSource, Musica.class);
					TableUtils.clearTable(connectionSource, Album.class);
					TableUtils.clearTable(connectionSource, Artista.class);
					TableUtils.clearTable(connectionSource, TopArtista.class);
					TableUtils.createTableIfNotExists(connectionSource, Favoritos.class);
					return null;
				}
			});
		} catch (java.sql.SQLException e) {
			android.util.Log.e(DaoProvider.class.getName(), "Erro ao limpar banco de dados", e);
			e.printStackTrace();
		}
	}
	
	// TODO: onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		// Obviamente vai ser maior... Mas vai que
		/*if(newVersion > oldVersion) {
			if(oldVersion < 2) {
				try {
					TableUtils.createTableIfNotExists(connectionSource, RegistroModificacao.class);
					TableUtils.createTableIfNotExists(connectionSource, RegistroConflito.class);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				RuntimeExceptionDao<FichaPre, String> dao = getFichaPreRuntimeDao();
				dao.executeRaw("ALTER TABLE 'FichaPre' ADD COLUMN DataPreenchimento DATETIME NULL", new String[]{});
				dao.executeRaw("ALTER TABLE 'FichaPre' ADD COLUMN Consentimento INTEGER NULL", new String[]{});
				dao.executeRaw("ALTER TABLE 'FichaPre' ADD COLUMN AnestesiologistaResponsavelId INTEGER NULL", new String[]{});
				
				android.util.Log.d("DatabaseUpdate", String.format("DB upgrade oldVersion: %d newVersion: %d", 1, 2));
			}
			// Logs
			android.util.Log.d("DatabaseUpdate", String.format("DB upgrade from version %d to version %d", oldVersion, newVersion));
			Log.logLocal(context, this, LogNivelEnum.INFO, LogGrupoEnum.GERAL, "DatabaseUpgrade", String.format("DB upgrade from version %d to version %d", oldVersion, newVersion));
			database.setVersion(DATABASE_VERSION);
		}*/
	}
	
	private Dao<Artista, String> artistaDao;
	private Dao<Album, String> albumDao;
	private Dao<Musica, String> musicaDao;
	private Dao<TopArtista, Integer> topArtistaDao;
	private Dao<Favoritos, String> favoritosDao;
	
	private RuntimeExceptionDao<Artista, String> artistaRuntimeDao;
	private RuntimeExceptionDao<Album, String> albumRuntimeDao;
	private RuntimeExceptionDao<Musica, String> musicaRuntimeDao;
	private RuntimeExceptionDao<TopArtista, Integer> topArtistaRuntimeDao;
	private RuntimeExceptionDao<Favoritos, String> favoritosRuntimeDao;

	public Dao<Artista, String> getArtistaDao() throws SQLException {
		if (artistaDao == null) {
			artistaDao = getDao(Artista.class);
		}
		return artistaDao;
	}

	public Dao<Album, String> getAlbumDao() throws SQLException {
		if (albumDao == null) {
			albumDao = getDao(Album.class);
		}
		return albumDao;
	}
	
	public Dao<Musica, String> getMusicaDao() throws SQLException {
		if (musicaDao == null) {
			musicaDao = getDao(Musica.class);
		}
		return musicaDao;
	}
	
	public Dao<TopArtista, Integer> getTopArtistaDao() throws SQLException {
		if (topArtistaDao == null) {
			topArtistaDao = getDao(TopArtista.class);
		}
		return topArtistaDao;
	}
	
	public Dao<Favoritos, String> getFavoritosDao() throws SQLException {
		if (favoritosDao == null) {
			favoritosDao = getDao(Favoritos.class);
		}
		return favoritosDao;
	}
	
	public RuntimeExceptionDao<Artista, String> getArtistaRuntimeDao() {
		if (artistaRuntimeDao == null) {
			artistaRuntimeDao = getRuntimeExceptionDao(Artista.class);
		}
		return artistaRuntimeDao;
	}
	
	public RuntimeExceptionDao<Album, String> getAlbumRuntimeDao() {
		if (albumRuntimeDao == null) {
			albumRuntimeDao = getRuntimeExceptionDao(Album.class);
		}
		return albumRuntimeDao;
	}

	public RuntimeExceptionDao<Musica, String> getMusicaRuntimeDao() {
		if (musicaRuntimeDao == null) {
			musicaRuntimeDao = getRuntimeExceptionDao(Musica.class);
		}
		return musicaRuntimeDao;
	}
	
	public RuntimeExceptionDao<TopArtista, Integer> getTopArtistaRuntimeDao() {
		if (topArtistaRuntimeDao == null) {
			topArtistaRuntimeDao = getRuntimeExceptionDao(TopArtista.class);
		}
		return topArtistaRuntimeDao;
	}
	
	public RuntimeExceptionDao<Favoritos, String> getFavoritosRuntimeDao() {
		if (favoritosRuntimeDao == null) {
			favoritosRuntimeDao = getRuntimeExceptionDao(Favoritos.class);
		}
		return favoritosRuntimeDao;
	}
	
	@Override
	public void close() {
		super.close();
		musicaRuntimeDao = null;
		artistaDao = null;
		albumDao = null;
		topArtistaDao = null;
		favoritosDao = null;
		
		musicaDao = null;
		artistaRuntimeDao = null;
		albumRuntimeDao = null;
		topArtistaRuntimeDao = null;
		favoritosRuntimeDao = null;
	}
	
}