package gal.sli.digalnet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class DataBaseHelper extends SQLiteOpenHelper {


    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/gal.sli.digalnet/databases/";

    private static final String DB_NAME = "galnet.db"; //en assets

    private static final int DATABASE_VERSION = 2; //1=3.0.28, 2=3.0.29

    private SQLiteDatabase myDataBase;

    private final Context myContext;


    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;


    }


    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase_ori() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            Log.v("Os meus logs", "Galnet existe!!!");
// By calling this method here onUpgrade will be called on a
// writeable database, but only if the version number has been
// bumped
            this.getWritableDatabase();
        } else {
            Log.v("Os meus logs", "Galnet non existe!!!");
        }
        dbExist = checkDataBase();
        if (!dbExist) {
// By calling this method and empty database will be created into
// the default system path of your application so we are gonna be
// able to overwrite that database with our database.
            Log.v("Os meus logs", "Creando estrutura do Galnet na app...");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
            Log.v("Os meus logs", "Datos de Galnet copiados desde assets...");
        }
    }


    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase_test() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            Log.d(TAG, "db exists");
// By calling this method here onUpgrade will be called on a writeable database,
// but only if the version number has been bumped
            this.getWritableDatabase();
        }

        dbExist = checkDataBase();

        if (!dbExist) {
//By calling this method and empty database will be created into the default system path
//of your application so we are gonna be able to overwrite that database with our database.

            this.getReadableDatabase();

            try {
                copyDataBase();

            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }


    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            if (checkDB.getVersion() != DATABASE_VERSION) {
                Log.v("Os meus logs", "Versións diferentes de Galnet!!!");
                String myPath = DB_PATH + DB_NAME;
                checkDB.close();
                SQLiteDatabase.deleteDatabase(new File(myPath));
                checkDB.close();
                return false;
            } else {
                checkDB.close();
                return true;
            }

        } else {
            return false;
        }

    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v("Os meus logs", "Hai que actualizar Galnet!!!");

    }


    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.


    public ArrayList<ArrayList<String>> getOneItem(String palabra, String lingua) {


        String table = "`wei_" + lingua + "-30_variant`, `wei_eng-30_synset`";
        String[] columns = {"word", "`wei_eng-30_synset`.ids", "`wei_eng-30_synset`.cat", "`wei_eng-30_synset`.gloss"};

        String selection;

        if (lingua.equals("por")) {
            selection = "word =? COLLATE NOCASE and  `wei_por-30_variant`.ids = `wei_eng-30_synset`.ids";
        } else if (lingua.equals("glg")) {
            selection = "word =? COLLATE NOCASE and  `wei_glg-30_variant`.ids = `wei_eng-30_synset`.ids";
        } else if (lingua.equals("spa")) {
            selection = "word =? COLLATE NOCASE and  `wei_spa-30_variant`.ids = `wei_eng-30_synset`.ids";
        } else if (lingua.equals("eus")) {
            selection = "word =? COLLATE NOCASE and  `wei_eus-30_variant`.ids = `wei_eng-30_synset`.ids";
        } else if (lingua.equals("cat")) {
            selection = "word =? COLLATE NOCASE and  `wei_cat-30_variant`.ids = `wei_eng-30_synset`.ids";
        } else if (lingua.equals("eng")) {
            selection = "word =? COLLATE NOCASE and  `wei_eng-30_variant`.ids = `wei_eng-30_synset`.ids";
        } else if (lingua.equals("ita")) {
            selection = "word =? COLLATE NOCASE and  `wei_ita-30_variant`.ids = `wei_eng-30_synset`.ids";
        } else if (lingua.equals("lat")) {
            selection = "word =? COLLATE NOCASE and  `wei_lat-30_variant`.ids = `wei_eng-30_synset`.ids";
        } else if (lingua.equals("deu")) {
            selection = "word =? COLLATE NOCASE and  `wei_deu-30_variant`.ids = `wei_eng-30_synset`.ids";
        } else if (lingua.equals("qcn")) {
            selection = "word =? and  `wei_qcn-30_variant`.ids = `wei_eng-30_synset`.ids";
        } else if (lingua.equals("zho")) {
            selection = "word =? and  `wei_zho-30_variant`.ids = `wei_eng-30_synset`.ids";
        } else {
            selection = "word =? COLLATE NOCASE and  `wei_glg-30_variant`.ids = `wei_eng-30_synset`.ids";
        }

        String replacedStr = palabra.trim();
        replacedStr = replacedStr.replaceAll("[\\s]", "_");
        String[] selectionArgs = {replacedStr};

        String groupBy = null;
        String having = null;
        String orderBy = "cat DESC";
        String limit = "1000";

        Cursor cursor = myDataBase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        if (cursor == null || cursor.getCount() == 0) {

            return (null);

        }

        ArrayList<String> lemas = new ArrayList<>();
        ArrayList<String> defs = new ArrayList<>();
        ArrayList<String> trads_glg = new ArrayList<>();
        ArrayList<String> trads_por = new ArrayList<>();
        ArrayList<String> trads_cat = new ArrayList<>();
        ArrayList<String> trads_eus = new ArrayList<>();
        ArrayList<String> trads_spa = new ArrayList<>();
        ArrayList<String> trads_deu = new ArrayList<>();
        ArrayList<String> trads_lat = new ArrayList<>();
        ArrayList<String> trads_ita = new ArrayList<>();
        ArrayList<String> trads_zho = new ArrayList<>();
        ArrayList<String> trads_qcn = new ArrayList<>();


        ArrayList<String> acepcion = new ArrayList<>();


        ArrayList<ArrayList<String>> entrada =
                new ArrayList<ArrayList<String>>(2);


        String lema;
        String def;
        String trad_eng_def;

        String trad_glg = "";
        String trad_por = "";
        String trad_cat = "";
        String trad_eus = "";
        String trad_spa = "";
        String trad_deu = "";
        String trad_lat = "";
        String trad_ita = "";
        String trad_zho = "";
        String trad_qcn = "";

        String synset;
        String cat;
        String glosa;
        String lema_galnet;


        int n = 0;

        if (cursor.moveToFirst()) {
            do {
                n++;
                lema_galnet = cursor.getString(cursor.getColumnIndex("word"));
                synset = cursor.getString(cursor.getColumnIndex("`wei_eng-30_synset`.ids"));
                cat = cursor.getString(cursor.getColumnIndex("`wei_eng-30_synset`.cat"));
                glosa = cursor.getString(cursor.getColumnIndex("`wei_eng-30_synset`.gloss"));

//EN

                String table_eng = "`wei_eng-30_variant`";
                String[] columns_eng = {"word"};
                String selection_eng = "ids =?";
                String[] selectionArgs_eng = {synset};
                String groupBy_eng = null;
                String having_eng = null;
                String orderBy_eng = "word";
                String limit_eng = "1000";


                Cursor cursor_eng = myDataBase.query(table_eng, columns_eng, selection_eng, selectionArgs_eng, groupBy_eng, having_eng, orderBy_eng, limit_eng);

                String word_eng;
                String words_eng = "";


                if (cursor_eng.moveToFirst()) {
                    do {
                        word_eng = cursor_eng.getString(cursor.getColumnIndex("`wei_eng-30_variant`.word"));
                        words_eng = words_eng + " " + word_eng;
                    } while (cursor_eng.moveToNext());

                }
                words_eng = words_eng.trim();
                words_eng = words_eng.replaceAll(" ", ", ");
                words_eng = words_eng.replaceAll("_", " ");

                if (!words_eng.equals("")) {


//GL

                    String table_glg = "`wei_glg-30_variant`";
                    String[] columns_glg = {"word", "mark"};
                    String selection_glg = "ids =?";
                    String[] selectionArgs_glg = {synset};
                    String groupBy_glg = null;
                    String having_glg = null;
                    String orderBy_glg = "word";
                    String limit_glg = "1000";


                    Cursor cursor_glg = myDataBase.query(table_glg, columns_glg, selection_glg, selectionArgs_glg, groupBy_glg, having_glg, orderBy_glg, limit_glg);

                    String word_glg = "";
                    String words_glg = "";
                    String mark_gl = "";
                    String word_mark_gl = "";


                    if (cursor_glg.moveToFirst()) {
                        do {
                            word_glg = cursor_glg.getString(cursor.getColumnIndex("`wei_glg-30_variant`.word"));
                            mark_gl = cursor_glg.getString(1);

                            if (mark_gl.equals("5"))
                                word_mark_gl = "<img src=\"d.png\"/>" + word_glg;
                            else if (mark_gl.equals("4"))
                                word_mark_gl = "<img src=\"c.png\"/>" + word_glg;
                            else if (mark_gl.equals("3"))
                                word_mark_gl = "<img src=\"t.png\"/>" + word_glg;
                            else if (mark_gl.equals("2"))
                                word_mark_gl = "<img src=\"s.png\"/>" + word_glg;
                            else if (mark_gl.equals("1"))
                                word_mark_gl = "<img src=\"f.png\"/>" + word_glg;
                            else if (mark_gl.equals("0"))
                                word_mark_gl = "<img src=\"v.png\"/>" + word_glg;
                            else
                                word_mark_gl = word_glg;

                            words_glg = words_glg + " " + word_mark_gl;

                        } while (cursor_glg.moveToNext());

                    }


                    words_glg = words_glg.trim();
                    words_glg = words_glg.replaceAll(" ", ", ");
                    words_glg = words_glg.replaceAll("_", " ");
                    words_glg = words_glg.replaceAll(">", "> ");


//PT

                    String table_por = "`wei_por-30_variant`";
                    String[] columns_por = {"word"};
                    String selection_por = "ids =?";
                    String[] selectionArgs_por = {synset};
                    String groupBy_por = null;
                    String having_por = null;
                    String orderBy_por = "word";
                    String limit_por = "1000";


                    Cursor cursor_por = myDataBase.query(table_por, columns_por, selection_por, selectionArgs_por, groupBy_por, having_por, orderBy_por, limit_por);

                    String word_por;
                    String words_por = "";


                    if (cursor_por.moveToFirst()) {
                        do {
                            word_por = cursor_por.getString(cursor.getColumnIndex("`wei_por-30_variant`.word"));
                            words_por = words_por + " " + word_por;
                        } while (cursor_por.moveToNext());

                    }

                    words_por = words_por.trim();
                    words_por = words_por.replaceAll(" ", ", ");
                    words_por = words_por.replaceAll("_", " ");

//CAT

                    String table_cat = "`wei_cat-30_variant`";
                    String[] columns_cat = {"word"};
                    String selection_cat = "ids =?";
                    String[] selectionArgs_cat = {synset};
                    String groupBy_cat = null;
                    String having_cat = null;
                    String orderBy_cat = "word";
                    String limit_cat = "1000";


                    Cursor cursor_cat = myDataBase.query(table_cat, columns_cat, selection_cat, selectionArgs_cat, groupBy_cat, having_cat, orderBy_cat, limit_cat);

                    String word_cat;
                    String words_cat = "";


                    if (cursor_cat.moveToFirst()) {
                        do {
                            word_cat = cursor_cat.getString(cursor.getColumnIndex("`wei_cat-30_variant`.word"));
                            words_cat = words_cat + " " + word_cat;
                        } while (cursor_cat.moveToNext());

                    }

                    words_cat = words_cat.trim();
                    words_cat = words_cat.replaceAll(" ", ", ");
                    words_cat = words_cat.replaceAll("_", " ");

//EUS

                    String table_eus = "`wei_eus-30_variant`";
                    String[] columns_eus = {"word"};
                    String selection_eus = "ids =?";
                    String[] selectionArgs_eus = {synset};
                    String groupBy_eus = null;
                    String having_eus = null;
                    String orderBy_eus = "word";
                    String limit_eus = "1000";


                    Cursor cursor_eus = myDataBase.query(table_eus, columns_eus, selection_eus, selectionArgs_eus, groupBy_eus, having_eus, orderBy_eus, limit_eus);

                    String word_eus;
                    String words_eus = "";


                    if (cursor_eus.moveToFirst()) {
                        do {
                            word_eus = cursor_eus.getString(cursor.getColumnIndex("`wei_eus-30_variant`.word"));
                            words_eus = words_eus + " " + word_eus;
                        } while (cursor_eus.moveToNext());

                    }

                    words_eus = words_eus.trim();
                    words_eus = words_eus.replaceAll(" ", ", ");
                    words_eus = words_eus.replaceAll("_", " ");


//ES

                    String table_spa = "`wei_spa-30_variant`";
                    String[] columns_spa = {"word"};
                    String selection_spa = "ids =?";
                    String[] selectionArgs_spa = {synset};
                    String groupBy_spa = null;
                    String having_spa = null;
                    String orderBy_spa = "word";
                    String limit_spa = "1000";


                    Cursor cursor_spa = myDataBase.query(table_spa, columns_spa, selection_spa, selectionArgs_spa, groupBy_spa, having_spa, orderBy_spa, limit_spa);

                    String word_spa;
                    String words_spa = "";


                    if (cursor_spa.moveToFirst()) {
                        do {
                            word_spa = cursor_spa.getString(cursor.getColumnIndex("`wei_spa-30_variant`.word"));
                            words_spa = words_spa + " " + word_spa;
                        } while (cursor_spa.moveToNext());

                    }

                    words_spa = words_spa.trim();
                    words_spa = words_spa.replaceAll(" ", ", ");
                    words_spa = words_spa.replaceAll("_", " ");


//DE

                    String table_deu = "`wei_deu-30_variant`";
                    String[] columns_deu = {"word"};
                    String selection_deu = "ids =?";
                    String[] selectionArgs_deu = {synset};
                    String groupBy_deu = null;
                    String having_deu = null;
                    String orderBy_deu = "word";
                    String limit_deu = "1000";


                    Cursor cursor_deu = myDataBase.query(table_deu, columns_deu, selection_deu, selectionArgs_deu, groupBy_deu, having_deu, orderBy_deu, limit_deu);

                    String word_deu;
                    String words_deu = "";


                    if (cursor_deu.moveToFirst()) {
                        do {
                            word_deu = cursor_deu.getString(cursor.getColumnIndex("`wei_deu-30_variant`.word"));
                            words_deu = words_deu + " " + word_deu;
                        } while (cursor_deu.moveToNext());

                    }

                    words_deu = words_deu.trim();
                    words_deu = words_deu.replaceAll(" ", ", ");
                    words_deu = words_deu.replaceAll("_", " ");

//LA

                    String table_lat = "`wei_lat-30_variant`";
                    String[] columns_lat = {"word"};
                    String selection_lat = "ids =?";
                    String[] selectionArgs_lat = {synset};
                    String groupBy_lat = null;
                    String having_lat = null;
                    String orderBy_lat = "word";
                    String limit_lat = "1000";


                    Cursor cursor_lat = myDataBase.query(table_lat, columns_lat, selection_lat, selectionArgs_lat, groupBy_lat, having_lat, orderBy_lat, limit_lat);

                    String word_lat;
                    String words_lat = "";


                    if (cursor_lat.moveToFirst()) {
                        do {
                            word_lat = cursor_lat.getString(cursor.getColumnIndex("`wei_lat-30_variant`.word"));
                            words_lat = words_lat + " " + word_lat;
                        } while (cursor_lat.moveToNext());

                    }

                    words_lat = words_lat.trim();
                    words_lat = words_lat.replaceAll(" ", ", ");
                    words_lat = words_lat.replaceAll("_", " ");

//IT

                    String table_ita = "`wei_ita-30_variant`";
                    String[] columns_ita = {"word"};
                    String selection_ita = "ids =?";
                    String[] selectionArgs_ita = {synset};
                    String groupBy_ita = null;
                    String having_ita = null;
                    String orderBy_ita = "word";
                    String limit_ita = "1000";


                    Cursor cursor_ita = myDataBase.query(table_ita, columns_ita, selection_ita, selectionArgs_ita, groupBy_ita, having_ita, orderBy_ita, limit_ita);

                    String word_ita;
                    String words_ita = "";


                    if (cursor_ita.moveToFirst()) {
                        do {
                            word_ita = cursor_ita.getString(cursor.getColumnIndex("`wei_ita-30_variant`.word"));
                            words_ita = words_ita + " " + word_ita;
                        } while (cursor_ita.moveToNext());

                    }

                    words_ita = words_ita.trim();
                    words_ita = words_ita.replaceAll(" ", ", ");
                    words_ita = words_ita.replaceAll("_", " ");

//ZH

                    String table_zho = "`wei_zho-30_variant`";
                    String[] columns_zho = {"word"};
                    String selection_zho = "ids =?";
                    String[] selectionArgs_zho = {synset};
                    String groupBy_zho = null;
                    String having_zho = null;
                    String orderBy_zho = "word";
                    String limit_zho = "1000";


                    Cursor cursor_zho = myDataBase.query(table_zho, columns_zho, selection_zho, selectionArgs_zho, groupBy_zho, having_zho, orderBy_zho, limit_zho);

                    String word_zho;
                    String words_zho = "";


                    if (cursor_zho.moveToFirst()) {
                        do {
                            word_zho = cursor_zho.getString(cursor.getColumnIndex("`wei_zho-30_variant`.word"));
                            words_zho = words_zho + " " + word_zho;
                        } while (cursor_zho.moveToNext());

                    }

                    words_zho = words_zho.trim();
                    words_zho = words_zho.replaceAll(" ", ", ");
                    words_zho = words_zho.replaceAll("_", " ");

//qcn

                    String table_qcn = "`wei_qcn-30_variant`";
                    String[] columns_qcn = {"word"};
                    String selection_qcn = "ids =?";
                    String[] selectionArgs_qcn = {synset};
                    String groupBy_qcn = null;
                    String having_qcn = null;
                    String orderBy_qcn = "word";
                    String limit_qcn = "1000";


                    Cursor cursor_qcn = myDataBase.query(table_qcn, columns_qcn, selection_qcn, selectionArgs_qcn, groupBy_qcn, having_qcn, orderBy_qcn, limit_qcn);

                    String word_qcn;
                    String words_qcn = "";


                    if (cursor_qcn.moveToFirst()) {
                        do {
                            word_qcn = cursor_qcn.getString(cursor.getColumnIndex("`wei_qcn-30_variant`.word"));
                            words_qcn = words_qcn + " " + word_qcn;
                        } while (cursor_qcn.moveToNext());

                    }

                    words_qcn = words_qcn.trim();
                    words_qcn = words_qcn.replaceAll(" ", ", ");
                    words_qcn = words_qcn.replaceAll("_", " ");


//COMUN A TODAS AS LINGUAS

                    lema = n + ". " + lema_galnet + " (" + cat + ".) ";
                    def = "<b> Def.</b>: " + glosa;
                    trad_eng_def = "<img src=\"eng.png\"/> " + words_eng + "<br/><br/><b> Def.</b>: " + glosa;

                    if (!words_glg.trim().isEmpty())
                        trad_glg = "<img src=\"glg.png\"/> " + words_glg;
                    if (!words_por.trim().isEmpty())
                        trad_por = "<img src=\"por.png\"/> " + words_por;
                    if (!words_cat.trim().isEmpty())
                        trad_cat = "<img src=\"cat.png\"/> " + words_cat;
                    if (!words_eus.trim().isEmpty())
                        trad_eus = "<img src=\"eus.png\"/> " + words_eus;
                    if (!words_spa.trim().isEmpty())
                        trad_spa = "<img src=\"spa.png\"/> " + words_spa;
                    if (!words_deu.trim().isEmpty())
                        trad_deu = "<img src=\"deu.png\"/> " + words_deu;
                    if (!words_lat.trim().isEmpty())
                        trad_lat = "<img src=\"lat.png\"/> " + words_lat;
                    if (!words_ita.trim().isEmpty())
                        trad_ita = "<img src=\"ita.png\"/> " + words_ita;
                    if (!words_zho.trim().isEmpty())
                        trad_zho = "<img src=\"zho.png\"/> " + words_zho;
                    if (!words_qcn.trim().isEmpty())
                        trad_qcn = "<img src=\"qcn.png\"/> " + words_qcn;

                    lemas.add(lema);
                    defs.add(def);

                    if (!words_glg.trim().isEmpty())
                        trads_glg.add(trad_glg);
                    if (!words_por.trim().isEmpty())
                        trads_por.add(trad_por);
                    if (!words_cat.trim().isEmpty())
                        trads_cat.add(trad_cat);
                    if (!words_eus.trim().isEmpty())
                        trads_eus.add(trad_eus);
                    if (!words_spa.trim().isEmpty())
                        trads_spa.add(trad_spa);
                    if (!words_deu.trim().isEmpty())
                        trads_deu.add(trad_deu);
                    if (!words_lat.trim().isEmpty())
                        trads_lat.add(trad_lat);
                    if (!words_ita.trim().isEmpty())
                        trads_ita.add(trad_ita);
                    if (!words_zho.trim().isEmpty())
                        trads_zho.add(trad_zho);
                    if (!words_qcn.trim().isEmpty())
                        trads_qcn.add(trad_qcn);

                    if (!acepcion.isEmpty())
                        acepcion.clear();
                    if (!words_eng.trim().isEmpty())
                        acepcion.add(trad_eng_def);
                    if (!words_glg.trim().isEmpty())
                        acepcion.add(trad_glg);
                    if (!words_por.trim().isEmpty())
                        acepcion.add(trad_por);
                    if (!words_cat.trim().isEmpty())
                        acepcion.add(trad_cat);
                    if (!words_eus.trim().isEmpty())
                        acepcion.add(trad_eus);
                    if (!words_spa.trim().isEmpty())
                        acepcion.add(trad_spa);
                    if (!words_deu.trim().isEmpty())
                        acepcion.add(trad_deu);
                    if (!words_lat.trim().isEmpty())
                        acepcion.add(trad_lat);
                    if (!words_ita.trim().isEmpty())
                        acepcion.add(trad_ita);
                    if (!words_zho.trim().isEmpty())
                        acepcion.add(trad_zho);
                    if (!words_qcn.trim().isEmpty())
                        acepcion.add(trad_qcn);

                    ArrayList<String> guardiola = (ArrayList<String>) acepcion.clone();
                    entrada.add(n - 1, guardiola);

                }

            } while (cursor.moveToNext());

            entrada.add(lemas);


        }

        return entrada;

    }

}


