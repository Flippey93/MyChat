package com.flippey.mychat.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flippey.mychat.R;
import com.flippey.mychat.adapter.SearchUserAdapter;
import com.flippey.mychat.entity.User;
import com.flippey.mychat.utils.DBUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/9 22:50
 */
public class AddFriendActivity extends BaseActivity {

    private EditText mName;
    private LinearLayout mLlNoData;
    private RecyclerView mRecyclerView;
    private ArrayList<User> mUserList = new ArrayList<>();
    private SearchUserAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addfriend;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.header_right).setVisibility(View.GONE);
        findViewById(R.id.header_left).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.header_title);
        title.setText(R.string.addFriend);
        mName = (EditText) findViewById(R.id.add_et_name);
        findViewById(R.id.add_iv_search).setOnClickListener(this);
        mLlNoData = (LinearLayout) findViewById(R.id.nodata_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String currentUser = EMClient.getInstance().getCurrentUser();
        ArrayList<String> contacts = DBUtil.getContacts(currentUser, this);
        mAdapter = new SearchUserAdapter(mUserList, contacts);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemButtonClickListener(new SearchUserAdapter.OnItemButtonClickListener() {
            @Override
            public void onItemButtonClick(final String username) {
                //添加好友
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().addContact(username, "炮友");
                            showToast("添加成功");
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            showToast("添加失败:"+e);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left:
                finish();
                break;
            case R.id.add_iv_search:
                searchUserName();
                break;
        }
    }

    private void searchUserName() {
        String trim = mName.getText().toString().trim();
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereStartsWith("username", trim);
        query.addWhereNotEqualTo("username", trim);
        query.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                mUserList.clear();
                mUserList.addAll(list);
                if (list == null || list.size() == 0) {
                    //隐藏RecyclerView
                    mRecyclerView.setVisibility(View.GONE);
                    mLlNoData.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mLlNoData.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int i, String s) {
                mRecyclerView.setVisibility(View.GONE);
                mLlNoData.setVisibility(View.VISIBLE);
            }
        });
    }
}
