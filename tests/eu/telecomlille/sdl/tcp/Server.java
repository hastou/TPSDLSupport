package eu.telecomlille.sdl.tcp;

import eu.telecomlille.sdl.IProcess;
import eu.telecomlille.sdl.ISignal;
import eu.telecomlille.sdl.Stepper;
import eu.telecomlille.sdl.tcp.PListener;
import eu.telecomlille.sdl.tcp.TSDUClosed;
import eu.telecomlille.sdl.tcp.TSDUConnected;
import eu.telecomlille.sdl.tcp.TSDUOpen;
import eu.telecomlille.sdl.tcp.TSDUStop;

/**
 * Mettre en oeuvre PListener et PConnection sans TCPLayer
 */
public class Server implements IProcess {
	/** Le PListener mis en oeuvre */
	protected PListener listener;
	/** La PConnection mise en oeuvre */
	protected IProcess connection;
	/** L'utilisateur du PListener */
	protected IProcess user;
	/** L'utilisateur de la PConnection */
	protected IProcess localPeer;

	protected void doIt() {
		/* this joue les 2 r�les Server et ClientProxy */
		user = this;
		localPeer = this;
		listener = new PListener("localhost", 35000, user);
		listener.setParent(null, null);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Server server = new Server();
		server.doIt();
	}

	@Override
	public void add(ISignal sig) {
		System.out.println("server received " + sig);
		// si une PConnection est cr��e
		if (sig instanceof TSDUConnected) {
			connection = sig.getSender();
			// Indiquer l'utilisateur de la PConnection
			connection.add(new TSDUOpen(user, localPeer));
		// si la PConnection est close (� l'initiative du client)
		} else if (sig instanceof TSDUClosed) {
			// arr�ter l'�coute en envoyant un TSDUStop
			listener.add(new TSDUStop(this));	// => L'�coute s'arr�te
// TODO d�commenter les lignes qui suivent pour tester l'arr�t du serveur
		} else if (sig instanceof TSDUData) {
			TSDUData s = (TSDUData) sig;
			if ("Salut".equals(s.line))
				listener.add(new TSDUStop(this));
		// si l'�coute s'arr�te
		} else if (sig instanceof TSDUStopped) {
			// clore la connexion
			connection.add(new TSDUClose(localPeer));
		}
	}

	@Override
	public void setParent(IProcess procParent, Stepper s) {
	}
}
