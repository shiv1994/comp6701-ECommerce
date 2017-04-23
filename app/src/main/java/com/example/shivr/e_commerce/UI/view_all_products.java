package com.example.shivr.e_commerce.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shivr.e_commerce.Product;
import com.example.shivr.e_commerce.ProductAdapter;
import com.example.shivr.e_commerce.R;

import java.util.ArrayList;
import java.util.List;


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
    private LayoutManagerType layoutManagerType;
    private RecyclerView.LayoutManager layoutManager;

    public view_all_products() {
        // Required empty public constructor
    }

    public static Fragment getInstance(){
        return new view_all_products();
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        productList = new ArrayList<Product>();
        productList.add(new Product("Apple MacBook", 678.99, "Fast Laptop"));
        productList.add(new Product("Apple MacBook PRO", 1678.99, "Fastest Laptop"));
        mAdapter = new ProductAdapter(productList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_all_products, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerAllProds);
        setRecyclerViewLayoutManager();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
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
}
