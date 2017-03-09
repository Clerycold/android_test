package com.test.hybird.ActivityQRcodeScanner.presenter;

import android.content.Context;

import com.test.hybird.ActivityQRcodeScanner.model.CodeScannerData;
import com.test.hybird.ActivityQRcodeScanner.model.CodeScannerModel;
import com.test.hybird.ActivityQRcodeScanner.view.CodeScannerView;

/**
 * Created by clery on 2016/12/27.
 */

public class CodeScannerPreCompl implements CodeScannerPre{

    Context context;
    CodeScannerView codeScannerView;
    CodeScannerData codeScannerData;

    public CodeScannerPreCompl(Context context, CodeScannerView codeScannerView){
        this.context=context;
        this.codeScannerView = codeScannerView;
        codeScannerData=new CodeScannerModel();
    }

    @Override
    public boolean CheckCodeScannerState() {
        return codeScannerData.isCodeScanner();
    }

    @Override
    public void setCodeScannerState(Boolean state) {
        codeScannerData.setCodeScanner(state);
    }

    @Override
    public boolean CheckEnmergenceState() {
        return codeScannerData.isEnmergence();
    }

    @Override
    public void setEnmergenceState() {
        codeScannerData.reverseEnmergence();
    }

    @Override
    public boolean CheckPreviewingState() {
        return codeScannerData.isPreviewing();
    }

    @Override
    public void setPreviewingState(Boolean state) {
        codeScannerData.setPreviewing(state);
    }
}
