package main.com.ngrewards.fonttypeclasses;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by technorizen on 14/6/18.
 */

public class LightEditText extends EditText {

    public LightEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LightEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LightEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            // font_style = Typeface.createFromAsset(NearByAct.this.getAssets(), "sftext.otf");
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "raleway_light.ttf");
            setTypeface(tf);
        }
    }

}