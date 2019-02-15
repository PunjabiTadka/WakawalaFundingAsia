package com.wakawala.baseclasses;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wakawala.R;
import com.wakawala.util.Util;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

public abstract class BaseActivity extends AppCompatActivity {

    //public static final String TAG = BaseActivity.class.getSimpleName();
    protected ViewDataBinding binding;
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    protected void setView(int layoutResId) {
        binding = DataBindingUtil.setContentView(this, layoutResId);
        try {
            initUI();
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.toString());
        }
    }

    protected abstract void initUI();

    protected abstract void loadData();

    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    protected <T extends ViewDataBinding> T getBinding() {
        return (T) binding;
    }

    protected void changeToolbarTitle(Toolbar toolbar, String title) {
        ((TextView) toolbar.findViewById(R.id.tv_toolbar)).setText(title);
    }

    protected void setToolbar(Toolbar toolbar, String title, boolean isBackEnabled) {
        // setToolbarTextColor(R.select_type_text_color.textColor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(null);
        ((TextView) toolbar.findViewById(R.id.tv_toolbar)).setText(title);
        if (isBackEnabled) {
            toolbar.setNavigationIcon(R.drawable.ic_left_icon);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_icon);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    protected void setToolbar(Toolbar toolbar, String title, boolean isBackEnabled, int backDrawable) {
        // setToolbarTextColor(R.select_type_text_color.textColor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(null);
        ((TextView) toolbar.findViewById(R.id.tv_toolbar)).setText(title);
        if (isBackEnabled) {
            toolbar.setNavigationIcon(backDrawable);
            getSupportActionBar().setHomeAsUpIndicator(backDrawable);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    /**
     * @param layout      Layout ID
     * @param fragment    Fragment
     * @param isBaskStack Boolean True or false_img
     * @param isAnimation required animations or not
     * @param TAG         Fragment Tag
     */

    public void changeFragment(int layout, Fragment fragment, boolean isBaskStack, boolean isAnimation, String TAG) {
        //hide keyboard when fragment change
        Util.hideKeyboard(this);
        if (isAnimation) {
            if (!isBaskStack) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right).replace(layout, fragment, TAG).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right).replace(layout, fragment, TAG).addToBackStack(null).commit();
            }
        } else {
            if (!isBaskStack) {
                getSupportFragmentManager().beginTransaction().replace(layout, fragment, TAG).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(layout, fragment, TAG).addToBackStack(null).commit();
            }
        }
    }

}
