package org.hsu.hsuapp.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

public class QRCodeUtil {

	private static final int BLACK = 0xff000000;
	private static final int WHITE = 0xFFFFFFFF;

	
	/**
	 * 生成QRCode二維碼<br>
	 * 在編碼時需要將com.google.zxing.qrcode.encoder.Encoder.java中的<br>
	 * static final String DEFAULT_BYTE_MODE_ENCODING = "ISO8859-1";<br>
	 * 修改為UTF-8，否則中文編譯後解析不了<br>
	 */
	public void encode(String contents, File file, BarcodeFormat format, int width, int height,
			Map<EncodeHintType, ?> hints) {
		try {
			contents = new String(contents.getBytes("UTF-8"), "ISO-8859-1");// 如果不想更改源碼，則將字符串轉換成ISO-8859-1編碼

			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, format, width, height);
			writeToFile(bitMatrix, "png", file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param matrix
	 * @param format
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage writeToBufferImage(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		return image;
	}
	
	/**
	 * 生成二維碼圖片<br>
	 * 
	 * @param matrix
	 * @param format
	 *            圖片格式
	 * @param file
	 *            生成二維碼圖片位置
	 * @throws IOException
	 */
	public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		ImageIO.write(image, format, file);
	}

	/**
	 * 生成二維碼內容<br>
	 * 
	 * @param matrix
	 * @return
	 */
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) == true ? BLACK : WHITE);
			}
		}
		return image;
	}

	/**
	 * 解析QRCode二維碼
	 */
	@SuppressWarnings("unchecked")
	public void decode(File file) {
		try {
			BufferedImage image;
			try {
				image = ImageIO.read(file);
				if (image == null) {
					System.out.println("Could not decode image");
				}
				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
				Result result;
				@SuppressWarnings("rawtypes")
				Hashtable hints = new Hashtable();
				// 解碼設置編碼方式為：utf-8
				hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
				result = new MultiFormatReader().decode(bitmap, hints);
				String resultStr = result.getText();
				System.out.println("解析後內容：" + resultStr);
			} catch (IOException ioe) {
				System.out.println(ioe.toString());
			} catch (ReaderException re) {
				System.out.println(re.toString());
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
}
