package com.test.hybird.control.json;

import android.os.Handler;

/**
 * Created by clery on 2016/12/29.
 */

public class NetRunnable implements Runnable{

    private int type;
    private String internetSite;
    private String postcontent;
    NetUtils Net = new NetUtils();
    public NetRunnable(Handler NetHandler, String path){
        setInternetSite(path);
        Net.SetHandler(NetHandler);
        this.type=0;
    }

    public NetRunnable(Handler NetHandler,String path,String postcontent){
        setInternetSite(path);
        this.postcontent=postcontent;
        Net.SetHandler(NetHandler);
        this.type=1;
    }

    @Override
    public void run() {
        switch (type){
            case 0:
                Net.get(internetSite);
                break;
            case 1:
                Net.post(internetSite,postcontent);
                break;
        }
    }
    private void setInternetSite(String path){
        if (path.equals("validMobileCode")) {
            internetSite = "http://172.16.4.249/api/Valid/validMobileCode";
        }
    }
}