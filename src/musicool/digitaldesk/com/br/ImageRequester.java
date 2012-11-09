package musicool.digitaldesk.com.br;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;

import musicool.digitaldesk.com.br.ImageThreadLoader.ImageLoadedListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;



public class ImageRequester {
	private static ImageThreadLoader imgLoader;
	
	/**
	 * Setar essas duas variaveis com os valores a baixo no Activity base da aplicacao 
	 * ImageRequester.kExternalDir = getExternalFilesDir(null).getAbsolutePath();
	 *	ImageRequester.kInternalDir = getFilesDir().getAbsolutePath();
	 */
	public static String kfilesDir;// = Environment.getDataDirectory() +  "/com.digitaldesk.beco203/";
	
	/**
	 * 
	 * @param externalCard caso true � verificado se o dir no cartao SD j� foi criado
	 * 	caso false � verificado se j� foi criado a pasta diretamente no data do App.
	 * @return
	 */
	private static boolean checkFolderExist(boolean externalCard){
		File folder = null;
		if (externalCard)
			folder = new File(kfilesDir);//Dir Externo
		else
			folder = new File(kfilesDir);//Dir Interno
		if(!folder.exists())//Criar diretorio caso n exista
		    return folder.mkdir();
		else 
		    return true; 
	}
	/**
	 * 
	 * @param nomeImagem 
	 * @param imagem
	 * 
	 * Metodo que salva localmente a imagem carregada, caso possua cartao SD salva no cartao
	 * caso n tenha um cartao SD salva diretamente no data do App.
	 */
	private static void saveLocalImg(String nomeImagem, Bitmap imagem){
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			imagem.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			Log.d("beco203", nomeImagem + ": " + bytes.size());
			
			File f = new File(kfilesDir +  nomeImagem);

//			if(f.createNewFile()){
			try {
				FileOutputStream fo = new FileOutputStream(f);
				fo.write(bytes.toByteArray());
				Log.d("beco203", "fo -> " + nomeImagem + ": " + bytes.size());

			} catch (Exception e) {
		    // } else { 
				Log.e("beco203", "Outro erro fudido, erro: " + e.toString()) ;
			}
			
			
		} catch (Exception e) {
			Log.e("beco203", "Fudeu na hora de salvar a imagem, erro: " + e.toString());
			// e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param imageNome nome da Imagem que ser� carregada/baixada
	 * @param ImageUrl link de onde a imagem se econtra
	 * @param imgList 
	 */
	public final static void DownloadIfNotExistImage(final String imageNome, String ImageUrl, final ImageLoadedListener imgList){
		if(imgLoader == null){
			imgLoader = new ImageThreadLoader();
		}
		File dataFile = Environment.getExternalStorageDirectory();
		if (kfilesDir == null) {
			File file = new File(dataFile.toString() + "/Android/data/com.musicool.image");
			file.mkdirs();
			kfilesDir = file.toString() + "/";
		}
		
		Bitmap bitmap = BitmapFactory.decodeFile(kfilesDir + imageNome); //"TRY" load SD
		
		if(bitmap != null){
			imgList.imageLoaded(bitmap);
		}else{
			ImageLoadedListener imgListener = new ImageLoadedListener() {
				@Override
				public void imageLoaded(Bitmap imageBitmap) {
					imgList.imageLoaded(imageBitmap);
					saveLocalImg(imageNome,imageBitmap);
				}
			};
			
			try {
				imgLoader.loadImage(ImageUrl, imgListener);
			} catch (MalformedURLException e) {
				Log.e("beco203", "Load imagem " + imageNome + " > " + e.toString());
			} catch (Exception e) {
				Log.e("beco203", "Load imagem " + imageNome + " > " + e.toString());
			}
		}
	}
}
