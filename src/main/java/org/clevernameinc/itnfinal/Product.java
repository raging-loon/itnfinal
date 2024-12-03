package org.clevernameinc.itnfinal;

public class Product {
    private final int mId;

    private String mDesc;
    private String mBrand;
    private double mRating;
    private double mWeight;
    private double mPrice;


    public Product(int id) {
        mId = id;
    }

    public String getDesc() {
        return mDesc;
    }

    public String getBrand() {
        return mBrand;
    }

    public double getRating() {
        return mRating;
    }

    public double getWeight() {
        return mWeight;
    }

    public double getPrice() {
        return mPrice;
    }

    public int getId() {
        return mId;
    }

    public void setDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public void setBrand(String mBrand) {
        this.mBrand = mBrand;
    }

    public void setRating(double mRating) {
        this.mRating = mRating;
    }

    public void setWeight(double mWeight) {
        this.mWeight = mWeight;
    }

    public void setPrice(double mPrice) {
        this.mPrice = mPrice;
    }

}
