package com.android.magic.wand.utils;

import android.util.Log;

public class UpdateLog {

	private static final String TAG = "[MagicWand]";

	private static boolean isDebug = true;

	public static void v(String strTag, String msg) {
		if (isDebug()) {
			Log.v(TAG, "[Pid:" + android.os.Process.myPid() + "][Tid:" +  android.os.Process.myTid() + "]" + "[" + strTag + "] " + msg);
		}
	}

	public static void d(String strTag, String msg) {
		if (isDebug()) {
			Log.d(TAG,"[Pid:" + android.os.Process.myPid() + "][Tid:" +  android.os.Process.myTid() + "]" + "[" + strTag + "] " + msg);
		}

	}

	public static void i(String strTag, String msg) {
		if (isDebug()) {
			Log.i(TAG, "[Pid:" + android.os.Process.myPid() + "][Tid:" +  android.os.Process.myTid() + "]" + "[" + strTag + "] " + msg);
		}
	}

	public static void w(String strTag, String msg) {
		if (isDebug()) {
			Log.w(TAG, "[Pid:" + android.os.Process.myPid() + "][Tid:" +  android.os.Process.myTid() + "]" + "[" + strTag + "] " + msg);
		}
	}

	public static void e(String strTag, String msg) {
		if (isDebug()) {
			Log.e(TAG, "[Pid:" + android.os.Process.myPid() + "][Tid:" +  android.os.Process.myTid() + "]" + "[" + strTag + "] " + msg);
		}
	}

	public static void e(String strTag, String msg, Throwable e) {
		if (isDebug()) {
			Log.e(TAG, "[Pid:" + android.os.Process.myPid() + "][Tid:" +  android.os.Process.myTid() + "]" + "[" + strTag + "] " + msg, e);
		}
	}

	public static void wtf(String strTag, String msg) {
		if (isDebug()) {
			Log.wtf(TAG, "[Pid:" + android.os.Process.myPid() + "][Tid:" +  android.os.Process.myTid() + "]" + "[" + strTag + "] " + msg);
		}
	}

	public static void iJSON(String strTag, String msg) {
		if (isDebug()) {
			Log.i(TAG, "[Pid:" + android.os.Process.myPid() + "][Tid:" +  android.os.Process.myTid() + "]" + "[" + strTag + "] " + format(msg));
		}
	}

	private static boolean isDebug() {
		return isDebug;
	}

	public static void setIsDebug(boolean params) {
		isDebug = params;
	}


	private static String format(String jsonStr) {
		try {
			int level = 0;
			StringBuffer jsonForMatStr = new StringBuffer();
			for(int i=0;i<jsonStr.length();i++){
				char c = jsonStr.charAt(i);
				if(level>0&&'\n'==jsonForMatStr.charAt(jsonForMatStr.length()-1)){
					jsonForMatStr.append(getLevelStr(level));
				}
				switch (c) {
					case '{':
					case '[':
						jsonForMatStr.append(c+"\n");
						level++;
						break;
					case ',':
						jsonForMatStr.append(c+"\n");
						break;
					case '}':
					case ']':
						jsonForMatStr.append("\n");
						level--;
						jsonForMatStr.append(getLevelStr(level));
						jsonForMatStr.append(c);
						break;
					default:
						jsonForMatStr.append(c);
						break;
				}
			}
			return jsonForMatStr.toString();
		} catch (Throwable e) {
			UpdateLog.e(TAG, "Throwable " + (e != null ? e.getMessage():""), e);
		}
		return jsonStr;
	}

	private static String getLevelStr(int level){
		StringBuffer levelStr = new StringBuffer();
		for(int levelI = 0;levelI<level ; levelI++){
			levelStr.append("\t");
		}
		return levelStr.toString();
	}
}
