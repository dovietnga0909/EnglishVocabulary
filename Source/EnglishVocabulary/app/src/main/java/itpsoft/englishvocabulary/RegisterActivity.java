package itpsoft.englishvocabulary;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Created by Do on 20/07/2015.
 */
public class RegisterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide keyboard firstTime
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        setContentView(R.layout.activity_register);
    }
}
