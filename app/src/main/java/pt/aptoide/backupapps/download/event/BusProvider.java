package pt.aptoide.backupapps.download.event;

import android.os.Handler;
import android.os.Looper;
import com.squareup.otto.Bus;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 01-07-2013
 * Time: 11:48
 * To change this template use File | Settings | File Templates.
 */
public class BusProvider extends Bus {

  private static final BusProvider BUS = new BusProvider();
  private final Handler mainThread = new Handler(Looper.getMainLooper());

  private BusProvider() {
    // No instances.
  }

  public static BusProvider getInstance() {
    return BUS;
  }

  @Override public void post(final Object event) {

    if (Looper.myLooper() == Looper.getMainLooper()) {
      super.post(event);
    } else {
      mainThread.post(new Runnable() {
        @Override public void run() {
          post(event);
        }
      });
    }
  }

  public void nativePost(final Object event) {
    super.post(event);
  }
}
