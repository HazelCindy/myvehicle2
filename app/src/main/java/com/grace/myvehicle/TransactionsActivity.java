package com.grace.myvehicle;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class TransactionsActivity extends AppCompatActivity {
    MaterialDialog dialog;
    Preferences preferences;
    ListView listView;
    ArrayList<Transaction> transactions = new ArrayList<>();
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(getApplicationContext());
        setContentView(R.layout.activity_transactions);
        listView = (ListView)findViewById(R.id.listView);
        customAdapter = new CustomAdapter(transactions, TransactionsActivity.this);
        listView.setAdapter(customAdapter);
        new TransService().execute(Utils.BASE_URL + "transactions/" + preferences.getUserId());
    }
    private class TransService extends AsyncTask<String, Void, Pair<Integer, String> > {
        protected void onPreExecute(){
            dialog = Utils.configureProgessDialog(TransactionsActivity.this, "Transactions", "Fetching MPESA transactions..");
            dialog.show();
        }

        @Override
        protected Pair<Integer, String> doInBackground(String... strings) {
            NetworkHandler handler = new NetworkHandler(getApplicationContext());
            return handler.doGet(strings[0]);
        }
        @Override
        protected void onPostExecute(Pair<Integer, String> result){
            dialog.dismiss();
            if(result != null && result.message != null){
                JsonParser jsonParser = new JsonParser();
                if (result.message == null || result.message.isEmpty())
                    return;
                JsonArray jo = (JsonArray) jsonParser.parse(result.message).getAsJsonArray();
                for (int i = 0; i < jo.size(); i++){
                    JsonObject jsonObject  = jo.get(i).getAsJsonObject();
                    Transaction transaction = new Transaction();
                    transaction.setAmount(jsonObject.get("amount").getAsInt());
                    transaction.setCode(jsonObject.get("mpesa_receipt_number").getAsString());
                    //transaction.setCarNo(jsonObject.get("car").getAsString());
                    transaction.setPhone(jsonObject.get("phone_number").getAsString());
                    transaction.setDate(jsonObject.get("transaction_date").getAsString());

                    transactions.add(transaction);

                    ///TODO: update adapter

                }
                customAdapter.notifyDataSetChanged();

                return;
            }else{
                dialog = Utils.configureDialog(TransactionsActivity.this, "Connection error", "Connection error","OK", null);
                dialog.show();
            }
        }
    }
}
