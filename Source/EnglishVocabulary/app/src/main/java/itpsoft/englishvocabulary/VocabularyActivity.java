package itpsoft.englishvocabulary;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import itpsoft.englishvocabulary.adapter.VocabularyAdapter;
import itpsoft.englishvocabulary.models.Vocabulary;
import itpsoft.englishvocabulary.ultils.SpeakEnglish;


public class VocabularyActivity extends ActionBarActivity implements TextToSpeech.OnInitListener {

    private ActionBar actionBar;
    private VocabularyAdapter adapter;
    private ArrayList<Vocabulary> listVocabulary;
    private ListView listView;
    private Vocabulary vocabulary;
    private int idTopic;
    private String topicName;
    private EditText edtEnglish, edtVietnamese;
    private Button btnAddVoca;
    private LinearLayout control_btn_update;
    private TextToSpeech textToSpeech;
    private SpeakEnglish speakEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        textToSpeech = new TextToSpeech(VocabularyActivity.this, this);
        speakEnglish = new SpeakEnglish(VocabularyActivity.this, textToSpeech);

        Intent intent = getIntent();
        idTopic = intent.getIntExtra("topic_id", 0);
        topicName = intent.getStringExtra("topic_name");

        edtEnglish = (EditText) findViewById(R.id.edtEnglish);
        edtVietnamese = (EditText) findViewById(R.id.edtVietnamese);
        btnAddVoca = (Button) findViewById(R.id.btnAddVoca);
        control_btn_update = (LinearLayout) findViewById(R.id.control_btn_update);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionBar)));
        actionBar.setTitle(topicName);

        listView = (ListView) findViewById(R.id.listVocabulary);
        vocabulary = new Vocabulary();
        listVocabulary = vocabulary.initListVocabulary(idTopic);
        adapter = new VocabularyAdapter(VocabularyActivity.this, listVocabulary);
        listView.setAdapter(adapter);

        //click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                speakEnglish.speakOut(((Vocabulary) adapterView.getAdapter().getItem(i)).getEnglish());
            }
        });

        //long click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                edtEnglish.setText(((Vocabulary) adapterView.getAdapter().getItem(i)).getEnglish());
                edtVietnamese.setText(((Vocabulary) adapterView.getAdapter().getItem(i)).getVietnamese());
                control_btn_update.setVisibility(View.VISIBLE);
                btnAddVoca.setVisibility(View.GONE);
                return true;
            }
        });

    }

    @Override
    protected void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_vocabulary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInit(int i) {

    }
}