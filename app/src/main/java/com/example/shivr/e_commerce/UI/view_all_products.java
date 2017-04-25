package com.example.shivr.e_commerce.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shivr.e_commerce.Product;
import com.example.shivr.e_commerce.ProductAdapter;
import com.example.shivr.e_commerce.R;
import com.example.shivr.e_commerce.SSLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link view_all_products.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class view_all_products extends Fragment {

    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private List<Product> productList;
    private List<Product> productListFiltered;
    private LayoutManagerType layoutManagerType;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;

    public view_all_products() {
        // Required empty public constructor
    }

    public static Fragment getInstance(){
        return new view_all_products();
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        productList = new ArrayList<Product>();
        HttpRequestTask httpRequestTask = new HttpRequestTask(getActivity());

//        productList.add(new Product("Apple MacBook", 678.99, "Fast Laptop"));
//        productList.add(new Product("Apple MacBook PRO", 1678.99, "Fastest Laptop"));

        httpRequestTask.execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_all_products, container, false);

        searchView = (SearchView) rootView.findViewById(R.id.searchBarAllProds);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerAllProds);
        setRecyclerViewLayoutManager();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                productListFiltered = new ArrayList<Product>();
                query = query.toLowerCase();
                for(Product p: productList){
                    if(p.getName().toLowerCase().contains(query)){
                        productListFiltered.add(p);
                    }
                }
                mAdapter = new ProductAdapter(productListFiltered);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);
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
        void onFragmentInteraction(Uri uri);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, String> {

        private Context context;
        private ProgressDialog progressDialog;

        public HttpRequestTask(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "Notice", "Loading Products...", true);
        }

        protected String doInBackground(Void... voids){

            HttpsURLConnection urlConnection=null;
            String resp="";
            SSLHelper sslHelper = SSLHelper.getInstance(context);

            // pulls down product info
            try {
                // Tell the URLConnection to use a SocketFactory from our SSLContext
                URL url = new URL("https://67.205.172.180/wp-json/wc/v2/products");
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

                int x, id;
                String name;
                String price;
                String desc;
                String longDesc;
                Double rating;
                JSONArray imgArr;
                JSONObject imgObj;

                for(x=0;x<jsonArray.length();x++){
                    JSONObject temp = jsonArray.getJSONObject(x);
                    id = temp.getInt("id");
                    name = temp.getString("name");
                    price = temp.getString("price");
                    desc = temp.getString("short_description");
                    longDesc = temp.getString("description");
                    rating = temp.getDouble("average_rating");

                    imgArr = temp.getJSONArray("images");
                    imgObj = imgArr.getJSONObject(0);

                    productList.add(new Product(name, Double.parseDouble(price), desc, longDesc, imgObj.getString("src"), rating));
                }

                mAdapter = new ProductAdapter(productList);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
