package fotorenkKilit;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class SaveToPicture {

	static String result;
	static int kac_adet_thread = 0;
	static int control = 0;
	static Thread tcontrol;
	static String sortedKeys;

	public static byte[] method(File file) throws IOException {

		FileInputStream fl = new FileInputStream(file);
		byte[] arr = new byte[(int) file.length()];
		fl.read(arr);
		fl.close();
		return arr;
	}

	public static void fotoyaKaydet(File photoPath, String sha512, String blowfished) throws Exception {

		File path = photoPath;
		byte[] array = method(path);
		System.out.println("array.length(): " + array.length);

		String array_input = Arrays.toString(array); // photoByte
		System.out.println("array_input.length(): " + array_input.length());

		String array_lzw = LZW.lzw_compressParalel(array_input);
		System.out.println(array_lzw.length());

		AES aes = new AES();
		aes.secretKeyAndIv(sha512, blowfished);
		String crypted = aes.encrypt(array_lzw.replaceAll("null", ""));


		Random random = new Random();
		int randomColor = random.nextInt(7);
		int plusR = 0, plusG = 0, plusB = 0;

		if (randomColor == 0) {
			plusR = +133;
			plusG = +43;
			plusB = -43;
		} else if (randomColor == 1) {
			plusR = +133;
			plusG = -43;
			plusB = +43;
		} else if (randomColor == 2) {
			plusR = +43;
			plusG = +133;
			plusB = -43;
		} else if (randomColor == 3) {
			plusR = +43;
			plusG = +133;
			plusB = +133;
		} else if (randomColor == 4) {
			plusR = +43;
			plusG = -43;
			plusB = +133;
		} else if (randomColor == 5) {
			plusR = +43;
			// plusG = + 43;
			// plusB = + 133;
		} else if (randomColor == 6) {
			plusR = +133;
			plusG = +133;
			plusB = +133;
		}

		int diziLength = crypted.length();
		int diziLengthbolu3 = crypted.length() / 3;
		double kareKokDouble = Math.sqrt(diziLengthbolu3);
		int karekokInt = (int) kareKokDouble + 1;
		int en = karekokInt;
		int boy = karekokInt;

		int asciiR[][] = new int[karekokInt][karekokInt];
		int asciiG[][] = new int[karekokInt][karekokInt];
		int asciiB[][] = new int[karekokInt][karekokInt];

		int mod3 = crypted.length() % 3;
		if (mod3 == 1) {
			crypted = crypted + ":";
			crypted = crypted + ":";
			diziLength = diziLength + 2;
		}
		if (mod3 == 2) {
			crypted = crypted + ":";
			diziLength = diziLength + 1;
		}

		int indisRGBx = 0;
		int indisRGBy = 0;
		int minR = (int) crypted.charAt(0), maxR = (int) crypted.charAt(0);
		int minG = (int) crypted.charAt(1), maxG = (int) crypted.charAt(1);
		int minB = (int) crypted.charAt(2), maxB = (int) crypted.charAt(2);

		for (int i = 0; i < diziLength; i += 3) {

			char ch1 = crypted.charAt(i);
			int swapR = (int) ch1 + plusR;
			asciiR[indisRGBx][indisRGBy] = swapR;
			if (swapR > maxR) {
				maxR = swapR;
			} else if (swapR < minR) {
				minR = swapR;
			}

			char ch2 = crypted.charAt(i + 1);
			int swapG = (int) ch2 + plusG;
			asciiG[indisRGBx][indisRGBy] = swapG;
			if (swapG > maxG) {
				maxG = swapG;
			} else if (swapG < minG) {
				minG = swapG;
			}

			char ch3 = crypted.charAt(i + 2);
			int swapB = (int) ch3 + plusB;
			asciiB[indisRGBx][indisRGBy] = swapB;
			if (swapB > maxB) {
				maxB = swapB;
			} else if (swapB < minB) {
				minB = swapB;
			}

			indisRGBy = indisRGBy + 1;

			if (indisRGBy == karekokInt) {
				indisRGBy = 0;
				indisRGBx = indisRGBx + 1;
			}
		}
		Picture.createAndShow(en, boy, asciiR, asciiG, asciiB, 0, randomColor);
	}
}
