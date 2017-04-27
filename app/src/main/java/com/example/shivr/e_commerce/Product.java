package com.example.shivr.e_commerce;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.Double2;
import android.widget.ImageView;

/**
 * Created by shivr on 21/04/2017.
 */

@SuppressLint("ParcelCreator")
public class Product implements Parcelable {
    private String name;
    private Double price;
    private String description;
    private String long_desc;
    private ImageView imageView;
    private String imgRef;
    private Double avg_rating;
    private Bitmap bitmap;


    public Product(String name, Double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(price);
        parcel.writeString(description);
        parcel.writeString(long_desc);
        parcel.writeString(imgRef);
        parcel.writeDouble(avg_rating);
        bitmap.writeToParcel(parcel,0);
    }

    public Product(String name, Double price, String description, String long_desc, String imgRef, Double avg_rating) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imgRef = imgRef;
        this.avg_rating = avg_rating;
        this.long_desc = long_desc;
    }

    public String getImgRef() {
        return imgRef;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Double getAvg_rating() {
        return avg_rating;
    }

    public String getLong_desc() {
        return long_desc;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
