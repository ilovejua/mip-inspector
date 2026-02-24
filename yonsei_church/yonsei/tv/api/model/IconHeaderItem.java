package yonsei_church.yonsei.tv.api.model;

import android.support.v17.leanback.widget.HeaderItem;

public class IconHeaderItem extends HeaderItem {
    public static final int ICON_NONE = -1;
    private static final String TAG = "IconHeaderItem";
    private int mIconResId;

    public IconHeaderItem(long j, String str, int i) {
        super(j, str);
        this.mIconResId = -1;
        this.mIconResId = i;
    }

    public IconHeaderItem(long j, String str) {
        this(j, str, -1);
    }

    public IconHeaderItem(String str) {
        super(str);
        this.mIconResId = -1;
    }

    public int getIconResId() {
        return this.mIconResId;
    }

    public void setIconResId(int i) {
        this.mIconResId = i;
    }
}
