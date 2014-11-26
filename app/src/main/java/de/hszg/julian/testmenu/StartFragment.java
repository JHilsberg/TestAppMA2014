package de.hszg.julian.testmenu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Julian on 20.11.2014.
 */
public class StartFragment extends Fragment{

    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        final EditText editText = (EditText) getActivity().findViewById(R.id.editText);
        editText.setText(preferences.getString("myvalue", ""));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static StartFragment newInstance(int sectionNumber) {
        StartFragment fragment = new StartFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public StartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
