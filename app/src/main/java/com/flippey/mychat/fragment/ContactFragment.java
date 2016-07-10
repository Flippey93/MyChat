package com.flippey.mychat.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.flippey.mychat.R;
import com.flippey.mychat.activity.AddFriendActivity;
import com.flippey.mychat.activity.BaseActivity;
import com.flippey.mychat.activity.ChatActivity;
import com.flippey.mychat.adapter.ContactAdapter;
import com.flippey.mychat.utils.DBUtil;
import com.flippey.mychat.utils.ThreadUtil;
import com.flippey.mychat.view.AlertDialog;
import com.flippey.mychat.widget.ContactListview;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/8 15:25
 */
public class ContactFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private ContactListview mContactListview;
    private ArrayList<String> mConatctList = new ArrayList<>();
    private ContactAdapter mContactAdapter;
    private Handler mHandler = new Handler();
    private String mCurrentUser;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    protected void initData(View view) {
        mContactListview = (ContactListview) view.findViewById(R.id.contactListview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeColors(Color.CYAN,Color.GREEN,Color.RED);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        view.findViewById(R.id.header_right).setOnClickListener(this);
        mCurrentUser = EMClient.getInstance().getCurrentUser();
        mContactAdapter = new ContactAdapter(mConatctList);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mContactListview.setAdapter(mContactAdapter);
            }
        });
        initOnClickListener();
        initOnLongClickListener();
        mSwipeRefreshLayout.setRefreshing(true);
        //从本地数据库获取
        loadContactFromDb();
        //从服务器获取好友列表
        loadContactFromServer();
    }

    private void initOnLongClickListener() {
        mContactListview.setOnLongItemClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long
                    id) {
                ContactAdapter adapter = (ContactAdapter) parent.getAdapter();
                final String contact = adapter.getItem(position);
                AlertDialog dialog = new AlertDialog(getContext()).builder();
                dialog.setMsg("确定一定以及肯定跟" + contact + "友尽了吗？？？？");
                dialog.setPositiveButton("再见", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteContacts(contact);
                    }
                });
                dialog.setNegativeButton("算了", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    private void deleteContacts(final String contact) {
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                BaseActivity activity = (BaseActivity) getActivity();
                try {
                    EMClient.getInstance().contactManager().deleteContact(contact);
                    activity.showToast("删除成功！");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    activity.showToast("删除失败："+e);
                }
            }
        });
    }

    private void initOnClickListener() {
        mContactListview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String contact = mConatctList.get(position);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(BaseActivity.SP_KEY_USERNAME, contact);
                startActivity(intent);
            }
        });
    }

    //从本地数据库加载
    private void loadContactFromDb() {
        ArrayList<String> contacts = DBUtil.getContacts(mCurrentUser, getContext());
        mConatctList.clear();
        mConatctList.addAll(contacts);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mContactAdapter.notifyDataSetChanged();
            }
        });
    }
    //从服务器获取
    public void loadContactFromServer() {
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> contactsFromServer = EMClient.getInstance().contactManager()
                            .getAllContactsFromServer();
                    DBUtil.updateTable(getContext(), mCurrentUser, contactsFromServer);
                    loadContactFromDb();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                } finally {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void initHeader(ImageView left, TextView title, ImageView right) {
        title.setText(R.string.contact_title);
        left.setVisibility(View.GONE);
        right.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_right:
                startActivity(new Intent(getActivity(), AddFriendActivity.class));
                break;
        }
    }

    @Override
    public void onRefresh() {
       //从服务器获取
        loadContactFromServer();
    }
}
