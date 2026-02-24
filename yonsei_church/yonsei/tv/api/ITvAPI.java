package yonsei_church.yonsei.tv.api;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import yonsei_church.yonsei.tv.api.model.HomeModel;
import yonsei_church.yonsei.tv.api.model.ReviewModel;
import yonsei_church.yonsei.tv.api.model.SearchSubMenuModel;
import yonsei_church.yonsei.tv.api.vo.LiveUrlItem;
import yonsei_church.yonsei.tv.api.vo.VideoItem;

public interface ITvAPI {
    @GET("token.php")
    Call<Void> getAppInfo(@Query("token") String str, @Query("os") String str2, @Query("v") String str3, @Query("mseq") String str4);

    @GET("tv_click.php")
    Call<Void> getContentClick(@Query("mt") String str, @Query("seq") int i);

    @GET("tv_list.php")
    Call<List<VideoItem>> getContentList(@Query("mt") String str, @Query("pg") String str2, @Query("key") String str3);

    @GET("tv_home.php")
    Call<List<HomeModel>> getHomeList(@Query("mt") String str, @Query("pg") String str2, @Query("list_qty") int i);

    @GET("liveurl.php")
    Call<LiveUrlItem> getLiveUrl(@Query("userid") String str);

    @GET("getNextVideoInfo.php")
    Call<VideoItem> getNextVideoInfo(@Query("key") String str, @Query("seq") int i);

    @GET("tv_review.php")
    Call<List<ReviewModel>> getReviewList(@Query("mt") String str, @Query("pg") String str2, @Query("list_qty") int i);

    @GET("tv_list.php")
    Call<List<VideoItem>> getSearchList(@Query("mt") String str, @Query("pg") String str2, @Query("key") String str3, @Query("sw") String str4, @Query("list_qty") int i);

    @GET("tv_menu.php")
    Call<List<SearchSubMenuModel>> getSearchSubMenuList(@Query("mt") String str);
}
