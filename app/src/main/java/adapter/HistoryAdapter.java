package adapter;

import android.content.Context;

import com.example.zl447.tohapp.R;

import base.BaseAdapter;
import base.BaseViewHolder;
import bean.SimpleHistory;

import java.util.List;

public class HistoryAdapter extends BaseAdapter<SimpleHistory> {
    public HistoryAdapter(Context mContext, List<SimpleHistory> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, SimpleHistory t) {
        holder.setText(R.id.tv_time,t.getDate())
                .setText(R.id.tv_title,t.getTitle());

    }
}
