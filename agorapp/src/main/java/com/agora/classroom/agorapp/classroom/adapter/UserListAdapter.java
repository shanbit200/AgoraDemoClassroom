package com.agora.classroom.agorapp.classroom.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.agora.classroom.agorapp.R;
import com.agora.classroom.agorapp.classroom.bean.user.Student;
import com.agora.classroom.agorapp.classroom.bean.user.User;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListAdapter extends BaseQuickAdapter<User, UserListAdapter.ViewHolder> {

    private int localUid;

    public UserListAdapter(int localUid) {
        super(R.layout.item_user_list);
        this.localUid = localUid;
        addChildClickViewIds(R.id.iv_btn_mute_audio, R.id.iv_btn_mute_video);
    }

    @Override
    protected void convert(@NonNull ViewHolder viewHolder, User user) {
        viewHolder.tv_name.setText(user.account);
        if (user.uid == localUid) {
            viewHolder.iv_btn_grant_board.setVisibility(View.VISIBLE);
            viewHolder.iv_btn_mute_audio.setVisibility(View.VISIBLE);
            viewHolder.iv_btn_mute_video.setVisibility(View.VISIBLE);
            viewHolder.iv_btn_grant_board.setSelected(((Student) user).grant_board == 1);
            viewHolder.iv_btn_mute_audio.setSelected(user.audio == 1);
            viewHolder.iv_btn_mute_video.setSelected(user.video == 1);
        } else {
            viewHolder.iv_btn_grant_board.setVisibility(View.GONE);
            viewHolder.iv_btn_mute_video.setVisibility(View.GONE);
            viewHolder.iv_btn_mute_audio.setVisibility(View.GONE);
        }
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.iv_btn_grant_board)
        ImageView iv_btn_grant_board;
        @BindView(R.id.iv_btn_mute_audio)
        ImageView iv_btn_mute_audio;
        @BindView(R.id.iv_btn_mute_video)
        ImageView iv_btn_mute_video;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
