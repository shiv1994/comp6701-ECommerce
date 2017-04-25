package com.example.shivr.e_commerce.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.shivr.e_commerce.Product;
import com.example.shivr.e_commerce.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link view_product_detail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class view_product_detail extends Fragment {
    private Integer productID;
    private OnFragmentInteractionListener mListener;
    private TextView nameView;
    private SimpleDraweeView imgView;
    private RatingBar ratingBarView;
    private TextView priceView;
    private TextView descView;

    private Product product;

    public view_product_detail() {
        // Required empty public constructor
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_product_detail, container, false);

        nameView = (TextView)view.findViewById(R.id.textViewProdName);
        imgView = (SimpleDraweeView)view.findViewById(R.id.imageViewSingleProd);
        ratingBarView = (RatingBar)view.findViewById(R.id.ratingBar);
        priceView = (TextView)view.findViewById(R.id.textViewProdPrice);
        descView = (TextView)view.findViewById(R.id.textViewProdDesc);

        nameView.setText(product.getName());
        priceView.setText("Price: $"+String.valueOf(product.getPrice()));
        descView.setText(product.getLong_desc());
        Uri imageUri = Uri.parse(product.getImgRef());
        imgView.setImageURI(imageUri);
        ratingBarView.setRating((product.getAvg_rating().floatValue()));

        return view;
    }

    public static view_product_detail getInstance (Product product){
        view_product_detail product_detail = new  view_product_detail();
        product_detail.setProduct(product);
        return  product_detail;
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
