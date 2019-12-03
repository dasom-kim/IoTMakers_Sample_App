package com.kt.iot.mobile.push;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * GCMReceiver
 * Push 메시지를 수신
 * GCM 서비스 종료로 인해, FCM으로 마이그레이션 개발이 필요
 */
public class GcmBroadcastReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		GCMIntentService.runIntentInService(context, intent);		//GCMIntentService 를 호출
	}
	
}