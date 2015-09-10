package itpsoft.englishvocabulary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import itpsoft.englishvocabulary.R;
import itpsoft.englishvocabulary.models.Topic;

public class DialogAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Topic> listTopic;
    private int resources;


    public DialogAdapter(Context context, int resources, ArrayList<Topic> listTopic) {
        this.context = context;
        this.resources = resources;
        this.listTopic = listTopic;
    }

    public void setData(ArrayList<Topic> listTopic) {
        this.listTopic = listTopic;
    }

    @Override
    public int getCount() {
        return listTopic.size();
    }

    @Override
    public Topic getItem(int arg0) {
        return listTopic.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return listTopic.get(arg0).getId();
    }

    public ArrayList<Topic> getData() {
        return listTopic;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        final Topic topic = getItem(position);
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(resources,
                    viewGroup, false);
            holder.status = (CheckBox) view.findViewById(R.id.ckStatus);
            holder.title = (TextView) view.findViewById(R.id.tvTitle);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.status.setChecked(topic.isChecked());
        holder.title.setText(topic.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topic.isChecked()) {
                    holder.status.setChecked(false);
                    topic.setIsChecked(false);
                } else {
                    holder.status.setChecked(true);
                    topic.setIsChecked(true);
                }
            }
        });

        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topic.isChecked()) {
                    holder.status.setChecked(false);
                    topic.setIsChecked(false);
                } else {
                    holder.status.setChecked(true);
                    topic.setIsChecked(true);
                }
            }
        });
        return view;
    }

    private class ViewHolder {
        CheckBox status;
        TextView title;
    }
}
