package itpsoft.englishvocabulary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import itpsoft.englishvocabulary.ultils.AdmodBanner;

/**
 * Created by Do on 28/07/2015.
 */
public class AboutActivity extends Activity implements View.OnClickListener {
    private Button btnFeedBack;
    private TextView txtWebsite;
    private AdView adView;
    private AdmodBanner admodBanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        /*adView = (AdView) findViewById(R.id.adView);
        admodBanner = new AdmodBanner(adView);*/

        btnFeedBack = (Button) findViewById(R.id.btnFeedBack);
        txtWebsite = (TextView) findViewById(R.id.tvWebsite);

        btnFeedBack.setOnClickListener(this);
        txtWebsite.setOnClickListener(this);


    }




    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = new Intent();
        switch (id){
            case R.id.btnFeedBack:
                intent.setClass(AboutActivity.this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.tvWebsite:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://itplus-academy.edu.vn"));
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }
}
