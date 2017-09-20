package com.nepalicoders.nepbayapp.utils;

import com.nepalicoders.nepbayapp.objects.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sulav on 12/10/15.
 */
public class HandleOrders {

    public static List<Order> getApprovedOrders(List<Order> orders){
        List<Order> orderList = new ArrayList<>();

        for(int i=0; i<orders.size(); i++){
            if(orders.get(i).getStatus().equals("approved")){
                orderList.add(orders.get(i));
            }
        }

        return orderList;
    }

    public static List<Order> getRejectedOrders(List<Order> orders){
        List<Order> orderList = new ArrayList<>();

        for(int i=0; i<orders.size(); i++){
            if(orders.get(i).getStatus().equals("rejected")){
                orderList.add(orders.get(i));
            }
        }

        return orderList;
    }

    public static List<Order> getPendingOrders(List<Order> orders){
        List<Order> orderList = new ArrayList<>();

        for(int i=0; i<orders.size(); i++){
            if(orders.get(i).getStatus().equals("waiting_payment")){
                orderList.add(orders.get(i));
            }
        }

        return orderList;
    }

}
