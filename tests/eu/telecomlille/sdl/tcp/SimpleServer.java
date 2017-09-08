package eu.telecomlille.sdl.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {

	public static void main(String[] args) {
		ServerSocket svrSock = null;
		BufferedReader br = null;
		try {
			svrSock = new ServerSocket(13);
			Socket sock = svrSock.accept();
			OutputStream os = sock.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			pw.println("Hello from server");
			InputStream is = sock.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			while (true) {
				String str = br.readLine();
				if (str == null)
					throw new IOException();
				System.out.println(str);
			}
		} catch (IOException e) {
			System.out.println("Socket closed");
		} finally {
			try {
				svrSock.close();
			} catch (Exception e) {
			}
		}
	}
}
