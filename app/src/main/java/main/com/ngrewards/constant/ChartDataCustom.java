package main.com.ngrewards.constant;

import android.app.Application;
import android.graphics.Path;
import android.graphics.Region;

import com.numetriclabz.numandroidcharts.ChartData;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by technorizen on 26/10/18.
 */

public class ChartDataCustom extends Application implements Serializable {

    public static final String LineChart = "LineChart";
    public static final String BarChart = "BarChart";
    private static final long serialVersionUID = 1L;
    private final Path mPath = new Path();
    private final Region mRegion = new Region();
    List<ChartData> list;
    private int y_values;
    private Float x_values, size, left, top, right, bottom, data, mValue, highest_value;
    private Float lowest_value, opening, closing;
    private String cordinate, pieLabel, sectorLabel, chartName, labels, pyramidLabel, legends;
    private int pyramid_value;
    private JSONObject radarData;
    private Float[] y_list;

    public ChartDataCustom(JSONObject data) {
        this.radarData = data;
    }

    public ChartDataCustom() {
    }

    public ChartDataCustom(int y_values, Float x_values) {
        this.y_values = y_values;
        this.x_values = x_values;
    }

    public ChartDataCustom(int y_values, Float x_values, Float size) {
        this.y_values = y_values;
        this.x_values = x_values;
        this.size = size;
    }

    protected ChartDataCustom(Float left, Float top, Float right, Float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    protected ChartDataCustom(int y_axis, Float x_axis, Float size, String cordinate) {
        this.y_values = y_axis;
        this.x_values = x_axis;
        this.size = size;
        this.cordinate = cordinate;
    }

    public ChartDataCustom(String label, Float val) {
        this.data = val;
        this.pieLabel = label;
    }

    protected ChartDataCustom(int y_axis, Float x_axis, String cordinate) {
        this.y_values = y_axis;
        this.x_values = x_axis;
        this.cordinate = cordinate;

    }

    public ChartDataCustom(List<ChartData> list) {
        this.list = list;
    }

    public ChartDataCustom(int y_axis, String x_axis) {
        this.y_values = y_axis;
        this.labels = x_axis;

    }

    public ChartDataCustom(int y_axis, String x_axis, float top) {
        this.y_values = y_axis;
        this.labels = x_axis;
        this.highest_value = highest_value;
    }

    public ChartDataCustom(float y_axis, String x_axis, String dd) {
        this.y_values = (int) y_axis;
        this.labels = x_axis;
    }

    public ChartDataCustom(Float x_values, Float highest_value, Float lowest_value, Float opening, Float closing) {
        this.x_values = x_values;
        this.highest_value = highest_value;
        this.lowest_value = lowest_value;
        this.opening = opening;
        this.closing = closing;
    }

    public ChartDataCustom(List<ChartData> list, String chartName) {
        this.list = list;
        this.chartName = chartName;
    }

    public ChartDataCustom(Float val) {

        this.data = val;
    }

    public ChartDataCustom(String label, int value) {
        this.pyramidLabel = label;
        this.pyramid_value = value;
    }

    public ChartDataCustom(Float[] y_axis, String legends) {
        this.legends = legends;
        this.y_list = y_axis;
    }

    public ChartDataCustom(String labels) {
        this.labels = labels;
    }


    public String getPyramidLabel() {
        return pyramidLabel;
    }

    public void setPyramidLabel(String label) {
        this.pyramidLabel = label;
    }

    public int getPyramid_value() {
        return pyramid_value;
    }

    public void setPyramid_value(int value) {
        this.pyramid_value = value;
    }

    public int getY_values() {
        return y_values;
    }

    public void setY_values(int y_values) {
        this.y_values = y_values;
    }

    public Float getX_values() {
        return x_values;
    }

    public void setX_values(Float x_values) {
        this.x_values = x_values;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public Float getLeft() {
        return left;
    }

    public void setLeft(Float left) {
        this.left = left;
    }

    public Float getTop() {
        return top;
    }

    public void setTop(Float top) {
        this.top = top;
    }

    public Float getRight() {
        return right;
    }

    public void setRight(Float right) {
        this.right = right;
    }

    public Float getBottom() {
        return bottom;
    }

    public void setBottom(Float bottom) {
        this.bottom = bottom;
    }

    public String getPieLabel() {
        return this.pieLabel;
    }

    public Float getValue() {
        return this.data;
    }

    public void setValue(Float data) {
        this.data = data;
    }

    public String getCordinate() {
        return this.cordinate;
    }

    public void setCordinate(String cordinate) {
        this.cordinate = cordinate;
    }

    public Path getPath() {
        return mPath;
    }

    public Region getRegion() {
        return mRegion;
    }

    public float getSectorValue() {
        return mValue;
    }

    public void setSectorValue(float value) {
        mValue = value;
    }

    public JSONObject getRadarData() {
        return this.radarData;
    }

    public Float getHighest_value() {
        return highest_value;
    }

    public void setHighest_value(float highest_value) {
        this.highest_value = highest_value;
    }

    public Float getLowest_value() {
        return lowest_value;
    }

    public void setLowest_value(float lowest_value) {
        this.lowest_value = lowest_value;
    }

    public Float getOpening() {
        return opening;
    }

    public void setOpening(float opening) {
        this.opening = opening;
    }

    public Float getClosing() {
        return closing;
    }

    public void setClosing(Float closing) {
        this.closing = closing;
    }

    public List<ChartData> getList() {
        return list;
    }

    public void setList(List<ChartData> list) {
        this.list = list;
    }

    public String getSectorLabel() {
        return sectorLabel;
    }

    public void setSectorLabel(String value) {
        sectorLabel = value;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getLabels() {
        if (labels == null) {
            labels = cordinate;
        }
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public Float[] getY_List() {
        return y_list;
    }

    public void setY_List(Float[] y_list) {
        this.y_list = y_list;
    }

    public String getLegends() {
        return legends;
    }

    public void setLegends(String legends) {
        this.legends = legends;
    }
}
