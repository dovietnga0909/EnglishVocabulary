package itpsoft.englishvocabulary.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import itpsoft.englishvocabulary.HomeActivity;
import itpsoft.englishvocabulary.R;
import itpsoft.englishvocabulary.TestActivity;
import itpsoft.englishvocabulary.models.Topic;
import itpsoft.englishvocabulary.ultils.Keyboard;

/**
 * Created by Thanh Tu on 6/8/2015.
 */
public class TopicGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Topic> data;
    private Topic topic;
    private HomeActivity homeActivity;
    private AlertDialog alertDialog;
    private Rect displayRectangle;

    public TopicGridAdapter(Context context, Topic topic) {
        this.context = context;
        this.topic = topic;
        data = topic.getAll();
        homeActivity = new HomeActivity();
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
        final ViewHolder viewHolder;
        final Topic item = getItem(i);
        if (view != null) {
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_topic1, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.imgTest = (ImageView) view.findViewById(R.id.imgTest);
            viewHolder.imgOption = (ImageView) view.findViewById(R.id.imgOption);
            viewHolder.txtNameTopic = (TextView) view.findViewById(R.id.txtNameTopic);
            viewHolder.txtNumberTopic = (TextView) view.findViewById(R.id.txtNumberVocabulary);
            view.setTag(viewHolder);
        }
        viewHolder.txtNameTopic.setText(item.getName());
        viewHolder.txtNumberTopic.setText(context.getResources().getString(R.string.number_vocabulary) + Integer.toString(item.getNumber()));
//        //imgTest
//        viewHolder.imgTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (item.getNumber() > 0) {
//                    Intent intent = new Intent();
//                    intent.setClass(context, TestActivity.class);
//                    intent.putExtra("topic_id", item.getId());
//                    intent.putExtra("topic_name", item.getName());
//                    context.startActivity(intent);
//                    ((Activity) context).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
//                } else {
//                    Toast.makeText(context, context.getResources().getString(R.string.topic_empty), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        //imgOption
        viewHolder.imgOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(context,viewHolder.imgOption);
                popupMenu.getMenuInflater().inflate(R.menu.item_option, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem itemMenu) {
                        switch (itemMenu.getItemId()){
                            case R.id.action_test:
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
                                break;
                            case R.id.action_update:
                                createDialogRenameTopic(getItem(i));
                                break;
                            case R.id.action_delete:
                                createDialogDeleteTopic(getItem(i));

                                break;
                        }
                        return false;
                    }
                });
            }
        });
        return view;
    }
    public void createDialogRenameTopic(final Topic t) {
        displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_topic, null, false);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
//                    dialogView.setMinimumHeight((int) (displayRectangle.height() * 0.9f));
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        ImageView dBack = (ImageView) dialogView.findViewById(R.id.back);
        TextView dTitle = (TextView) dialogView.findViewById(R.id.title);
        final EditText dText = (EditText) dialogView.findViewById(R.id.text);
        final ImageView dDelete = (ImageView) dialogView.findViewById(R.id.delete);
        Button dEdit = (Button) dialogView.findViewById(R.id.add);

        dTitle.setText(context.getString(R.string.rename));
        dEdit.setText(context.getString(R.string.save));
        dText.setHint(context.getString(R.string.insert_new_name));
        dText.append(t.getName());
        dText.setEllipsize(TextUtils.TruncateAt.END);
        View.OnClickListener dOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.delete:
                        dText.setText("");
                        break;
                    case R.id.back:
                        if (alertDialog.isShowing())
                            alertDialog.dismiss();
                        break;
                    case R.id.add:
                        if (dText.getText().toString().trim().length() > 0) {
                            String name = dText.getText().toString();
                            name = name.replace("'", "\"");
                            int result = topic.rename(t, name);
                            if (result == Topic.EDIT_SUCCESS) {
                                homeActivity.testSyncCate();
                                homeActivity.testSyncVoca();
                                if (alertDialog.isShowing())
                                    alertDialog.dismiss();
                                notifyDataSetChanged();
                                Toast.makeText(context, context.getString(R.string.edited), Toast.LENGTH_SHORT).show();
                            } else if (result == Topic.EDIT_SAME) {
                                Toast.makeText(context, context.getString(R.string.same), Toast.LENGTH_SHORT).show();
                            } else if (result == Topic.EDIT_EXITS) {
                                Toast.makeText(context, context.getString(R.string.exits), Toast.LENGTH_SHORT).show();
                            } else if (result == Topic.EDIT_FALSE) {
                                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, context.getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
        dDelete.setOnClickListener(dOnClickListener);
        dBack.setOnClickListener(dOnClickListener);
        dEdit.setOnClickListener(dOnClickListener);
        Keyboard.showKeyboardDialog(alertDialog);
        dText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!dText.getText().toString().trim().equals("")) {
                    dDelete.setVisibility(View.VISIBLE);
                } else {
                    dDelete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void createDialogDeleteTopic(final Topic t) {
        displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_delete, null, false);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        ImageView dBack = (ImageView) dialogView.findViewById(R.id.back);
        TextView dContent = (TextView) dialogView.findViewById(R.id.content);
        Button dDelete = (Button) dialogView.findViewById(R.id.delete);

        View.OnClickListener dOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.back:
                        if (alertDialog.isShowing())
                            alertDialog.dismiss();
                        break;
                    case R.id.delete:
                        if (alertDialog.isShowing())
                            alertDialog.dismiss();
                        int result = topic.delete(t);
                        if (result == Topic.DELETE_SUCCESS) {
                            notifyDataSetChanged();
                            homeActivity.testSyncCate();
                            homeActivity.testSyncVoca();
//                            String strIdPopubCurrent = SPUtil.instance(context).get(SPUtil.KEY_CHOOSED_ID_POPUB_TOPIC,context.getString(R.string.choose_topic));
//
                            Toast.makeText(context, context.getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
        dDelete.setOnClickListener(dOnClickListener);
        dBack.setOnClickListener(dOnClickListener);
        dContent.setText(context.getString(R.string.really_delete) + " " +t.getName() + " " + context.getString(R.string.warning));
    }

    private class ViewHolder {
        ImageView imgTest;
        ImageView imgOption;
        TextView  txtNameTopic;
        TextView  txtNumberTopic;
    }
}
