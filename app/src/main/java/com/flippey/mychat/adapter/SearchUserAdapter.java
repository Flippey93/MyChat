package com.flippey.mychat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.flippey.mychat.R;
import com.flippey.mychat.entity.User;

import java.util.List;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/9 23:20
 */
public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.MyViewHolder> {

    private List<User> mData;
    private List<String> mContacts;

    public SearchUserAdapter(List<User> data,List<String> contacts) {
        this.mData = data;
        this.mContacts = contacts;
    }
    @Override
    public SearchUserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_searchlist,
                parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchUserAdapter.MyViewHolder holder, int position) {
        final User user = mData.get(position);
        holder.username.setText(user.getUsername());
        holder.time.setText(user.getCreatedAt());
        if (mContacts.contains(user.getUsername())) {
            holder.add.setText("已经是好友啦");
            holder.add.setEnabled(false);
        } else {
            holder.add.setText("添加");
            holder.add.setEnabled(true);
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemButtonClick(user.getUsername());
                    }
                }
            });
        }
    }

    public interface OnItemButtonClickListener{
        public void onItemButtonClick(String username);
    }

    private OnItemButtonClickListener onItemClickListener;
    public void setOnItemButtonClickListener(OnItemButtonClickListener listener){
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView username;
        private TextView time;
        private Button add;
        public MyViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.tv_username);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            add = (Button) itemView.findViewById(R.id.btn_add);
        }
    }
}
