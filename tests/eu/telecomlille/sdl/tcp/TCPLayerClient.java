package eu.telecomlille.sdl.tcp;

import eu.telecomlille.sdl.IProcess;
import eu.telecomlille.sdl.ISignal;
import eu.telecomlille.sdl.Stepper;
import eu.telecomlille.sdl.tcp.TCPLayer;
import eu.telecomlille.sdl.tcp.TSDUClose;
import eu.telecomlille.sdl.tcp.TSDUConnect;
import eu.telecomlille.sdl.tcp.TSDUConnected;
import eu.telecomlille.sdl.tcp.TSDUData;

public class TCPLayerClient implements IProcess {
	protected TCPLayer tcp;

	public void doIt() {
		tcp = new TCPLayer();
		tcp.setParent(this, null);
		tcp.add(new TSDUConnect(this, "localhost", 13));
	}

	public static void main(String[] args) {
		TCPLayerClient client = new TCPLayerClient();
		client.doIt();
	}

	@Override
	public void add(ISignal sig) {
		System.out.println(sig);
		if (sig instanceof TSDUConnected) {
			IProcess sender = sig.getSender();
			// envoyer des éléments de protocole
			sender.add(new TSDUData(this, "Bonjour"));
			sender.add(new TSDUData(this, "Salut"));
			// clore la connexion
			sender.add(new TSDUClose(this));
		}
	}

	@Override
	public void setParent(IProcess procParent, Stepper s) {
	}
}
