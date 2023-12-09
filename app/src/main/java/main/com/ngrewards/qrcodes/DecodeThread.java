package main.com.ngrewards.qrcodes;

/**
 * Created by technorizen on 13/7/18.
 */

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;

import main.com.ngrewards.QrCodeActivity;

final class DecodeThread extends Thread {

    private final QrCodeActivity mActivity;
    private final CountDownLatch mHandlerInitLatch;
    private Handler mHandler;

    DecodeThread(QrCodeActivity activity) {
        this.mActivity = activity;
        mHandlerInitLatch = new CountDownLatch(1);
    }

    Handler getHandler() {
        try {
            mHandlerInitLatch.await();
        } catch (InterruptedException ie) {
            // continue?
        }
        return mHandler;
    }

    @Override
    public void run() {
        Looper.prepare();
        mHandler = new DecodeHandler(mActivity);
        mHandlerInitLatch.countDown();
        Looper.loop();
    }

}