package com.example.fjh.libraryfinal.Control;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FJH on 2017/5/14.
 */

public class ActivityCollector {
    public static List<Activity> activities=new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finishAll(){
        for(Activity activity: activities){
           if(!activity.isFinishing()){
               activity.finish();
           }
        }
    }
}
