package itpsoft.englishvocabulary.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import itpsoft.englishvocabulary.PopubVocaActivity;
import itpsoft.englishvocabulary.receiver.ScreenOnReceiver;
import itpsoft.englishvocabulary.ultils.SPUtil;

/**
 * Created by Do on 11/09/2015.
 */
public class ScreenOnService extends Service {
    BroadcastReceiver mReceiver = null;
    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // Register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenOnReceiver();
        registerReceiver(mReceiver, filter);
    }

    /**
     * @param intent
     * @param startId
     * @deprecated Implement {@link #onStartCommand(Intent, int, int)} instead.
     */
    @Override
    public void onStart(Intent intent, int startId) {
        boolean screenState = false;

        try{
            // Get ON/OFF values sent from receiver ( AEScreenOnOffReceiver.java )
            screenState = intent.getBooleanExtra("screen_state", false);

        }catch(Exception e){}

        if (screenState) {
            Log.d("NgaDV", "Screen On");
            int num_screen_on = SPUtil.instance(ScreenOnService.this).get(SPUtil.KEY_NUM_SCREEN_ON, 1);
            int num_screen_on_temp = num_screen_on;
            if (num_screen_on_temp == 1){
                num_screen_on += 1;

                SPUtil.instance(ScreenOnService.this).set(SPUtil.KEY_NUM_SCREEN_ON, num_screen_on);
            }
            if (num_screen_on_temp == 2){
                int first_run_popub_activity = SPUtil.instance(ScreenOnService.this).get(SPUtil.KEY_FIRST_RUN_POPUB_ACTIVITY, 1);
                // Some time required to start any service
                if (first_run_popub_activity == 1){
                    Intent intent1 = new Intent(ScreenOnService.this, PopubVocaActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    SPUtil.instance(ScreenOnService.this).set(SPUtil.KEY_NUM_SCREEN_ON, 1);
                }

            }
        } else {
            Log.d("NgaDV","Screen Off");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
        public void onDestroy() {
//        super.onDestroy();
        Log.d("NgaDV", "Service  destroy");
        if(mReceiver!=null)
            unregisterReceiver(mReceiver);
    }
}
