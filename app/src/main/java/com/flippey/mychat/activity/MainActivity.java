package com.flippey.mychat.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flippey.mychat.R;
import com.flippey.mychat.fragment.BaseFragment;
import com.flippey.mychat.fragment.ContactFragment;
import com.flippey.mychat.fragment.ConversationFragment;
import com.flippey.mychat.fragment.PluginFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/8 11:04
 */
public class MainActivity extends BaseActivity{

    private TextView mAllUnRead;
    private ImageView mConversation;
    private ImageView mContact;
    private ImageView mPlugin;
    private List<BaseFragment> mFragmentList = new ArrayList<>();
    private int currentIndex = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mConversation = (ImageView) findViewById(R.id.iv_conversation);
        mContact = (ImageView) findViewById(R.id.iv_contact);
        mPlugin = (ImageView) findViewById(R.id.iv_plugin);
        mAllUnRead = (TextView) findViewById(R.id.tv_all_unread);

        mConversation.setOnClickListener(this);
        mContact.setOnClickListener(this);
        mPlugin.setOnClickListener(this);

        //默认选中消息列表
        mConversation.setSelected(true);
    }

    @Override
    protected void initData() {
        initFragment();
    }

    private void initFragment() {
        mFragmentList.clear();
        ConversationFragment conversationFragment = new ConversationFragment();
        ContactFragment contactFragment = new ContactFragment();
        PluginFragment pluginFragment = new PluginFragment();
        mFragmentList.add(conversationFragment);
        mFragmentList.add(contactFragment);
        mFragmentList.add(pluginFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, conversationFragment,
                "0").commit();
    }

    @Override
    public void onClick(View v) {
        int index = 0;
        mConversation.setSelected(false);
        mContact.setSelected(false);
        mPlugin.setSelected(false);
        switch (v.getId()) {
            case R.id.iv_conversation:
                index = 0;
                mConversation.setSelected(true);
                break;
            case R.id.iv_contact:
                index = 1;
                mContact.setSelected(true);
                break;
            case R.id.iv_plugin:
                index = 2;
                mPlugin.setSelected(true);
                break;
        }
        if (index == currentIndex) {
            return;
        }
        BaseFragment fragment = mFragmentList.get(index);
        //判断当前点击fragment是否已经添加到activity中
        if (fragment.isAdded()) {
            //显示点击到fragment
            showFragment(fragment, mFragmentList.get(currentIndex));
        } else {
            //添加到activity中
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_content, fragment, index + "")
                    .hide(mFragmentList.get(currentIndex))
                    .commit();
        }
        currentIndex = index;
    }

    private void showFragment(BaseFragment showFragment, BaseFragment hideFragment) {
        getSupportFragmentManager().beginTransaction()
                .show(showFragment)
                .hide(hideFragment)
                .commit();

    }
}
