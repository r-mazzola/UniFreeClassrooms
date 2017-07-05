package com.project.rm.unifreeclassrooms;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rm on 28/06/2017.
 */

public class MessageListAdapter extends ArrayAdapter<Messaggio> {
    private Activity context;
    private List<Messaggio> messageList;

    public MessageListAdapter(Activity context, List<Messaggio> messageList){
        super(context,R.layout.list_layout, messageList);
        this.context=context;
        this.messageList=messageList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView tvCorso = (TextView) listViewItem.findViewById(R.id.tvCorso);
        TextView tvTesto = (TextView) listViewItem.findViewById(R.id.tvTesto);
        TextView tvTime = (TextView) listViewItem.findViewById(R.id.tvTime);

        Messaggio messaggio = messageList.get(position);
        tvCorso.setText(messaggio.getCorso());
        tvTesto.setText(messaggio.getTestoMessaggio());
        tvTime.setText(messaggio.getTimeStamp());

        return listViewItem;
    }
}
