package com.naruto.mekvahandelivery.customer_pickup;

import android.net.Uri;

public class CustomerPickupData {

    private int index;
    private Uri photoUri;

    CustomerPickupData(int index, Uri photoUri) {
        this.index = index;
        this.photoUri = photoUri;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }
}
