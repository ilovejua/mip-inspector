package yonsei_church.yonsei.tv.api.vo;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class VideoListItem {
    @SerializedName("videos")
    private List<VideoItem> videos;

    public List<VideoItem> getVideos() {
        return this.videos;
    }
}
