package com.example.zl447.tohapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import base.BaseActivity;
import base.BaseFragment;
import fragment.LikeFragment;
import fragment.TodayInHistoryFragment;

import butterknife.BindView;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.fl_main)
    FrameLayout mFlMain;
    @BindView(R.id.navigation)
    NavigationView mNavigation;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawer;

    private ActionBarDrawerToggle mDrawerToggle;

    private TodayInHistoryFragment mTodayFragment;
    private LikeFragment mLikeFragment;
    private BaseFragment mCurrentFragmet;

    private MenuItem mCurrentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

        initData();

        addListener();
    }

    private void initUI() {
        mToolBar.setTitle("历史上的今天");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolBar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawer.addDrawerListener(mDrawerToggle);
    }

    private void initData() {
        mTodayFragment = new TodayInHistoryFragment();
        mLikeFragment = new LikeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_main, mTodayFragment)
                .commit();
        mCurrentFragmet = mTodayFragment;
        mCurrentItem = mNavigation.getMenu().findItem(R.id.drawer_todayInHistory);
        mCurrentItem.setChecked(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void addListener() {
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mCurrentItem.setChecked(false);
                mCurrentItem = item;
                switch (item.getItemId()) {
                    case R.id.drawer_todayInHistory:
                        switchFragment(mTodayFragment);
                        mCurrentFragmet = mTodayFragment;
                        mToolBar.setTitle("历史上的今天");
                        break;
                    case R.id.drawer_like:
                        switchFragment(mLikeFragment);
                        mCurrentFragmet = mLikeFragment;
                        mToolBar.setTitle("喜欢");
                        break;

                }
                item.setChecked(true);
                mDrawer.closeDrawers();
                return true;
            }
        });
    }

    private void switchFragment(BaseFragment to) {
        if (mCurrentFragmet != null) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (to.isAdded()) {
                transaction.hide(mCurrentFragmet).show(to).commit();
            } else {
                transaction.hide(mCurrentFragmet).add(R.id.fl_main, to).commit();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mNavigation.isShown()) {
            mDrawer.closeDrawers();
        } else {
            showExit();
        }
    }

    /**
     * 退出
     */
    private void showExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("您确定要退出吗？")
                .setCancelable(true)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishAll();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .setCanceledOnTouchOutside(false);
        builder.show();
    }
}
