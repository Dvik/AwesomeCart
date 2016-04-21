package com.transferret.whizzbuy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.transferret.whizzbuy.adapters.ShoppingListAdapter;
import com.transferret.whizzbuy.model.Tasks;

import java.util.ArrayList;

/**
 * Created by Divya on 12/13/2015.
 */
public class ShoppingList extends AppCompatActivity {

    RatingBar ratingBar, ratingBar1;
    ImageView img;
    ArrayList<Tasks> titleList;
    ArrayList<Tasks> tickedList;
    ArrayList<Tasks> titleTextList;

    ProgressDialog pDialog;

    RecyclerView rv;
    ShoppingListAdapter adapter;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPrefCustomerInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppinglist);
        pDialog = new ProgressDialog(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPrefCustomerInfo = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_CUSTOMER_key), Context.MODE_PRIVATE);

        //titlelist for unchecked items
        titleList = new ArrayList<Tasks>();

        //tickedlist for checked items
        tickedList = new ArrayList<Tasks>();

        titleTextList = new ArrayList<Tasks>();

        titleTextList.add(new Tasks("Title",false));

        rv = (RecyclerView) findViewById(R.id.act1_recycler);

        // rv.getWindowId().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        LinearLayoutManager llm = new LinearLayoutManager(this.getApplicationContext());

        rv.setLayoutManager(llm);

        adapter = new ShoppingListAdapter(getApplicationContext(), titleList, tickedList, titleTextList);
        rv.setAdapter(adapter);

        //creating json to be sent to API for fetching shopping list fron server
        //See the mobile number you have to take from user profile data
        //I don't have it. So, i've hardcoded it.
        JsonObject json1 = new JsonObject();

        json1.addProperty("MobileNumber", sharedPrefCustomerInfo.getString("MOBILE_NUMBER", ""));

        pDialog.setMessage("Fetching shopping list...");
        pDialog.show();
        //This is where ion starts
        Ion.with(getApplicationContext())
                .load("POST", "http://whizzbuytest.herokuapp.com/fetchshoppinglist")
                .setJsonObjectBody(json1)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        pDialog.dismiss();
                        if(titleTextList.size()>0)
                            titleTextList.clear();

                        if (result != null) {

                            if (result.has("error")) {
                                Toast.makeText(getApplicationContext(), result.get("error").getAsString(), Toast.LENGTH_SHORT).show();

                            } else {
                                if (result.get("status").getAsString().equals("1000")) {
                                    JsonArray tickedArray = result.get("checked").getAsJsonArray();
                                    JsonArray titleArray = result.get("unchecked").getAsJsonArray();

                                    titleTextList.add(new Tasks(result.get("title").getAsString(),false));


                                /*tickedList = new Gson().fromJson(tickedArray, new TypeToken<List<Tasks>>() {
                                }.getType());
                                titleList = new Gson().fromJson(titleArray, new TypeToken<List<Tasks>>() {
                                }.getType());*/

                                    for (int i = 0; i < tickedArray.size(); i++) {
                                        tickedList.add(new Tasks(tickedArray.get(i).getAsString(), true));
                                    }
                                    for (int i = 0; i < titleArray.size(); i++) {
                                        titleList.add(new Tasks(titleArray.get(i).getAsString(), false));
                                    }
                                    //Refreshing adapter after both list has been updated
                                    adapter.notifyDataSetChanged();


                                } else {
                                    Toast.makeText(getApplicationContext(), result.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Bad Request", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        //for that back button on actionbar
        if (id == android.R.id.home) {
            saveShoppingList();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveShoppingList();
    }

    //On press of back button , this is triggered
    public void saveShoppingList() {


        ArrayList<String> arrayList1 = new ArrayList<>();
        for (int i = 0; i < titleList.size(); i++) {
            arrayList1.add(titleList.get(i).name);
        }

        ArrayList<String> arrayList2 = new ArrayList<>();

        for (int i = 0; i < tickedList.size(); i++) {
            arrayList2.add(tickedList.get(i).name);
        }


        String json1 = new Gson().toJson(arrayList1);

        String json2 = new Gson().toJson(arrayList2);

/*

        pDialog = new ProgressDialog(ShoppingList.this);
        pDialog.setMessage("Saving shopping list...");
        pDialog.show();
*/
        Ion.with(getApplicationContext())
                .load("POST", "http://whizzbuytest.herokuapp.com/saveshoppinglist")
                .setBodyParameter("MobileNumber", sharedPrefCustomerInfo.getString("MOBILE_NUMBER", ""))
                .setBodyParameter("Checked",json2)
                .setBodyParameter("Unchecked",json1)
                .setBodyParameter("Title",titleTextList.get(0).name)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                     //   pDialog.dismiss();

                        if (result != null) {
                            Toast.makeText(getApplicationContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("dvdxv", result.toString());
                        if (result == null) {
                            Toast.makeText(getApplicationContext(), "Errror", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }
}