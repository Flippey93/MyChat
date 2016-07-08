package com.flippey.mychat.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flippey.mychat.R;
import com.flippey.mychat.utils.DialogUtil;
import com.flippey.mychat.view.AlertDialog;
import com.hyphenate.chat.EMClient;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/8 15:27
 */
public class PluginFragment extends BaseFragment {
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plugin, container, false);
    }

    @Override
    protected void initData(View view) {
        Button exit = (Button) view.findViewById(R.id.exit);
        exit.setOnClickListener(this);
        //获取当前登录的用户
        String currentUser = EMClient.getInstance().getCurrentUser();
        if (!TextUtils.isEmpty(currentUser)) {
           exit.setText(getString(R.string.exit_btn_content).replace("%%",currentUser));
        }
    }

    @Override
    protected void initHeader(ImageView left, TextView title, ImageView right) {
        left.setVisibility(View.GONE);
        title.setText(R.string.plugin_title);
        right.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        new AlertDialog(getActivity()).builder().setTitle("退出当前账号")
                .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
                .setPositiveButton("确认退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logOut();
                    }
                }).setNegativeButton("成为达人", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        }).show();}

    private void logOut() {
        ProgressDialog dialog = DialogUtil.makeDialog(getActivity(), "正在注销...");
        dialog.show();

    }
}
