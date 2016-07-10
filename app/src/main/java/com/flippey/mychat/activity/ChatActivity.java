package com.flippey.mychat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.flippey.mychat.R;
import com.flippey.mychat.adapter.ChatAdapter;
import com.flippey.mychat.listener.MyMsgListener;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/10 19:05
 */
public class ChatActivity extends BaseActivity implements TextWatcher, TextView
        .OnEditorActionListener {

    private TextView mHeaderTitle;
    private RecyclerView mRecyclerView;
    private Button mSend;
    private EditText mInput;
    private List<EMMessage> mMessages = new ArrayList<>();
    private ChatAdapter mChatAdapter;
    private MyMsgListener mMsgListener;
    private String mContact;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        findViewById(R.id.header_left).setOnClickListener(this);
        mHeaderTitle = (TextView) findViewById(R.id.header_title);
        findViewById(R.id.header_right).setVisibility(View.GONE);
        mHeaderTitle.setText(R.string.chatWith);
        mRecyclerView = (RecyclerView) findViewById(R.id.chat_recyclerview);
        mChatAdapter = new ChatAdapter(mMessages);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mChatAdapter);
        mSend = (Button) findViewById(R.id.chat_btn_send);
        mSend.setOnClickListener(this);
        mSend.setEnabled(false);
        mInput = (EditText) findViewById(R.id.chat_et_input);
        mInput.addTextChangedListener(this);
        mInput.setOnEditorActionListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mContact = intent.getStringExtra(BaseActivity.SP_KEY_USERNAME);
        if (TextUtils.isEmpty(mContact)) {
            showToast("聊天对象迷失在了五次元！！");
            finish();
            return;
        }
        mHeaderTitle.setText(getString(R.string.chatWith).replace("%%", mContact));
        //获取聊天记录
        EMConversation conversation =
                EMClient.getInstance().chatManager().getConversation(mContact);
        if (conversation != null) {
            List<EMMessage> allMessages = conversation.getAllMessages();
            mMessages.clear();
            mMessages.addAll(allMessages);
            mChatAdapter.notifyDataSetChanged();
            mRecyclerView.smoothScrollToPosition(mMessages.size() - 1);
            //将消息标记为已读
            conversation.markAllMessagesAsRead();
        }
        //接受别人发来的消息
        initMsgListener();
    }

    private void initMsgListener() {
        mMsgListener = new MyMsgListener(){
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                updateConversation();
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(mMsgListener);
    }
    //更新会话
    private void updateConversation() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager()
                        .getConversation(mContact);
                conversation.markAllMessagesAsRead();
                if (conversation != null) {
                    List<EMMessage> allMessages = conversation.getAllMessages();
                    mMessages.clear();
                    mMessages.addAll(allMessages);
                    mChatAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(mMessages.size() - 1);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left:
                finish();
                break;
            case R.id.chat_btn_send:
                sendMsg();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString())) {
            mSend.setEnabled(false);
        } else {
            mSend.setEnabled(true);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.chat_et_input && actionId == EditorInfo.IME_ACTION_SEND) {
            sendMsg();
        }
        return true;
    }

    private void sendMsg() {
        String msg = mInput.getText().toString().trim();
        if (TextUtils.isEmpty(msg)) {
            showToast("内容不能为空");
            return;
        }
        //清空编辑框
        mInput.getText().clear();
        EMMessage emMessage = EMMessage.createTxtSendMessage(msg, mContact);
        //添加消息发送监听
        emMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                updateConversation();
            }

            @Override
            public void onError(int i, String s) {
                showToast("消息发送失败：" + s);
                updateConversation();
            }

            @Override
            public void onProgress(int i, String s) {
                updateConversation();
            }
        });

        //发送消息并将消息保存到本地数据库
        EMClient.getInstance().chatManager().sendMessage(emMessage);
        mMessages.add(emMessage);
        mChatAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(mMessages.size() - 1);
    }
}
