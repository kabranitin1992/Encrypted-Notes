package com.aroner.encrypted_notes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aroner.encrypted_notes.Bean.GridviewBean;
import com.aroner.encrypted_notes.GlobalModule;
import com.aroner.encrypted_notes.R;

import java.util.ArrayList;

/**
 * Created by shree on 13-10-2017.
 */

public class GridviewAdapter extends ArrayAdapter<String> {
    ArrayList<String> arrlstGridviewBean;
    Context ctx;
    int resourseId;
    GridviewBean bean;

    public GridviewAdapter(Context ctx, int resourseId, ArrayList<String> arrlstGridviewBean) {
        super(ctx, resourseId, arrlstGridviewBean);
        this.ctx = ctx;
        this.resourseId = resourseId;
        this.arrlstGridviewBean = arrlstGridviewBean;
    }

    public static class Holder {

        TextView tvText;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final GridviewAdapter.Holder holder;
        View rootView = convertView;
        try {


            if (rootView == null || rootView.getTag() == null) {
                LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
                holder = new GridviewAdapter.Holder();

                rootView = inflater.inflate(resourseId, parent, false);

                holder.tvText = (TextView) rootView.findViewById(R.id.tvText);


            } else {
                holder = (GridviewAdapter.Holder) rootView.getTag();
            }
//            bean = arrlstGridviewBean.get(position);

//            holder.tvText.setTextSize(Integer.parseInt(GlobalModule.textSize));
            holder.tvText.setText(arrlstGridviewBean.get(position));


//            String oldDateString =arrlstNotificationBean.get(position).getDate() ;
//            String newDateString;
//
//            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
//            Date d = sdf.parse(oldDateString);
//            sdf.applyPattern(NEW_FORMAT);getSubstringEncryptKey
//            newDateString = sdf.format(d);


//
//            Picasso.with(ctx).load(arrlstNotificationBean.get(position).getImage()).into(holder.ivImage);

        } catch (Exception e) {
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }


}
