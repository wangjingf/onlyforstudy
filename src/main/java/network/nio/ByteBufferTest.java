package network.nio;

import java.nio.ByteBuffer;

public class ByteBufferTest {

	public static void main(String[] args) {
		ByteBuffer  buffer = ByteBuffer.allocate(256);
		buffer.get();
		System.out.println(buffer.position());
	}

}
