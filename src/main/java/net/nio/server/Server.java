package net.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class Server implements Runnable {
	private Selector selector;
	private ServerSocketChannel serverChannel;
	private volatile boolean stop;
	public Server(int port) {
		try {
			selector = selector.open();
			serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);//非阻塞的channel
			serverChannel.socket().bind(new InetSocketAddress("127.0.0.1", 8010),1024);
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);//channel注册到selector上
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	public void stop(){
		this.stop = true;
	}
	public static void main(String[] args) throws IOException {
		Server server = new Server(1024);
		new Thread(server).start();
		
	}
	private void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){
			if(key.isAcceptable()){
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc= ssc.accept();
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
			}
			if(key.isReadable()){
				SocketChannel sc = (SocketChannel) key.channel();
				 ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				 int readBytes = sc.read(readBuffer);
				 if(readBytes>0){
					 readBuffer.flip();
					 byte[] bytes  = new byte[readBuffer.remaining()];
					 readBuffer.get(bytes);
					 String body = new String(bytes,"utf-8");
					 System.out.println("the time server receive order:" + body);
					 String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)? new Date(System.currentTimeMillis()).toString():"BAD ORDER";
					 doWrite(sc,currentTime);
				 }else if(readBytes<0){//链路关闭
					 key.cancel();
					 sc.close();
				 }else{
					 ;//忽略
				 }
			}
		}
	}
	private void doWrite(SocketChannel sc, String response) throws IOException     {
		if(response!=null && response.trim().length()>0){
			byte[] bytes = response.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			sc.write(writeBuffer);
		}
	}
	@Override
	public void run() {
		while(!stop){
			try{
				selector.select(1000);
				Set selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				while(it.hasNext()){
					SelectionKey key = it.next();
					it.remove();
				}
			}catch(Exception e){
				
			}
		}
	}

}
