package yonsei_church.yonsei.tv.api.model;

import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ObjectAdapter;

public class CustomListRow extends ListRow {
    private int mNumRows = 1;

    public CustomListRow(HeaderItem headerItem, ObjectAdapter objectAdapter) {
        super(headerItem, objectAdapter);
    }

    public CustomListRow(long j, HeaderItem headerItem, ObjectAdapter objectAdapter) {
        super(j, headerItem, objectAdapter);
    }

    public CustomListRow(ObjectAdapter objectAdapter) {
        super(objectAdapter);
    }

    public void setNumRows(int i) {
        this.mNumRows = i;
    }

    public int getNumRows() {
        return this.mNumRows;
    }
}
