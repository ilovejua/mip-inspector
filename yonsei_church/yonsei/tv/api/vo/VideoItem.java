package yonsei_church.yonsei.tv.api.vo;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class VideoItem implements Serializable {
    @SerializedName("date")
    private String date;
    @SerializedName("nextseq")
    private String nextseq;
    @SerializedName("nexttitle")
    private String nexttitle;
    @SerializedName("nexturl")
    private String nexturl;
    @SerializedName("seq")
    private String seq;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;

    public String getThumbnail() {
        return this.thumbnail;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDate() {
        return this.date;
    }

    public String getSeq() {
        return this.seq;
    }

    public String getUrl() {
        return this.url;
    }

    public String getNextseq() {
        return this.nextseq;
    }

    public String getNexturl() {
        return this.nexturl;
    }

    public String getNexttitle() {
        return this.nexttitle;
    }
}
