package com.example.shivr.e_commerce.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shivr.e_commerce.R;
import com.example.shivr.e_commerce.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class coupon_list extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String coupon;
    private TextView textViewCoupon;

    public coupon_list() {
        // Required empty public constructor
    }

    public static coupon_list newInstance() {
        coupon_list fragment = new coupon_list();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get list of received coupons.
        coupon = Utils.getSharedPrefString(Utils.getSharedPrefs(getContext()), Utils.coupon);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_coupon_list, container, false);
        textViewCoupon = (TextView) rootView.findViewById(R.id.textViewCoupon);
        textViewCoupon.setText("Coupon: "+coupon);
        return rootView;
    }

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void obtainCouponFromWooCommerce(){

    }
}
