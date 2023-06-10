package main.com.ngrewards.constant;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by technorizen on 26/10/18.
 */

public class AxisFormatterCustom {
    public List<Float> horizontal_width_list = new ArrayList<>();
    float hor_ratio, horstart, graphheight, width, border, horizontal_width, graphwidth, height;
    float colwidth;
    int label_size, size, ver_ratio;
    Canvas canvas;
    Paint paint, textPaint;
    List<ChartDataCustom> values;
    List<String> hori_labels;
    String description;
    private final List<String> colorList = new ArrayList<>();
    private int legendTop, legendLeft, legendRight, legendBottom;
    private RectF legends;

    // Plot XY Lables
    public void PlotXYLabels(float graphheight, float width,
                             float graphwidth, float height,
                             List<String> hori_labels, int maxY_values, Canvas canvas,
                             float horstart, float border, List<Float> horizontal_width_list,
                             float horizontal_width, Paint paint, List<ChartDataCustom> values,
                             float maxX_values, String description) {

        initializeValues(graphheight, width, graphwidth, height, hori_labels, canvas, horstart, border,
                horizontal_width_list, horizontal_width, paint, values, description);

        paint.setTextAlign(Paint.Align.CENTER);
        size = values.size();

        if (hori_labels != null) {
            size = hori_labels.size();
        }

        //label_size = size - 1;
        label_size = maxY_values;
        ver_ratio = maxY_values + 1;  // Vertical label ratio
        // ver_ratio =  1;  // Vertical label ratio
        paint.setColor(Color.BLACK);

        for (int i = 0; i < ver_ratio; i++) {
            paint.setTextSize(22);
            createY_axis(i);
        }

        if (hori_labels != null) {
            size = hori_labels.size();

        }

        if (values.get(0).getLabels() != null) {

            for (int j = 0; j < size + 1; j++) {

                createX_axis(j);
            }
        } else {

            label_size = size - 1;
            hor_ratio = maxX_values / label_size;

            for (int j = 0; j < size; j++) {

                createX_axis(j);
            }

        }

        if (description != null) {
            Description();
        }

        paint.setTextSize(22);

    }


    protected void createY_axis(int i) {
        float ver_height = ((graphheight / label_size) * i) + border;
        if (i == ver_ratio - 1) {
            canvas.drawLine(horstart, ver_height, width - (border), ver_height, paint); // Draw vertical line
        } else {
            canvas.drawLine(horstart, ver_height, border, ver_height, paint); // Draw vertical line
        }

        paint.setColor(Color.BLACK);
        int Y_labels = (int) ver_ratio - 1 - i;

        // String y_labels = String.format("%.1f", Y_labels*ver_ratio);
        // String y_labels = String.valueOf(Y_labels*ver_ratio);
        String y_labels = String.valueOf(Y_labels);
        paint.setTextAlign(Paint.Align.RIGHT);
        //paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(y_labels, horstart - 15, ver_height - 10, paint);
        paint.setTextAlign(Paint.Align.LEFT);
        //paint.setTextAlign(Paint.Align.CENTER);
    }


    protected void createX_axis(int i) {
        if (values.get(0).getLabels() != null) {

            horizontal_width = ((graphwidth / size) * i) + horstart;

        } else {

            horizontal_width = ((graphwidth / label_size) * i) + horstart;

        }

        horizontal_width_list.add(horizontal_width);
        // canvas.drawLine(horstart, graphheight + border, horstart, border, paint);
        if (i == 0) {
            canvas.drawLine(horizontal_width, graphheight + border, horizontal_width, border, paint);

        } else {
            canvas.drawLine(horizontal_width, graphheight + border, horizontal_width, graphheight + 2 * border, paint);
        }

        if (values.get(0).getLabels() != null) {

            DrawLabelsString(i);

        } else {

            DrawHorizotalLabels(i);

        }

    }

    protected void DrawLabelsString(int i) {
        paint.setColor(Color.BLACK);

        if (i > 1) {

            colwidth = horizontal_width_list.get(1) - horizontal_width_list.get(0);
            canvas.drawText(values.get(i - 1).getLabels(), horizontal_width - colwidth / 1.5f, height - 38, paint);

        } else if (i != 0 && i == 1) {
            canvas.drawText(values.get(i - 1).getLabels(), horizontal_width / 2, height - 38, paint);
        }
    }

    protected void DrawHorizotalLabels(int i) {


        // paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextAlign(Paint.Align.CENTER);
        if (i == 0)
            paint.setTextAlign(Paint.Align.CENTER);
        //paint.setTextAlign(Paint.Align.LEFT);


        String x_values = String.format("%.1f", i * hor_ratio);
        paint.setColor(Color.BLACK);


        if (hori_labels != null) {

            canvas.drawText(hori_labels.get(i), horizontal_width - 10, height - 38, paint);

        } else {

            canvas.drawText(x_values, horizontal_width - 10, height - 38, paint);
        }
    }


    protected void Description() {

        paint.setTextSize(28);
        this.canvas.drawText(description, graphwidth - horstart, height + 50, paint);
    }

    protected void initializeValues(float graphheight, float width,
                                    float graphwidth, float height,
                                    List<String> hori_labels, Canvas canvas,
                                    float horstart, float border, List<Float> horizontal_width_list,
                                    float horizontal_width, Paint paint, List<ChartDataCustom> values, String description) {

        this.graphheight = graphheight;
        this.width = width;
        this.graphwidth = graphwidth;
        this.height = height;
        this.hori_labels = hori_labels;
        this.canvas = canvas;
        this.horstart = horstart;
        this.border = border;
        this.horizontal_width_list = horizontal_width_list;
        this.horizontal_width = horizontal_width;
        this.paint = paint;
        this.values = values;
        this.description = description;
    }

    //  return the maximum y_value
    public int getMaxY_Values(List<ChartDataCustom> values) {

        int largest = Integer.MIN_VALUE;
        for (int i = 0; i < values.size(); i++)
            if (values.get(i).getY_values() > largest)
                largest = values.get(i).getY_values();
        return largest;
    }

    //  return the maximum y_value
    public float getMaxX_Values(List<ChartDataCustom> values) {

        float largest = Integer.MIN_VALUE;
        for (int i = 0; i < values.size(); i++)
            if (values.get(i).getX_values() > largest)
                largest = values.get(i).getX_values();
        return largest;
    }

    // return the minimum value
    public float getMinValues(List<ChartDataCustom> values) {

        float smallest = Integer.MAX_VALUE;
        for (int i = 0; i < values.size(); i++)
            if (values.get(i).getY_values() < smallest)
                smallest = values.get(i).getX_values();
        return smallest;
    }

    public List<String> getColorList() {

        colorList.add("#FF6600");
        colorList.add("#DC143C");
        colorList.add("#40E0D0");
        colorList.add("#A52A2A");
        colorList.add("#9932CC");
        colorList.add("#228B22");
        colorList.add("#FF0000");
        colorList.add("#DAA520");
        colorList.add("#FFA500");
        colorList.add("#A0522D");
        return colorList;
    }

    protected int getLargestSize(List<ChartDataCustom> values) {
        int largest = Integer.MIN_VALUE;
        Log.e("value size", values.size() + "");
        for (int j = 0; j < values.size(); j++) {
            if (values.get(j).getList().get(j).getY_values() > largest) {
                largest = j;
            }
        }
        return largest;
    }

    protected int getSmallestSize(List<ChartDataCustom> values) {
        int smallest = Integer.MAX_VALUE;
        for (int j = 0; j < values.size(); j++) {
            if (values.get(j).getList().size() < smallest) {
                smallest = values.get(j).getList().size();
            }
        }
        return smallest;
    }

    //  return the maximum y_value
    protected float getMultiMaxX_Values(List<ChartDataCustom> values) {

        float largest = Integer.MIN_VALUE;
        for (int j = 0; j < values.size(); j++) {

            for (int i = 0; i < values.get(j).getList().size(); i++) {

                if (values.get(j).getList().get(i).getX_values() > largest)
                    largest = values.get(j).getList().get(i).getX_values();
            }

        }
        return largest;
    }

    //  return the maximum y_value
    protected float getMultiMaxY_Values(List<ChartDataCustom> values) {

        float largest = Integer.MIN_VALUE;
        for (int j = 0; j < values.size(); j++) {

            for (int i = 0; i < values.get(j).getList().size(); i++) {

                if (values.get(j).getList().get(i).getY_values() > largest)
                    largest = values.get(j).getList().get(i).getY_values();
            }

        }
        return largest;
    }

    public void setLegegendPoint(List<String> legends_list, List<Integer> color_code_list) {

        legendTop = (int) height - 10;
        legendLeft = (int) (width * 0.1);
        legendRight = (int) graphwidth;
        legendBottom = (int) height;

        legends = new RectF(legendLeft, legendTop, legendRight, legendBottom);
        Legends(legends_list, color_code_list);

    }

    private void Legends(List<String> legends_list, List<Integer> color_code_list) {
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(22f);

        int left = (int) (graphwidth * 0.1);
        for (int i = 0; i < legends_list.size(); i++) {

            String label = legends_list.get(i);

            float text_width = textPaint.measureText(label, 0, label.length());

            int color = color_code_list.get(i);

            if (!((graphwidth - legendLeft) > (text_width + 60))) {

                legendTop -= 60;
                legendLeft = left;
            }

            addLegends(canvas, color, legendTop, legendLeft, legendRight, legendBottom, label);
            legendLeft += ((int) text_width + 60);
        }
    }

    private void addLegends(Canvas canvas, int color, int top, int left, int right, int bottom, String label) {

        legends = new RectF(left, top, right, bottom);

        Rect r = new Rect(left, top, left + 30, top + 30);
        paint.setColor(Color.parseColor(getColorList().get(color)));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(r, paint);
        canvas.drawText(label, left + 40, top + 20, textPaint);
    }
}

