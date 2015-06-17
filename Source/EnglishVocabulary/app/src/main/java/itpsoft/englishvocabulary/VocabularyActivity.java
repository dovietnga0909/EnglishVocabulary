package itpsoft.englishvocabulary;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.ArrayList;

import itpsoft.englishvocabulary.adapter.VocabularyAdapter;
import itpsoft.englishvocabulary.models.Vocabulary;


public class VocabularyActivity extends ActionBarActivity {

    private ActionBar actionBar;
    private VocabularyAdapter adapter;
    private ArrayList<Vocabulary> listVocabulary;
    private ListView listView;
    private Vocabulary vocabulary;
    private int idTopic;
    private String topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        Intent intent = getIntent();
        idTopic = intent.getIntExtra("topic_id", 0);
        topicName = intent.getStringExtra("topic_name");

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionBar)));
        actionBar.setTitle(topicName);

        listView = (ListView) findViewById(R.id.listVocabulary);
        vocabulary = new Vocabulary();
        listVocabulary = vocabulary.initListVocabulary(idTopic);
        adapter = new VocabularyAdapter(VocabularyActivity.this, listVocabulary);
        listView.setAdapter(adapter);

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
}
