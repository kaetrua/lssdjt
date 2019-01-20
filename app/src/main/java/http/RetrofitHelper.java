package http;


import bean.Histroy;
import bean.Picture;
import bean.SimpleHistory;
import common.Constants;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class RetrofitHelper {

    private static RetrofitHelper mRetrofitHelper;
    private APIService mApiService;
    private APIService mGrilApiService;

    public RetrofitHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://v.juhe.cn/todayOnhistory/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mApiService = retrofit.create(APIService.class);
        Retrofit grilRetofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mGrilApiService = grilRetofit.create(APIService.class);

    }


    public static RetrofitHelper getInstance() {
        if (mRetrofitHelper == null) {
            synchronized (RetrofitHelper.class) {
                if (mRetrofitHelper == null) {
                    mRetrofitHelper = new RetrofitHelper();
                }
            }
        }
        return mRetrofitHelper;
    }


    /**
     * 获取历史列表
     */
    public Observable<HttpResponse<SimpleHistory>> getHistoryList(int month, int date) {
        if (mApiService == null) {
            new RetrofitHelper();
        }
        return mApiService.getHistoryList(Constants.APP_KEY, month + "/" + date);
    }

    /**
     * 获取历史详情
     *
     * @param eid
     */
    public Observable<HttpResponse<Histroy<Picture>>> getHistoryDetail(String eid) {
        if (mApiService == null) {
            new RetrofitHelper();
        }
        return mApiService.getHitoryDetail(Constants.APP_KEY, eid);
    }

}
