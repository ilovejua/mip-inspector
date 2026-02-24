package yonsei_church.yonsei.tv.api.vo;

import com.google.gson.annotations.SerializedName;

public class BannerItem {
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("url")
    private String url;

    public String getThumbnail() {
        return this.thumbnail;
    }

    public String getUrl() {
        return this.url;
    }
}
