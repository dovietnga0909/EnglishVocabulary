package itpsoft.englishvocabulary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import itpsoft.englishvocabulary.R;
import itpsoft.englishvocabulary.models.Vocabulary;

/**
 * Created by luand_000 on 05/06/2015.
 */
public class VocabularyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Vocabulary> listVocabulary;

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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_vocabulary, parent, false);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Vocabulary vocabulary = listVocabulary.get(position);

        return convertView;
    }

    class ViewHolder {
        TextView txtEnglish;
        TextView txtVietnamese;
    }
}
