package com.project.rm.unifreeclassrooms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the fragment initially
        FreeClassFragment fragment =new FreeClassFragment();
        FragmentManager fm = getSupportFragmentManager();
        getFragmentManager().popBackStack();
        fm.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("HOME").commit();

        toolbar  = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag("fragBack") != null) {

        }
        else {
            super.onBackPressed();
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            Fragment frag = getSupportFragmentManager().findFragmentByTag("fragBack");
            FragmentTransaction transac = getSupportFragmentManager().beginTransaction().remove(frag);
            transac.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //Dichiariamo un fragment generico
        Fragment newFragment = new Fragment();
        FragmentManager fm = getSupportFragmentManager();

        switch(item.getItemId()) {
            case R.id.nav_time_choice:
                newFragment = new FreeClassFragment();
                break;
            case R.id.nav_notice_board:
                newFragment = new FreeClassFragment();
                Intent notibeBoardIntent=new Intent(MainActivity.this, MessageBoardActivity.class);
                startActivity(notibeBoardIntent);
                break;
            case  R.id.nav_account:
                newFragment = new FreeClassFragment();
                Intent accountIntent=new Intent(MainActivity.this, AccountActivity.class);
                startActivity(accountIntent);
                break;
            case  R.id.nav_info:
                newFragment = new InfoFragment();
                break;
        }


        getFragmentManager().popBackStack();
        fm.beginTransaction().replace(R.id.fragment_container, newFragment).addToBackStack("fragBack").commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
