package yonsei_church.yonsei.tv.api.vo;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class SubMenuItem implements Serializable {
    @SerializedName("category2")
    private String category2;
    @SerializedName("key2")
    private String key2;

    public String getCategory2() {
        return this.category2;
    }

    public String getKey2() {
        return this.key2;
    }
}
