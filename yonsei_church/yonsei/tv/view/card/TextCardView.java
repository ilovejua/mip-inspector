package yonsei_church.yonsei.tv.view.card;

import android.content.Context;
import android.support.v17.leanback.widget.BaseCardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import yonsei_church.yonsei.tv.R;

public class TextCardView extends BaseCardView {
    public TextCardView(Context context) {
        super(context, (AttributeSet) null, R.style.TextCardStyle);
        LayoutInflater.from(getContext()).inflate(R.layout.text_card, this);
        setFocusable(true);
    }
}
