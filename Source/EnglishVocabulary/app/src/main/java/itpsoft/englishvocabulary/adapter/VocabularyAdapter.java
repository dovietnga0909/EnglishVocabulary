package itpsoft.englishvocabulary.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import itpsoft.englishvocabulary.R;
import itpsoft.englishvocabulary.models.Vocabulary;
import itpsoft.englishvocabulary.ultils.SpeakEnglish;

/**
 * Created by luand_000 on 05/06/2015.
 */
public class VocabularyAdapter extends BaseAdapter implements TextToSpeech.OnInitListener {

    private Context context;
    private ArrayList<Vocabulary> listVocabulary;
    private TextToSpeech textToSpeech;
    private SpeakEnglish speakEnglish;

    public VocabularyAdapter(Context context, ArrayList<Vocabulary> listVocabulary) {
        this.context = context;
        this.listVocabulary = listVocabulary;
    }

    @Override
    public int getCount() {
        return listVocabulary.size();
    }

    @Override
    public Object getItem(int position) {
        return listVocabulary.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listVocabulary.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_vocabulary, parent, false);

            viewHolder.txtEnglish = (TextView) convertView.findViewById(R.id.txtEnglish);
            viewHolder.txtVietnamese = (TextView) convertView.findViewById(R.id.txtVietnamese);
            viewHolder.imgSound = (ImageView) convertView.findViewById(R.id.imgSound);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Vocabulary vocabulary = listVocabulary.get(position);
        viewHolder.txtEnglish.setText(vocabulary.getEnglish());
        viewHolder.txtVietnamese.setText(vocabulary.getVietnamese());

        textToSpeech = new TextToSpeech(context, this);
        speakEnglish = new SpeakEnglish(context, textToSpeech);

        //click sound
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakEnglish.speakOut(vocabulary.getEnglish());

            }
        });

        return convertView;
    }

    @Override
    public void onInit(int i) {

    }

    class ViewHolder {
        TextView txtEnglish;
        TextView txtVietnamese;
        ImageView imgSound;
    }
}
