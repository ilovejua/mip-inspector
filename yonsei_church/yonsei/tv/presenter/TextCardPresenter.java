package yonsei_church.yonsei.tv.presenter;

import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.TextView;
import yonsei_church.yonsei.tv.R;
import yonsei_church.yonsei.tv.api.vo.SubMenuItem;
import yonsei_church.yonsei.tv.view.card.TextCardView;

public class TextCardPresenter extends Presenter {
    private static int sDefaultTextColor;
    private static int sSelectedTextColor;

    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
    }

    /* access modifiers changed from: private */
    public static void updateCardTextColor(TextCardView textCardView, boolean z) {
        ((TextView) textCardView.findViewById(R.id.extra_text)).setTextColor(z ? sSelectedTextColor : sDefaultTextColor);
    }

    public Presenter.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        sDefaultTextColor = ContextCompat.getColor(viewGroup.getContext(), R.color.default_sub_menu_text);
        sSelectedTextColor = ContextCompat.getColor(viewGroup.getContext(), R.color.selected_sub_menu_text);
        AnonymousClass1 r0 = new TextCardView(viewGroup.getContext()) {
            public void setSelected(boolean z) {
                TextCardPresenter.updateCardTextColor(this, z);
                super.setSelected(z);
            }
        };
        r0.setFocusable(true);
        r0.setFocusableInTouchMode(true);
        updateCardTextColor(r0, false);
        return new Presenter.ViewHolder(r0);
    }

    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object obj) {
        ((TextView) viewHolder.view.findViewById(R.id.extra_text)).setText(((SubMenuItem) obj).getCategory2());
    }
}
