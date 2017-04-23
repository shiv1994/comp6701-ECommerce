package com.example.shivr.e_commerce;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shivr.e_commerce.UI.view_product_detail;

import java.util.List;

/**
 * Created by shivr on 21/04/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView title, price, desc;
        public ImageView imageView;

        public MyViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.productName);
            price = (TextView) itemView.findViewById(R.id.productPrice);
            desc = (TextView) itemView.findViewById(R.id.productDescSmall);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewProduct);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Position", "" + getAdapterPosition());
                    FragmentManager fragmentManager = ((MainActivity)itemView.getContext()).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.content_frame, new view_product_detail())
                            .addToBackStack("tag")
                            .commit();
                }
            });
        }
    }

    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_listing, parent, false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(ProductAdapter.MyViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.title.setText(product.getName());
        holder.desc.setText(product.getDescription());
        holder.price.setText(String.valueOf(product.getPrice()));
        holder.imageView.setImageResource(R.drawable.laptop);
    }


    public int getItemCount() {
        return productList.size();
    }

    }

