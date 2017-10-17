package security;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class Base64Coder {

	public static void main(String[] args) throws IOException {
		String str = "进行base64编码";
		BASE64Encoder encoder = new BASE64Encoder();
		String result = encoder.encode(str.getBytes("utf-8"));
		System.out.println(result);
		BASE64Decoder  decoder = new BASE64Decoder();
		byte[] ret =  decoder.decodeBuffer(result);
		System.out.println(new String(ret,"utf-8"));
	
				ret=	decoder.decodeBuffer("f4Myrgy6YlaWdo4lvv");
						System.out.println(new String(ret,"gbk"));
	}

}
