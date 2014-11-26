package de.hszg.julian.testmenu;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Julian on 20.11.2014.
 */
public class SaveDatabaseFragment extends Fragment{
    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public void onResume(){
        super.onResume();

        TextView textViewNames = (TextView) getActivity().findViewById(R.id.textViewNames);
        StringBuilder completePrint = new StringBuilder();

        NameDatabaseHelper dbHelper = new NameDatabaseHelper(getActivity());
        SQLiteDatabase nameDb = dbHelper.getReadableDatabase();

        String[] allColumns = { dbHelper.COLUMN_ID,
                dbHelper.COLUMN_FIRSTNAME, dbHelper.COLUMN_NAME};

        Cursor cursor = nameDb.query(dbHelper.NAME_TABLE_NAME,
                allColumns, null, null, null, null, null);

        textViewNames.setText("First Name, Name \n");

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static SaveDatabaseFragment newInstance(int sectionNumber) {
        SaveDatabaseFragment fragment = new SaveDatabaseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SaveDatabaseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_save_database, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
