package com.flippey.mychat.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flippey.mychat.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/10 20:04
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    private List<EMMessage> mData;

    public ChatAdapter(List<EMMessage> data) {
        this.mData = data;
    }
    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = mData.get(position);
        return emMessage.direct() == EMMessage.Direct.SEND?0:1;
    }


    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ChatViewHolder holder = null;
        if (viewType == 0) {
            //发送到消息
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                    .item_chat_send, parent, false);
            holder = new ChatViewHolder(view);
        }else if(viewType == 1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                    .item_chat_receive, parent,
                    false);
            holder = new ChatViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        EMMessage emMessage = mData.get(position);
        long msgTime = emMessage.getMsgTime();
        holder.time.setText(DateUtils.getTimestampString(new Date(msgTime)));
        if (position == 0) {
            holder.time.setVisibility(View.VISIBLE);
        } else {
            //获取当前的聊天记录与前一个聊天记录的时间差
            EMMessage emMessage1 = mData.get(position - 1);
            long msgTime1 = emMessage1.getMsgTime();
            if (DateUtils.isCloseEnough(msgTime, msgTime1)) {
                //时间间隔短
                holder.time.setVisibility(View.GONE);
            } else {
                holder.time.setVisibility(View.VISIBLE);
            }
        }
        //判断文本内容
        if (emMessage.getType() == EMMessage.Type.TXT) {
            EMTextMessageBody body = (EMTextMessageBody) emMessage.getBody();
            String message = body.getMessage();
            holder.content.setText(message);
        }
        //发送状态修改
        if (holder.state != null) {
            switch (emMessage.status()) {
                case SUCCESS:
                    holder.state.setVisibility(View.GONE);
                    break;
                case FAIL:
                    holder.state.setVisibility(View.VISIBLE);
                    holder.state.setImageResource(R.drawable.msg_error);
                    break;
                case CREATE:
                case INPROGRESS:
                    holder.state.setVisibility(View.VISIBLE);
                    holder.state.setImageResource(R.drawable.msg_state_anim);
                    AnimationDrawable drawable = (AnimationDrawable) holder.state.getDrawable();
                    if (drawable.isRunning()) {
                        drawable.stop();
                    }
                    drawable.start();
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        private final TextView content;
        private final TextView time;
        private final ImageView state;
        public ChatViewHolder(View itemView) {
            super(itemView);
            content = (TextView)itemView.findViewById(R.id.chat_contect);
            time = (TextView) itemView.findViewById(R.id.chat_time);
            state = (ImageView) itemView.findViewById(R.id.chat_state);
        }
    }

}
