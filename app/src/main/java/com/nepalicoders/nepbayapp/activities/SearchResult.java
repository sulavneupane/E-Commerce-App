package com.nepalicoders.nepbayapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.adapters.EndlessScrollListener;
import com.nepalicoders.nepbayapp.adapters.GridViewProductsAdapter;
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

public class SearchResult extends AppCompatActivity {
    String searchQuery;
    TextView noResultMessage;
    TextView noInternetMessage;

    LinearLayout progressView;

    CircularProgressView pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        noResultMessage = (TextView) findViewById(R.id.noResultMessage);
        noInternetMessage = (TextView) findViewById(R.id.noInternetMessage);

        progressView = (LinearLayout) findViewById(R.id.loadingMore);
        pd = (CircularProgressView) findViewById(R.id.loading);

        searchQuery = getIntent().getExtras().getString("searchQuery");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(Html.fromHtml("Search Result: " + searchQuery));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
        map.put("search_value", searchQuery); //map.put can be use as many time as required
        map.put("start_limit", "0"); //map.put can be use as many time as required
        map.put("limit", "20");

        BackgroundThread thread = new BackgroundThread(ApiUrl.SEARCH_PRODUCT_URL, map, Connection.GET);
        thread.setBackgroundListener(this, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                pd.setVisibility(View.GONE);
                noInternetMessage.setVisibility(View.VISIBLE);

            }

            @Override
            public void onResponse(Response response, String result) throws IOException {
                if (response.isSuccessful()) {
                    pd.setVisibility(View.GONE);

                    final List<Product> products = JsonParser.parseSearchResult(result);
                    if (products.size() > 0) {
                        final GridViewProductsAdapter adapter = new GridViewProductsAdapter(SearchResult.this, products);
                        StaggeredGridView gridview = (StaggeredGridView) findViewById(R.id.two_products_grid_view);
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
                                    map.put("search_value", searchQuery); //map.put can be use as many time as required
                                    map.put("start_limit", String.valueOf(totalItemCount)); //map.put can be use as many time as required
                                    map.put("limit", "20");

                                    BackgroundThread thread = new BackgroundThread(ApiUrl.SEARCH_PRODUCT_URL, map, Connection.GET);

                                    thread.setBackgroundListener(SearchResult.this, new Callback() {
                                        @Override
                                        public void onFailure(Request request, IOException e) {
                                            progressView.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onResponse(Response response, String result) throws IOException {
                                            List<Product> products = JsonParser.parseSearchResult(result);
                                            adapter.updateProducts(products);
                                            progressView.setVisibility(View.GONE);
                                        }
                                    });

                                    thread.execute();
                                    loading = true;
                                }
                            }
                        });
                    } else {
                        noResultMessage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        thread.execute();
    }

}
