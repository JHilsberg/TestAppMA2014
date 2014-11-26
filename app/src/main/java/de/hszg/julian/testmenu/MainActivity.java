package de.hszg.julian.testmenu;

import android.app.Activity;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.EditText;


import android.util.Log;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, StartFragment.newInstance(position + 1))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, SaveFileFragment.newInstance(position + 1))
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, SaveDatabaseFragment.newInstance(position + 1))
                        .commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, LocationFragment.newInstance(position + 1))
                        .commit();
                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    public void saveDataPreferences(View v){
        EditText editText = (EditText) findViewById(R.id.editText);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("myvalue", editText.getText().toString());

        editor.commit();
    }

    public void saveDataInFile(View v){
        EditText textForSaveFile = (EditText) findViewById(R.id.textForSaveFile);

        String textToSave = textForSaveFile.getText().toString();

        try {
            FileOutputStream fos = openFileOutput("save_file", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(textToSave);
            bw.flush();
            fos.close();
            Log.i("Info", "Message was saved");
        } catch (IOException e) {
            Log.e("IO Error", e.getStackTrace().toString());
        }
    }

    public void saveDataInDatabase(View v) {
        EditText textFirstName = (EditText) findViewById(R.id.editFirstname);
        EditText textName = (EditText) findViewById(R.id.editName);
        TextView textViewNames = (TextView) findViewById(R.id.textViewNames);

        String nameToSave = textName.getText().toString();
        String firstNameToSave = textFirstName.getText().toString();
        StringBuilder completePrint = new StringBuilder();

        NameDatabaseHelper dbHelper = new NameDatabaseHelper(getApplicationContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_FIRSTNAME, firstNameToSave);
        values.put(dbHelper.COLUMN_NAME, nameToSave);

        database.insert(dbHelper.NAME_TABLE_NAME, null, values);

        String[] allColumns = { dbHelper.COLUMN_ID,
                dbHelper.COLUMN_FIRSTNAME, dbHelper.COLUMN_NAME};

        Cursor cursor = database.query(dbHelper.NAME_TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            completePrint.append(cursor.getString(
                    cursor.getColumnIndexOrThrow(dbHelper.COLUMN_ID))+ ", "
                    + cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_FIRSTNAME))+ ", "
                    + cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_NAME)) +" \n");
            cursor.moveToNext();
        }
        textViewNames.setText(completePrint.toString());
        cursor.close();
    }

    public void dropNames(View v) {
        NameDatabaseHelper dbHelper = new NameDatabaseHelper(getApplicationContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(database, dbHelper.DATABASE_VERSION, dbHelper.DATABASE_VERSION + 1);
    }
}
