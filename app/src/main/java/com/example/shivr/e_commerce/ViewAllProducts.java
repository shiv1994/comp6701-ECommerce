package com.example.shivr.e_commerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;

import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import java.util.ArrayList;
import java.util.List;

public class ViewAllProducts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_products);

        productList = new ArrayList<Product>();
        productList.add(new Product("Apple MacBook", 678.99, "Fast Laptop"));
        productList.add(new Product("Apple MacBook PRO", 1678.99, "Fastest Laptop"));

        mAdapter = new ProductAdapter(productList);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerAllProds);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
