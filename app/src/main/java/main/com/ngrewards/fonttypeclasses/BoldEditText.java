package main.com.ngrewards.fonttypeclasses;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by technorizen on 14/6/18.
 */

public class BoldEditText extends androidx.appcompat.widget.AppCompatEditText {

    public BoldEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BoldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoldEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            // font_style = Typeface.createFromAsset(NearByAct.this.getAssets(), "sftext.otf");
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "raleway_heavy.ttf");
            setTypeface(tf);
        }
    }

}