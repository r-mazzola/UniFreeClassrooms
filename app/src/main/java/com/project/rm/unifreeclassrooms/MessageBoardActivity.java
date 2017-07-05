package com.project.rm.unifreeclassrooms;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageBoardActivity extends AppCompatActivity {
    List<Messaggio> messageList;
    TextView tvInfo;
    ListView lvMessaggi;

    DatabaseReference databaseMessages;

    List<Messaggio> messaggeList;
    List<Corso> corsiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvMessaggi=(ListView)findViewById(R.id.lvMessaggi);
        messaggeList =new ArrayList<>();
        tvInfo = (TextView)findViewById(R.id.tvInfo);

        final Corsi_DBHandler db_corsi = new Corsi_DBHandler(MessageBoardActivity.this);
        corsiList = db_corsi.getAllCorsi();

        databaseMessages = FirebaseDatabase.getInstance().getReference("messages");

    }


    //    Gestione back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){
        super.onStart();

        setTvInfo("non_iscritto");

        final Messaggi_DBHandler msgDB = new Messaggi_DBHandler(MessageBoardActivity.this);

// GESTIONE OFFLINE CON DB LOCALE
        /*List<Messaggio> allMsgList = msgDB.getAllMsg();
        if(!allMsgList.isEmpty()){
            messaggeList.clear();
            for(Messaggio m : allMsgList){
                for(Corso c : corsiList) {
                    if (c.getNome_Corso().equals(m.getCorso()))
                        messageList.add(m);
                }
            }
        }
        MessageListAdapter adapter = new MessageListAdapter(MessageBoardActivity.this, messaggeList);
        lvMessaggi.setAdapter(adapter);*/
//FINE GESTIONE DB LOCALE

        databaseMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messaggeList.clear();
                msgDB.deletAllMsg();
                for(DataSnapshot messageSnapshot : dataSnapshot.getChildren()){
                    Messaggio messaggio = messageSnapshot.getValue(Messaggio.class);
                    msgDB.addMsg(messaggio);
                    for(Corso c : corsiList){
                        if(c.getNome_Corso().equals(messaggio.getCorso()))
                            messaggeList.add(messaggio);
                    }
                }
                MessageListAdapter adapter = new MessageListAdapter(MessageBoardActivity.this, messaggeList);
                lvMessaggi.setAdapter(adapter);
                setTvInfo("nascondi");
                setTvInfo("no_message");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        setTvInfo("no_message");

    }

    public void setTvInfo(String opt){
        switch (opt){
            case "non_iscritto":
                if(corsiList.isEmpty())
                    tvInfo.setText("Non sei iscritto a nessun corso");
                else
                    tvInfo.setText("CARICAMENTO...");
                tvInfo.setVisibility(View.VISIBLE);
                break;
            case "carica":
                tvInfo.setText("CARICAMENTO...");
                tvInfo.setVisibility(View.VISIBLE);
                break;
            case "no_message":
                if(tvInfo.getText().equals("CARICAMENTO...") && lvMessaggi.getCount()==0){
                    tvInfo.setText("Nessun messaggio");
                    tvInfo.setVisibility(View.VISIBLE);
                }
                break;
            case "nascondi":
                if(tvInfo.getText().equals("CARICAMENTO...") || lvMessaggi.getCount()!=0)
                    tvInfo.setVisibility(View.GONE);
            default:
                break;
        }
    }
}
