package eu.telecomlille.sdl.tcp;

import eu.telecomlille.sdl.IProcess;
import eu.telecomlille.sdl.ISignal;
import eu.telecomlille.sdl.Stepper;
import eu.telecomlille.sdl.tcp.TCPLayer;
import eu.telecomlille.sdl.tcp.TSDUClosed;
import eu.telecomlille.sdl.tcp.TSDUConnected;
import eu.telecomlille.sdl.tcp.TSDUData;
import eu.telecomlille.sdl.tcp.TSDUListen;
import eu.telecomlille.sdl.tcp.TSDUListening;
import eu.telecomlille.sdl.tcp.TSDUOpen;
import eu.telecomlille.sdl.tcp.TSDUStop;

public class TCPLayerServer implements IProcess {
	protected TCPLayer tcp;
	protected IProcess listener;
	protected IProcess connection;
	protected IProcess user;
	protected IProcess localPeer;

	public void doIt() {
		tcp = new TCPLayer();
		tcp.setParent(this, null);
		user = this;
		localPeer = this;
		tcp.add(new TSDUListen(user, "localhost", 13));
	}

	public static void main(String[] args) {
		TCPLayerServer server = new TCPLayerServer();
		server.doIt();
	}

	@Override
	public void add(ISignal sig) {
		System.out.println("TCPLayerServer : "+sig);
		IProcess sender = sig.getSender();
		// sur confirmation de l'écoute
		if (sig instanceof TSDUListening) {
			// noter le pid du PListener
			listener = sender;
		// sur création d'une PConnection
		} else if (sig instanceof TSDUConnected) {
			// noter le pid de la PConnection
			connection = sender;
			// indiquer le localPeer
			sender.add(new TSDUOpen(user, localPeer));
		// sur réception de données
		} else if (sig instanceof TSDUData) {
			TSDUData sigData = (TSDUData) sig;
			String line = sigData.line;
			// renvoyer la donnée en écho
			sender.add(new TSDUData(this, line));
			// si line = 'Salut'
			if ("Salut".equals(line)) {
				// clore la PConnection
				connection.add(new TSDUClose(localPeer));
			}
		// sur clôture de la PConnection
		} else if (sig instanceof TSDUClosed) {
			// arrêter l'écoute
			listener.add(new TSDUStop(user));
		// sur arrêt de l'écoute
		} else if (sig instanceof TSDUStopped) {
			// clore la PConnection
			connection.add(new TSDUClose(localPeer));
		}
	}

	@Override
	public void setParent(IProcess procParent, Stepper s) {
	}
}
