package de.hszg.julian.testmenu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Julian on 20.11.2014.
 */
public class SaveFileFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public void onResume(){
        super.onResume();

        EditText textForSaveFile = (EditText) getActivity().findViewById(R.id.textForSaveFile);

        FileInputStream fis = null;
        try {
            String thisLine = "test";

            InputStreamReader reader = new InputStreamReader(getActivity().openFileInput("save_file"));
            BufferedReader br = new BufferedReader(reader);

            while((thisLine = br.readLine()) != null){
                textForSaveFile.setText(thisLine);
            }
            br.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e2) {
                    Log.e("IO Problem", e2.getStackTrace().toString());
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static SaveFileFragment newInstance(int sectionNumber) {
        SaveFileFragment fragment = new SaveFileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SaveFileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_save_file, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
