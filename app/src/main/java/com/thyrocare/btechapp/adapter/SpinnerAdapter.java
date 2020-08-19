package com.thyrocare.btechapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thyrocare.btechapp.R;

import java.util.ArrayList;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class SpinnerAdapter extends ArrayAdapter<String> {

	private Activity activity;
    private ArrayList<String> data;
    public Resources res;
    public String strTextColor;
    public String strFont;
    Typeface font;
    String tempValues=null;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
	public SpinnerAdapter(
            Activity activitySpinner,
            int textViewResourceId,
            ArrayList<String> objects,
            Resources resLocal
    )
	 {
        super(activitySpinner, textViewResourceId, objects);
        
        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;
        res      = resLocal;
        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

    	/********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_items, parent, false);
        
        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues =data.get(position);
        
        TextView label        = (TextView)row.findViewById(R.id.company);

        ImageView companyLogo = (ImageView)row.findViewById(R.id.image);
        RelativeLayout relbg=(RelativeLayout) row.findViewById(R.id.relSpinBg);
        relbg.setBackgroundColor(Color.parseColor("#00000000"));
        label.setText(tempValues);

        return row;
      }
 }
