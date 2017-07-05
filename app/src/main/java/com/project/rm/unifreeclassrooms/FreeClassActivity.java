package com.project.rm.unifreeclassrooms;

/**
 * Created by Marco on 13/05/2017.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FreeClassActivity extends AppCompatActivity {

    private TableLayout stk;
    private String oraInizio;
    private String oraFine;
    private String edList;
    private String[] edScelti;
    private ArrayList<String> noteList;
    private ArrayList<String> auleOccupateList;
    private ArrayList<String> auleLibereList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_class);
        //inserisce la freccia di ritorno alla home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //LEGGO I PARAMETRI PASSATI DA CLASSFRAGMENT
        oraInizio = getIntent().getStringExtra("Time_1");
        oraFine = getIntent().getStringExtra("Time_2");
        edList = getIntent().getStringExtra("Ed_list");
        //Edificio scelto
        if(edList.equals(""))
            edScelti = new String[]{"**********"};
        else
            edScelti = edList.split(",");

        auleOccupateList=new ArrayList<String>();
        auleLibereList=new ArrayList<String>();
        noteList = new ArrayList<String>();

        //Setto testo TexView e Intestazione tabella
        TextView tvShowTime=(TextView)findViewById(R.id.tvShowTimeRange);
        tvShowTime.setText("Aule libere dalle "+oraInizio+" alle "+oraFine);
        //Setto tabella
        stk = (TableLayout) findViewById(R.id.table_main);

        String todayUrl = "http://www03.unibg.it//orari//orario_giornaliero.php?db=IN&data=oggi&orderby=ora";
        String otherDayUrl = "http://www03.unibg.it//orari//orario_giornaliero.php?db=IN&data=30/05/2017&orderby=ora";
        //Aziono il parsing della pagina html con gli orari
        ( new FreeClassActivity.ParseURL() ).execute(new String[]{todayUrl});

    }

    //    Gestione back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){
        super.onStart();
    }


    //Creo intestazione tabella
    public void createTableHeader(){
        //riga della tabella
        TableRow tHeader = new TableRow(this);
        //prima colonna
        TextView tv0 = new TextView(this);
        tv0.setText(" AULA ");
        tv0.setTextAppearance(this, R.style.CustomTitle);
        tv0.setGravity(Gravity.CENTER);
        tHeader.addView(tv0);
        //seconda colonna
        TextView tv1 = new TextView(this);
        tv1.setText(" ORARIO ");
        tv1.setTextAppearance(this, R.style.CustomTitle);
        tv1.setGravity(Gravity.CENTER);
        tHeader.addView(tv1);
        //terza colonna
        TextView tv2 = new TextView(this);
        tv2.setText(" STATO ");
        tv2.setTextAppearance(this, R.style.CustomTitle);
        tv2.setGravity(Gravity.CENTER);
        tHeader.addView(tv2);
        //aggiungo la riga
        stk.addView(tHeader);
    }

//---------------METODI DI PROVA: CARICO TUTTE LE RIGHE DELLA TABELLA CHE RISPETTANO I REQUISITI
    //Carico le aule filtrate in base agli edScelti nella riga
    public void loadAndWriteAuleFiltrateList(ArrayList<String> al, ArrayList<String> ol, ArrayList<String> nl, char ed, TableLayout tbl){
        addToAuleOccupateList(al,ol, nl);
        for(int k=0;k<al.size();k++){
            boolean foundBusy=false;
            if(al.get(k).trim().charAt(0)==ed){
                for(int i=0;i<auleOccupateList.size();i++){
                    if(al.get(k).equals(auleOccupateList.get(i)))
                        foundBusy=true;
                }
                if(!foundBusy){
                    //ricorda di creare la riga ogni volta!
                    TableRow tbRow = new TableRow(this);
                    //Aggiungo l'aula
                    TextView tAula = new TextView(this);
                    tAula.setText(al.get(k));
                    tAula.setTextAppearance(this, R.style.CustomTextView);
                    tAula.setGravity(Gravity.CENTER);
                    tbRow.addView(tAula);
                    //Aggiungo l'orario
                    TextView tOrario = new TextView(this);
                    tOrario.setText(ol.get(k));
                    tOrario.setTextAppearance(this, R.style.CustomTextView);
                    tOrario.setGravity(Gravity.CENTER);
                    tbRow.addView(tOrario);
                    //Aggiungo lo stato
                    TextView tStato = new TextView(this);
                    tStato.setText("OCCUPATA");
                    tStato.setTextAppearance(this, R.style.CustomTextView);
                    tStato.setGravity(Gravity.CENTER);
                    tbRow.addView(tStato);
                    //aggiungo la riga finita
                    tbl.addView(tbRow);
                }
            }
        }
    }
    //Carico tutte le righe in tabella
    public void loadTableRow_PROVA(ArrayList<String> al, ArrayList<String> ol, ArrayList<String> nl, ArrayList<String> all, TableLayout tbl, String[] edifici){
        for (int i = 0; i < edifici.length; i++) {
            switch(edifici[i].charAt(9)){
                case 'A':
                    loadAndWriteAuleFiltrateList(al, ol, nl, 'A', tbl);
                    break;
                case 'B':
                    loadAndWriteAuleFiltrateList(al, ol, nl, 'B', tbl);
                    break;
                case 'C':
                    loadAndWriteAuleFiltrateList(al, ol, nl, 'C', tbl);
                    break;
                default:
                    loadAndWriteAuleFiltrateList(al, ol, nl, 'A', tbl);
                    loadAndWriteAuleFiltrateList(al, ol, nl, 'B', tbl);
                    loadAndWriteAuleFiltrateList(al, ol, nl, 'C', tbl);
                    break;
            }
        }
    }

    //Test methods
    public void stampaTutto(ArrayList<String> al, ArrayList<String> ol, ArrayList<String> nl, TableLayout tbl){
        for(int i=0;i<al.size();i++){
            //ricorda di creare la riga ogni volta!
            TableRow tbRow = new TableRow(this);
            //Aggiungo l'aula
            TextView tAula = new TextView(this);
            tAula.setText(al.get(i));
            tAula.setTextAppearance(this, R.style.CustomTextView);
            tAula.setGravity(Gravity.CENTER);
            tbRow.addView(tAula);
            //Aggiungo l'orario
            TextView tOrario = new TextView(this);
            tOrario.setText(ol.get(i));
            tOrario.setTextAppearance(this, R.style.CustomTextView);
            tOrario.setGravity(Gravity.CENTER);
            tbRow.addView(tOrario);

            String isSosp;
            if(nl.get(i).charAt(0)=='S')
                isSosp="true";
            else
                isSosp="";

            //Aggiungo lo stato
            TextView tNote = new TextView(this);
            tNote.setText(isSosp);
            tNote.setTextAppearance(this, R.style.CustomTextView);
            tNote.setGravity(Gravity.CENTER);
            tbRow.addView(tNote);
            //aggiungo la riga finita
            tbl.addView(tbRow);
        }
    }

// METODI UTILIZZATI NELLA VERSIONE FINALE
//-----------UTILS--------------
    //Controllo orario inserito con quello di ogni aula e mando in output se è occupata o no
    public boolean isBusy(ArrayList<String> ol, int index){
        SimpleDateFormat parser = new SimpleDateFormat("HH.mm");
        Date beginLimit;
        Date endLimit;
        Date beginClass;
        Date endClass;
        boolean busy=false;
        String orari[]=ol.get(index).split("-");
        try{
            beginLimit=parser.parse(oraInizio);
            endLimit=parser.parse(oraFine);
            beginClass=parser.parse(orari[0]);
            endClass=parser.parse(orari[1]);
            //    beginClass = beginLimit    ||    endClass = endLimit
            if(beginClass.equals(beginLimit) || endClass.equals(endLimit)){
                //                     beginClass >= beginLimit                     &&                   beginClass <= endLimit                      ||                     endClass >= beginLimit                   &&                   endClass <= endLimit
                if(((beginClass.after(beginLimit) || beginClass.equals(beginLimit)) && (beginClass.before(endLimit) || beginClass.equals(endLimit))) || ((endClass.after(beginLimit) || endClass.equals(beginLimit)) && (endClass.before(endLimit) || endClass.equals(endLimit)))){
                    busy=true;
                }else{
                    busy=false;
                }
            }else{
                //    beginClass > beginLimit    &&    beginClass < endLimit     ||     endClass > beginLimit   &&    endClass < endLimit
                if((beginClass.after(beginLimit) && beginClass.before(endLimit)) || (endClass.after(beginLimit) && endClass.before(endLimit))){
                    busy=true;
                }else
                    busy=false;
            }
        }catch (ParseException e){
            Toast.makeText(getApplicationContext(),"PARSE EXCEPTION",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return busy;
    }
    //Aggiungo le aule occupate a auleOccupateList
    public void addToAuleOccupateList(ArrayList<String> al, ArrayList<String> ol, ArrayList<String> nl){
        for(int i=0;i<al.size();i++){
            if(isBusy(ol,i) && nl.get(i).charAt(0)!='S')
                auleOccupateList.add(al.get(i));
        }
    }

//---------STAMPO SOLO LA LISTA DELLE AULE LIBERE IN QUEL RANGE ORARIO-----------
    //Aggiungo le aule a auleLibereList SOLO SE non sono già presenti
    public void addToAuleLibereList(ArrayList<String> al, int index){
        boolean found=false;
        if(auleLibereList.size()==0)
            auleLibereList.add(al.get(index));

        for(int i=0;i<auleLibereList.size();i++){
            if(al.get(index).equals(auleLibereList.get(i)))
                found=true;
        }
        if(!found)
            auleLibereList.add(al.get(index));
    }
    //Cerco le aule libere e le aggiungo alla lista tramite il metodo addToAuleLibereList
    public void findAuleLibere(ArrayList<String> al, ArrayList<String> ol, ArrayList<String> nl, ArrayList<String> all){
        addToAuleOccupateList(al, ol, nl);
        for(int k=0;k<al.size();k++){
            boolean foundBusy=false;
            for(int i=0;i<auleOccupateList.size();i++){
                if(al.get(k).equals(auleOccupateList.get(i)))
                    foundBusy=true;
            }
            if(!foundBusy){
                addToAuleLibereList(al,k);
            }
        }
    }
    //Scrivo effettivamente le aule libere trovate in tabella nella riga, in base all'edificio scelto
    public void writeAuleLibere(ArrayList<String> al, ArrayList<String> ol, ArrayList<String> nl, ArrayList<String> all, TableLayout tbl, char ed){
        findAuleLibere(al, ol, nl, all);
        for(int i=0;i<all.size();i++){
            if(all.get(i).charAt(0)==ed){
                TableRow tbRow = new TableRow(this);

                TextView tvLeft=new TextView(this);
                tbRow.addView(tvLeft);

                TextView tAula = new TextView(this);
                tAula.setText(all.get(i));
                tAula.setTextAppearance(this, R.style.CustomTitle);
                tAula.setGravity(Gravity.CENTER);
                tbRow.addView(tAula);

                tbl.addView(tbRow);
            }
        }
    }
    //Carico le righe scritte prima in tabella
    public void loadTableRow(ArrayList<String> al, ArrayList<String> ol, ArrayList<String> nl, ArrayList<String> all, TableLayout tbl, String[] edifici){
        for (int i = 0; i < edifici.length; i++) {
            switch(edifici[i].charAt(9)){
                case 'A':
                    writeAuleLibere(al,ol,nl,all,tbl,'A');
                    break;
                case 'B':
                    writeAuleLibere(al,ol,nl,all,tbl,'B');
                    break;
                case 'C':
                    writeAuleLibere(al,ol,nl,all,tbl,'C');
                    break;
                default:
                    writeAuleLibere(al,ol,nl,all,tbl,'A');
                    writeAuleLibere(al,ol,nl,all,tbl,'B');
                    writeAuleLibere(al,ol,nl,all,tbl,'C');
                    break;
            }
        }
    }



//------------------CLASSE PARSING SULLA PAGINA HTML--------------------------
    public class ParseURL extends AsyncTask<String, Void, ArrayList<String>> {
        ArrayList<String> auleList = new ArrayList<String>();
        ArrayList<String> orariList = new ArrayList<String>();

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            try {
                Log.d("JSwa", "Connecting to ["+strings[0]+"]");
                Document doc  = Jsoup.connect(strings[0]).get();
                Log.d("JSwa", "Connected to ["+strings[0]+"]");

                Element table = doc.select("table").get(0);
                Elements rowElems = table.select("tr");
                for(int i=1; i<rowElems.size();i++){
                    Element row = rowElems.get(i);
                    Elements cols = row.select("td");
                    String aule[]=cols.get(3).text().split("\\(");
                    //Prendo solo le aule degli ed A,B o C
                    if((aule[0].charAt(0)=='A') || (aule[0].charAt(0)=='B') || (aule[0].charAt(0)=='C')){
                        auleList.add(aule[0]);
                        String orari[]=cols.get(3).text().split("\\)");
                        orariList.add(orari[1]);
                        noteList.add(cols.get(4).text().trim());
                    }
                }
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return auleList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        //Attraverso l'attributo s viene passato il return del metodo doInBackground, quindi auleList
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            //TexView di caricamento
            TextView t=(TextView)findViewById(R.id.tvLoading);
            t.setVisibility(View.GONE);

            loadTableRow(s, orariList, noteList, auleLibereList ,stk, edScelti);

//METODI DI PROVA
//            createTableHeader();
//            loadTableRow_PROVA(s,orariList,noteList,auleLibereList,stk,edScelti);
            //stampaTutto(s,orariList,noteList,stk);
        }

  }//end ParseURL
}//end FreeClassActivity
