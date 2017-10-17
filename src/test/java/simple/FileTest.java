package simple;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class FileTest  extends TestCase{
	public void testFile() throws IOException{
		File f = new File("G:/Temp");
		File sub = new File(f,"sub.tmp");
		//sub.createNewFile();
		sub.mkdirs();
		System.out.println(sub.isDirectory());
	}
}
