package com.nepalicoders.nepbayapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nepalicoders.nepbayapp.R;

/**
 * Created by kshitij on 12/9/15.
 */
public class Message {

    public static void snackBarLongPositive(View v, String message){
        Snackbar snackbar = Snackbar.make(v.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) snackbar.getView();
        group.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimaryLight));
        snackbar.show();
    }

    public static void snackBarShortPositive(View v, String message){
        Snackbar snackbar = Snackbar.make(v.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) snackbar.getView();
        group.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimaryLight));
        snackbar.show();
    }

    public static void snackBarLongNegative(View v, String message){
        Snackbar snackbar = Snackbar.make(v.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) snackbar.getView();
        group.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent));
        snackbar.show();
    }

    public static void snackBarShortNegative(View v, String message){
        Snackbar snackbar = Snackbar.make(v.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) snackbar.getView();
        group.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent));
        snackbar.show();
    }

    public static void alertDialogSimple(String title,String message, final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)

                /*
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity

                    }
                })*/

                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    /*public void alertDialogLoading(Context context, String title, String message, int time){

        ProgressDialog.show(context, title, message);

        try {
            Thread.sleep(time);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        ProgressDialog.dismiss();
    }*/

}
