import java.io.IOException;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class PMMIDlet extends MIDlet {
	private Game instance;

	public final void startApp() {
		if (instance == null) {
			try {
				instance = new Game();
				Display dpy;
				(dpy = Display.getDisplay(this)).setCurrent(instance);
				Game.setDisplay(dpy);
				instance.init(this);
			} catch (IOException e) {
			}
		} else {
			Game.Method29();
			Game.Method31();
		}
	}

	public final void pauseApp() {
		Game.Field4 = 0;
		Game.Field12 = true;
		Game.Method29();
		Game.Method31();
		notifyPaused();
		Game.Method261();
	}

	public final void destroyApp(boolean unconditional) {
	}

	public final void exit() {
		notifyDestroyed();
	}
}
