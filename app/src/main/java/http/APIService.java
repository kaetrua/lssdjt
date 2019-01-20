package http;



import bean.Histroy;
import bean.Picture;
import bean.SimpleHistory;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface APIService {

    /**
     * 获取历史列表
     */
    @FormUrlEncoded
    @POST("queryEvent.php")
    Observable<HttpResponse<SimpleHistory>> getHistoryList(@Field("key") String key, @Field("date") String date);


    /**
     * 获取历史详情
     */
    @FormUrlEncoded
    @POST("queryDetail.php")
    Observable<HttpResponse<Histroy<Picture>>> getHitoryDetail(@Field("key") String key, @Field("e_id") String e_id);

}
