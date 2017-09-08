package eu.telecomlille.sdl.tcp;

import java.io.IOException;
import java.net.Socket;

import eu.telecomlille.sdl.IProcess;
import eu.telecomlille.sdl.ISignal;
import eu.telecomlille.sdl.Stepper;
import eu.telecomlille.sdl.tcp.PConnection;
import eu.telecomlille.sdl.tcp.TSDUClose;
import eu.telecomlille.sdl.tcp.TSDUConnected;
import eu.telecomlille.sdl.tcp.TSDUData;
import eu.telecomlille.sdl.tcp.TSDUOpen;

public class Client implements IProcess {
	protected PConnection tcp;

	public void doIt() throws IOException {
		// établir la connexion vers localhost sur le port 13
		// (il faut lancer Server.java sur cette machine)
		Socket sock = new Socket("localhost", 35000);
		String name = sock.getLocalAddress().getHostAddress()+":"+sock.getLocalPort();
		tcp = new PConnection(sock, this, name);
		tcp.setParent(null, null);
		tcp.add(new TSDUOpen(this, this));
	}

	public static void main(String[] args) throws IOException {
		new Client().doIt();
	}

	@Override
	public void add(ISignal sig) {
		System.out.println("Client : "+sig);
		if (sig instanceof TSDUConnected) {
			// envoyer des éléments de protocole
			tcp.add(new TSDUData(this, "Bonjour"));
			tcp.add(new TSDUData(this, "Salut"));
			// clore la connexion
			tcp.add(new TSDUClose(this));
		}
	}

	@Override
	public void setParent(IProcess procParent, Stepper s) {
	}
}
