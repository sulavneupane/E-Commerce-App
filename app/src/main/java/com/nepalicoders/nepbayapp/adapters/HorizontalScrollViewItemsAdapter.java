package com.nepalicoders.nepbayapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.activities.MainActivity;

/**
 * Created by sulav on 12/12/15.
 */
public class HorizontalScrollViewItemsAdapter extends RecyclerView.Adapter<HorizontalScrollViewItemsAdapter.HorizontalScrollViewItemsViewHolder> {

    private LayoutInflater inflater;
    private String items[];
    private String prices[];
    private Context context;

    public HorizontalScrollViewItemsAdapter(Context context, String items[], String prices[]){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.items = items;
        this.prices = prices;
    }

    @Override
    public HorizontalScrollViewItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_templates_horizontal_scroll_view_single_product, parent, false);
        HorizontalScrollViewItemsViewHolder holder = new HorizontalScrollViewItemsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HorizontalScrollViewItemsViewHolder holder, final int position) {
        holder.productName.setText(Html.fromHtml(items[position]));
        holder.singleProductToolbar.setTitle(Html.fromHtml(prices[position]));
        if (holder.singleProductToolbar != null) {
            // inflate your menu
            if(holder.singleProductToolbar.getMenu().size() == 0){
                holder.singleProductToolbar.inflateMenu(R.menu.single_product_menu);
            }
            holder.singleProductToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int id = menuItem.getItemId();
                    switch (id){
                        case R.id.action_add_cart:
                            if(context instanceof MainActivity){
                                int num_of_items = ((MainActivity)context).hot_number + 1;
                                ((MainActivity)context).updateHotCount(num_of_items);
                                ((MainActivity)context).shakeCart();
                            }
                            break;

                        case R.id.action_add_wishlist:
                            Toast.makeText(context, "Click on Add to Wishlist for Item " + items[position], Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    class HorizontalScrollViewItemsViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        Toolbar singleProductToolbar;

        public HorizontalScrollViewItemsViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            singleProductToolbar = (Toolbar) itemView.findViewById(R.id.single_product_toolbar);
        }
    }
}
