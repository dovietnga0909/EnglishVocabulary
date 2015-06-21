package itpsoft.englishvocabulary.ultils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Do on 10/06/2015.
 */
public class SpeakEnglish implements TextToSpeech.OnInitListener {
    TextToSpeech textToSpeech;
    Context mContext;
    String textSpeech;

    public SpeakEnglish(Context mContext, TextToSpeech textToSpeech) {
        this.mContext = mContext;
        this.textToSpeech = textToSpeech;
    }

    /**
     * Called to signal the completion of the TextToSpeech engine initialization.
     *
     * @param status {@link TextToSpeech#SUCCESS} or {@link TextToSpeech#ERROR}.
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            //US Anh my
            //UK Anh Anh
            int result = textToSpeech.setLanguage(Locale.ENGLISH);

            if(result == textToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                android.util.Log.d("NgaDV", "Language is not supported");
                Toast.makeText(mContext, "Ngon ngu nay khong dc ho tro", Toast.LENGTH_LONG).show();
            }else {
                speakOut(textSpeech);
            }
        }else {
            android.util.Log.d("NgaDV", "Khoi tao Language fail");
        }
    }

    public void speakOut(String textSpeech){
        textToSpeech.speak(textSpeech,TextToSpeech.QUEUE_FLUSH,null);
    }

    public boolean isSpeaking(){
        if(textToSpeech.isSpeaking()){
            return true;
        }
        else {
            return  false;
        }
    }
}
