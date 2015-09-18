package itpsoft.englishvocabulary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Do on 11/09/2015.
 */
public class PopubAlarmService extends Service {
    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * @param intent
     * @param startId
     * @deprecated Implement {@link #onStartCommand(Intent, int, int)} instead.
     */
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
