import java.io.IOException;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class PMMIDlet extends MIDlet {
   private a Field474;

   public final void startApp() {
      if (this.Field474 == null) {
         try {
            this.Field474 = new a();
            Display var1;
            (var1 = Display.getDisplay(this)).setCurrent(this.Field474);
            a.Method99(var1);
            this.Field474.Method0(this);
         } catch (IOException var2) {
         }
      } else {
         a.Method29();
         a.Method31();
      }
   }

   public final void pauseApp() {
      a.Field4 = 0;
      a.Field12 = true;
      a.Method29();
      a.Method31();
      this.notifyPaused();
      a.Method261();
   }

   public final void destroyApp(boolean var1) {
   }

   public final void Method304() {
      this.notifyDestroyed();
   }
}
