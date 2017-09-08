package eu.telecomlille.sdl.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleClient {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		InetAddress addr;
		try {
			addr = InetAddress.getByName("localhost");
			Socket sock = new Socket(addr, 13);
			OutputStream os = sock.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			pw.println("Bonjour");
			pw.println("Salut");
			pw.close();
			sock.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Socket closed");;
		}
	}

}
