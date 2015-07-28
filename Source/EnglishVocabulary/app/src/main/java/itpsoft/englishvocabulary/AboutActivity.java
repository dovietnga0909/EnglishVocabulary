package itpsoft.englishvocabulary;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
                Toast.makeText(getApplicationContext(),"chuc nang dang cap nhat",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
