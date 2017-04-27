package com.example.shivr.e_commerce.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.shivr.e_commerce.GeoFenceLocationService;
import com.example.shivr.e_commerce.MainActivity;
import com.example.shivr.e_commerce.R;
import com.example.shivr.e_commerce.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;

public class settings extends Fragment {


    private OnFragmentInteractionListener mListener;
    private Switch locationSwitch;
    private SharedPreferences sharedPreferences;

    public settings() {
        // Required empty public constructor
    }


    public static settings newInstance() {
        settings fragment = new settings();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sharedPreferences = Utils.getSharedPrefs(this.getContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        locationSwitch = (Switch) rootView.findViewById(R.id.switch1);
        locationSwitch.setChecked(Utils.getSharedPrefsBoolean(sharedPreferences, Utils.locationOn));
        locationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locationSwitch.isChecked()){
                    Utils.insertSharedPrefs(Utils.locationOn, true, sharedPreferences);
                    Intent intent = new Intent(getContext(), GeoFenceLocationService.class);
                    getActivity().startService(intent);
                    Utils.makeShowSnackbar("Location Enabled.", rootView);
                }
                else{
                    Utils.insertSharedPrefs(Utils.geoFencesSet, false, sharedPreferences);
                    Utils.insertSharedPrefs(Utils.locationOn, false, sharedPreferences);
                    getActivity().stopService(new Intent(getContext(), GeoFenceLocationService.class));
                    Utils.makeShowSnackbar("Location Disabled.", rootView);
                }
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
