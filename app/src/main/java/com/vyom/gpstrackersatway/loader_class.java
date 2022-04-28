package com.vyom.gpstrackersatway;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class loader_class {
    Activity activity;
    static AlertDialog dialogue;
    loader_class(Activity myact)
    {
        activity = myact;

    }
    void startdialogue(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.spinner,null));
        builder.setCancelable(true);
        dialogue =builder.create();
        dialogue.show();

    }

static void dismissdig()
{
        dialogue.dismiss();

}
}
