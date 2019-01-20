package mvp.contact;


import base.BasePresent;
import bean.SimpleHistory;

import java.util.List;



public class TodayInHistoryContact {
    public interface View {
        void showProgressDialog();

        void dismissProgressDialog();

        void showContent(List<SimpleHistory> result);

        void showFail(String error);
    }

    public interface Present extends BasePresent {
        void getData(int month, int day);
        //void getCurrentDate();
    }
}
