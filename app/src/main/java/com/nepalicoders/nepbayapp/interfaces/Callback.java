package com.nepalicoders.nepbayapp.interfaces;

import com.nepalicoders.nepbayapp.adapters.CartAdapter;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by sabin on 12/4/15.
 */
public interface Callback {

    void onFailure(Request request, IOException e);
    void onResponse(Response response, String result) throws IOException;
}
