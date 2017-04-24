package com.example.shivr.e_commerce;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by shivr on 21/04/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList){
        this.productList = productList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price, desc;
        public ImageView imageView;
        public Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.productName);
            price = (TextView) itemView.findViewById(R.id.productPrice);
            desc = (TextView) itemView.findViewById(R.id.productDescSmall);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewProduct);
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_listing, parent, false);
        final Context context = parent.getContext();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent( context , ProductDetail.class));
            }
        });
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
