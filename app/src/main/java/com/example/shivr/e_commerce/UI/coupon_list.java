package com.example.shivr.e_commerce.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shivr.e_commerce.Coupon;
import com.example.shivr.e_commerce.R;
import com.example.shivr.e_commerce.SSLHelper;
import com.example.shivr.e_commerce.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

public class coupon_list extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String coupon;
    private TextView textViewCoupon;
    private List<Coupon> couponList;
    private Coupon selectedCoupon;

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

    private class CouponRequestTask extends AsyncTask<Void, Void, String> {

        private Context context;
        private ProgressDialog progressDialog;

        public CouponRequestTask(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "Notice", "Loading Coupons...", true);
        }

        protected String doInBackground(Void... voids){

            HttpsURLConnection urlConnection=null;
            String resp="";
            SSLHelper sslHelper = SSLHelper.getInstance(context);

            // pulls down coupon info
            try {
                // Tell the URLConnection to use a SocketFactory from our SSLContext
                URL url = new URL("https://67.205.172.180/wp-json/wc/v2/coupons");
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setSSLSocketFactory(sslHelper.getSSLContext().getSocketFactory());

                // credentials
                String user = "ck_970486c965566c58a881f7501f244bc77bebd732";
                String pass = "cs_070469e8a858dd957792d25fe51ec8234aedabbf";
                String auth = user + ":" + pass;
                byte [] encoded  = Base64.encode(auth.getBytes(), Base64.NO_WRAP);
                urlConnection.setRequestProperty("Authorization", "Basic " +
                        new String(encoded));

                urlConnection.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {

                        return true;
                    }
                });
                urlConnection.connect();

                if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    resp = result.toString();
                }
            }
            catch(SSLPeerUnverifiedException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }

            return resp;
        }

        protected void onPostExecute(String response){
            JSONArray jsonArray=null;
            try{
                jsonArray = new JSONArray(response);

                int x;
                String name;
                String id;
                String disc;
                String desc;

                for(x=0;x<jsonArray.length();x++){
                    JSONObject temp = jsonArray.getJSONObject(x);
                    id = temp.getString("code");
                    disc = temp.getString("amount");
                    desc = temp.getString("description");

                    couponList.add(new Coupon(Jsoup.parse(id).text(), Double.parseDouble(disc), Jsoup.parse(desc).text()));
                }
                getRandomCoupon();
                // use randomly selected coupon to populate view here
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private Coupon getRandomCoupon(){
        selectedCoupon=null;

        // get a randomly select coupon from the list of coupons
        Random random = new Random();
        selectedCoupon = couponList.get(random.nextInt(couponList.size()));

        return selectedCoupon;
    }
}
