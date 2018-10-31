package com.mtq.bus.freighthelper.utils;

import java.io.File;
import java.io.FileInputStream;

import android.util.Base64;

public class MtqStringUtils {
	/**
	* encodeBase64File:(将文件转成base64 字符串). <br/>
	* @param path 文件路径
	* @return
	* @throws Exception
	* @since JDK 1.6
	*/
	public static String encodeBase64File(String path) throws Exception {
	File  file = new File(path);
	FileInputStream inputFile = new FileInputStream(file);
	byte[] buffer = new byte[(int)file.length()];
	inputFile.read(buffer);
	        inputFile.close();
	        return Base64.encodeToString(buffer,Base64.DEFAULT);
	}

}
