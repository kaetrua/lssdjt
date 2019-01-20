package fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zl447.tohapp.R;
import com.example.zl447.tohapp.CalendarActivity;
import com.example.zl447.tohapp.HistoryDetailActivity;
import adapter.HistoryAdapter;
import base.BaseAdapter;
import base.BaseFragment;
import bean.SimpleHistory;
import common.Constants;
import mvp.contact.TodayInHistoryContact;
import mvp.presenter.TodayInHistoryPresenter;
import utils.CircularAnimUtil;
import utils.ProgressDialogUtil;
import utils.SnackBarUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;



public class TodayInHistoryFragment extends BaseFragment implements TodayInHistoryContact.View {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_time_bar)
    LinearLayout mTimeBar;

    private List<SimpleHistory> mDatas = new ArrayList<>();
    private HistoryAdapter mAdapter;
    private TodayInHistoryPresenter mPresent;
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private Calendar mCalendar;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_today_in_history;
    }

    @Override
    protected void initEvents() {

        init();

        initTime();

        initRecyclerView();

        addListener();
    }

    private void init() {
        mPresent = new TodayInHistoryPresenter(this);

        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });


        EventBus.getDefault().register(this);
    }

    private void initTime() {
        mCalendar = Calendar.getInstance();
        currentYear = mCalendar.get(Calendar.YEAR);
        currentMonth = mCalendar.get(Calendar.MONTH) + 1;
        currentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        setTVTime();


        mPresent.getData(currentMonth, currentDay);


    }


    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new HistoryAdapter(getActivity(), mDatas, R.layout.item_history);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void addListener() {
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), HistoryDetailActivity.class);
                intent.putExtra(Constants.EID, mDatas.get(position).getE_id());
                intent.putExtra(Constants.DATE, mDatas.get(position).getDate());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, "shareView");
                startActivity(intent, options.toBundle());

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mPresent.getData(currentMonth, currentDay);
            }
        });
    }


    @Subscribe
    public void onEventMainThread(CalendarDay date) {
        currentYear = date.getYear();
        currentMonth = date.getMonth() + 1;
        currentDay = date.getDay();

        mSwipeRefreshLayout.setRefreshing(true);
        setTVTime();
        mPresent.getData(currentMonth, currentDay);
    }

    @OnClick({R.id.rl_previous, R.id.rl_next, R.id.floatActionBtn, R.id.tv_time})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_previous:
                setPreVious();
                setTVTime();
                mSwipeRefreshLayout.setRefreshing(true);
                mPresent.getData(currentMonth, currentDay);
                break;
            case R.id.rl_next:
                setNext();
                setTVTime();

                mSwipeRefreshLayout.setRefreshing(true);
                mPresent.getData(currentMonth, currentDay);
                break;
            case R.id.floatActionBtn:
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                CircularAnimUtil.startActivityForResult(getActivity(), intent, Constants.CODE_CALENDAR, v, R.color.colorPrimary);
                break;
            case R.id.tv_time:
                currentYear = mCalendar.get(Calendar.YEAR);
                currentMonth = mCalendar.get(Calendar.MONTH) + 1;
                currentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                setTVTime();

                mSwipeRefreshLayout.setRefreshing(true);
                mPresent.getData(currentMonth, currentDay);
                break;
            default:
                break;
        }

    }

    private void setPreVious() {
        if (currentMonth == 3) {
            currentDay--;
            if (currentDay < 1) {
                if ((currentYear % 4 == 0 && currentYear % 100 != 0) || currentYear % 400 == 0) {
                    //闰年
                    currentDay = 29;
                    currentMonth--;
                } else {
                    //平年
                    currentDay = 28;
                    currentMonth--;
                }
            }
        } else if (currentMonth == 1) {
            currentDay--;
            if (currentDay < 1) {
                currentDay = 31;
                currentMonth = 12;
                currentYear--;
            }
        } else if (currentMonth == 2 || currentMonth == 4 || currentMonth == 6 || currentMonth == 8 || currentMonth == 9 || currentMonth == 11) {
            currentDay--;
            if (currentDay < 1) {
                currentDay = 31;
                currentMonth--;
            }

        } else {
            currentDay--;
            if (currentDay < 1) {
                currentDay = 30;
                currentMonth--;
            }
        }


    }

    private void setNext() {

        if (currentMonth == 2) {
            //二月
            if ((currentYear % 4 == 0 && currentYear % 100 != 0) || currentYear % 400 == 0) {
                //闰年
                currentDay++;
                if (currentDay > 29) {
                    currentDay = 1;
                    currentMonth++;
                }

            } else {
                //平年
                currentDay++;
                if (currentDay > 28) {
                    currentDay = 1;
                    currentMonth++;
                }
            }

        } else if (currentMonth == 1 || currentMonth == 3 || currentMonth == 5 || currentMonth == 6 || currentMonth == 8 || currentMonth == 10) {
            //大月
            currentDay++;
            if (currentDay > 31) {
                currentDay = 1;
                currentMonth++;
            }


        } else if (currentMonth == 12) {
            currentDay++;
            if (currentDay > 31) {
                currentDay = 1;
                currentMonth = 1;
                currentYear++;
            }

        } else {
            //小月
            currentDay++;
            if (currentDay > 30) {
                currentDay = 1;
                currentMonth++;
            }
        }
    }

    public void setTVTime() {
        tvTime.setText(currentYear + "年" + currentMonth + "月" + currentDay + "日");
    }

    @Override
    public void showProgressDialog() {
        ProgressDialogUtil.showDialog(getActivity());
    }

    @Override
    public void dismissProgressDialog() {
        ProgressDialogUtil.dismissDialog();
    }

    @Override
    public void showContent(List<SimpleHistory> result) {

        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        /**
         * 排序
         */
        Collections.sort(result, new Comparator<SimpleHistory>() {
            @Override
            public int compare(SimpleHistory o1, SimpleHistory o2) {
                int eid1 = Integer.parseInt(o1.getE_id());
                int eid2 = Integer.parseInt(o2.getE_id());
                return eid2 - eid1;
            }
        });
        mAdapter.updateData(result);
    }

    @Override
    public void showFail(String error) {

        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        SnackBarUtil.showLong(mRootView, error);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
