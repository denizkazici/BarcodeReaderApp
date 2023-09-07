package com.denizkazici.project;

import java.util.ArrayList;

public class productData {
    private static productData productData = null;

    private productData() {
    }

    public static productData getInstance() {

        if (productData == null)
        {
            productData = new productData();
        }
        return productData;
    }

    private static product myproduct = null ;
    public  product getItem( ) {
        return myproduct;
    }

    public void setItem(product data) {
        myproduct.productName = data.productName;
        myproduct.productCatagory = data.productCatagory;
        myproduct.description = data.description;
        myproduct.photo=data.photo;

    }


}

