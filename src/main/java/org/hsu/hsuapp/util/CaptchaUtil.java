package org.hsu.hsuapp.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 驗證碼工具類
 *
 * @author &lt;a href="http://www.micmiu.com"&gt;Michael Sun&lt;/a&gt;
 */
public class CaptchaUtil {

	// 隨機產生的字符串
	private static final String RANDOM_STRS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static final String FONT_NAME = "Fixedsys";
	private static final int FONT_SIZE = 40;

	private Random random = new Random();

	private int width = 176;// 圖片寬
	private int height = 55;// 圖片高
	private int lineNum = 100;// 幹擾線數量
	private int strNum = 4;// 隨機產生字符數量

	/**
	 * 生成隨機圖片
	 */
	public BufferedImage genRandomCodeImage(StringBuffer randomCode) {
		// BufferedImage類是具有緩沖區的Image類
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		// 獲取Graphics對象,便於對圖像進行各種繪制操作
		Graphics g = image.getGraphics();
		// 設置背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);

		// 設置幹擾線的顏色
		g.setColor(getRandColor(110, 120));

		// 繪制幹擾線
		for (int i = 0; i <= lineNum; i++) {
			drowLine(g);
		}
		// 法1：繪制隨機字符
//		g.setFont(new Font(FONT_NAME, Font.ROMAN_BASELINE, FONT_SIZE));
//		for (int i = 1; i <= strNum; i++) {
//			randomCode.append(drowString(g, i));
//		}

		// 法2：
		Font f = new Font(FONT_NAME, Font.ROMAN_BASELINE, FONT_SIZE);
		Rectangle rect = new Rectangle(0, 0, width, height);
		for (int i = 1; i <= strNum; i++) {
			randomCode.append(String.valueOf(getRandomString(random.nextInt(RANDOM_STRS.length()))));
		}
		drawCenteredString(g, randomCode.toString(), rect, f);
		g.dispose();
		return image;
	}

	/**
	 * 給定範圍獲得隨機顏色
	 */
	private Color getRandColor(int fc, int bc) {
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 繪制字符串
	 */
	private String drowString(Graphics g, int i) {

		g.setColor(new Color(random.nextInt(101), random.nextInt(111), random.nextInt(121)));

		// 取得一個隨機字串符
		String rand = String.valueOf(getRandomString(random.nextInt(RANDOM_STRS.length())));

		g.translate(random.nextInt(3), random.nextInt(3));

		g.drawString(rand, 13 * i, 16);

		return rand;
	}

	/**
	 * Draw a String centered in the middle of a Rectangle.
	 *
	 * @param g    The Graphics instance.
	 * @param text The String to draw.
	 * @param rect The Rectangle to center the text in.
	 */
	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
		// Get the FontMetrics
		FontMetrics metrics = g.getFontMetrics(font);
		// Determine the X coordinate for the text
		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		// Determine the Y coordinate for the text (note we add the ascent, as in java2d
		// 0 is top of the screen)
		int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		// Set the font
		g.setFont(font);
		// Draw the String
		g.drawString(text, x, y);
	}

	/**
	 * 繪制幹擾線
	 */
	private void drowLine(Graphics g) {
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int x0 = random.nextInt(16);
		int y0 = random.nextInt(16);
		g.drawLine(x, y, x + x0, y + y0);
	}

	/**
	 * 獲取隨機的字符
	 */
	private String getRandomString(int num) {
		return String.valueOf(RANDOM_STRS.charAt(num));
	}

	public static void main(String[] args) {
		CaptchaUtil tool = new CaptchaUtil();
		StringBuffer code = new StringBuffer();
		BufferedImage image = tool.genRandomCodeImage(code);
		System.out.println("random code = " + code);
		try {
			// 將內存中的圖片通過流動形式輸出到客戶端
			ImageIO.write(image, "JPEG", new FileOutputStream(new File("D://tmp/random-code.jpg")));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
