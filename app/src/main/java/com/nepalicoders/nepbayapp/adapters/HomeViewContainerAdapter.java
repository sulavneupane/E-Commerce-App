package com.nepalicoders.nepbayapp.adapters;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.application.App;
import com.nepalicoders.nepbayapp.fragments.CategoryProductsListingFragment;
import com.nepalicoders.nepbayapp.fragments.HomeFragment;
import com.nepalicoders.nepbayapp.fragments.OfferProductsListingFragment;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.json.JsonParser;
import com.nepalicoders.nepbayapp.objects.HomeBanner;
import com.nepalicoders.nepbayapp.objects.Product;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kshitij on 12/10/15.
 */
public class HomeViewContainerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private HomeFragment homeFragment;
    Tracker mTracker;
    private Context context;

    List<Product> offerProducts;
    Map<Integer,ArrayList<Product>> allProducts = new HashMap<Integer,ArrayList<Product>>();

    //banner contains the banner retrieved from the server
    //containerHeader is the string array representing titles of Horizontal Scroll View
    //items array is created for representing the title of items in all horizontal view containers
    //prices array is created for representing the prices of items in all horizontal view containers
    private List<HomeBanner> banners;
    private String containerHeader[] ;
    private String categoryIds[];
    /*private String items[][] = {{"United Uncook Kadhai", "LG Refrigerator", "Havells Induction Cooker", "CG 42", "LG Microwave Oven", "LG Chest Freezer"},
                                {"Elysium Signature 2", "Verbatim Dog Tag USB ", "Micro USB Charging Data Cable", "Prolink Elegant Dual Speaker", "Prolink Funky Stereo Speaker"},
                                {"Blue Color Long Hooded Jumper", "White Woolen Sweater", "Leather Long Red Jacket", "Leather Black Long Jacket", "Army Combat Printed Jacket", "Woolen Long Over coat", "Crop Woolen Sweater"},
                                {"Red Jordan Hoodies", "Black Full Sweater", "Yellow/Grey Stripe Sweater", "Adidas Full Blue T-Shirt", "Adidas Full Black T-Shirt", "Leather Jacket HQ Black", "Black Addidas Track Jacket", "Red Wind Cheater"}};

    private String prices[][] = {{"Rs. 2509.20", "Rs. 57030.80", "Rs. 2999.00", "Rs. 78995.00", "Rs. 12410.80", "Rs. 51234.80"},
                                {"Rs. 23000.00", "Rs. 1235.00", "Rs. 560.00", "Rs. 1570.00", "Rs. 2240.00"},
                                {"Rs. 1679.20", "Rs. 1299.00", "Rs. 3839.20", "Rs. 3839.20", "Rs. 2719.20", "Rs. 3199.20", "Rs. 1499.00"},
                                {"Rs. 2799.20", "Rs. 1199.20", "Rs. 1679.20", "Rs. 1999.20", "Rs. 1999.20", "Rs. 3359.20", "Rs. 1599.20", "Rs. 2559.20"}};*/

    //View type constants for finding out whether the view is for images slider or horizontal view container
    private static final int VIEW_TYPE_HOME_BANNER = 0;
    private static final int VIEW_TYPE_OFFERS_CONTAINER = 1;
    private static final int VIEW_TYPE_PRODUCTS_CONTAINER = 2;

    //Constructor for receiving values, that are required in the adapter, from the Home Fragment
    public HomeViewContainerAdapter(Context context, List<HomeBanner> banners, String[] header, String[] Ids, HomeFragment fragment) {
        this.context = context;
        this.homeFragment = fragment;
        this.banners = banners;
        this.containerHeader = header;
        this.categoryIds = Ids;
        mTracker = ((App)fragment.getActivity().getApplication()).getDefaultTracker();
    }

    //Returns the view type i.e. whether the view type is images slider or horizontal scroll view container
    @Override
    public int getItemViewType(int position) {
        //Banners are at the beginning of the home fragment only so if the position is zero then return view type for images slider else for Horizontal scroll view container
        if (position == 0) {
            return VIEW_TYPE_HOME_BANNER;
        } else if(position==1){
            return VIEW_TYPE_OFFERS_CONTAINER;
        } else {
            return VIEW_TYPE_PRODUCTS_CONTAINER;
        }
    }

    //Creates every single new row in the recyclerview
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            //Returns view for images slider if the view type is images slider
            case VIEW_TYPE_HOME_BANNER:
                v = LayoutInflater.from(context).inflate(R.layout.fragment_templates_home_image_slider, parent, false);
                holder = new SliderViewHolder(v);
                break;

            //Returns view for horizontal scroll view container if the view type is horizontal scroll view container
            case VIEW_TYPE_OFFERS_CONTAINER:
                v = LayoutInflater.from(context).inflate(R.layout.fragment_templates_horizontal_scroll_view_products_container, parent, false);
                holder = new OfferContainerViewHolder(v);
                break;

            case VIEW_TYPE_PRODUCTS_CONTAINER:
                v = LayoutInflater.from(context).inflate(R.layout.fragment_templates_horizontal_scroll_view, parent, false);
                holder = new ProductContainerViewHolder(v);
                break;
        }
        return holder;
    }

    //Binds data to every single row in the recyclerview
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {
            //If the view type is for images slider then adjust data of images slider the view
            case VIEW_TYPE_HOME_BANNER:
                final SliderViewHolder sliderViewHolder = (SliderViewHolder) holder;
                if (sliderViewHolder.imageSlider != null) {
                    HomeImagesSliderAdapter adapter = new HomeImagesSliderAdapter(homeFragment.getChildFragmentManager());

                    if(banners!=null){
                        for(int i=0; i<banners.size(); i++){
                            HomeBanner banner = banners.get(i);

                            adapter.addFragment(banner.getLink(), banner.getImage());
                        }
                    }
                    sliderViewHolder.imageSlider.setAdapter(adapter);
                    sliderViewHolder.indicator.setViewPager(sliderViewHolder.imageSlider);

                    //Auto swiping of viewpager pages
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            sliderViewHolder.imageSlider.setCurrentItem((sliderViewHolder.imageSlider.getCurrentItem()+1)%banners.size());
                            handler.postDelayed(this, 5000);
                        }
                    }, 5000);

                    sliderViewHolder.imageSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            if(mTracker != null){
                                mTracker.setScreenName("ImageSlider - " + position);
                                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                            }
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                }
                break;

            //If the view type is for horizontal scroll view container then adjust data of horizontal scroll view container
            case VIEW_TYPE_OFFERS_CONTAINER:
                //ContainerViewHolder is for holding the view items of horizontal scroll view container
                final OfferContainerViewHolder containerViewHolder = (OfferContainerViewHolder) holder;
                containerViewHolder.title.setText("Offer Zone");
                //Setting onclick listener in the MORE link of the Horizontal Scroll View Container
                containerViewHolder.moreLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        homeFragment.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.drawer_fragment, new OfferProductsListingFragment()).addToBackStack(null).commit();
                    }
                });

                if(offerProducts==null) {
                    Map<String, String> offerMap = new HashMap<>(); //Use HasMap to store parameter required for the API
                    offerMap.put("limit", "10");
                    offerMap.put("random", "true");

                    BackgroundThread offerThread = new BackgroundThread(ApiUrl.OFFERS_DISPLAY_URL_TEMPORARY, offerMap, Connection.GET);
                    offerThread.setBackgroundListener(context, new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {

                            containerViewHolder.progressView.setVisibility(View.GONE);

                        }

                        @Override
                        public void onResponse(Response response, String result) throws IOException {
                            if(response.isSuccessful()){
                                final List<Product> products = JsonParser.parseOfferProducts(result);
                                //Log.d("OfferResult", String.valueOf(products.size()));

                                //Inside Horizontal scroll view container there is another recycler view that adjusts the products items in horizontal scrolling form
                                //The adapter is constructed in the same patter as this one is constructed
                                offerProducts = products;
                                (containerViewHolder).productAdapter.setOfferProducts(products);
                                (containerViewHolder).productAdapter.notifyDataSetChanged();
                                //return products;
                            }

                            containerViewHolder.progressView.setVisibility(View.GONE);
                        }
                    });

                    offerThread.execute();
                }else{
                    containerViewHolder.progressView.setVisibility(View.GONE);

                    (containerViewHolder).productAdapter.setOfferProducts(offerProducts);
                    (containerViewHolder).productAdapter.notifyDataSetChanged();
                }
                break;

            case VIEW_TYPE_PRODUCTS_CONTAINER:
                //ContainerViewHolder is for holding the view items of horizontal scroll view container
                final ProductContainerViewHolder containerProductViewHolder = (ProductContainerViewHolder) holder;
                containerProductViewHolder.title.setText(Html.fromHtml(containerHeader[position - 2]));
                //Setting onclick listener in the MORE link of the Horizontal Scroll View Container
                containerProductViewHolder.moreLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle pass = new Bundle();
                        pass.putString("sub_cat_ID", categoryIds[position-2]);//passing the clicked value to the next fragment
                        pass.putString("sub_cat_title", containerHeader[position-2]);
                        Fragment nextFrag = new CategoryProductsListingFragment();
                        nextFrag.setArguments(pass);
                        homeFragment.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.drawer_fragment, nextFrag).addToBackStack(null).commit();
                    }
                });

                if(!(allProducts.containsKey(position))) {
                    Map<String, String> productsMap = new HashMap<>(); //Use HasMap to store parameter required for the API
                    productsMap.put("id", categoryIds[position - 2]); //map.put can be use as many time as required, dont change the 'id'
                    productsMap.put("limit", "10");
                    productsMap.put("random", "true");

                    BackgroundThread productsThread = new BackgroundThread(ApiUrl.PRODUCTS_DISPLAY_URL_TEMPORARY, productsMap, Connection.GET);
                    productsThread.setBackgroundListener(context, new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {

                            containerProductViewHolder.progressView.setVisibility(View.GONE);

                        }

                        @Override
                        public void onResponse(Response response, String result) throws IOException {
                            if(response.isSuccessful()){
                                final ArrayList<Product> products = JsonParser.parseCategoryProductResult(result);

                                /*if(products.size() > 0){*/

                                    //Inside Horizontal scroll view container there is another recycler view that adjusts the products items in horizontal scrolling form
                                    //The adapter is constructed in the same patter as this one is constructed
                                    allProducts.put(position, products);
                                    (containerProductViewHolder).productAdapter.setProducts(products);
                                    (containerProductViewHolder).productAdapter.notifyDataSetChanged();
                                    //return products;

                                /*} else {

                                    containerProductViewHolder.noProductFound.setVisibility(View.VISIBLE);

                                }*/
                            }

                            containerProductViewHolder.progressView.setVisibility(View.GONE);
                        }
                    });

                    productsThread.execute();
                }else{
                    containerProductViewHolder.progressView.setVisibility(View.GONE);

                    (containerProductViewHolder).productAdapter.setProducts(allProducts.get(position));
                    (containerProductViewHolder).productAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (containerHeader.length+2);
    }

    //Class to hold views of Horizontal Scroll View Container
    class ProductContainerViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView moreLink;
        RecyclerView horizontalScrollViewItems;
        HorizontalScrollViewCategoryItemsAdapter productAdapter;
        CircularProgressView progressView;
        TextView noProductFound;

        public ProductContainerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.header_offer);
            moreLink = (TextView) itemView.findViewById(R.id.header_more);
            horizontalScrollViewItems = (RecyclerView) itemView.findViewById(R.id.horizontal_scroll_view_items);
            progressView = (CircularProgressView) itemView.findViewById(R.id.loading);
            noProductFound = (TextView) itemView.findViewById(R.id.no_product_found);

            productAdapter = new HorizontalScrollViewCategoryItemsAdapter(context, null);
            horizontalScrollViewItems.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(context, org.solovyev.android.views.llm.LinearLayoutManager.HORIZONTAL, false));
            horizontalScrollViewItems.setAdapter(productAdapter);
        }
    }

    //Class to hold views of Images Slider
    class SliderViewHolder extends RecyclerView.ViewHolder {

        ViewPager imageSlider;
        CirclePageIndicator indicator;

        public SliderViewHolder(View itemView) {
            super(itemView);
            imageSlider = (ViewPager) itemView.findViewById(R.id.imageSlider);
            indicator = (CirclePageIndicator)itemView.findViewById(R.id.indicator);
        }
    }

    //Class to hold view of Offer Items
    class OfferContainerViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView moreLink;
        RecyclerView horizontalScrollViewItems;
        HorizontalViewOfferItemsAdapter productAdapter;
        CircularProgressView progressView;
        TextView noProductFound;

        public OfferContainerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.header_offer);
            moreLink = (TextView) itemView.findViewById(R.id.header_more);
            horizontalScrollViewItems = (RecyclerView) itemView.findViewById(R.id.horizontal_scroll_view_items);
            progressView = (CircularProgressView) itemView.findViewById(R.id.loading);
            noProductFound = (TextView) itemView.findViewById(R.id.no_product_found);

            productAdapter = new HorizontalViewOfferItemsAdapter(context, null);
            horizontalScrollViewItems.setAdapter(productAdapter);
            horizontalScrollViewItems.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(context, org.solovyev.android.views.llm.LinearLayoutManager.HORIZONTAL, false));

        }
    }
}
