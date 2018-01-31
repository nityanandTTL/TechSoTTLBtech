package com.thyrocare.activity;

/**
 * Created by E4904 on 4/22/2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.DataSnapshot;
import com.thyrocare.R;
import com.thyrocare.uiutils.AbstractActivity;
import com.thyrocare.utils.api.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Users extends AbstractActivity {
    ListView usersList;
    String old ="old";
    TextView noUsersText,users,countt;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    EditText inputSearch;
    String TAG = "Users";
    int SelectedPostionbydefault = 0;

     ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);
        users = (TextView)findViewById(R.id.userlistnames);
        inputSearch = (EditText) findViewById(R.id.inputSearch);



        pd = new ProgressDialog(Users.this);
        pd.setMessage("Loading...");
        pd.show();



        String url = "https://finalchat-df79b.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
                final String comeFromGet = getIntent().getStringExtra("comeFrom");
                final String comefrom = getIntent().getStringExtra("Comeform");
                Logger.error("comeFromget " + comeFromGet);
                if (comeFromGet.equals("service")){
                    for (int i = 0; i < al.size(); i++) {

                        if (al.get(i).equalsIgnoreCase(comefrom)) {

                            Log.e(TAG, "onCreate: " + al.get(i));

                            SelectedPostionbydefault = i;
                            UserDetails.username = appPreferenceManager.getLoginResponseModel().getUserName();
                            UserDetails.chatWith = al.get(i);
                            startActivity(new Intent(Users.this, Chat.class));

                        }
                    }
                }



            }
 },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Users.this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = arrayAdapter.getItem(position);
                startActivity(new Intent(Users.this, Chat.class));

            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Users.this.arrayAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(UserDetails.username)) {
                    Logger.error("key "+key);
                    al.add(key);
                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this,R.layout.inflator_users,R.id.userlistnames, al));

            /*final ArrayAdapter<String> */  arrayAdapter = new ArrayAdapter<String>
                    (this, R.layout.inflator_users,R.id.userlistnames, al){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    // Get the current item from ListView
                    View view = super.getView(position,convertView,parent);
                    if(position %2 == 1)
                    {
                        // Set a background1 color for ListView regular row/item
                        view.setBackgroundColor(Color.parseColor("#616161"));
                    }
                    else
                    {
                        // Set the background1 color for alternate row/item
                        view.setBackgroundColor(Color.parseColor("#212121"));
                    }
                    return view;
                }
            };

           // usersList.setAdapter(arrayAdapter);
            usersList.setAdapter(arrayAdapter);

            countt= (TextView)findViewById(R.id.counts);
            // countt.setText(old);

        }

        pd.dismiss();
    }
   private class CustomAdapter extends BaseAdapter implements Filterable{

       @Override
       public int getCount() {
           return 0;
       }

       @Override
       public Object getItem(int position) {
           return null;
       }

       @Override
       public long getItemId(int position) {
           return 0;
       }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
           return null;
       }

       @Override
       public Filter getFilter() {


           Filter filter = new Filter() {

               @SuppressWarnings("unchecked")
               @Override
               protected void publishResults(CharSequence constraint, FilterResults results) {

                   al = (ArrayList<String>) results.values;
                   notifyDataSetChanged();
               }

               @Override
               protected FilterResults performFiltering(CharSequence constraint) {

                   FilterResults results = new FilterResults();
                   ArrayList<String> FilteredArrayNames = new ArrayList<String>();

                   // perform your search here using the searchConstraint String.

                   constraint = constraint.toString().toLowerCase();
                   for (int i = 0; i < al.size(); i++) {
                       String dataNames = al.get(i);
                       if (dataNames.toLowerCase().startsWith(constraint.toString()))  {
                           FilteredArrayNames.add(dataNames);
                       }
                   }

                   results.count = FilteredArrayNames.size();
                   results.values = FilteredArrayNames;
                   Log.e("VALUES", results.values.toString());

                   return results;
               }
           };

           return filter;
       }
   }
}
