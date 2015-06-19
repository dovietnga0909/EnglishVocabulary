package itpsoft.englishvocabulary.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import itpsoft.englishvocabulary.R;
import itpsoft.englishvocabulary.TestActivity;
import itpsoft.englishvocabulary.models.Topic;

/**
 * Created by Thanh Tu on 6/8/2015.
 */
public class TopicAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Topic> data;
    private Topic topic;

    public TopicAdapter(Context context, Topic topic) {
        this.context = context;
        this.topic = topic;
        data = topic.getAll();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        data = topic.getAll();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Topic getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return data.get(i).getId();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        final Topic item = getItem(i);
        if (view != null) {
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_topic, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.item = (LinearLayout) view.findViewById(R.id.item);
            viewHolder.check = (ImageView) view.findViewById(R.id.check);
            viewHolder.number = (TextView) view.findViewById(R.id.number);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.divider = (View) view.findViewById(R.id.divider);
            view.setTag(viewHolder);
        }
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0, 0});
        drawable.setColor(Color.parseColor(item.getColor()));
        drawable.setCornerRadius(20);
        viewHolder.number.setBackgroundDrawable(drawable);
        viewHolder.name.setText(item.getName());
        viewHolder.number.setText(Integer.toString(item.getNumber()));
        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getNumber() > 0) {
                    Intent intent = new Intent();
                    intent.setClass(context, TestActivity.class);
                    intent.putExtra("topic_id", item.getId());
                    intent.putExtra("topic_name", item.getName());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.topic_empty), Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (i == data.size() - 1) {
            viewHolder.divider.setVisibility(View.GONE);
        } else {
            viewHolder.divider.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private class ViewHolder {
        LinearLayout item;
        ImageView check;
        TextView number, name;
        View divider;
    }
}
