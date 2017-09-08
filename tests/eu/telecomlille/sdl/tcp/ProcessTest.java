package eu.telecomlille.sdl.tcp;

import eu.telecomlille.sdl.Process;

public class ProcessTest extends Process {
	public void doIt() {
		setParent(null, null); // démarre le thread du Process
		// TODO envoyer des signaux à ce Process : TSDUConnect, TSDUData et TSDUClose
	}

	// TODO redéfinir onStart
		// TODO afficher onStart

	// TODO redéfinir dispatch
	protected void dispatch() {
		// TODO afficher le signal _sig (hérité)
		// TODO afficher sender (hérité)
		// TODO appeler stop hérité sur réception du signal TSDUClose
	}

	public static void main(String[] args) {
		new ProcessTest().doIt();
	}
}
