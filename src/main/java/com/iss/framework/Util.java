package com.iss.framework;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class Util {

	public static void cutImage(File srcFile) throws IOException {
		int width = 200;
		int height = 200;
		//File srcFile = new File(srcPath);
		BufferedImage image = ImageIO.read(srcFile);
		int srcWidth = image.getWidth(null);
		int srcHeight = image.getHeight(null);
		int newWidth = 0, newHeight = 0;
		int x = 0, y = 0;
		double scale_w = (double) width / srcWidth;
		double scale_h = (double) height / srcHeight;
		System.out.println("scale_w=" + scale_w + ",scale_h=" + scale_h);
		// 按原比例缩放图片
		if (scale_w < scale_h) {
			newHeight = height;
			newWidth = (int) (srcWidth * scale_h);
			x = (newWidth - width) / 2;
		} else {
			newHeight = (int) (srcHeight * scale_w);
			newWidth = width;
			y = (newHeight - height) / 2;
		}
		BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		newImage.getGraphics().drawImage(image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
		// 保存缩放后的图片
		//String fileSufix = srcFile.getName().substring(srcFile.getName().lastIndexOf(".") + 1);

		//String string = UUID.randomUUID().toString();

		//File destFile = new File(srcFile.getParent(), string + "." + fileSufix);
		File destFile = new File(srcFile.getAbsolutePath());
		// 保存裁剪后的图片
		ImageIO.write(newImage.getSubimage(x, y, width, height), "png", destFile);
	}

	/**
	 * 密码MD5加密
	 * @param s
	 * @return
	 */
	public static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
