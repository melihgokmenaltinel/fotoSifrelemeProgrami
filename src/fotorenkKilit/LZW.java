package fotorenkKilit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class LZW {

	public static String lzw_compress(String input) {
		HashMap<String, Integer> dictionary = new LinkedHashMap<>();
		String[] data = (input + "").split("");
		String out = "";
		ArrayList<String> temp_out = new ArrayList<>();
		String currentChar;
		String phrase = data[0];
		int code = 256;
		for (int i = 1; i < data.length; i++) {
			currentChar = data[i];
			if (dictionary.get(phrase + currentChar) != null) {
				phrase += currentChar;
			} else {
				if (phrase.length() > 1) {
					temp_out.add(Character.toString((char) dictionary.get(phrase).intValue()));
				} else {
					temp_out.add(Character.toString((char) Character.codePointAt(phrase, 0)));
				}

				dictionary.put(phrase + currentChar, code);
				code++;
				phrase = currentChar;
			}
		}

		if (phrase.length() > 1) {
			temp_out.add(Character.toString((char) dictionary.get(phrase).intValue()));
		} else {
			temp_out.add(Character.toString((char) Character.codePointAt(phrase, 0)));
		}

		for (String outchar : temp_out) {
			out += outchar;
		}
		return out;
	}

	public static String lzw_extract(String input) {
		HashMap<Integer, String> dictionary = new LinkedHashMap<>();
		String[] data = (input + "").split("");
		String currentChar = data[0];
		String oldPhrase = currentChar;
		String out = currentChar;
		int code = 256;
		String phrase = "";
		for (int i = 1; i < data.length; i++) {
			int currCode = Character.codePointAt(data[i], 0);
			if (currCode < 256) {
				phrase = data[i];
			} else {
				if (dictionary.get(currCode) != null) {
					phrase = dictionary.get(currCode);
				} else {
					phrase = oldPhrase + currentChar;
				}
			}
			out += phrase;
			currentChar = phrase.substring(0, 1);
			dictionary.put(code, oldPhrase + currentChar);
			code++;
			oldPhrase = phrase;
		}
		return out;
	}

	static int diziUzunlugu_compressParalel;
	static int threadControlSayac_compressParalel = 0;
	static StringBuilder output_compressParalel_bf = new StringBuilder();

	public static String lzw_compressParalel(String input) {
		input = input.replaceAll(", ", ",");

		int bolmeSabiti = 100000; // 100000
		diziUzunlugu_compressParalel = input.length() / bolmeSabiti;
		if (input.length() % bolmeSabiti != 0) {
			diziUzunlugu_compressParalel = diziUzunlugu_compressParalel + 1;
		}
		String inputDizi[] = new String[diziUzunlugu_compressParalel];
		String outputDizi[] = new String[diziUzunlugu_compressParalel];
		for (int i = 0; i < diziUzunlugu_compressParalel; i++) {
			if (i + 1 != diziUzunlugu_compressParalel) {
				inputDizi[i] = input.substring(i * bolmeSabiti, (i + 1) * bolmeSabiti);
			} else {
				inputDizi[i] = input.substring(i * bolmeSabiti, ((i * bolmeSabiti) + (input.length() % bolmeSabiti)));
			}
		}

		Thread t1[] = new Thread[diziUzunlugu_compressParalel];
		for (int j = 0; j < t1.length; j++) {
			int val = j;
			t1[val] = new Thread() {
				public void run() {
					// System.out.println("val " + val);
					HashMap<String, Integer> dictionary = new LinkedHashMap<>();
					String[] data = (inputDizi[val] + "").split("");
					String out = "";
					ArrayList<String> temp_out = new ArrayList<>();
					String currentChar;
					String phrase = data[0];
					int code = 256;
					for (int i = 1; i < data.length; i++) {
						currentChar = data[i];
						if (dictionary.get(phrase + currentChar) != null) {
							phrase += currentChar;
						} else {
							if (phrase.length() > 1) {
								temp_out.add(Character.toString((char) dictionary.get(phrase).intValue()));
							} else {
								temp_out.add(Character.toString((char) Character.codePointAt(phrase, 0)));
							}

							dictionary.put(phrase + currentChar, code);
							code++;
							phrase = currentChar;
						}
					}

					if (phrase.length() > 1) {
						temp_out.add(Character.toString((char) dictionary.get(phrase).intValue()));
					} else {
						temp_out.add(Character.toString((char) Character.codePointAt(phrase, 0)));
					}

					for (String outchar : temp_out) {
						out += outchar;
					}

					outputDizi[val] = "_" + out;
					// System.out.println(val + " inputDizi[val]: " + inputDizi[val]);

					// System.out.println(val + " outputDizi[val]: " + outputDizi[val]);
				}
			};

			t1[val].start();
		}
		while (true) {
			for (int k = 0; k < diziUzunlugu_compressParalel; k++) {
				if (t1[k] != null && t1[k].isAlive() == false) {

					t1[k] = null;
					System.gc();
					threadControlSayac_compressParalel = threadControlSayac_compressParalel + 1;

				}
			}
			if (threadControlSayac_compressParalel == diziUzunlugu_compressParalel) {

				for (int l = 0; l < t1.length; l++) {
					output_compressParalel_bf.append(outputDizi[l]);
				}
				break;
			}
		}

		return output_compressParalel_bf.toString();
	}

	static int diziUzunlugu_decompressParalel;
	static int threadControlSayac_decompressParalel = 0;
	static StringBuilder output_decompressParalel_bf = new StringBuilder();

	public static String lzw_extractParalel(String input) {

		input = input.substring(1, input.length());
		diziUzunlugu_decompressParalel = input.split("_").length;
		String inputDizi[] = new String[diziUzunlugu_decompressParalel];
		String outputDizi[] = new String[diziUzunlugu_decompressParalel];

		for (int i = 0; i < diziUzunlugu_decompressParalel; i++) {
			inputDizi[i] = input.split("_")[i];
		}
		Thread t1[] = new Thread[diziUzunlugu_decompressParalel];
		for (int j = 0; j < t1.length; j++) {
			int val = j;
			t1[val] = new Thread() {
				public void run() {
					HashMap<Integer, String> dictionary = new LinkedHashMap<>();
					String[] data = (inputDizi[val] + "").split("");
					String currentChar = data[0];
					String oldPhrase = currentChar;
					String out = currentChar;
					int code = 256;
					String phrase = "";
					for (int i = 1; i < data.length; i++) {
						int currCode = Character.codePointAt(data[i], 0);
						if (currCode < 256) {
							phrase = data[i];
						} else {
							if (dictionary.get(currCode) != null) {
								phrase = dictionary.get(currCode);
							} else {
								phrase = oldPhrase + currentChar;
							}
						}
						out += phrase;
						currentChar = phrase.substring(0, 1);
						dictionary.put(code, oldPhrase + currentChar);
						code++;
						oldPhrase = phrase;
					}

					outputDizi[val] = out;

				}
			};
			t1[val].start();

		}

		while (true) {
			for (int k = 0; k < diziUzunlugu_decompressParalel; k++) {
				if (t1[k] != null && t1[k].isAlive() == false) {
					t1[k] = null;
					System.gc();
					threadControlSayac_decompressParalel = threadControlSayac_decompressParalel + 1;

				}
			}
			if (threadControlSayac_decompressParalel == diziUzunlugu_decompressParalel) {
				for (int l = 0; l < t1.length; l++) {
					output_decompressParalel_bf.append(outputDizi[l]);
				}
				break;
			}
		}

		return output_decompressParalel_bf.toString().replaceAll(",", ", ");

	}

}