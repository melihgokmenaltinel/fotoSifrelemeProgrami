package fotorenkKilit;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class Picture {
	static Thread t1, t2, t3, t4, t5, t6;

	String img_name;
	BufferedImage buf_img;
	int width;
	int height;
	static File saveAs = new File(
			"C:\\Users\\melihgokmen.altinel\\Desktop\\YENİ2" /* + new Random().nextInt() */ + ".jpg");

	public Picture(int w, int h, int pixelBits) {
		this.width = w;
		this.height = h;

		if (pixelBits == 24) {
			buf_img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		} else if ((pixelBits == 32)) {
			buf_img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		} else {
			buf_img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		}
	}

	public int width() {
		width = buf_img.getWidth();
		return width;
	}

	public int height() {
		height = buf_img.getHeight();
		return height;
	}

	public int getPixelSize(int col, int row) {
		Color color = new Color(buf_img.getRGB(col, row));
		int bit = color.getAlpha();
		return bit;
	}

	public Color get(int col, int row) {
		Color color = new Color(buf_img.getRGB(col, row));
		return color;
	}

	public void set(int col, int row, Color color) {
		buf_img.setRGB(col, row, color.getRGB());
	}

	public void save() {
		try {
			ImageIO.write(buf_img, "png", saveAs);
		} catch (IOException ex) {
			Logger.getLogger(Picture.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void show() throws IOException {
		Desktop.getDesktop().open(saveAs);
	}

	public static void createAndShow(int width, int height, int[][] store_r, int[][] store_g, int[][] store_b,
			int pixelBits) throws IOException {
		Picture picture = new Picture(width, height, pixelBits);
		for (int row = 0; row < width; row++) {
			for (int col = 0; col < height; col++) {
				picture.set(row, col, new Color(store_r[row][col], store_g[row][col], store_b[row][col]));
			}
		}
		picture.save();
	}

	public static void createAndShow(int width, int height, int[][] store_r, int[][] store_g, int[][] store_b,
			int pixelBits, int randomColor) throws IOException {
		Picture picture = new Picture(width, height, pixelBits);
		for (int row = 0; row < width; row++) {
			for (int col = 0; col < height; col++) {
				if (store_r[row][col] != 0) {
					picture.set(row, col, new Color(store_r[row][col], store_g[row][col], store_b[row][col]));
				} else {
					picture.set(row, col, new Color(randomColor, randomColor, randomColor));
				}
			}
		}
		picture.save();
	}

	public static void zip() throws IOException {

		File imageFile = new File("C:\\Users\\melihgokmen.altinel\\Desktop\\c.jpg");
		File compressedImageFile = new File("C:\\Users\\melihgokmen.altinel\\Desktop\\zip.jpg");

		InputStream is = new FileInputStream(imageFile);
		OutputStream os = new FileOutputStream(compressedImageFile);

		float quality = 0.9f;

		BufferedImage image = ImageIO.read(is);

		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");

		if (!writers.hasNext())
			throw new IllegalStateException("No writers found");

		ImageWriter writer = (ImageWriter) writers.next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		writer.setOutput(ios);

		ImageWriteParam param = writer.getDefaultWriteParam();

		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(quality);

		writer.write(null, new IIOImage(image, null, null), param);

		is.close();
		os.close();
		ios.close();
		writer.dispose();

	}

	public void photoTypeControl(String dosyaAdi) throws IOException {
		File file = new File(dosyaAdi);

		ImageInputStream iis = ImageIO.createImageInputStream(file);

		Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);

		if (!iter.hasNext()) {
			throw new RuntimeException("No readers found!");
		}

		ImageReader reader = iter.next();

		System.out.println("Format: " + reader.getFormatName());

		iis.close();
	}

	public static void destroyPhoto(String dosyaAdi) throws IOException {

		saveAs = new File(dosyaAdi);

		t1 = new Thread() {
			public void run() {
				Picture picture = new Picture(4000, 4000, 32);
				int colorR[][];
				int colorG[][];
				int colorB[][];

				int rndR, rndG, rndB;
				int rndrow, rndcol;

				Random random = new Random();
				for (int i = 0; i < 2; i++) {
					rndrow = random.nextInt(4000) + 10;
					rndcol = random.nextInt(4000) + 10;

					colorR = new int[rndrow][rndcol];
					colorG = new int[rndrow][rndcol];
					colorB = new int[rndrow][rndcol];

					System.out.println(rndrow + " " + rndcol);
					for (int row = 0; row < rndrow; row++) {
						for (int col = 0; col < rndcol; col++) {
							rndR = random.nextInt(255);
							rndG = random.nextInt(255);
							rndB = random.nextInt(255);

							colorR[row][col] = rndR;
							colorG[row][col] = rndG;
							colorB[row][col] = rndB;
						}
					}
					try {
						picture.createAndShow(rndrow, rndcol, colorR, colorG, colorB, 32);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t1.start();

		t2 = new Thread() {
			public void run() {
				Picture picture = new Picture(4000, 4000, 32);
				int colorR[][];
				int colorG[][];
				int colorB[][];

				int rndR, rndG, rndB;
				int rndrow, rndcol;

				Random random = new Random();
				for (int i = 0; i < 2; i++) {
					rndrow = random.nextInt(4000) + 10;
					rndcol = random.nextInt(4000) + 10;

					colorR = new int[rndrow][rndcol];
					colorG = new int[rndrow][rndcol];
					colorB = new int[rndrow][rndcol];

					System.out.println(rndrow + " " + rndcol);
					for (int row = 0; row < rndrow; row++) {
						for (int col = 0; col < rndcol; col++) {
							rndR = random.nextInt(255);
							rndG = random.nextInt(255);
							rndB = random.nextInt(255);

							colorR[row][col] = rndR;
							colorG[row][col] = rndG;
							colorB[row][col] = rndB;
						}
					}
					try {
						picture.createAndShow(rndrow, rndcol, colorR, colorG, colorB, 32);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// picture.show();
				}
			}
		};
		t2.start();

		t3 = new Thread() {
			public void run() {
				Picture picture = new Picture(4000, 4000, 32);
				int colorR[][];
				int colorG[][];
				int colorB[][];

				int rndR, rndG, rndB;
				int rndrow, rndcol;

				Random random = new Random();
				for (int i = 0; i < 2; i++) {
					rndrow = random.nextInt(4000) + 10;
					rndcol = random.nextInt(4000) + 10;

					colorR = new int[rndrow][rndcol];
					colorG = new int[rndrow][rndcol];
					colorB = new int[rndrow][rndcol];

					System.out.println(rndrow + " " + rndcol);
					for (int row = 0; row < rndrow; row++) {
						for (int col = 0; col < rndcol; col++) {
							rndR = random.nextInt(255);
							rndG = random.nextInt(255);
							rndB = random.nextInt(255);

							colorR[row][col] = rndR;
							colorG[row][col] = rndG;
							colorB[row][col] = rndB;
						}
					}
					try {
						picture.createAndShow(rndrow, rndcol, colorR, colorG, colorB, 32);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// picture.show();
				}
			}
		};
		t3.start();

		t4 = new Thread() {
			public void run() {
				Picture picture = new Picture(4000, 4000, 32);
				int colorR[][];
				int colorG[][];
				int colorB[][];

				int rndR, rndG, rndB;
				int rndrow, rndcol;

				Random random = new Random();
				for (int i = 0; i < 2; i++) {
					rndrow = random.nextInt(4000) + 10;
					rndcol = random.nextInt(4000) + 10;

					colorR = new int[rndrow][rndcol];
					colorG = new int[rndrow][rndcol];
					colorB = new int[rndrow][rndcol];

					System.out.println(rndrow + " " + rndcol);
					for (int row = 0; row < rndrow; row++) {
						for (int col = 0; col < rndcol; col++) {
							rndR = random.nextInt(255);
							rndG = random.nextInt(255);
							rndB = random.nextInt(255);

							colorR[row][col] = rndR;
							colorG[row][col] = rndG;
							colorB[row][col] = rndB;
						}
					}
					try {
						picture.createAndShow(rndrow, rndcol, colorR, colorG, colorB, 32);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// picture.show();
				}
			}
		};
		t4.start();

		t5 = new Thread() {
			public void run() {
				Picture picture = new Picture(4000, 4000, 32);
				int colorR[][];
				int colorG[][];
				int colorB[][];

				int rndR, rndG, rndB;
				int rndrow, rndcol;

				Random random = new Random();
				for (int i = 0; i < 2; i++) {
					rndrow = random.nextInt(4000) + 10;
					rndcol = random.nextInt(4000) + 10;

					colorR = new int[rndrow][rndcol];
					colorG = new int[rndrow][rndcol];
					colorB = new int[rndrow][rndcol];

					System.out.println(rndrow + " " + rndcol);
					for (int row = 0; row < rndrow; row++) {
						for (int col = 0; col < rndcol; col++) {
							rndR = random.nextInt(255);
							rndG = random.nextInt(255);
							rndB = random.nextInt(255);

							colorR[row][col] = rndR;
							colorG[row][col] = rndG;
							colorB[row][col] = rndB;
						}
					}
					try {
						picture.createAndShow(rndrow, rndcol, colorR, colorG, colorB, 32);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// picture.show();
				}
			}
		};
		t5.start();

		t6 = new Thread() {
			public void run() {
				Picture picture = new Picture(4000, 4000, 32);
				int colorR[][];
				int colorG[][];
				int colorB[][];

				int rndR, rndG, rndB;
				int rndrow, rndcol;

				Random random = new Random();
				for (int i = 0; i < 2; i++) {
					rndrow = random.nextInt(4000) + 10;
					rndcol = random.nextInt(4000) + 10;

					colorR = new int[rndrow][rndcol];
					colorG = new int[rndrow][rndcol];
					colorB = new int[rndrow][rndcol];

					System.out.println(rndrow + " " + rndcol);
					for (int row = 0; row < rndrow; row++) {
						for (int col = 0; col < rndcol; col++) {
							rndR = random.nextInt(255);
							rndG = random.nextInt(255);
							rndB = random.nextInt(255);

							colorR[row][col] = rndR;
							colorG[row][col] = rndG;
							colorB[row][col] = rndB;
						}
					}
					try {
						picture.createAndShow(rndrow, rndcol, colorR, colorG, colorB, 32);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t6.start();
	}

}
