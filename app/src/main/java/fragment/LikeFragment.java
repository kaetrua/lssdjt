package fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zl447.tohapp.HistoryDetailActivity;
import com.example.zl447.tohapp.R;

import adapter.LikeAdapter;
import base.BaseAdapter;
import base.BaseFragment;
import base.HistoryLikeBean;
import common.Constants;
import common.DefaultItemTouchHelpCallback;
import db.RealmHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class LikeFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    private RealmHelper mRealmHelper;
    private LikeAdapter mAdapter;
    private DefaultItemTouchHelpCallback mCallback;
    private List<HistoryLikeBean> mDatas = new ArrayList<>();

    @Override
    protected void initEvents() {
        mSrl.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRealmHelper = new RealmHelper(getActivity());
        mDatas = mRealmHelper.queryAllHistoryLike();
        mAdapter = new LikeAdapter(getActivity(), mDatas, R.layout.item_like);
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter.getItemCount() <= 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        }
        mSrl.setRefreshing(false);

        setItemTouch();

        addListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mSrl.isRefreshing()) {
            mSrl.setRefreshing(true);
        }
        List<HistoryLikeBean> datas = mRealmHelper.queryAllHistoryLike();
        mAdapter.updateData(datas);
        if (mAdapter.getData().size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }

        mSrl.setRefreshing(false);
    }

    private void setItemTouch() {
        mCallback = new DefaultItemTouchHelpCallback(new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int position) {
                // 滑动删除的时候，从数据库、数据源移除，并刷新UI
                mRealmHelper.deleteHistoryLike(mDatas.get(position).geteId());
                mDatas.remove(position);
                mAdapter.notifyItemRemoved(position);
                if (mAdapter.getItemCount() <= 0) {
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public boolean onMove(int srcPosition, int targetPosition) {
                mAdapter.notifyItemMoved(srcPosition, targetPosition);
                return false;
            }
        });
        mCallback.setSwipeEnable(true);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void addListener() {
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), HistoryDetailActivity.class);
                intent.putExtra(Constants.EID, mDatas.get(position).geteId());
                intent.putExtra(Constants.DATE, mDatas.get(position).getDate());
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "shareView");
                startActivity(intent, options.toBundle());
            }
        });
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<HistoryLikeBean> datas = mRealmHelper.queryAllHistoryLike();
                mAdapter.updateData(datas);

                mSrl.setRefreshing(false);
                if (mAdapter.getItemCount() <= 0) {
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_like;
    }

}
