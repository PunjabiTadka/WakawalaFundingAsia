package com.wakawala.ui.actvities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wakawala.R;
import com.wakawala.adapters.CommentAdapter;
import com.wakawala.baseclasses.BaseActivity;
import com.wakawala.databinding.ActivityCommentsBinding;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsActivity extends BaseActivity {

    private ActivityCommentsBinding mBinding;

    public static void newInstance(Context context) {
        context.startActivity(new Intent(context, CommentsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_comments);
    }

    @Override
    protected void initUI() {
        mBinding = getBinding();
        setToolbar(mBinding.includedToolbar.toolbar, getString(R.string.comments), true);
    }

    @Override
    protected void loadData() {
        mBinding.rcvComments.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        mBinding.rcvComments.setAdapter(new CommentAdapter());
    }
}
