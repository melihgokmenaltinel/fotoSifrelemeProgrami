package fotorenkKilit;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class CryptMain {

	public static void main(String[] args) throws Exception {

		File path = new File("C:\\Users\\melihgokmen.altinel\\Desktop\\a.png");

		System.out.println("şifrelemek için şifre giriniz");
		Scanner comePass = new Scanner(System.in);
		String pass = comePass.next();
		String sha512ed = SHA.encryptThisString512_500000(SHA.encryptThisString512(pass));
		String sha128ed = SHA.encryptThisString128(SHA.encryptThisString128(pass));
		String blowfished = Blowfish.encrypt(sha128ed, sha512ed);

		SaveToPicture.fotoyaKaydet(path, sha512ed, blowfished);
	}
}