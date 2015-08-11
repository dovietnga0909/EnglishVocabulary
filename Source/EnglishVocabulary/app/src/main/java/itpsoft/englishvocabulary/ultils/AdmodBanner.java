package itpsoft.englishvocabulary.ultils;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by LuanDT on 11/08/2015.
 */
public class AdmodBanner {
    private AdView adView;
    private AdRequest adRequest;

    public AdmodBanner(AdView adView) {
        this.adView = adView;
        Log.d("LuanDT", "new AdmodBanner");
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d("LuanDT", "AdmodBanner onAdLoaded()");
                super.onAdLoaded();
            }
        });
    }
}
