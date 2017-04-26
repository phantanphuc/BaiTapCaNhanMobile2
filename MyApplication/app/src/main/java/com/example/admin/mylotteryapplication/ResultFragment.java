package com.example.admin.mylotteryapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.widget.RelativeLayout;
import android.widget.Toast;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.END_OF;
import static android.widget.RelativeLayout.RIGHT_OF;

public class ResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    LinearLayout resuldHolder;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String ProvinceName;
    private String JSONData;
    private List<resultdata> resultlist;
    private List<TextView> LabelList;

    private OnFragmentInteractionListener mListener;

    public ResultFragment() {
        // Required empty public constructor
    }


    public static ResultFragment newInstance(String param1, String param2) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ProvinceName = getArguments().getString(ARG_PARAM1);
            JSONData = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ////////////////////////////////////////////////////


        resultlist = new ArrayList<>();
        LabelList = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_result, container, false);

        if (JSONData.equals("")) return v;

        JSONObject obj;
        try {
            obj = new JSONObject(JSONData);

            //JSONArray DateArray = obj.getJSONArray("")

            Iterator<String> mykey = obj.keys();
            for (Iterator<String> iter = mykey; iter.hasNext(); ) {
                String element = iter.next();

                JSONObject dateData = obj.getJSONObject(element);
                resultdata newdata = new resultdata(element);

                JSONArray DBarray = dateData.getJSONArray("DB");
                newdata.price[0] = DBarray.getString(0);

                for (int priceiteraor = 1; priceiteraor < 9; ++ priceiteraor){
                    JSONArray pricedata = dateData.getJSONArray("" + priceiteraor);

                    for (int m = 0; m < pricedata.length(); ++m){
                        newdata.price[priceiteraor] = newdata.price[priceiteraor] + pricedata.getString(m) + "\n";
                    }


                }

                resultlist.add(newdata);

            }








        }  catch (Exception e){
            Log.d("mytag", e.getMessage());
        }



        ////////////////////////////////////////////////////


        TextView province = (TextView) v.findViewById(R.id.Textview_Province);


        province.setText(ProvinceName);

        resuldHolder = (LinearLayout) v.findViewById(R.id.Layout_resultHolder);
        Context mContext = v.getContext();

        for (int i = 0; i < 9; ++i){
            RelativeLayout holder = new RelativeLayout(mContext);

            TextView TextviewPrice = new TextView(mContext);
            TextView TextviewResult = new TextView(mContext);

            TextviewPrice.setTextSize(30);
            TextviewResult.setTextSize(30);
            if (i == 0){
                TextviewPrice.setText("Prize DB");
            } else {
                TextviewPrice.setText("Prize " + i);
            }

            TextviewResult.setText("masjaoihsiao");

            int TextviewPriceID = 1000 + i;
            TextviewPrice.setId(TextviewPriceID);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
            params.addRule(RIGHT_OF, TextviewPriceID);
            params.addRule(END_OF, TextviewPriceID);
            params.leftMargin = 50;
            params.topMargin = 5;
            TextviewResult.setLayoutParams(params);




            holder.addView(TextviewPrice);
            holder.addView(TextviewResult);

            LabelList.add(TextviewResult);

            resuldHolder.addView(holder);

        }



        String[] DateList = new String[resultlist.size()];
        for (int k = 0; k < resultlist.size(); ++k){
            DateList[k] = resultlist.get(k).date;
        }

        final Spinner ProvinceSpinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(),
                android.R.layout.simple_spinner_item, DateList);
        ProvinceSpinner.setAdapter(adapter);

        /////////////////////////////////////

        ProvinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {


                try{
                    setTable(resultlist.get((int)ProvinceSpinner.getSelectedItemId()));
                }catch (Exception e){
                    Log.d("mytag", e.getMessage());
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });










        return v;
    }

    private  void setTable(resultdata input){
        for (int i = 0; i < 9; ++i){
            LabelList.get(i).setText(input.price[i]);
        }
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

class resultdata{
    resultdata(String pdate){
        date = pdate;
        price = new String[9];
        for (int i = 0; i < price.length; ++i){
            price[i] = "";
        }
    }
    public String date;
    public String[] price;
}