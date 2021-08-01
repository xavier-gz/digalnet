package gal.sli.digalnet.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import gal.sli.digalnet.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;

    // group titles
    private List<String> listDataGroup;

    // child data in format of header title, child title
    private HashMap<String, List<String>> listDataChild;

    public ExpandableListViewAdapter(Context context, List<String> listDataGroup,
                                     HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.listDataGroup = listDataGroup;
        this.listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


   private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            int id;

            if (source.equals("glg.png")) {
                id = R.drawable.glg;
                Drawable d = context.getResources().getDrawable(id);
                //d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
                d.setBounds(0,10,d.getIntrinsicWidth()/5,d.getIntrinsicHeight()/5);
                //getIntrinsicWidth()/5 divide á metade o tamaño da icona
                return d;

            }
            else if (source.equals("eng.png")) {
                id = R.drawable.eng;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,10,d.getIntrinsicWidth()/5,d.getIntrinsicHeight()/5);
                return d;

            }
            else if (source.equals("por.png")) {
                id = R.drawable.por;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,10,d.getIntrinsicWidth()/5,d.getIntrinsicHeight()/5);
                return d;

            }
            else if (source.equals("cat.png")) {
                id = R.drawable.cat;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,10,d.getIntrinsicWidth()/5,d.getIntrinsicHeight()/5);
                return d;

            }
            else if (source.equals("eus.png")) {
                id = R.drawable.eus;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,10,d.getIntrinsicWidth()/5,d.getIntrinsicHeight()/5);
                return d;

            }
            else if (source.equals("spa.png")) {
                id = R.drawable.spa;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,10,d.getIntrinsicWidth()/5,d.getIntrinsicHeight()/5);
                return d;

            }
            else if (source.equals("zho.png")) {
                id = R.drawable.zho;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,10,d.getIntrinsicWidth()/5,d.getIntrinsicHeight()/5);
                return d;

            }
            else if (source.equals("qcn.png")) {
                id = R.drawable.qcn;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,10,d.getIntrinsicWidth()/5,d.getIntrinsicHeight()/5);
                return d;

            }
            else if (source.equals("deu.png")) {
                id = R.drawable.deu;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,10,d.getIntrinsicWidth()/5,d.getIntrinsicHeight()/5);
                return d;

            }
            else if (source.equals("ita.png")) {
                id = R.drawable.ita;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,10,d.getIntrinsicWidth()/5,d.getIntrinsicHeight()/5);
                return d;

            }
            else if (source.equals("lat.png")) {
                id = R.drawable.lat;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,10,d.getIntrinsicWidth()/5,d.getIntrinsicHeight()/5);
                return d;

            }
            else if (source.equals("c.png")) {
                id = R.drawable.c;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,0,d.getIntrinsicWidth()/6,d.getIntrinsicHeight()/6);
            return d;

            }
            else if (source.equals("d.png")) {
                id = R.drawable.d;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,0,d.getIntrinsicWidth()/6,d.getIntrinsicHeight()/6);
                return d;

            }
           else if (source.equals("s.png")) {
                id = R.drawable.s;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,0,d.getIntrinsicWidth()/6,d.getIntrinsicHeight()/6);
                return d;
            }
             else if (source.equals("t.png")) {
                id = R.drawable.t;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,0,d.getIntrinsicWidth()/6,d.getIntrinsicHeight()/6);
                return d;

            }
           else if (source.equals("v.png")) {
                id = R.drawable.v;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,0,d.getIntrinsicWidth()/6,d.getIntrinsicHeight()/6);
                return d;

            }
            else if (source.equals("f.png")) {
                id = R.drawable.f;
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0,0,d.getIntrinsicWidth()/6,d.getIntrinsicHeight()/6);
                return d;

            }
            else {
                return null;
            }

        }
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row_child, null);
        }

//espazo entre fillos...:  left, top, right, bottom
       if (!isLastChild) {
           convertView.setPadding(10, 0, 20, 20);
       }
            else {
           convertView.setPadding(10, 0, 20, 30);
       }
//...espazo entre fillos


            TextView textViewChild = convertView
                    .findViewById(R.id.textViewChild);

            textViewChild.setText(Html.fromHtml(childText, new ImageGetter(), null));


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);


        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row_group, null);

        }

        convertView.setPadding(0, 0, 15, 10);

        TextView textViewGroup = convertView
                .findViewById(R.id.textViewGroup);
        textViewGroup.setTypeface(null, Typeface.BOLD);
        textViewGroup.setText(headerTitle);
        return convertView;

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

