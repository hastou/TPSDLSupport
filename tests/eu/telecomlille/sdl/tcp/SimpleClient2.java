package eu.telecomlille.sdl.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleClient2 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		InetAddress addr;
		try {
			addr = InetAddress.getByName("localhost");
			@SuppressWarnings("resource")
			Socket sock = new Socket(addr, 13);
			OutputStream os = sock.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			pw.println("Bonjour");
			pw.println("Salut");
			InputStream is = sock.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			while (true) {
				String str = br.readLine();
				if (str == null)
					throw new IOException();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Socket closed");;
		}
	}

}
