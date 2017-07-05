package com.project.rm.unifreeclassrooms;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FreeClassFragment extends DialogFragment {
    private String BOT;
    private String EOT;


    ListView myList;

    Activity context;

    //CREARE UN FRAGMENT
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=getActivity();
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_free_class, container, false);
    }
//VARIABILI GLOBALI PER SALVARE IL VALORE SELEZIONATO NELLO SPINNER
    private void tempo_inizio(String t){
        BOT=t;
    }
    private void tempo_Fine(String t){
        EOT=t;
    }

    //private void EdificioAll(int all) {All=all;}

    public void onStart(){
        super.onStart();
        //Creiamo un listener sullo spinner che funzioni prima che venga premuto il bottone
        //spinner di inzio
        Spinner sp=(Spinner)context.findViewById(R.id.spinner_begin);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                String selected = (String)adapter.getItemAtPosition(pos);
                tempo_inizio(selected.toString());
            }
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
        //spinner di fine
        Spinner sp2=(Spinner)context.findViewById(R.id.spinner_end);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                String selected = (String)adapter.getItemAtPosition(pos);
                tempo_Fine(selected.toString());
            }
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        //Lista CheckBox edifici

        String[] Edifici = {
                "Edificio A",
                "Edificio B",
                "Edificio C",
        };

        myList = (ListView)context.findViewById(R.id.list_Ed);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, Edifici);
        myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        myList.setAdapter(adapter);



        //Listener del button
        Button bt=(Button)context.findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //create an Intent object
                Intent intent=new Intent(context, FreeClassActivity.class);

                //All'evento di onclick sul buttone controllo chi nella lista ha il check
                //e viene messo in una stringa con , come separatore
                String selected="";
                int cntChoice = myList.getCount();

                SparseBooleanArray sparseBooleanArray = myList.getCheckedItemPositions();
                int k=0;
                for(int i = 0; i < cntChoice; i++){

                    if(sparseBooleanArray.get(i)) {

                        selected+= myList.getItemAtPosition(i).toString() + ",";

                    }
                }
                //add data to the Intent object
                int inizio=Integer.parseInt(BOT.replaceAll("[\\D]",""));
                int fine=Integer.parseInt(EOT.replaceAll("[\\D]",""));

                if(inizio<fine){
                    intent.putExtra("Time_1", BOT);
                    intent.putExtra("Time_2", EOT);
                    intent.putExtra("Ed_list", selected);
                    startActivity(intent);
                }
               else
                    Toast.makeText(getActivity(),"Orario di Fine inferiore ad orario d'inizio!", Toast.LENGTH_LONG).show();
            }

        });

    }//end onStart() method
}
