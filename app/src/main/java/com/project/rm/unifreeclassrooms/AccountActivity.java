package com.project.rm.unifreeclassrooms;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Marco on 12/05/2017.
 */

public class AccountActivity extends AppCompatActivity{
    ListView lvCorsiSeguiti;
    ListView lvCorsiTotali;

    private List<String> corsiSeguitiList;
    private List<String> corsiTotaliList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);
        //inseriamo la gestione del pulsante UP per tornare indietro
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        corsiSeguitiList=new ArrayList<>();
        corsiTotaliList =new ArrayList<>();
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

        final Corsi_DBHandler db = new Corsi_DBHandler(this);
        List<Corso> allCorsi = db.getAllCorsi();

        for(int i=0; i<allCorsi.size(); i++)
            corsiSeguitiList.add(allCorsi.get(i).getNome_Corso());

//GESTIONE CORSI SEGUITI
        //setto la ListView
        lvCorsiSeguiti =(ListView)findViewById(R.id.lv_corsi_seguiti);
        lvCorsiSeguiti.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapterSeguiti = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, corsiSeguitiList);
        lvCorsiSeguiti.setAdapter(adapterSeguiti);
        ListUtils.setDynamicHeight(lvCorsiSeguiti);
//DELETE CORSO
        //Bottone per eliminare corso e visualizzare riepilogo
        Button deleteCorsoBtn=(Button)findViewById(R.id.bt_delete_corso);
        deleteCorsoBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                SparseBooleanArray sparseBooleanArray = lvCorsiSeguiti.getCheckedItemPositions();
                //Controllo che abbia selezionato almeno un corso
                if(sparseBooleanArray.size()==0){
                    Toast.makeText(getApplicationContext(),"Seleziona un corso da cancellare",Toast.LENGTH_SHORT).show();
                    return;
                }
                for(int i=0;i<lvCorsiSeguiti.getCount();i++){
                    if(sparseBooleanArray.get(i)){
                        db.deleteCorso(lvCorsiSeguiti.getItemAtPosition(i).toString());
                    }
                }

//-----------REFRESH DELLA LISTVIEW-----------
                refreshListView();
            }});

//GESTIONE CORSI TOTALI
        //Gestione ListView
        corsiTotaliList = Arrays.asList(getResources().getStringArray(R.array.nuovi_corsi));
        lvCorsiTotali =(ListView)findViewById(R.id.lv_nuovi_corsi);
        ArrayAdapter<String> adapterNuovi = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, corsiTotaliList);
        lvCorsiTotali.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvCorsiTotali.setAdapter(adapterNuovi);
        ListUtils.setDynamicHeight(lvCorsiTotali);
//FOLLOW CORSO
        Button followCorsoBtn=(Button)findViewById(R.id.bt_segui_corso);
        followCorsoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selected="";
                SparseBooleanArray sparseBooleanArray = lvCorsiTotali.getCheckedItemPositions();
                if(sparseBooleanArray.size()==0){
                    Toast.makeText(getApplicationContext(),"Seleziona un corso",Toast.LENGTH_SHORT).show();
                    return;
                }
                for(int i = 0; i < lvCorsiTotali.getCount(); i++){
                    if(sparseBooleanArray.get(i)) {
                        selected+= lvCorsiTotali.getItemAtPosition(i).toString() + ",";
                        lvCorsiTotali.setItemChecked(i,false);
                    }
                }
                //aggiorno il db
                addToDBCorso(selected);

//-----------REFRESH DELLA LISTVIEW-----------
                refreshListView();
            }
        });
    }//end onStart()

    //Metodo per aggiornare il db con i nuovi corsi seguiti
    public void addToDBCorso(String corsiSelezionati){
        final Corsi_DBHandler db = new Corsi_DBHandler(this);
        List<Corso> corsi = db.getAllCorsi();
        String[] selectedCorsi = corsiSelezionati.split(",");
        if (selectedCorsi.length>0){
            for(int i=0;i<selectedCorsi.length;i++){
                String[] info = selectedCorsi[i].split(":");
                boolean corsoFound=false;
                Corso corso = new Corso(info[0],info[1]);
                for(Corso c : corsi){
                    if(c.getNumero_Corso().equals(corso.getNumero_Corso()))
                        corsoFound=true;
                }
                if(!corsoFound){
                    db.addCorso(corso);
                }
            }
        }
    }

    //Metodo per aggiornare le ListView
    public void refreshListView(){
        final Corsi_DBHandler db = new Corsi_DBHandler(AccountActivity.this);
        List<Corso> allCorsi = db.getAllCorsi();
        corsiSeguitiList.clear();
        for(int i=0; i<allCorsi.size(); i++)
            corsiSeguitiList.add(allCorsi.get(i).getNome_Corso());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AccountActivity.this, android.R.layout.simple_list_item_multiple_choice, corsiSeguitiList);
        lvCorsiSeguiti.setAdapter(adapter);
        ListUtils.setDynamicHeight(lvCorsiSeguiti);
    }


//CLASSE UTILS LISTVIEW per settare l'altezza dinamicamente
    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight()+60;
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            if(height<=650)
                params.height = height + ((mListView.getDividerHeight()) * (mListAdapter.getCount()));
            else
                params.height = 650;
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}