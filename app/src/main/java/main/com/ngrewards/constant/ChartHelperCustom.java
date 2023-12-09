package main.com.ngrewards.constant;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

/**
 * Created by technorizen on 26/10/18.
 */

public class ChartHelperCustom {
    public void createBar(List<ChartDataCustom> list_cordinate, Canvas canvas, Paint paint) {
        for (int i = 0; i < list_cordinate.size(); i++) {

            canvas.drawRect(list_cordinate.get(i).getLeft(), list_cordinate.get(i).getTop(),
                    list_cordinate.get(i).getRight(), list_cordinate.get(i).getBottom(), paint);
        }

    }
}
