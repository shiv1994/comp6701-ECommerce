package com.example.shivr.e_commerce.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shivr.e_commerce.R;
import com.example.shivr.e_commerce.Utils;

public class coupon_view extends Fragment {

    private OnFragmentInteractionListener mListener;
    private SharedPreferences sharedPreferences;

    private TextView textViewCouponCode, textViewCouponDesc, textViewCouponDiscount;


    public coupon_view() {
        // Required empty public constructor
    }

    public static coupon_view newInstance() {
        coupon_view fragment = new coupon_view();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get list of received coupons.
        sharedPreferences = Utils.getSharedPrefs(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_coupon_view, container, false);
        textViewCouponCode = (TextView) rootView.findViewById(R.id.textViewCoupon);
        textViewCouponDesc = (TextView) rootView.findViewById(R.id.textViewCouponDesc);
        textViewCouponDiscount = (TextView) rootView.findViewById(R.id.textViewCouponAmountOff);

        textViewCouponCode.setText(Utils.getSharedPrefString(sharedPreferences, Utils.couponCode));
        textViewCouponDesc.setText(Utils.getSharedPrefString(sharedPreferences, Utils.couponDesc));
        textViewCouponDiscount.setText((Utils.getSharedPrefString(sharedPreferences, Utils.couponAmountOff)));
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
}
