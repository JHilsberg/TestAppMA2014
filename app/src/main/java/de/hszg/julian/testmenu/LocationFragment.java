package de.hszg.julian.testmenu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Julian on 23.11.2014.
 */
public class LocationFragment extends Fragment{

    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView textViewLocation;

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Context context = getActivity();
        textViewLocation = (TextView) getActivity().findViewById(R.id.textViewLocation);

        if (savedInstanceState != null) {
            textViewLocation.setText(savedInstanceState.getString("location_text"));
        }

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Log.i("location", "location changed");
                try {
                    Geocoder gc = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    for (int i = 0; i < addresses.size(); i++){
                        textViewLocation.setText("Country: " + addresses.get(i).getCountryName() + "\n" +
                        "City: " + addresses.get(i).getLocality()+ "\n" + "Postal code: " + addresses.get(i).getPostalCode());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(context,
                        "Provider disabled: " + provider + "Check your config", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(context,
                        "Provider enabled: " + provider, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("location", "location status changed" + status);
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15*1000, 0,
                locationListener);
    }

    public static LocationFragment newInstance(int sectionNumber) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public LocationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("location_text", "Please wait. Location loading");
    }
}
