package secure;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SHADigest {
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException{
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		StringBufferInputStream in = new StringBufferInputStream("myStrign");
		//InputStream in = null;
		int ch;
		while((ch = in.read())!=-1){
			digest.update((byte)ch);
		}
		byte[] bytes = digest.digest();
		for(byte b : bytes){
			int v = b & 0xff;
			System.out.print(v+" ");
		}
		System.out.println("");
		System.out.println(Arrays.toString(bytes));
	}
}
