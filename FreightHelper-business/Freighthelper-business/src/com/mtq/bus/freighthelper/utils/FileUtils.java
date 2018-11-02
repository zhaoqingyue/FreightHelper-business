package com.mtq.bus.freighthelper.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.ImageColumns;

@SuppressWarnings("deprecation")
public class FileUtils {

	public static final String BASE_PATH = "/MTQBusFreightHelperFile";
	public static final String PATH = "/MTQBusFreightHelperFile/";
	
	public static void initFile() {
		File file = new File(Environment.getExternalStorageDirectory()
				+ FileUtils.BASE_PATH);
		if (!file.exists())
			file.mkdirs();

		File file1 = new File(Environment.getExternalStorageDirectory()
				+ FileUtils.PATH + "tempFile");
		if (!file1.exists())
			file1.mkdirs();
	}

	public static String getFileStorePath() {
		return Environment.getExternalStorageDirectory() + PATH;
	}

	public static String getTmpCacheFilePath() {
		return Environment.getExternalStorageDirectory() + PATH + "tempFile";
	}

	/**
	 * 
	 * @Title: getFromAssets
	 * @Description: 读取本地文件信息(该方法可以换段落)
	 * @param fileName
	 *            : 本地文件名
	 * @return: String
	 */
	public static String getFromAssets(Context context, String fileName) {
		String text = "";
		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			int lenght = in.available(); // 获取文件的字节数
			byte[] buffer = new byte[lenght]; // 创建byte数组
			in.read(buffer); // 将文件中的数据读到byte数组中
			text = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}


	/** 
	 * @Title: delete
	 * @Description: 删除文件
	 * @param file
	 * @return: void
	 */
	public static void delete(File file) 
	{
		if (file.isFile()) 
		{  
			file.delete();  
			return;  
		}  
		
		if(file.isDirectory())
		{  
			File[] childFiles = file.listFiles();  
			if (childFiles == null || childFiles.length == 0) 
			{  
				file.delete();  
				return;
			}  
			
			for (int i=0; i<childFiles.length; i++) 
			{  
		        delete(childFiles[i]);  
			}  
	        file.delete();  
	    }  
	}
	public static String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { ImageColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}
	
	/**
	 * the traditional io way
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public static byte[] toByteArray(String filename) throws IOException {

		File f = new File(filename);
		if (!f.exists()) {
			throw new FileNotFoundException(filename);
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}
}
