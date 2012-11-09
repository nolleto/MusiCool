package musicool.digitaldesk.com.br;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
 
import org.apache.http.util.ByteArrayBuffer;
 
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.ImageView;

public class ImageManager {
	 
    private static String PATH = "/Android/com.musicool.image/";  //put the downloaded file here
    private static int IO_BUFFER_SIZE = 2048;
    
//    public static Bitmap loadBitmap(String url) {
//        Bitmap bitmap = null;
//        InputStream in = null;
//        BufferedOutputStream out = null;
//
//        try {
//            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);
//
//            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
//            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
//            copy(in, out);
//            out.flush();
//
//            final byte[] data = dataStream.toByteArray();
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            //options.inSampleSize = 1;
//
//            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
//        } catch (IOException e) {
//            Log.e("ImageManager", "Could not load Bitmap from: " + url);
//        } finally {
//            closeStream(in);
//            closeStream(out);
//        }
//
//        return bitmap;
//    }
    
    public boolean CheckImageInDevice(String url) {
    	String[] cutURL = url.split("/");
        String imageName = cutURL[cutURL.length - 1];
        
        File dataFile = Environment.getExternalStorageDirectory();
        
        File file = new File(dataFile.toString() + "/Android/data/com.musicool.image");
        
        File imageFile = new File(file, imageName);
        
        BitmapDrawable imagem = new BitmapDrawable(imageFile.toString());
        
    	if (imagem.getBitmap() == null) {
			return false;
		} else {
			return true;
		}
    }
    
    public static BitmapDrawable DownloadFromUrl(String imageURL) {  //this is the downloader method
            try {
                    URL url = new URL(imageURL); //you can write here any link
                    
                    String[] cutURL = imageURL.split("/");
                    String imageName = cutURL[cutURL.length - 1];
                    
                    //File file = new File(PATH + imageName);
                    
                    File dataFile = Environment.getExternalStorageDirectory();
                    
                    File file = new File(dataFile.toString() + "/Android/data/com.musicool.image");
                    
                    // have the object build the directory structure, if needed.
                    file.mkdirs();
                    // create a File object for the output file
                    //File outputFile = new File(dataFile+ "/Android/data/", "com.musicool.image");
                    
                    File imageFile = new File(file, imageName);
                    
                    BitmapDrawable existeImagem = new BitmapDrawable(imageFile.toString());

                    if (existeImagem.getBitmap() != null) {
						return existeImagem;
					} 

                    long startTime = System.currentTimeMillis();
                    Log.d("ImageManager", "download begining");
                    Log.d("ImageManager", "download url:" + url);
                    Log.d("ImageManager", "downloaded file name:" + file);
                    /* Open a connection to that URL. */
                    URLConnection ucon = url.openConnection();

                    /*
                     * Define InputStreams to read from the URLConnection.
                     */
                    InputStream is = ucon.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);

                    /*
                     * Read bytes to the Buffer until there is nothing more to read(-1).
                     */
                    ByteArrayBuffer baf = new ByteArrayBuffer(50);
                    int current = 0;
                    while ((current = bis.read()) != -1) {
                            baf.append((byte) current);
                    }
                    
                    /* Convert the Bytes read to a String. */
                    FileOutputStream fos = new FileOutputStream(imageFile);
                    fos.write(baf.toByteArray());
                    fos.close();
                    Log.d("ImageManager", "download ready in"
                                    + ((System.currentTimeMillis() - startTime) / 1000)
                                    + " sec");

                    return new BitmapDrawable(imageFile.toString());
                    
                    //criar img
            } catch (IOException e) {
                    Log.d("ImageManager", "Error: " + e);
                    e.printStackTrace();
            }
            return null;

    }
}