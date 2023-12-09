package main.com.ngrewards.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import main.com.ngrewards.BuildConfig;
import main.com.ngrewards.Interfaces.onDateSetListener;
import main.com.ngrewards.R;
import main.com.ngrewards.constant.MySession;

public class Tools {

    public static void updateResources(Context context, String language) {
        try {
            Log.e("TAG", "updateResources: languagelanguage----- " + language);
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Resources resources = context.getResources();

            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = locale;
            res.updateConfiguration(conf, dm);
            //  AppCompatDelegate.setApplicationLocales(appLocale)
            LocaleHelper.setLocale(context, language);
        } catch (Exception e) {
            Log.e("TAG", "updateResources: " + e.getMessage());
            Log.e("TAG", "updateResources: " + e.getLocalizedMessage());
            Log.e("TAG", "updateResources: " + e.getCause());
        }
    }

    public static void reupdateResources(Context context) {
        try {
            MySession mySession = new MySession(context);
            mySession.getValueOf(MySession.KEY_LANGUAGE);
            Log.e("TAG", "updateResources: languagelanguage-----" +
                    " " + mySession.getValueOf(MySession.KEY_LANGUAGE));
            Locale locale = new Locale(mySession.getValueOf(MySession.KEY_LANGUAGE));
            Locale.setDefault(locale);
            Resources resources = context.getResources();

            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = locale;
            res.updateConfiguration(conf, dm);
            //  AppCompatDelegate.setApplicationLocales(appLocale)
            LocaleHelper.setLocale(context, mySession.getValueOf(MySession.KEY_LANGUAGE));
        } catch (Exception e) {
            Log.e("TAG", "updateResources: " + e.getMessage());
            Log.e("TAG", "updateResources: " + e.getLocalizedMessage());
            Log.e("TAG", "updateResources: " + e.getCause());
        }
    }

    public static File persistImage(Bitmap bitmap, Context cOntext) {
        File filesDir = cOntext.getCacheDir();
        @SuppressLint("SimpleDateFormat")
        File imageFile = new File(filesDir, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg");
        OutputStream os = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                os = Files.newOutputStream(imageFile.toPath());
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            assert os != null;
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("TAG", "persistImage: " + e.getMessage());
        }

        return imageFile;

    }

    public static File persistVideo(Uri bitmap, Context cOntext) {
        File filesDir = cOntext.getCacheDir();
        @SuppressLint("SimpleDateFormat") File imageFile = new File(filesDir, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg");
        OutputStream os = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                os = Files.newOutputStream(imageFile.toPath());
            }
            bitmap.getPath();
            assert os != null;
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("TAG", "persistImage: " + e.getMessage());
        }

        return imageFile;

    }

    public static void ToolsShowDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                .create().show();
        Log.d("TAG", message);
    }

    public static Tools get() {
        return new Tools();
    }

    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "getting address...";
        if (context != null) {
            Geocoder geocoder = new Geocoder(context.getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
                if (addresses != null) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder();

                    for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    }
                    strAdd = strReturnedAddress.toString();
                    Log.w("My Current address", strReturnedAddress.toString());
                } else {
                    strAdd = "No Address Found";
                    Log.w("My Current address", "No Address returned!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                strAdd = "Cant get Address";
                Log.w("My Current address", "Canont get Address!");
            }
        }
        return strAdd;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static String ChangeDateFormat(String format, String date) {
        SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat new_format = new SimpleDateFormat(format);
        Date newDate = null;
        try {
            newDate = old_format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new_format.format(newDate);
    }

    public static void DatePicker(Context context, onDateSetListener listener) {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; // your format

                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                listener.SelectedDate(sdf.format(myCalendar.getTime()));
            }

        };
        new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public static void MakeCall(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }

    public static String getCurrent(Type type) {
        String cd = "";
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat cf = new SimpleDateFormat("HH:mm a");
        cd = type == Type.DATE ? df.format(c) : cf.format(c);
        return cd;
    }

    public static void HideKeyboard(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static String getTimeAgo(String crdate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        String currenttime = dateFormat.format(cal.getTime());

        Date CreatedAt = null;
        Date current = null;
        try {
            CreatedAt = dateFormat.parse(crdate);
            current = dateFormat.parse(currenttime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Get msec from each, and subtract.
        long diff = current.getTime() - CreatedAt.getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String time = null;
        if (diffDays > 0) {
            if (diffDays == 1) {
                time = diffDays + " day ago ";
            } else {
                time = diffDays + " days ago ";
            }
        } else {
            if (diffHours > 0) {
                if (diffHours == 1) {
                    time = diffHours + " hr ago";
                } else {
                    time = diffHours + " hrs ago";
                }
            } else {
                if (diffMinutes > 0) {
                    if (diffMinutes == 1) {
                        time = diffMinutes + " min ago";
                    } else {
                        time = diffMinutes + " mins ago";
                    }
                } else {
                    if (diffSeconds > 0) {
                        time = diffSeconds + " secs ago";
                    }
                }

            }

        }
        return time;
    }

    public static void TimePicker(Context context, onDateSetListener listene) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                listene.SelectedDate(getTimeInMillSec(selectedHour + ":" + selectedMinute));
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Set arrival time");
        mTimePicker.show();
    }

    public static void TimePicker(Context context, onDateSetListener listene, boolean is12hour, boolean previous_time) {
        Calendar mcurrentTime = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (!previous_time) {
                    if (selectedHour > hour) {
                        int hours = selectedHour % 12;
                        listene.SelectedDate(String.format("%02d:%02d %s", hours == 0 ? 12 : hours,
                                selectedMinute, selectedHour < 12 ? "am" : "pm"));
                    } else if (selectedHour == hour && selectedMinute >= minute) {
                        int hours = selectedHour % 12;
                        listene.SelectedDate(String.format("%02d:%02d %s", hours == 0 ? 12 : hours,
                                selectedMinute, selectedHour < 12 ? "am" : "pm"));
                    } else {
                        listene.SelectedDate(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                minute, hour < 12 ? "am" : "pm"));
                    }
                } else {
                    int hours = selectedHour % 12;
                    listene.SelectedDate(String.format("%02d:%02d %s", hours == 0 ? 12 : hours,
                            selectedMinute, selectedHour < 12 ? "am" : "pm"));
                }
            }
        }, hour, minute, is12hour);
        mTimePicker.setTitle("Set arrival time");

        mTimePicker.show();
    }

    public static String getTimeInMillSec(String givenDateString) {
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(timeInMilliseconds);
    }

    public static String getRealPathFromUri(Context context, Uri uri) {
        String filePath = "";
        String scheme = uri.getScheme();
        if (scheme == null) filePath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            filePath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                cursor = context.getContentResolver().query(uri, proj, null, null);
            }
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
            if (TextUtils.isEmpty(filePath)) {
                filePath = getRealPathFromUri(context, uri);
            }
        }
        return filePath;

    }

    public static String getFilePathForNorMediaUri(Context context, Uri uri) {
        String filePath = "";
        Cursor cursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            cursor = context.getContentResolver().query(uri, null, null, null);
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columIndex = cursor.getColumnIndexOrThrow("data");
                filePath = cursor.getString(columIndex);
            }
            cursor.close();

        }
        return filePath;
    }

    public void ShareApp(Context context) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    public void LaunchMarket(Context context) {
        Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    public void isLocationEnabled(Context mContext, LocationManager locationManager) {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle(mContext.getString(R.string.enable_location));
            alertDialog.setMessage(mContext.getString(R.string.your_locations_setting_is_not_enabled_please_enabled_it_in_settings_menu));
            alertDialog.setPositiveButton(mContext.getString(R.string.location_settings), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });
            alertDialog.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
    }

    public enum Type {
        DATE, TIME
    }
}
