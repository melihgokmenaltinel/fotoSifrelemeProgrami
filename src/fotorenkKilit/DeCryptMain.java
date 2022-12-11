package fotorenkKilit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class DeCryptMain {
	static String[] swapListR;
	static String[] swapListG;
	static String[] swapListB;

	static Thread[] t100;
	static int ii = 0;
	static int threadNumber = 0;
	static ArrayList<String> arrayListR;
	static ArrayList<String> arrayListG;
	static ArrayList<String> arrayListB;
	static long baslangic = System.nanoTime();
	static int width;
	static int height;
	static int pixelBits;

	public static void main(String[] args) throws Exception {

		System.out.println("ŞİFRE ÇÖZME BAŞLADI  -------------");

		System.out.println("**fotoğrafı tekrar oluşturmak için şifre giriniz");
		Scanner convertComePass = new Scanner(System.in);
		String convertPass = convertComePass.next();
		String convertSha512ed = SHA.encryptThisString512_500000(SHA.encryptThisString512(convertPass));
		String convertSha128ed = SHA.encryptThisString128(SHA.encryptThisString128(convertPass));
		String blowfished = Blowfish.encrypt(convertSha128ed, convertSha512ed);
		String comeFromTxt = "";

		BufferedImage buf_img = null;
		try {
			buf_img = ImageIO.read(new File("C:\\Users\\melihgokmen.altinel\\Desktop\\YENİ2.jpg"));
		} catch (IOException ex) {
			Logger.getLogger(Picture.class.getName()).log(Level.SEVERE, null, ex);
		}
		width = buf_img.getWidth();
		height = buf_img.getHeight();

		String result = null;

		int controlRandomColor = buf_img.getRGB(width - 1, height - 1);
		int controlRandomColorBlue = (controlRandomColor & 0xff);
		int controlRandomColorGreen = ((controlRandomColor & 0xff00) >> 8);
		int controlRandomColorRed = ((controlRandomColor & 0xff0000) >> 16);
		int plusR = 0, plusG = 0, plusB = 0;

		if (controlRandomColorBlue == controlRandomColorGreen && controlRandomColorBlue == controlRandomColorRed) {

			if (controlRandomColorBlue == 0) {
				plusR = -133;
				plusG = -43;
				plusB = +43;
			} else if (controlRandomColorBlue == 1) {
				plusR = -133;
				plusG = +43;
				plusB = -43;
			} else if (controlRandomColorBlue == 2) {
				plusR = -43;
				plusG = -133;
				plusB = +43;
			} else if (controlRandomColorBlue == 3) {
				plusR = -43;
				plusG = -133;
				plusB = -133;
			} else if (controlRandomColorBlue == 4) {
				plusR = -43;
				plusG = +43;
				plusB = -133;
			} else if (controlRandomColorBlue == 5) {
				plusR = -43;
				// plusG = - 43;
				// plusB = - 133;
			} else if (controlRandomColorBlue == 6) {
				plusR = -133;
				plusG = -133;
				plusB = -133;
			}

		}

		for (int row = 0; row < width; row++) {
			for (int col = 0; col < height; col++) {

				int color = buf_img.getRGB(row, col);
				int blue = (color & 0xff) + plusB;
				int green = ((color & 0xff00) >> 8) + plusG;
				int red = ((color & 0xff0000) >> 16) + plusR;
				char blueC = (char) (blue);
				char greenC = (char) (green);
				char redC = (char) (red);
				result = result + redC + greenC + blueC;

			}
		}
		result = result.replaceAll("null", "");
		System.out.println("fotodan gelen length:" + result.length());

		AES aes = new AES();
		aes.secretKeyAndIv(convertSha512ed, blowfished);
		comeFromTxt = aes.decrypt(result);

		if (comeFromTxt != null) {
			System.out.println("şifresi çözülmüş:" + comeFromTxt.length());

			comeFromTxt = LZW.lzw_extractParalel(comeFromTxt);
			System.out.println("LZW ters sıkıştırılmış:" + comeFromTxt.length());
			comeFromTxt = comeFromTxt.replaceAll("\\[", "").replaceAll("\\]", "");

			int str_length = comeFromTxt.split(", ").length;
			String[] comeFromTxtArray = comeFromTxt.split(", ");

			byte[] array = new byte[str_length];
			System.out.println("array.length " + array.length);

			for (int i = 0; i < array.length; i++) {
				int swap = Integer.valueOf(comeFromTxtArray[i]);
				array[i] = (byte) swap;
			}

			Files.write(new File("C:\\Users\\melihgokmen.altinel\\Desktop\\a2.jpg").toPath(), array);

		} else {
			System.out.println("*Şifre Yanlış");
		}

	}
}
