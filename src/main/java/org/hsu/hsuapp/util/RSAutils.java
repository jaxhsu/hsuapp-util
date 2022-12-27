package org.hsu.hsuapp.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSAutils {

	private static final String	PUBLIC_KEY	= "RSAPublicKey";
	private static final String	PRIVATE_KEY	= "RSAPrivateKey";

	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		RSAutils RSAutils = new RSAutils();
		
		String data = "1qaz2wsx";// 需加密資料
		
		boolean createNewKey = true; // 是否產生新的key
		boolean isFileNeedBase64 = true; // 產keyFile是否需要base64
		String dir_path = "D:/tmp/key";
		String publicKey_fileName = "vpmRsaPublicKey";
		String privateKey_fileName = "vpmRsaPrivateKey";

		byte[] dataByteArray = data.getBytes();
		try {
			File dest_public_file = new File(dir_path, publicKey_fileName);
			File dest_private_file = new File(dir_path, privateKey_fileName);			
			
			// 生成新金鑰
			if (createNewKey) {
				if (dest_public_file.exists()) {
					dest_public_file.delete();
				}
				if (dest_private_file.exists()) {
					dest_private_file.delete();
				}

				Map<String, Key> keysMap = GenerateRSAkey(2048);
				if (isFileNeedBase64) {
					String publicKey = getPublicKey(keysMap);
					RSAutils.SaveToBase64File(publicKey, dest_public_file);

					String privateKey = getPrivateKey(keysMap);
					RSAutils.SaveToBase64File(privateKey, dest_private_file);
				} else {
					Key public_key = (Key) keysMap.get(PUBLIC_KEY);
					RSAutils.SaveToFile(public_key.getEncoded(), dest_public_file);

					Key private_key = (Key) keysMap.get(PRIVATE_KEY);
					RSAutils.SaveToFile(private_key.getEncoded(), dest_private_file);
				}
			}
			
			// 讀取金鑰
			String publicKey = RSAutils.ReadBase64File(dest_public_file);
			String privateKey = RSAutils.ReadBase64File(dest_private_file);

			// 公鑰加密、私鑰解密
			// 加密
			byte[] encryptDataByteArray = encryptByPublicKey(dataByteArray, publicKey);
			String encryptData = encode(encryptDataByteArray);

			// 解密
			String decryptData = encryptData;
			byte[] decryptDataByteArray = decryptByPrivateKey(decode(decryptData), privateKey);
			String result = new String(decryptDataByteArray);

			System.out.println("公鑰:" + publicKey);
			System.out.println("私鑰:" + privateKey);
			System.out.println("加密前:" + data + "\n");
			System.out.println("公鑰加密後:" + encryptData);
			System.out.println("私鑰解密後:" + result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成RSA金鑰
	 * 
	 * @param keysize 
	 * @return Map
	 * @throws NoSuchAlgorithmException 
	 */
	public static Map<String, Key> GenerateRSAkey(int keysize) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGen;
		Map<String, Key> keyMap = new HashMap<>(2);

		keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(keysize);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);

		return keyMap;
	}

	/**
	 * 獲取公鑰
	 * 
	 * @param keyMap 
	 * @return String
	 */
	public static String getPublicKey(Map<String, Key> keyMap) {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return encode(key.getEncoded());
	}

	/**
	 * 獲取私鑰
	 * 
	 * @param keyMap 
	 * @return String
	 */
	public static String getPrivateKey(Map<String, Key> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return encode(key.getEncoded());
	}
	
	/**
	 * 公鑰加密
	 *  
	 * @param data 
	 * @param Key 
	 * @return byte[]
	 * @throws Exception 
	 */
	public static byte[] encryptByPublicKey(byte[] data, String Key) throws Exception {
		byte[] keyBytes = decode(Key);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		Key publicKey = kf.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(kf.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 私鑰加密
	 * 
	 * @param data 
	 * @param Key 
	 * @return byte[]
	 * @throws Exception 
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String Key) throws Exception {
		byte[] keyBytes = decode(Key);

		PKCS8EncodedKeySpec pcks8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		Key privateKey = kf.generatePrivate(pcks8KeySpec);

		Cipher cipher = Cipher.getInstance(kf.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);

	}

	/**
	 * 公鑰解密
	 * 
	 * @param data 
	 * @param key 
	 * @return byte[]
	 * @throws Exception 
	 */
	public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decode(key);

		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		Key publicKey = kf.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(kf.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 私鑰解密
	 * 
	 * @param data 
	 * @param key 
	 * @return byte[]
	 * @throws Exception 
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decode(key);

		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		Key privateKey = kf.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(kf.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * BASE64加密
	 * 
	 * @param source 
	 * @return String
	 */
	public static String encode(byte[] source) {
		return new BASE64Encoder().encodeBuffer(source);
	}

	/**
	 * BASE64解密
	 * 
	 * @param source 
	 * @return String
	 * @throws IOException 
	 */
	public static byte[] decode(String source) throws IOException {
		return new BASE64Decoder().decodeBuffer(source);
	}

	/**
	 * 儲存檔案
	 * 
	 * @param data 
	 * @param file 
	 * @return boolean
	 */
	public boolean SaveToFile(byte[] data, File file) {
		boolean state = false;
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(new FileOutputStream(file));
			dos.write(data);
			dos.flush();

			state = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return state;
	}
	
	/**
	 * 儲存檔案
	 * 
	 * @param data 
	 * @param file 
	 * @return boolean
	 */
	public boolean SaveToBase64File(String data, File file) {
		boolean state = false;
		BufferedWriter bufw = null;
		try {
			bufw = new BufferedWriter(new FileWriter(file));
			bufw.write(data);
			bufw.flush();

			state = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufw != null) {
				try {
					bufw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return state;
	}	
	
	/**
	 * 讀取檔案
	 * 
	 * @param file 
	 * @return byte[]
	 */
	public byte[] ReadFile(File file) {
		byte[] bytes = null;
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new FileInputStream(file));
			bytes = new byte[dis.available()];
			dis.read(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bytes;
	}
	
	/**
	 * 讀取檔案
	 * 
	 * @param file 
	 * @return String
	 */
	public String ReadBase64File(File file) {
		StringBuffer data = new StringBuffer();
		BufferedReader bufr = null;
		try {
			bufr = new BufferedReader(new FileReader(file));
			String line;
			while ((line = bufr.readLine()) != null) {
				data.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufr != null) {
				try {
					bufr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data.toString();
	}
	
}
