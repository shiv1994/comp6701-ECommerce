package com.example.shivr.e_commerce;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shivr.e_commerce.UI.view_product_detail;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.net.URI;
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
        public SimpleDraweeView imageDraweeView;
        public ImageView imageView;

        public MyViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.productName);
            price = (TextView) itemView.findViewById(R.id.productPrice);
            desc = (TextView) itemView.findViewById(R.id.productDescSmall);
//            imageDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.imageViewProduct);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewProduct);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = ((MainActivity)itemView.getContext()).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, view_product_detail.getInstance(productList.get(getAdapterPosition())))
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
        Uri imageUri = Uri.parse(product.getImgRef());

//        holder.imageDraweeView.setImageURI(imageUri);
        holder.imageView.setImageURI(imageUri);
        holder.title.setText("Name: "+product.getName());
        holder.desc.setText("Description: "+product.getDescription());
        holder.price.setText("Price: $"+String.valueOf(product.getPrice()));

    }


    public int getItemCount() {
        return productList.size();
    }

    }

