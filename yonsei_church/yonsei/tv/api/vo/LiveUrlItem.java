package yonsei_church.yonsei.tv.api.vo;

import com.google.gson.annotations.SerializedName;

public class LiveUrlItem {
    @SerializedName("finishTime")
    private String finishTime;
    @SerializedName("livebr")
    private String livebr;
    @SerializedName("liveflag")
    private String liveflag;
    @SerializedName("url")
    private String url;

    public String getUrl() {
        return this.url;
    }

    public String getLivebr() {
        return this.livebr;
    }

    public String getLiveflag() {
        return this.liveflag;
    }

    public String getFinishTime() {
        return this.finishTime;
    }
}
