package main.com.ngrewards.drawlocation;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by bhupesh on 9/7/2016.
 */
public class WebOperations {

    private final String BaseUrl = "";
    //  private String BaseUrl = "http://technorizen.co.in/";
    private final HttpClient httpClient = new DefaultHttpClient();
    private final String filepath = "ShoparStorage";
    File myInternalFile;
    Context context;
    private String url = null;
    private HttpEntity reqEntity = null;
    private String json = null;
    private String filename = "";

    public WebOperations(Context context) {
        this.context = context;
        myInternalFile = Environment.getExternalStorageDirectory();
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File directory = contextWrapper.getDir(filepath, Context.MODE_PRIVATE);
        myInternalFile = new File(directory, filename);
    }

    public HttpEntity getReqEntity() {
        return reqEntity;
    }

    public void setReqEntity(HttpEntity reqEntity) {
        this.reqEntity = reqEntity;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String doGet() {
        try {
            HttpGet httpGet = new HttpGet(BaseUrl + url);

            HttpResponse response = httpClient.execute(httpGet);

            json = EntityUtils.toString(response.getEntity());
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }

    }

    public String doGet1() {
        try {
            HttpGet httpGet = new HttpGet(url);

            HttpResponse response = httpClient.execute(httpGet);

            json = EntityUtils.toString(response.getEntity());

            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }

    }

    public String doGetMap() {
        try {
            HttpGet httpGet = new HttpGet(url);

            HttpResponse response = httpClient.execute(httpGet);

            json = EntityUtils.toString(response.getEntity());

            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }

    }

    public String doPost() {
        try {

            HttpPost httpPost = new HttpPost(BaseUrl + url);
            httpPost.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(httpPost);

            json = EntityUtils.toString(response.getEntity());

            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }

    }

    public String doPostMap() {
        try {

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(httpPost);

            json = EntityUtils.toString(response.getEntity());

            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }

    }

    public void saveData(String data) {
        File file = new File(context.getFilesDir(), "files");
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            FileOutputStream fOut = context.openFileOutput(filename, Context.MODE_WORLD_READABLE);
            fOut.write(data.getBytes());
            fOut.close();
            System.out.println("data saved");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getData(String fn) {
        try {
            FileInputStream fin = context.openFileInput(fn);
            int c;
            String temp = "";
            while ((c = fin.read()) != -1) {
                temp = temp + (char) c;

            }
            System.out.println("data of " + fn + "   " + temp);
            fin.close();
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
    }
}
