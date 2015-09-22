package itpsoft.englishvocabulary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import itpsoft.englishvocabulary.adapter.VocabularyAdapter;
import itpsoft.englishvocabulary.models.Vocabulary;
import itpsoft.englishvocabulary.ultils.Log;
import itpsoft.englishvocabulary.ultils.SPUtil;
import itpsoft.englishvocabulary.ultils.SpeakEnglish;

/**
 * Created by Do on 13/09/2015.
 */
public class PopubVocaActivity extends AboutActivity implements TextToSpeech.OnInitListener,View.OnClickListener {


    private VocabularyAdapter adapter;
    private ArrayList<Vocabulary> listVocabulary, listVocaDisplay;
    private ListView listView;
    private Vocabulary vocabulary;
    public  String strIdTopic;
    private TextToSpeech textToSpeech;
    private SpeakEnglish speakEnglish;
    private Button btnFinish,btnStartApp;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popub_voca);
        init();
        SPUtil.instance(PopubVocaActivity.this).set(SPUtil.KEY_FIRST_RUN_POPUB_ACTIVITY, 0);
        Log.d("NgaDV", "KEY_FIRST_RUN_POPUB_ACTIVITY: " + SPUtil.instance(PopubVocaActivity.this).get(SPUtil.KEY_FIRST_RUN_POPUB_ACTIVITY, 1) + "");

    }
    private void init(){
        context = PopubVocaActivity.this;
        textToSpeech = new TextToSpeech(context, this);
        speakEnglish = new SpeakEnglish(context, textToSpeech);

        strIdTopic = SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_ID_POPUB_TOPIC,"");

        Log.d("NgaDV","strIdTopic: " + strIdTopic);

        btnFinish = (Button)findViewById(R.id.btnFinish);
        btnStartApp = (Button)findViewById(R.id.btnStartApp);

        //listview
        listView = (ListView) findViewById(R.id.lvVoca);
        vocabulary = new Vocabulary(context);
        listVocabulary = vocabulary.getVocaByStrId(strIdTopic);
        listVocaDisplay = new ArrayList<>();
        adapter = new VocabularyAdapter(context, listVocaDisplay);

        setListVoca();
        btnFinish.setOnClickListener(this);
        btnStartApp.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.btnFinish:
                SPUtil.instance(PopubVocaActivity.this).set(SPUtil.KEY_FIRST_RUN_POPUB_ACTIVITY, 1);
                Log.d("NgaDV", "KEY_FIRST_RUN_POPUB_ACTIVITY: " + SPUtil.instance(PopubVocaActivity.this).get(SPUtil.KEY_FIRST_RUN_POPUB_ACTIVITY, 1) + "");
                finish();
                break;
            case R.id.btnStartApp:
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("itpsoft.englishvocabulary");
                startActivity(launchIntent);
                SPUtil.instance(PopubVocaActivity.this).set(SPUtil.KEY_FIRST_RUN_POPUB_ACTIVITY, 1);
                Log.d("NgaDV", "KEY_FIRST_RUN_POPUB_ACTIVITY: " + SPUtil.instance(PopubVocaActivity.this).get(SPUtil.KEY_FIRST_RUN_POPUB_ACTIVITY, 1) + "");
                finish();
                break;
        }
    }
    //setListVoca
    private void setListVoca(){
        Collections.shuffle(listVocabulary);
        if(listVocabulary.size() >= Integer.parseInt(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_KEY_POPUB_NUMBER_VOCA,"")) ){
            for (int i = 0;i < Integer.parseInt(SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_KEY_POPUB_NUMBER_VOCA,""));i++){
                listVocaDisplay.add(listVocabulary.get(i));
            }
        }else {
                for (int i = 0;i < listVocabulary.size();i++){
                listVocaDisplay.add(listVocabulary.get(i));
            }
        }
        Log.d("NgaDV","listVocabulary.size(): " + listVocabulary.size());
        Log.d("NgaDV","listVocaDisplay.size(): " + listVocaDisplay.size());


        listView.setAdapter(adapter);

        //click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                speakEnglish.speakOut(((Vocabulary) adapterView.getAdapter().getItem(i)).getEnglish());
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SPUtil.instance(PopubVocaActivity.this).set(SPUtil.KEY_FIRST_RUN_POPUB_ACTIVITY, 1);
        Log.d("NgaDV", "KEY_FIRST_RUN_POPUB_ACTIVITY: " + SPUtil.instance(PopubVocaActivity.this).get(SPUtil.KEY_FIRST_RUN_POPUB_ACTIVITY, 1) + "");
    }

    @Override
    public void onInit(int i) {

    }
    @Override
    protected void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        SPUtil.instance(PopubVocaActivity.this).set(SPUtil.KEY_FIRST_RUN_POPUB_ACTIVITY, 1);
        super.onDestroy();
    }
}
