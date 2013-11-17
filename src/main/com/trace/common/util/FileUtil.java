package com.trace.common.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

public class FileUtil {
	
	private static final Log logger = LogFactory.getLog(FileUtil.class);

	public static boolean checkFileFormat(String fileName, String formatStr) {
		if (StringUtils.isBlank(formatStr)) {
			return false;
		}
		String[] formats = formatStr.split(",");
		String file = fileName.toLowerCase();
		for (String element : formats) {
			if (file.endsWith("." + element)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean saveFile(InputStream in, String fileName) {
		File outFile = new File(fileName);
		try {
			FileOutputStream out = new FileOutputStream(outFile);
			byte[] temp = new byte[10240];
			int length = -1;
			while ((length = in.read(temp)) > 0)
				out.write(temp, 0, length);
			out.flush();
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return false;
		}
		return true;
	}
	
	public static boolean saveFile(String in, String fileName,String enconding){
		
		byte[] data = null;
		try {
			data = in.getBytes(enconding);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File file = new File(fileName);
		File parentFile = file.getParentFile();
		if (!parentFile.exists() && !parentFile.isDirectory()) {
			parentFile.mkdirs();
		}
		try {
			DataOutputStream out = new DataOutputStream(new
			FileOutputStream(file));
			for (int i = 0; i < data.length; i++)
			out.write(data[i]);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.getStackTrace());
			return false;
		}
		return true;
	}
	
	public static void delFile(String path) {
		File file = new File(path);
		if (file.isDirectory()) {
			for (String fileName : file.list()) {
				delFile(path + "/" + fileName);
			}
		}
		file.delete();
	}
	
	/**
	 * 复制目录下的文件（不包括该目录）到指定目录，会连同子目录一起复制过去
	 * @param targetDir
	 * @param path
	 */
	public static void copyFileFromDir(String targetDir, String path) {
		File file = new File(path);
		createFile(targetDir, false);
		if (file.isDirectory()) {
			copyFileToDir(targetDir, listFile(file));
		}
	}
	
	/**
	 * 复制目录下的文件（不包含该目录和子目录，只复制目录下的文件）到指定目录
	 * @param targetDir
	 * @param path
	 */
	public static void copyFileOnly(String targetDir, String path) {
		File file = new File(path);
		File targetFile = new File(targetDir);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File subFile : files) {
				if (subFile.isFile()) {
					copyFile(targetFile, subFile);
				}
			}
		}
	}
	
	/**
	 * 复制目录到指定目录。targetDir是目标目录，path是源目录
	 * 该方法会将path以及path下的文件和子目录全部复制到目标目录
	 * @param targetDir
	 * @param path
	 */
	public static void copyDir(String targetDir, String path) {
		File targetFile = new File(targetDir);
		createFile(targetFile, false);
		File file = new File(path);
		if (targetFile.isDirectory() && file.isDirectory()) {
			copyFileToDir(targetFile.getAbsolutePath() + "/" + file.getName(), listFile(file));
		}
	}
	
	/**
	 * 复制一组文件到指定目录。targetDir是目标目录，filePath是需要复制的文件路径
	 * @param targetDir
	 * @param filePath
	 */
	public static void copyFileToDir(String targetDir, String... filePath) {
		Assert.hasText(targetDir, "targetDir must has text");
		File targetFile = new File(targetDir);
		Assert.isTrue(targetFile.isDirectory(), "targetDir must be a directory");
		if (! targetFile.exists()) {
			targetFile.mkdir();
		}
		for (String path : filePath) {
			File file = new File(path);
			if (file.isDirectory()) {
				copyFileToDir(targetDir + "/" + file.getName(), listFile(file));
			} else {
				copyFileToDir(targetDir, file, "");
			}
		}
	}
	
	/**
	 * 复制文件到指定目录。targetDir是目标目录，file是源文件名，newName是重命名的名字
	 * @param targetDir
	 * @param file
	 * @param newName
	 */
	public static void copyFileToDir(String targetDir, File file, String newName) {
		String newFile = "";
		if (! StringUtils.isBlank(newName)) {
			newFile = targetDir + "/" + newName;
		} else {
			newFile = targetDir + "/" + file.getName();
		}
		File tFile = new File(newFile);
		copyFile(tFile, file);
	}
	
	/**
	 * 复制文件。targetFile为目标文件，file为源文件
	 * @param targetFile
	 * @param file
	 */
	public static void copyFile(File targetFile, File file) {
		if (targetFile.exists()) {
			if (logger.isInfoEnabled()) {
				logger.info("file " + targetFile.getAbsolutePath() + " already exist,skip!");
			}
			return;
		} else {
			createFile(targetFile, true);
		}
		if (logger.isInfoEnabled()) {
			logger.info("copy file " + file.getAbsolutePath() + " to " + targetFile.getAbsolutePath());
		}
		try {
			InputStream is = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(targetFile);
			byte[] buffer = new byte[1024];
			while (is.read(buffer) != -1) {
				fos.write(buffer);
			}
			is.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		}
	}
	
	public static String[] listFile(File dir) {
		String absolutPath = dir.getAbsolutePath();
		String[] paths = dir.list();
		String[] files = new String[paths.length];
		for (int i = 0; i < paths.length; i++) {
			files[i] = absolutPath + "/" + paths[i];
		}
		return files;
	}
	
	public static void createFile(String path, boolean isFile){
		createFile(new File(path), isFile);
	}
	
	public static void createFile(File file, boolean isFile) {
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				createFile(file.getParentFile(), false);
			} else {
				if (isFile) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
						logger.error(e);
					}
				} else {
					file.mkdir();
				}
			}
		}
	}
}
