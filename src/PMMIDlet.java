import java.io.IOException;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class PMMIDlet extends MIDlet {
	private Game instance;

	public final void startApp() {
		if (instance == null) {
			try {
				instance = new Game();
				Display dpy = Display.getDisplay(this);
				dpy.setCurrent(instance);
				Game.setDisplay(dpy);
				instance.init(this);
			} catch (IOException e) {
			}
		} else {
			Game.queueAllSoundsForCleanup();
			Game.queueSoundCleanup();
		}
	}

	public final void pauseApp() {
		Game.heldKeys = 0;
		Game.paused = true;
		Game.queueAllSoundsForCleanup();
		Game.queueSoundCleanup();
		notifyPaused();
		Game.setPauseScreenDraw();
	}

	public final void destroyApp(boolean unconditional) {
	}

	public final void exit() {
		notifyDestroyed();
	}
}
