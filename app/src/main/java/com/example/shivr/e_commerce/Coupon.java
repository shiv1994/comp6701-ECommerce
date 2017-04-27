package com.example.shivr.e_commerce;

/**
 * Created by mdls8 on 4/27/2017.
 */

public class Coupon {

    private String code;
    private Double amountOff;
    private String description;

    public Coupon(String code, Double amountOff, String description) {
        this.code = code;
        this.amountOff = amountOff;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getAmountOff() {
        return amountOff;
    }

    public void setAmountOff(Double amountOff) {
        this.amountOff = amountOff;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
