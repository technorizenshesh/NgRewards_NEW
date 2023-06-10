package main.com.ngrewards.marchant.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;
import main.com.ngrewards.R;
import main.com.ngrewards.marchant.merchantbottum.MerStatusAct;


/**
 * Created by technorizen on 21/6/18.
 */

public class FragMerSales extends Fragment {

    private Spinner timespinner;
    private ArrayList<String> timesellist;
    private View v;
    private MonthSelAdp monthSelAdp;
    private GraphView graph, monthgraph, yeargraph;
    private SwipeRefreshLayout swipeToRefresh;
    private TextView graph_time;
    LineChartView saleschart;
    private String week_startdate = "", week_last_date = "", current_year_str = "", current_month_str = "", selected_str = "";

    public FragMerSales() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.frag_mer_sales_lay, container, false);
        idint();
        return v;
    }

    private void idint() {
        saleschart = v.findViewById(R.id.saleschart);

        List<PointValue> values = new ArrayList<PointValue>();
        values.add(new PointValue(0, 2));
        values.add(new PointValue(1, 4));
        values.add(new PointValue(2, 3));
        values.add(new PointValue(3, 4));

        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        saleschart.setLineChartData(data);

        SimpleDateFormat format = new SimpleDateFormat("MMMM dd");
        Calendar calendar = Calendar.getInstance();
        // calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setFirstDayOfWeek(calendar.getFirstDayOfWeek());
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        String[] days = new String[7];
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            week_startdate = days[0];
            week_last_date = days[6];
            Log.e("days >", "> " + days[i]);
        }


        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        current_month_str = month_date.format(cal.getTime());
        int mon = month + 1;
        current_year_str = String.valueOf(year);
        // current_month_str = String.valueOf(mon);
        Log.e("Current >>", " >" + year + " " + month);
        timesellist = new ArrayList<>();
        timesellist.add("Weekly");
        timesellist.add("Monthly");
        timesellist.add("Annual");
        graph = v.findViewById(R.id.graph);
        monthgraph = v.findViewById(R.id.monthgraph);
        yeargraph = v.findViewById(R.id.yeargraph);
        swipeToRefresh = v.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                swipeToRefresh.setRefreshing(false);
            }
        });
        graph_time = v.findViewById(R.id.graph_time);
        timespinner = v.findViewById(R.id.timespinner);
        monthSelAdp = new MonthSelAdp(getActivity(), timesellist);
        timespinner.setAdapter(monthSelAdp);
        timespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (timesellist.get(position).equalsIgnoreCase("Weekly")) {
                    graph_time.setText(week_startdate + " - " + week_last_date);
                    graph.removeAllSeries();
                    yeargraph.setVisibility(View.GONE);
                    monthgraph.setVisibility(View.GONE);
                    graph.setVisibility(View.VISIBLE);

                    if (MerStatusAct.salesBeanListArrayList != null && !MerStatusAct.salesBeanListArrayList.isEmpty()) {
                        DataPoint[] dataPoints = new DataPoint[MerStatusAct.salesBeanListArrayList.get(0).getWeek().size()]; // declare an array of DataPoint objects with the same size as your list
                        for (int i = 0; i < MerStatusAct.salesBeanListArrayList.get(0).getWeek().size(); i++) {
                            dataPoints[i] = new DataPoint(i, MerStatusAct.salesBeanListArrayList.get(0).getWeek().get(i).getTotal()); // not sure but I think the second argument should be of type double
                        }
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints); // This one should be obvious right? :)
                        graph.addSeries(series);
                        series.setDrawDataPoints(true);
                        series.setDataPointsRadius(12);
                        series.setThickness(10);
                        graph.refreshDrawableState();
                        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
                        staticLabelsFormatter.setHorizontalLabels(new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"});
                        //   staticLabelsFormatter.setVerticalLabels(new String[] {"Sun","Mon","Tue","Tue","Tue"});
                        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                        graph.getViewport().setScrollable(true); // enables horizontal scrolling
                        graph.getViewport().setScrollableY(true); // enables vertical scrolling
                        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
                        graph.getViewport().setScalableY(true);
                    }


                } else if (timesellist.get(position).equalsIgnoreCase("Monthly")) {
                    monthgraph.removeAllSeries();

                    yeargraph.setVisibility(View.GONE);
                    monthgraph.setVisibility(View.VISIBLE);
                    graph.setVisibility(View.GONE);


                    if (MerStatusAct.salesBeanListArrayList != null && !MerStatusAct.salesBeanListArrayList.isEmpty()) {
                        DataPoint[] dataPoints = new DataPoint[MerStatusAct.salesBeanListArrayList.get(0).getMonth().size()]; // declare an array of DataPoint objects with the same size as your list
                        for (int i = 0; i < MerStatusAct.salesBeanListArrayList.get(0).getMonth().size(); i++) {
                            dataPoints[i] = new DataPoint(i, MerStatusAct.salesBeanListArrayList.get(0).getMonth().get(i).getTotal()); // not sure but I think the second argument should be of type double
                        }
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints); // This one should be obvious right? :)
                        monthgraph.addSeries(series);
                        series.setDrawDataPoints(true);
                        series.setDataPointsRadius(12);
                        series.setThickness(10);
                        monthgraph.refreshDrawableState();
                        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(monthgraph);
                        staticLabelsFormatter.setHorizontalLabels(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"});
                        //   staticLabelsFormatter.setVerticalLabels(new String[] {"Sun","Mon","Tue","Tue","Tue"});
                        monthgraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                        monthgraph.getGridLabelRenderer().setTextSize(20f);
                        monthgraph.getViewport().setScrollable(true); // enables horizontal scrolling
                        monthgraph.getViewport().setScrollableY(true); // enables vertical scrolling
                        monthgraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
                        monthgraph.getViewport().setScalableY(true);
                        monthgraph.setTitleTextSize(10);
                    }


                    graph_time.setText("" + current_month_str);
                } else if (timesellist.get(position).equalsIgnoreCase("Annual")) {
                    graph_time.setText("" + current_year_str);
                    yeargraph.setVisibility(View.VISIBLE);
                    monthgraph.setVisibility(View.GONE);
                    graph.setVisibility(View.GONE);

                    yeargraph.removeAllSeries();
                    if (MerStatusAct.salesBeanListArrayList != null && !MerStatusAct.salesBeanListArrayList.isEmpty()) {
                        DataPoint[] dataPointsyear = new DataPoint[MerStatusAct.salesBeanListArrayList.get(0).getYear().size()]; // declare an array of DataPoint objects with the same size as your list
                        for (int i = 0; i < MerStatusAct.salesBeanListArrayList.get(0).getYear().size(); i++) {
                            dataPointsyear[i] = new DataPoint(i, MerStatusAct.salesBeanListArrayList.get(0).getYear().get(i).getTotal()); // not sure but I think the second argument should be of type double
                            Log.e("DATA YESR >>", " >> " + dataPointsyear[i] + "  ,,  " + MerStatusAct.salesBeanListArrayList.get(0).getYear().get(i).getTotal());
                        }

                        String[] namesArr = new String[MerStatusAct.salesBeanListArrayList.get(0).getYear().size()];
                        for (int i = 0; i < MerStatusAct.salesBeanListArrayList.get(0).getYear().size(); i++) {
                            namesArr[i] = MerStatusAct.salesBeanListArrayList.get(0).getYear().get(i).getYearName();
                        }


                        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPointsyear); // This one should be obvious right? :)
                        yeargraph.addSeries(series);
                        series.setDrawDataPoints(true);
                        series.setDataPointsRadius(12);
                        series.setThickness(10);
                        yeargraph.refreshDrawableState();
                        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(yeargraph);
                        staticLabelsFormatter.setHorizontalLabels(namesArr);
                        //staticLabelsFormatter.setHorizontalLabels(new String[] {"Sun","Mon","Tue","Tue","Tue"});
                        yeargraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                        yeargraph.getViewport().setScrollable(true); // enables horizontal scrolling
                        yeargraph.getViewport().setScrollableY(true); // enables vertical scrolling
                        yeargraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
                        yeargraph.getViewport().setScalableY(true);
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public static Date getWeekStartDate() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        return calendar.getTime();
    }

    public static Date getWeekEndDate() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                graph.removeAllSeries();
                if (MerStatusAct.salesBeanListArrayList != null && !MerStatusAct.salesBeanListArrayList.isEmpty()) {
                    DataPoint[] dataPoints = new DataPoint[MerStatusAct.salesBeanListArrayList.get(0).getWeek().size()]; // declare an array of DataPoint objects with the same size as your list
                    for (int i = 0; i < MerStatusAct.salesBeanListArrayList.get(0).getWeek().size(); i++) {
                        dataPoints[i] = new DataPoint(i, MerStatusAct.salesBeanListArrayList.get(0).getWeek().get(i).getTotal()); // not sure but I think the second argument should be of type double
                    }
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints); // This one should be obvious right? :)
                    graph.addSeries(series);
                    series.setDrawDataPoints(true);
                    series.setDataPointsRadius(12);
                    series.setThickness(10);
                    graph.refreshDrawableState();
                    StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
                    staticLabelsFormatter.setHorizontalLabels(new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"});
                    //   staticLabelsFormatter.setVerticalLabels(new String[] {"Sun","Mon","Tue","Tue","Tue"});
                    graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                    graph.getViewport().setScrollable(true); // enables horizontal scrolling
                    graph.getViewport().setScrollableY(true); // enables vertical scrolling
                    graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
                    graph.getViewport().setScalableY(true);
                }


            } catch (Exception e) {

            }


        }
    };

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("SalesEarningData"));
    }

    public class MonthSelAdp extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        private final ArrayList<String> timesellist;

        public MonthSelAdp(Context applicationContext, ArrayList<String> timesellist) {
            this.context = applicationContext;
            this.timesellist = timesellist;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return timesellist == null ? 0 : timesellist.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.spinner_layout, null);
            TextView names = (TextView) view.findViewById(R.id.name_tv);
            ImageView country_flag = (ImageView) view.findViewById(R.id.country_flag);
            //  TextView countryname = (TextView) view.findViewById(R.id.countryname);
            selected_str = timesellist.get(i);
            names.setText(timesellist.get(i));
            return view;
        }
    }

}
//graph view tutorial=======
//https://www.numetriclabz.com/android-line-graph-using-graphview-library-tutorial/