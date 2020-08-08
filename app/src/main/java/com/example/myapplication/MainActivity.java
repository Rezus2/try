package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBMatches mDBConnector;
    ListView mListView;
    myListAdapter myAdapter;

    int ADD_ACTIVITY = 0;
    int UPDATE_ACTIVITY = 1;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBConnector = new DBMatches(this);
        myAdapter=new myListAdapter(this, mDBConnector.selectAll());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
//                Intent i = new Intent(mContext, AddActivity.class);
                Intent i = new Intent(this, AddActivity.class); // весь контекст содержится уже внутри данной активности
                startActivityForResult(i, ADD_ACTIVITY);
               updateList();
                return true;
            case R.id.deleteAll:
                mDBConnector.deleteAll();
                updateList();
                return true;
            case R.id.exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
//                Intent i = new Intent(mContext, AddActivity.class);
                Intent i = new Intent(this, AddActivity.class); // соответсвенно
                Matches md = mDBConnector.select(info.id);
                i.putExtra("Matches", md);
                startActivityForResult(i, UPDATE_ACTIVITY);
//                updateList(); та же ситуация
                return true;
            case R.id.delete:
                mDBConnector.delete(info.id);
                updateList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void updateList() {
        myAdapter.setArrayMyData(mDBConnector.selectAll());
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == ADD_ACTIVITY) {
            if (data != null && data.hasExtra("Matches")) {
                Matches md = (Matches) data.getExtras().getSerializable("Matches");
                if (md != null) {
                    if (requestCode == UPDATE_ACTIVITY)
                        mDBConnector.update(md);
                    else
                        mDBConnector.insert(md.getName(), md.getSurname(), md.getMiddlename(), md.getGroup());
                    updateList();
                }
            }
        }
    }

    class myListAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;
        private ArrayList<Matches> arrayMyMatches;

        public myListAdapter(Context ctx, ArrayList<Matches> arr) {
            mLayoutInflater = LayoutInflater.from(ctx);
            setArrayMyData(arr);
        }

        public ArrayList<Matches> getArrayMyData() {
            return arrayMyMatches;
        }

        public void setArrayMyData(ArrayList<Matches> arrayMyData) {
            this.arrayMyMatches = arrayMyData;
        }

        public int getCount() {
            return arrayMyMatches.size();
        }

        public Object getItem(int position) {

            return position;
        }

        public long getItemId(int position) {
            Matches md = arrayMyMatches.get(position);
            if (md != null) {
                return md.getId();
            }
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null)
                convertView = mLayoutInflater.inflate(R.layout.item, null);

            TextView vSurName = (TextView) convertView.findViewById(R.id.surname);
            TextView vName = (TextView) convertView.findViewById(R.id.name);
            TextView vMiddleName = (TextView) convertView.findViewById(R.id.middlename);
            TextView vGroup = (TextView) convertView.findViewById(R.id.group);


            Matches md = arrayMyMatches.get(position);
            vName.setText(md.getName());
            vSurName.setText(md.getSurname());
            vMiddleName.setText(md.getMiddlename());
            vGroup.setText(md.getGroup());

            return convertView;
        }
    }
}
