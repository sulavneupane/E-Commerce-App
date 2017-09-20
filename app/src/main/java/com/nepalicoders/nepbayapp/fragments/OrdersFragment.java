package com.nepalicoders.nepbayapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nepalicoders.nepbayapp.R;
import com.nepalicoders.nepbayapp.StaticInfo.VariableNames;
import com.nepalicoders.nepbayapp.activities.MainActivity;
import com.nepalicoders.nepbayapp.adapters.AnimatedExpandableListView;
import com.nepalicoders.nepbayapp.api.ApiUrl;
import com.nepalicoders.nepbayapp.interfaces.Callback;
import com.nepalicoders.nepbayapp.json.JsonParser;
import com.nepalicoders.nepbayapp.objects.Order;
import com.nepalicoders.nepbayapp.utils.BackgroundThread;
import com.nepalicoders.nepbayapp.utils.Connection;
import com.nepalicoders.nepbayapp.utils.HandleOrders;
import com.nepalicoders.nepbayapp.utils.Message;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {

    private AnimatedExpandableListView listView;
    private OrdersAdapter adapter;
    private ArrayList<String> parentItems, child;
    int lastPosition = -1;
    List<GroupItem> items = new ArrayList<GroupItem>();
    String sharedPreferences_sessionKey;
    String sharedPreferences_username;
    String sharedPreferences_userid;
    SharedPreferences sharedPreferences;

    RelativeLayout myOrdersLayout;
    TextView noInternetMessage;
    CircularProgressView pd;

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_drawer_order_layout, container, false);

        //animation for order fragment
        YoYo.with(Techniques.FadeInLeft).duration(700).playOn(view);

        noInternetMessage = (TextView) view.findViewById(R.id.noInternetMessage);
        myOrdersLayout = (RelativeLayout) view.findViewById(R.id.my_orders_layout);
        pd = (CircularProgressView) view.findViewById(R.id.loading);

        sharedPreferences = getActivity().getSharedPreferences(VariableNames.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sharedPreferences_sessionKey = sharedPreferences.getString(VariableNames.SESSION_KEY, "");
        sharedPreferences_username = sharedPreferences.getString(VariableNames.USERNAME, "");
        sharedPreferences_userid = sharedPreferences.getString(VariableNames.USERID, "");

        if (!sharedPreferences_username.equals("") && !sharedPreferences_sessionKey.equals("") && !sharedPreferences_userid.equals("")) {

            Map<String, String> map = new HashMap<>(); //Use HasMap to store parameter required for the API
            map.put("username", sharedPreferences_username); //map.put can be use as many time as required
            map.put("sessionkey", sharedPreferences_sessionKey);
            map.put("userid", "2015063004130082.217.91.149");

            BackgroundThread thread = new BackgroundThread(ApiUrl.VIEW_ORDERS_URL, map, Connection.GET);
            thread.setBackgroundListener(getActivity(), new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    pd.setVisibility(View.GONE);
                    noInternetMessage.setVisibility(View.VISIBLE);
                    myOrdersLayout.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(Response response, String result) throws IOException {
                    if (response.isSuccessful()) {
                        pd.setVisibility(View.GONE);

                        List<Order> orders = JsonParser.parseOrders(result);

                        ((TextView) view.findViewById(R.id.total_order)).setText(String.valueOf(orders.size()));


                        List<Order> approvedOrders = HandleOrders.getApprovedOrders(orders);
                        List<Order> rejectedOrders = HandleOrders.getRejectedOrders(orders);
                        List<Order> pendingOrders = HandleOrders.getPendingOrders(orders);

                        //items = new ArrayList<GroupItem>();

                        String[] orderNames = new String[]{
                                "Approved Orders",
                                "Rejected Orders",
                                "Pending Orders"
                        };

                        int[] artworkArray = new int[]{
                                R.drawable.ic_approved,
                                R.drawable.ic_rejected,
                                R.drawable.ic_pending
                        };

                        // Populate our list with groups and it's children
                        for (int i = 0; i < orderNames.length; i++) {
                            GroupItem item = new GroupItem();

                            item.title = orderNames[i];
                        /*if (i % 2 != 0) {
                            item.artwork = R.drawable.ic_home;
                        } else {
                            item.artwork = R.drawable.ic_category;
                        }*/
                            item.artwork = artworkArray[i];
                            /*item.artwork = R.drawable.ic_orders;*/

                            if (i == 0) {

                                if (approvedOrders.size() > 0) {

                                    for (int j = 0; j < approvedOrders.size(); j++) {

                                        Date date = new Date(Long.parseLong(approvedOrders.get(j).getDate()) * 1000 - 20700000);
                                        SimpleDateFormat f = new SimpleDateFormat("d/MMM/yyyy K:mmaaa");
                                        String dateFormatted = f.format(date);

                                        ChildItem child = new ChildItem();
                                        child.ItemName = dateFormatted + " " + approvedOrders.get(j).getItemName();
                                        child.hint = " Rs. " + approvedOrders.get(j).getItemCost() + "  Qty." + approvedOrders.get(j).getItemQuantity();
                                        //child.hint = "Parent " + i + ", Child " + j;
                                        item.items.add(child);
                                    }
                                    items.add(item);

                                } else {

                                    ChildItem childItem = new ChildItem();
                                    childItem.ItemName = "Approved Order(s) Not Found!";
                                    childItem.ItemPrice = "";
                                    item.items.add(childItem);
                                    items.add(item);

                                }

                            } else if (i == 1) {

                                if (rejectedOrders.size() > 0) {

                                    for (int j = 0; j < rejectedOrders.size(); j++) {

                                        Date date = new Date(Long.parseLong(rejectedOrders.get(j).getDate()) * 1000 - 20700000);
                                        SimpleDateFormat f = new SimpleDateFormat("d/MMM/yyyy");
                                        String dateFormatted = f.format(date);

                                        ChildItem child = new ChildItem();
                                        child.ItemName = dateFormatted + " " + rejectedOrders.get(j).getItemName();
                                        child.hint = " Rs. " + rejectedOrders.get(j).getItemCost() + "  Qty." + rejectedOrders.get(j).getItemQuantity();
                                        //child.hint = "Parent " + i + ", Child " + j;
                                        item.items.add(child);
                                    }
                                    items.add(item);

                                } else {

                                    ChildItem childItem = new ChildItem();
                                    childItem.ItemName = "Rejected Order(s) Not Found!";
                                    childItem.ItemPrice = "";
                                    item.items.add(childItem);
                                    items.add(item);

                                }

                            } else if (i == 2) {

                                if (pendingOrders.size() > 0) {

                                    for (int j = 0; j < pendingOrders.size(); j++) {

                                        Date date = new Date(Long.parseLong(pendingOrders.get(j).getDate()) * 1000 - 20700000);
                                        SimpleDateFormat f = new SimpleDateFormat("d/MMM/yyyy K:mmaaa");
                                        String dateFormatted = f.format(date);

                                        ChildItem child = new ChildItem();
                                        child.ItemName = dateFormatted + " " + pendingOrders.get(j).getItemName();
                                        child.hint = " Rs. " + pendingOrders.get(j).getItemCost() + "  Qty." + pendingOrders.get(j).getItemQuantity();
                                        //child.hint = "Parent " + i + ", Child " + j;
                                        item.items.add(child);
                                    }
                                    items.add(item);

                                } else {

                                    ChildItem childItem = new ChildItem();
                                    childItem.ItemName = "Pending Order(s) Not Found!";
                                    childItem.ItemPrice = "";
                                    item.items.add(childItem);
                                    items.add(item);

                                }

                            }
                        }


                        adapter = new OrdersAdapter(getActivity());
                        adapter.setData(items);

                        listView = (AnimatedExpandableListView) view.findViewById(R.id.listView);
                        listView.setAdapter(adapter);

                        // In order to show animations, we need to use a custom click handler
                        // for our ExpandableListView.
                        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                // We call collapseGroupWithAnimation(int) and
                                // expandGroupWithAnimation(int) to animate group
                                // expansion/collapse.

                                if (listView.isGroupExpanded(groupPosition)) {
                                    listView.collapseGroupWithAnimation(groupPosition);
                                    //lastPosition = -1;
                                    //groupPosition = -1;
                                } else {
                                    listView.expandGroupWithAnimation(groupPosition);
                                }

                                return true;
                            }
                        });
                        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                ChildItem item = items.get(groupPosition).items.get(childPosition);
                                //new AlertDialog.Builder(getActivity()).setTitle("Order Details").setMessage(item.ItemName+" \n"+item.hint).setCancelable(true).show();
                                Message.alertDialogSimple("Order Details", item.ItemName + " \n" + item.hint, getActivity());
                                return true;
                            }
                        });

                        //return null;
                    }
                }
            });

            thread.execute();

        } else {

            Message.snackBarShortNegative(getActivity().getWindow().getDecorView(), "Not Logged In!");

        }

        return view;
    }

    public static class GroupItem {
        String title;
        int artwork;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    public static class ChildItem {
        String ItemName;
        String ItemPrice;
        String hint;
    }

    public static class ChildHolder {
        TextView item_name;
        TextView item_price;
        TextView hint;
    }

    public static class GroupHolder {
        TextView title;
        CircleImageView artwork;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class OrdersAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public OrdersAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }


        //Child group view
        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.fragment_drawer_order_listitem, parent, false);
                holder.item_name = (TextView) convertView.findViewById(R.id.childItem);
                holder.item_price = (TextView) convertView.findViewById(R.id.childPrice);
                holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            holder.item_name.setText(item.ItemName);
            holder.item_price.setText(item.ItemPrice);
            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }


        //main Group view
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.fragment_drawer_order_groupitem, parent, false);

                holder.title = (TextView) convertView.findViewById(R.id.parentTitle);

                holder.artwork = (CircleImageView) convertView.findViewById(R.id.artwork);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            holder.artwork.setImageResource(item.artwork);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }


}



