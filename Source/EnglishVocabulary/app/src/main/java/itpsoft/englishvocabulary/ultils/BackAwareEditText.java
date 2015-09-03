package itpsoft.englishvocabulary.ultils;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by Do on 25/08/2015.
 */
public class BackAwareEditText extends EditText {
    private BackPressedListener mOnImeBack;
    public BackAwareEditText(Context context) {
        super(context);
    }
    /* constructors */

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (mOnImeBack != null) mOnImeBack.onImeBack(this);
        }
        return super.dispatchKeyEvent(event);
    }

    public void setBackPressedListener(BackPressedListener listener) {
        mOnImeBack = listener;
    }

    public interface BackPressedListener {
        void onImeBack(BackAwareEditText editText);
    }
}
