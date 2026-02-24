package yonsei_church.yonsei.tv.api.vo;

import com.google.gson.annotations.SerializedName;

public class LiveMessageItem {
    @SerializedName("livebr")
    private String livebr;

    public String getLivebr() {
        return this.livebr;
    }
}
