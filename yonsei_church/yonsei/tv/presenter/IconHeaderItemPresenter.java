package yonsei_church.yonsei.tv.presenter;

import android.content.res.Resources;
import android.support.v17.leanback.widget.PageRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.RowHeaderPresenter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import yonsei_church.yonsei.tv.R;
import yonsei_church.yonsei.tv.api.model.IconHeaderItem;

public class IconHeaderItemPresenter extends RowHeaderPresenter {
    private static int sDefaultTextColor;
    private static int sSelectedTextColor;

    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
    }

    private static void updateCardTextColor(View view, boolean z) {
        ((TextView) view.findViewById(R.id.header_label)).setTextColor(z ? sSelectedTextColor : sDefaultTextColor);
    }

    public RowHeaderPresenter.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        sDefaultTextColor = ContextCompat.getColor(viewGroup.getContext(), R.color.default_main_menu_text);
        sSelectedTextColor = ContextCompat.getColor(viewGroup.getContext(), R.color.selected_main_menu_text);
        View inflate = ((LayoutInflater) viewGroup.getContext().getSystemService("layout_inflater")).inflate(R.layout.icon_header_item, (ViewGroup) null);
        inflate.setFocusable(true);
        inflate.setFocusableInTouchMode(true);
        return new RowHeaderPresenter.ViewHolder(inflate);
    }

    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object obj) {
        IconHeaderItem iconHeaderItem = (IconHeaderItem) ((PageRow) obj).getHeaderItem();
        View view = viewHolder.view;
        ImageView imageView = (ImageView) view.findViewById(R.id.header_icon);
        int iconResId = iconHeaderItem.getIconResId();
        if (iconResId != -1) {
            imageView.setImageDrawable(view.getResources().getDrawable(iconResId, (Resources.Theme) null));
        }
        ((TextView) view.findViewById(R.id.header_label)).setText(iconHeaderItem.getName());
    }

    /* access modifiers changed from: protected */
    public void onSelectLevelChanged(RowHeaderPresenter.ViewHolder viewHolder) {
        if (viewHolder.view.hasFocus()) {
            updateCardTextColor(viewHolder.view, true);
        } else {
            updateCardTextColor(viewHolder.view, false);
        }
    }
}
