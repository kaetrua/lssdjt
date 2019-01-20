package mvp.contact;


import base.BasePresent;
import base.BaseView;
import bean.Histroy;
import bean.Picture;

public class HistoryDetailContact {
    public interface View extends BaseView {
       void showData(Histroy<Picture> result);
    }

    public interface Present extends BasePresent {
        void getHistoryData(String eId);
    }
}
