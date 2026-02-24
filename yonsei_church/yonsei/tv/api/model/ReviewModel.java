package yonsei_church.yonsei.tv.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import yonsei_church.yonsei.tv.api.vo.VideoListItem;

public class ReviewModel {
    @SerializedName("items")
    private List<VideoListItem> items;

    public List<VideoListItem> getItems() {
        return this.items;
    }
}
