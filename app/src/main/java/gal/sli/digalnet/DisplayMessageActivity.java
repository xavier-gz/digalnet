package gal.sli.digalnet;

import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gal.sli.digalnet.adapter.ExpandableListViewAdapter;




public class DisplayMessageActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;

    private ExpandableListViewAdapter expandableListViewAdapter;

    private List<String> listDataGroup;

    private HashMap<String, List<String>> listDataChild;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String message = extras.getString("EXTRA_PALABRA");
        String lingua = extras.getString("EXTRA_LINGUA");

        DataBaseHelper myDbHelper = new DataBaseHelper(this);


        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }

        String palabra = message;
        String ling;

        switch (lingua) {
            case "1":
                ling = "glg";
                break;
            case "2":
                ling = "por";
                break;
            case "3":
                ling = "cat";
                break;
            case "4":
                ling = "eus";
                break;
            case "5":
                ling = "spa";
                break;
            case "6":
                ling = "eng";
                break;
            case "7":
                ling = "deu";
                break;
            case "8":
                ling = "lat";
                break;
            case "9":
                ling = "ita";
                break;
            case "10":
                ling = "zho";
                break;
            case "11":
                ling = "qcn";
                break;
            default:
                ling = "glg";
                break;
        }


        ArrayList<ArrayList<String>> resultado = myDbHelper.getOneItem(palabra, ling);

        if (resultado == null) {

            Intent refresh = new Intent(this, MainActivity.class);

            overridePendingTransition( 0, 0);
            startActivity(refresh);
            overridePendingTransition( 0, 0);

            Toast toast = Toast.makeText(this.getBaseContext(), getString(R.string.recuncar), Toast.LENGTH_SHORT);
            LinearLayout layout = (LinearLayout) toast.getView();
            if (layout.getChildCount() > 0) {
                TextView tv = (TextView) layout.getChildAt(0);
                tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            }
            toast.show();

        } else {

            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            myToolbar.setTitle(getResources().getString(R.string.app_name));
            myToolbar.setNavigationIcon(R.drawable.search);

            // initializing the views
            initViews();

            // initializing the objects
            initObjects();

            // preparing list data
            initListData(resultado);

        }

    }
        /**
         * method to initialize the views
         */
        private void initViews () {

            expandableListView = findViewById(R.id.expandableListView);

        }

        /**
         * method to initialize the objects
         */
        private void initObjects () {

            // initializing the list of groups
            listDataGroup = new ArrayList<>();

            // initializing the list of child
            listDataChild = new HashMap<>();

            // initializing the adapter object
            expandableListViewAdapter = new ExpandableListViewAdapter(this, listDataGroup, listDataChild);

            // setting list adapter
            expandableListView.setAdapter(expandableListViewAdapter);


        }


        /*
         * Preparing the list data
         */
        private void initListData (ArrayList < ArrayList < String > > resultado){


            // Adding group data

            int f = resultado.size() - 1;

            for (int j = 0; j < resultado.get(f).size(); j++) {
                listDataGroup.add(resultado.get(f).get(j));
            }
            int z = 0;
            for (int i = 0; i < f; i++) {
                listDataChild.put(listDataGroup.get(z), resultado.get(i));
                z++;
            }


            //expandir primeiro grupo
            //só a primera vez

            expandableListView.collapseGroup(0);

            //Expandir só un grupo cada vez
            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                // Keep track of previous expanded parent
                int previousGroup = -1;

                @Override
                public void onGroupExpand(int groupPosition) {
                    // Collapse previous parent if expanded.
                    if ((previousGroup != -1) && (groupPosition != previousGroup)) {
                        expandableListView.collapseGroup(previousGroup);
                    }
                    previousGroup = groupPosition;
                }
            });

            expandableListView.expandGroup(0);


            // notify the adapter
            expandableListViewAdapter.notifyDataSetChanged();

        }
        //Imprimir lista expandible


        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu2, menu);
            return true;
        }


        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle presses on the action bar items
            switch (item.getItemId()) {
                case R.id.share:
                    shareTextUrl();
                    return true;

                case R.id.help:
                    //popup window code here

                    // set the message to display
                    String axuda = getString(R.string.exposicion).concat(getString(R.string.abreviaturas));

                    // Linkify the message
                    final SpannableString u = new SpannableString(axuda);
                    Linkify.addLinks(u, Linkify.ALL);

                    final AlertDialog f = new AlertDialog.Builder(this)
                            .setPositiveButton(android.R.string.ok, null)
                            .setTitle(getString(R.string.help))
                            .setMessage(u)
                            .create();

                    f.show();

                    // Make the textview clickable. Must be called after show()
                    ((TextView) f.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                    return true;

                case R.id.info:
                    //popup window code here

                    // set the message to display
                    String mensaxe = getString(R.string.contacto).concat(getString(R.string.datos));

                    // Linkify the message
                    final SpannableString t = new SpannableString(mensaxe);
                    Linkify.addLinks(t, Linkify.ALL);

                    final AlertDialog e = new AlertDialog.Builder(this)
                            .setPositiveButton(android.R.string.ok, null)
                            .setTitle(getString(R.string.app_name))
                            .setMessage(t)
                            .create();

                    e.show();

                    // Make the textview clickable. Must be called after show()
                    ((TextView) e.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                    return true;

                case R.id.iraweb:
                    Uri uri = Uri.parse("http://sli.uvigo.gal/digalnet/");
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    return true;


                default:
                    return super.onOptionsItemSelected(item);
            }
        }


        private void shareTextUrl () {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            }

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            String message = extras.getString("EXTRA_PALABRA");
            String lingua = extras.getString("EXTRA_LINGUA");

            String idl;

            switch (lingua) {
                case "1":
                    idl = "GL";
                    break;
                case "2":
                    idl = "PT";
                    break;
                case "3":
                    idl = "CA";
                    break;
                case "4":
                    idl = "EU";
                    break;
                case "5":
                    idl = "ES";
                    break;
                case "6":
                    idl = "EN";
                    break;
                case "7":
                    idl = "DE";
                    break;
                case "8":
                    idl = "LA";
                    break;
                case "9":
                    idl = "IT";
                    break;
                case "10":
                    idl = "ZH_S";
                    break;
                case "11":
                    idl = "ZH_T";
                    break;
                default:
                    idl = "GL";
                    break;
            }


            String url = "http://sli.uvigo.gal/digalnet/index.php?lang=gl&lingua=" + idl + "&pescuda=" + message;
            String cabeceira = "\"" + message + "\" (" + idl + ") " + "- " + getString(R.string.app_name);
            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, cabeceira);
            share.putExtra(Intent.EXTRA_TEXT, url);

            startActivity(Intent.createChooser(share, getString(R.string.share)));
        }


}