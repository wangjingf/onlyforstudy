package io.study.net.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;

public class Client {
    public static void main(String[] args) throws IOException {
         Socket socket = new Socket();
        SocketAddress address = null;
         socket.connect(address);

    }
}
