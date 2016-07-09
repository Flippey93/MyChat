package com.flippey.mychat.adapter;

import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.flippey.mychat.R;
import com.flippey.mychat.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/9 9:43
 */
public class ContactAdapter extends BaseAdapter implements SectionIndexer{
    private List<String> mUserName;
    private SparseIntArray mSparseIntArray = new SparseIntArray();
    private SparseIntArray mSparseIntArray2 = new SparseIntArray();
    public ContactAdapter(List<String> data) {
        this.mUserName = data;
    }


    @Override
    public int getCount() {
        return mUserName.size();
    }

    @Override
    public String getItem(int position) {
        return mUserName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,
                    parent, false);
            holder.mName = (TextView) convertView.findViewById(R.id.contact_lv_name);
            holder.mUpcase = (TextView) convertView.findViewById(R.id.contact_lv_upcase);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String username = mUserName.get(position);
        holder.mName.setText(username);
        //设置分隔条目的字母
        String initial = StringUtil.getInitial(username);
        holder.mUpcase.setText(initial);
        if (position == 0) {
            holder.mUpcase.setVisibility(View.VISIBLE);
        } else {
            String preUsername = mUserName.get(position - 1);
            String preInitial = StringUtil.getInitial(preUsername);
            if (initial.equals(preInitial)) {
                holder.mUpcase.setVisibility(View.GONE);
            } else {
                holder.mUpcase.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }
    //返回统计当前adapter中数据总共可以划分为多少个section
    @Override
    public String[] getSections() {
        ArrayList<String> upcaseNum = new ArrayList<>();
        for (int i = 0; i < mUserName.size(); i++) {
            String name = mUserName.get(i);
            String initial = StringUtil.getInitial(name);
            if (!upcaseNum.contains(initial)) {
                upcaseNum.add(initial);
                //记录upcaseNum和position的关系
                mSparseIntArray.put(upcaseNum.size() - 1, i);
            }
            mSparseIntArray2.put(i, upcaseNum.size() - 1);
        }
        return upcaseNum.toArray(new String[upcaseNum.size()]);
    }
    //传递section的索引返回position
    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSparseIntArray.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return mSparseIntArray2.get(position);
    }

    static class ViewHolder{
        TextView mName;
        TextView mUpcase;
    }
}
