package fotorenkKilit;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Blowfish {

	static String key = "123";

	public static String encrypt(String key, String text) {
		try {
			byte[] keyData = (key).getBytes();
			SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] hasil = cipher.doFinal(text.getBytes());
			return new String(Base64.getEncoder().encode(hasil));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String decrypt(String key, String text) {
		try {
			byte[] keyData = (key).getBytes();
			SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

			byte[] hasil = cipher.doFinal(java.util.Base64.getDecoder().decode(text.getBytes("UTF-8")));
			return new String(hasil);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
