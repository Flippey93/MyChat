package com.flippey.mychat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flippey.mychat.R;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/8 14:51
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化header
        View view = getView();
        ImageView left = (ImageView) view.findViewById(R.id.header_left);
        TextView title = (TextView) view.findViewById(R.id.header_title);
        ImageView right = (ImageView) view.findViewById(R.id.header_right);

        initHeader(left, title, right);
        initData(view);
    }

    protected abstract void initData(View view);

    protected abstract void initHeader(ImageView left, TextView title, ImageView right);


}
