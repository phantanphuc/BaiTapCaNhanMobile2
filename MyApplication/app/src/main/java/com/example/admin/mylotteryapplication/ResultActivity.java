package com.example.admin.mylotteryapplication;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ResultActivity extends AppCompatActivity {

    Fragment[] FragmentList;
    Fragment current;
    String[] ProvinceArray = {"AG", "BD", "BL", "BP", "BTH", "CM", "CT", "HCM"};
    Activity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FragmentList = new Fragment[8];

        LinearLayout ResultConstrainLayout = (LinearLayout) findViewById(R.id.LinearLayoutHolder);
        Fragment myfrag = ResultFragment.newInstance("hcm", "");
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.add(ResultConstrainLayout.getId(), myfrag , "fragment" + 1).commit();
        current = myfrag;

        ResultRetriever requester = new ResultRetriever("http://thanhhungqb.tk:8080/kqxsmn", this, FragmentList, current);
        requester.execute();


        mContext = this;

        final Spinner ProvinceSpinner = (Spinner) findViewById(R.id.ProvinceSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ProvinceArray);
        ProvinceSpinner.setAdapter(adapter);











        //Bundle bundle = new Bundle();
        //bundle.putString("test", "hcm");
        //Fragment myfrag = new ResultFragment();
        //myfrag.setArguments(bundle);

        //ConstraintLayout ResultConstrainLayout = (ConstraintLayout) findViewById(R.id.ResultConstrainLayout);

        //ft.replace(R.id.Fragment_Result, (Fragment)myfrag).commit();





    }

}


class ResultRetriever extends AsyncTask<Void, Void, String> {

    private String url;
    private Activity mContext;
    Fragment[] fragmentList;
    Fragment current;

    public ResultRetriever(String url, Activity pcontext, Fragment[] list, Fragment pcurrent) {
        this.url = url;
        mContext = pcontext;
        fragmentList = list;
        current = pcurrent;
    }



    @Override
    protected String doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            //String inputStreamString = new Scanner(input).useDelimiter("\\A").next();

            return new Scanner(input).useDelimiter("\\A").next();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        String[] ProvinceArray = {"AG", "BD", "BL", "BP", "BTH", "CM", "CT", "HCM"};

        JSONObject obj;
        try {
            obj = new JSONObject(result);

            for (int i = 0; i < ProvinceArray.length; ++i){
                JSONObject provinceData = obj.getJSONObject(ProvinceArray[i]);
                fragmentList[i] = ResultFragment.newInstance(ProvinceArray[i], provinceData.toString());
            }





        }  catch (Exception e){
            Log.d("mytag", e.getMessage());
        }


        replaceFragment();

        final Spinner ProvinceSpinner = (Spinner) mContext.findViewById(R.id.ProvinceSpinner);

        ProvinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                Toast.makeText(mContext, ": " + ProvinceSpinner.getSelectedItemId(), Toast.LENGTH_LONG).show();

                try{
                    replaceFragment();
                }catch (Exception e){
                    Log.d("mytag", e.getMessage());
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });



    }


    void replaceFragment(){
        final Spinner ProvinceSpinner = (Spinner) mContext.findViewById(R.id.ProvinceSpinner);

        android.app.FragmentManager fm2 = mContext.getFragmentManager();
        android.app.FragmentTransaction ft2 = fm2.beginTransaction();
        //Fragment amyfrag = ResultFragment.newInstance("vungtau", "");
        ft2.replace(current.getId(), (Fragment) fragmentList[(int)ProvinceSpinner.getSelectedItemId()]).commit();
        current = fragmentList[(int)ProvinceSpinner.getSelectedItemId()];
    }


}