package gal.sli.digalnet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner spinnerLanguages = findViewById(R.id.language_spinner);
        ArrayAdapter<String> spinnerLanguagesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.language_array));
        spinnerLanguages.setAdapter(spinnerLanguagesArrayAdapter);


        final EditText editText = findViewById(R.id.editText);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String language = preferences.getString("lingua", "");
        if(!language.equalsIgnoreCase(""))
        {
            int spinnerPosition = spinnerLanguagesArrayAdapter.getPosition(language);
            spinnerLanguages.setSelection(spinnerPosition);

        }

        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("lingua",spinnerLanguages.getSelectedItem().toString());
                editor.apply();


                String lingua = spinnerLanguages.getSelectedItem().toString();
                String intro = getResources().getString(R.string.edit_message);

                String local = Locale.getDefault().getLanguage();
                if (local.equals("en"))
                    editText.setHint(intro+" "+lingua);
                else
                    editText.setHint(intro+" "+lingua.toLowerCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageView img = findViewById(R.id.imageView3);
        img.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://sli.uvigo.gal"));
                startActivity(intent);
            }
        });

        ImageView img2 = findViewById(R.id.imageView4);
        img2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://sli.uvigo.gal"));
                startActivity(intent);
            }
        });

        Toolbar myToolbar = findViewById(R.id.portada_toolbar);
        setSupportActionBar(myToolbar);


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.iraweb:
                Uri uri = Uri.parse( "http://sli.uvigo.gal/digalnet/" );
                startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
                return true;

            case R.id.info:
                // set the message to display
                String mensaxe = getString(R.string.contacto).concat(getString(R.string.datos));

                // Linkify the message
                final SpannableString t = new SpannableString(mensaxe);
                Linkify.addLinks(t, Linkify.ALL);

                final AlertDialog e = new AlertDialog.Builder(this)
                        .setPositiveButton(android.R.string.ok, null)
                        .setTitle(getString(R.string.app_name))
                        .setMessage( t )
                        .create();

                e.show();

                // Make the textview clickable. Must be called after show()
                ((TextView)e.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when the user taps the Send button
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = findViewById(R.id.editText);
        String message = editText.getText().toString();
        Spinner spinner = findViewById(R.id.language_spinner);

        int Hold = spinner.getSelectedItemPosition() + 1 ;
        String langId = String.valueOf(Hold) ;

        Bundle extras = new Bundle();
        extras.putString("EXTRA_PALABRA",message);
        extras.putString("EXTRA_LINGUA",langId);
        intent.putExtras(extras);

        startActivity(intent);
    }



}
