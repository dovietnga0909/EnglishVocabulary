package itpsoft.englishvocabulary.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

import itpsoft.englishvocabulary.service.ScreenOnService;
import itpsoft.englishvocabulary.ultils.Log;

/**
 * Created by Do on 10/09/2015.
 */
public class ScreenOnReceiver extends BroadcastReceiver {
    private boolean screenState;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

            screenState = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            screenState = true;

        }
        Log.d("NgaDV", "screenState: " + screenState);
        // Send Current screen ON/OFF value to service
            Intent i = new Intent(context, ScreenOnService.class);
            i.putExtra("screen_state", screenState);
//            Intent i1 = createExplicitFromImplicitIntent(context,i);
            context.startService(i);
    }

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
}
