package com.flippey.mychat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.flippey.mychat.R;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/8 20:32
 */
public class ContactListview extends RelativeLayout{

    private ListView mLv;
    public ContactListview(Context context) {
        this(context,null);
    }

    public ContactListview(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ContactListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData();
    }

    private void initData() {
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_contact, this, true);
        mLv = (ListView) view.findViewById(R.id.contact_lv);
    }

    public void setAdapter(ListAdapter adapter) {
        mLv.setAdapter(adapter);
    }


    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mLv.setOnItemClickListener(onItemClickListener);
    }

    public void setOnLongItemClickListener(AdapterView.OnItemLongClickListener
                                                   onItemLongClickListener) {
       mLv.setOnItemLongClickListener(onItemLongClickListener);
    }
}
