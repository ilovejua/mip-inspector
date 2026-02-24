package yonsei_church.yonsei.tv.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import yonsei_church.yonsei.tv.api.vo.BannerItem;
import yonsei_church.yonsei.tv.api.vo.VideoListItem;

public class HomeModel {
    @SerializedName("bannerimg")
    private List<BannerItem> bannerimg;
    @SerializedName("category")
    private String category;
    @SerializedName("items")
    private List<VideoListItem> items;
    @SerializedName("key")
    private String key;

    public List<BannerItem> getBannerimg() {
        return this.bannerimg;
    }

    public String getCategory() {
        return this.category;
    }

    public String getKey() {
        return this.key;
    }

    public List<VideoListItem> getItems() {
        return this.items;
    }
}
