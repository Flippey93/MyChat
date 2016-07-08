package com.flippey.mychat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flippey.mychat.R;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/8 15:08
 */
public class ConversationFragment extends BaseFragment {
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversation, container, false);
    }

    @Override
    protected void initData(View view) {

    }

    @Override
    protected void initHeader(ImageView left, TextView title, ImageView right) {
        left.setVisibility(View.GONE);
        title.setText(R.string.conversation_title);
        right.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

    }
}
