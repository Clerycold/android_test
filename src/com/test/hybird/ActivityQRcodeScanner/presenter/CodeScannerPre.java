package com.test.hybird.ActivityQRcodeScanner.presenter;

/**
 * Created by clery on 2016/12/27.
 */

public interface CodeScannerPre {
    boolean CheckCodeScannerState();
    void setCodeScannerState(Boolean state);
    boolean CheckEnmergenceState();
    void setEnmergenceState();
    boolean CheckPreviewingState();
    void setPreviewingState(Boolean state);
}
