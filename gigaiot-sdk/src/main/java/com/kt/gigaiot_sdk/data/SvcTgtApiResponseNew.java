package com.kt.gigaiot_sdk.data;

import java.util.ArrayList;

public class SvcTgtApiResponseNew extends Response {

    Paging pagings;
    ArrayList<SvcTgtNew> svcTgts;

    public SvcTgtApiResponseNew(String responseCode, String message, Paging pagings, ArrayList<SvcTgtNew> svcTgts) {
        super(responseCode, message);
        this.pagings = pagings;
        this.svcTgts = svcTgts;
    }

    public Paging getPagings() {
        return pagings;
    }

    public ArrayList<SvcTgtNew> getSvcTgts() {
        return svcTgts;
    }
}
