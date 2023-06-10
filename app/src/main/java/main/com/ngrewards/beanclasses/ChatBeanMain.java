package main.com.ngrewards.beanclasses;

import java.util.ArrayList;

/**
 * Created by technorizen on 24/10/18.
 */

public class ChatBeanMain {
    String date;
    ArrayList<ConverSession> converSessionArrayList;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ConverSession> getConverSessionArrayList() {
        return converSessionArrayList;
    }

    public void setConverSessionArrayList(ArrayList<ConverSession> converSessionArrayList) {
        this.converSessionArrayList = converSessionArrayList;
    }
}
