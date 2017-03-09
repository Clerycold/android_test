package com.test.hybird.ActivityQRcodeScanner.model;

/**
 * Created by clery on 2016/12/27.
 */

public interface CodeScannerData {

    Boolean isCodeScanner();
    void setCodeScanner(Boolean state);
    Boolean isEnmergence();
    void reverseEnmergence();
    Boolean isPreviewing();
    void setPreviewing(Boolean state);
}
