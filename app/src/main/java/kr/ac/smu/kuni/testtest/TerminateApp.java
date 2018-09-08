package kr.ac.smu.kuni.testtest;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Juseok Song on 2016-01-22.
 */
public class TerminateApp {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public TerminateApp(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity,
            "한번 더 누르시면 앱을 종료합니다", Toast.LENGTH_SHORT);
    toast.show();
}
}