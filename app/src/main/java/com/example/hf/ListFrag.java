package com.example.hf;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFrag extends Fragment {


    private ListView listview;
    private ArrayList<String> food_list = new ArrayList<String>();
    private FrameLayout frameLayout;
    dataBaseHelper dbHelper;
    SQLiteDatabase db;
    private Cursor cursor;
    protected static final String ACTIVITY_NAME = "ListFrag";
    private ChatAdapter messageAdapter;






    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    private String mParam1;
//    private String mParam2;

    public ListFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFrag.
     */
    public static ListFrag newInstance(String param1, String param2) {
        ListFrag fragment = new ListFrag();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        dbHelper = new dataBaseHelper(getActivity());

        db = dbHelper.getWritableDatabase();

        cursor = db.rawQuery("SELECT message FROM tableofMsg", null);
        messageAdapter =new ChatAdapter(getActivity());

        listview = view.findViewById(R.id.list_view);


        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString( cursor.getColumnIndex( dataBaseHelper.KEY_MESSAGE) ) );
            Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount() );

            food_list.add(cursor.getString(cursor.getColumnIndex(dataBaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();


            for(int j =0; j <cursor.getColumnCount(); j++){
                Log.i(ACTIVITY_NAME, "Name of each column in Cursor is = " +
                        cursor.getColumnName(j));



            }

        }
        cursor.close();


        listview.setAdapter (messageAdapter);
        return view;
    }

    public class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context context) {
            super(context, 0);
        }

        public int getCount(){

            return food_list.size();
        }

        public String getItem(int position){
            Log.e("Prabhjot", food_list.get(position));
            return food_list.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ListFrag.this.getLayoutInflater();
            TextView message = null;
            View result = null;

            if(position%2==0){
                result = inflater.inflate(R.layout.cal_row,null);
                message = result.findViewById(R.id.calTextView);

            }
            else{
                result = inflater.inflate(R.layout.cal_row,null);
                message = result.findViewById(R.id.calTextView);

            }


            message.setText(getItem(position));
            return result;

        }//C:\Users\prab9\AndroidStudioProjects\AndroidAssignments

    }

}