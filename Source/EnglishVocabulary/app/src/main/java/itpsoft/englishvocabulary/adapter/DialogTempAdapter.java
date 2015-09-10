package itpsoft.englishvocabulary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import java.util.ArrayList;

import itpsoft.englishvocabulary.R;
import itpsoft.englishvocabulary.models.Temp;

public class DialogTempAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Temp> listTemp;
    private int resources;
    private CheckedTextView ckItem;

    public DialogTempAdapter(Context context, ArrayList<Temp> listTemp, int resources) {
        this.context = context;
        this.listTemp = listTemp;
        this.resources = resources;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Temp> getListTemp() {
        return listTemp;
    }

    public void setListTemp(ArrayList<Temp> listTemp) {
        this.listTemp = listTemp;
    }

    public int getResources() {
        return resources;
    }

    public void setResources(int resources) {
        this.resources = resources;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return listTemp.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Temp getItem(int position) {
        return listTemp.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Temp temp = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(resources,parent,false);

            ckItem = (CheckedTextView)convertView.findViewById(R.id.ckItem);
            convertView.setTag(ckItem);
        }else {
            ckItem = (CheckedTextView) convertView.getTag();
        }

        ckItem.setText(temp.getText());

        return convertView;
    }
}
