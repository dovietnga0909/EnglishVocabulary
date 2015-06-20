package itpsoft.englishvocabulary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import itpsoft.englishvocabulary.R;
import itpsoft.englishvocabulary.models.MenuItem;
import itpsoft.englishvocabulary.ultils.SPUtil;

/**
 * Created by Thanh Tu on 6/6/2015.
 */
public class MenuAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Object> items;

    public MenuAdapter(Context context, ArrayList<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        Object item = getItem(i);
        if (view != null) {
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_menu,
                    viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.item = (LinearLayout) view.findViewById(R.id.item);
            viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.value = (TextView) view.findViewById(R.id.value);
            viewHolder.indicator = (View) view.findViewById(R.id.indicator);
            view.setTag(viewHolder);
        }
        if(item instanceof MenuItem){
            viewHolder.item.setVisibility(View.VISIBLE);
            viewHolder.indicator.setVisibility(View.GONE);
            ShapeDrawable drawable = new ShapeDrawable();
            drawable.setShape(new OvalShape());
            drawable.getPaint().setColor(Color.parseColor(((MenuItem) item).getColor()));
            viewHolder.icon.setBackgroundDrawable(drawable);
            viewHolder.icon.setImageResource(((MenuItem) item).getIcon());
            viewHolder.title.setText(((MenuItem) item).getTitle());
            viewHolder.value.setText(((MenuItem)item).getValue());
        }else{
            viewHolder.item.setVisibility(View.GONE);
            viewHolder.indicator.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private class ViewHolder {
        LinearLayout item;
        ImageView icon;
        TextView title, value;
        View indicator;
    }
}
