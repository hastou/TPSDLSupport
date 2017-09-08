package eu.telecomlille.sdl.tcp;

import eu.telecomlille.sdl.IProcess;
import eu.telecomlille.sdl.ISignal;
import eu.telecomlille.sdl.Stepper;

public class IProcessTest implements IProcess {
	// TODO declare and instantiate a Fifo

	public void doIt() {
		setParent(null, null);
		// TODO send some signals to this IProcess (from the primary thread)
		// TODO TSDUConnect, TSDUData and TSDUClose
	}

	public static void main(String[] args) {
		new IProcessTest().doIt();
	}

	@Override
	public void add(ISignal sig) {
		// TODO In a thread safe manner
			// TODO store sig in a fifo so that the other thread can get and consume it
			// notify only if the Fifo has just become non empty
	}

	@Override
	public void setParent(IProcess procParent, Stepper s) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				// TODO catch any Exception or Error
					// TODO from within an infinite loop
						// TODO in a thread safe manner
							// TODO wait while the Fifo is empty
							// TODO then get a signal from it
						// TODO process the signal (display it)
						// TODO exit the loop if sig is TSDUClose
			}
		};
		// TODO start another thread in the Runnable run method
	}
}
