package yonsei_church.yonsei.tv.presenter;

import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.RowPresenter;
import yonsei_church.yonsei.tv.api.model.CustomListRow;

public class CustomListRowPresenter extends ListRowPresenter {
    /* access modifiers changed from: protected */
    public void onBindRowViewHolder(RowPresenter.ViewHolder viewHolder, Object obj) {
        ((ListRowPresenter.ViewHolder) viewHolder).getGridView().setNumRows(((CustomListRow) obj).getNumRows());
        super.onBindRowViewHolder(viewHolder, obj);
    }

    /* access modifiers changed from: protected */
    public void initializeRowViewHolder(RowPresenter.ViewHolder viewHolder) {
        super.initializeRowViewHolder(viewHolder);
        setShadowEnabled(false);
    }
}
