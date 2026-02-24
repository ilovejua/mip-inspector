package yonsei_church.yonsei.tv.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import yonsei_church.yonsei.tv.api.vo.SubMenuItem;

public class SearchSubMenuModel {
    @SerializedName("category")
    private String category;
    @SerializedName("items")
    private List<SubMenuItem> items;
    @SerializedName("key")
    private String key;
    @SerializedName("livebr")
    private String livebr;

    public String getCategory() {
        return this.category;
    }

    public String getKey() {
        return this.key;
    }

    public String getLivebr() {
        return this.livebr;
    }

    public List<SubMenuItem> getItems() {
        return this.items;
    }
}
