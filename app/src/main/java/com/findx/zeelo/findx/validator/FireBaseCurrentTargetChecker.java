package com.findx.zeelo.findx.validator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findx.zeelo.findx.Constants;
import com.findx.zeelo.findx.model.TargetPerson;
import com.findx.zeelo.findx.registration.MySharedPreferences;

import java.io.IOException;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class FireBaseCurrentTargetChecker {
    public SharedPreferences userPrefs;
    Activity activity;
    Context applicationContext;

    public FireBaseCurrentTargetChecker(Context ac) {
        applicationContext = ac;
    }

    public boolean checkChangedUserIsCurrentUser(HashMap<String, Object> userMap) {
        String userName = new MySharedPreferences(applicationContext).getCurrentUserName();
        boolean isCurrentUserON = false;
        if(userName!=null) {
            TargetPerson Tperson = null;
            try {
                Tperson = new ObjectMapper().convertValue(userMap.get(userName), TargetPerson.class);
                isCurrentUserON = Tperson.getStatus().equalsIgnoreCase(Constants.STATUS_ON);
            }catch (Exception e){
                Log.e("ERROR", "ERROR WHILE GETTING FROM MAP: ");}
        }

        return isCurrentUserON;
    }

    public Context getApplicationContext(){
        return applicationContext;
    }

}
