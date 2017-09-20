package com.nepalicoders.nepbayapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.etsy.android.grid.StaggeredGridView;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.adapters.EndlessScrollListener;
import com.nepalicoders.nepbayapp.adapters.GridViewOffersAdapter;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.json.JsonParser;
import com.nepalicoders.nepbayapp.objects.Product;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sulav on 12/15/15.
 */
public class OfferProductsListingFragment extends Fragment {

    View view;
    CircularProgressView pd;

    LinearLayout progressView;

    public OfferProductsListingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category_products_listing, container, false);

        progressView = (LinearLayout) view.findViewById(R.id.loadingMore);

        pd = (CircularProgressView) view.findViewById(R.id.loading);

        YoYo.with(Techniques.FadeInRight).duration(700).playOn(view);

        Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
        map.put("start_limit", "0");
        map.put("limit", "20");

        BackgroundThread thread = new BackgroundThread(ApiUrl.OFFERS_DISPLAY_URL_TEMPORARY, map, Connection.GET);
        thread.setBackgroundListener(getActivity(), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                pd.setVisibility(View.GONE);

            }

            @Override
            public void onResponse(Response response, String result) throws IOException {
                if(response.isSuccessful()){
                    List<Product> products = JsonParser.parseOfferProducts(result);
                    final GridViewOffersAdapter adapter = new GridViewOffersAdapter(getActivity(), products);
                    StaggeredGridView gridview = (StaggeredGridView) view.findViewById(R.id.two_products_grid_view);
                    gridview.setAdapter(adapter);

                    gridview.setOnScrollListener(new EndlessScrollListener() {
                        private boolean loading = true;
                        private int previousTotal = 0;
                        private int currentPage = 0;
                        private int visibleThreshold = 5;

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

                            if (loading) {
                                if (totalItemCount > previousTotal) {
                                    loading = false;
                                    previousTotal = totalItemCount;
                                    currentPage++;
                                }
                            }
                            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                                progressView.setVisibility(View.VISIBLE);

                                Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
                                map.put("start_limit", String.valueOf(totalItemCount));
                                map.put("limit", "20");

                                BackgroundThread thread = new BackgroundThread(ApiUrl.OFFERS_DISPLAY_URL_TEMPORARY, map, Connection.GET);

                                thread.setBackgroundListener(getActivity(), new Callback() {
                                    @Override
                                    public void onFailure(Request request, IOException e) {
                                        progressView.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onResponse(Response response, String result) throws IOException {
                                        List<Product> products = JsonParser.parseOfferProducts(result);
                                        adapter.updateProducts(products);
                                        progressView.setVisibility(View.GONE);
                                    }
                                });

                                thread.execute();
                                loading = true;
                            }
                        }
                    });

                    pd.setVisibility(View.GONE);
                }
            }
        });
        thread.execute();

        return view;
    }

}
