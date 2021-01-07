package org.hsu.hsuapp.baidu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5編碼相關的類
 * 
 * @author wangjingtao
 * 
 */
public class MD5 {
    // 首先初始化一個字符數組，用來存放每個16進制字符
    private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f' };

    /**
     * 獲得一個字符串的MD5值
     * 
     * @param input 輸入的字符串
     * @return 輸入字符串的MD5值
     * 
     */
    public static String md5(String input) {
        if (input == null)
            return null;

        try {
            // 拿到一個MD5轉換器（如果想要SHA1參數換成”SHA1”）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 輸入的字符串轉換成字節數組
            byte[] inputByteArray = input.getBytes("utf-8");
            // inputByteArray是輸入字符串轉換得到的字節數組
            messageDigest.update(inputByteArray);
            // 轉換並返回結果，也是字節數組，包含16個元素
            byte[] resultByteArray = messageDigest.digest();
            // 字符數組轉換成字符串返回
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 獲取文件的MD5值
     * 
     * @param file
     * @return
     */
    public static String md5(File file) {
        try {
            if (!file.isFile()) {
                System.err.println("文件" + file.getAbsolutePath() + "不存在或者不是文件");
                return null;
            }

            FileInputStream in = new FileInputStream(file);

            String result = md5(in);

            in.close();

            return result;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String md5(InputStream in) {

        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");

            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer)) != -1) {
                messagedigest.update(buffer, 0, read);
            }

            in.close();

            String result = byteArrayToHex(messagedigest.digest());

            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String byteArrayToHex(byte[] byteArray) {
        // new一個字符數組，這個就是用來組成結果字符串的（解釋一下：一個byte是八位二進制，也就是2位十六進制字符（2的8次方等於16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍歷字節數組，通過位運算（位運算效率高），轉換成字符放到字符數組中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }

        // 字符數組組合成字符串返回
        return new String(resultCharArray);

    }

}
