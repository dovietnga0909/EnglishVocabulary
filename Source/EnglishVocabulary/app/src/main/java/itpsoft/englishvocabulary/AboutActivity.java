package itpsoft.englishvocabulary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Do on 28/07/2015.
 */
public class AboutActivity extends Activity {
    private Button btnFeedBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        btnFeedBack = (Button) findViewById(R.id.btnFeedBack);

        btnFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this,FeedBackActivity.class);
                startActivity(intent);
            }
        });
    }
}
