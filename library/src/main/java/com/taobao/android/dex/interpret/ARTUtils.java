//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.taobao.android.dex.interpret;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;

@SuppressWarnings("JniMissingFunction")
@TargetApi(21)
public class ARTUtils {
    private static final String TAG = "ARTUtils";
    private static volatile boolean sInit = false;

    static {
        try {

            System.loadLibrary("dexinterpret");

            Class<?> vmRuntime = Class.forName("dalvik.system.VMRuntime");
            Method getRuntime = vmRuntime.getDeclaredMethod("getRuntime");
            getRuntime.setAccessible(true);
            Object runtime = getRuntime.invoke(null);
            Method getTargetSdkVersion = vmRuntime.getDeclaredMethod("getTargetSdkVersion");
            getTargetSdkVersion.setAccessible(true);
            int targetSdkVersion = (int) getTargetSdkVersion.invoke(runtime);

            // Log.w(TAG, "targetSdkVersion : " + targetSdkVersion);

            nativeInit(false, targetSdkVersion);

            sInit = true;
        } catch (Throwable e) {
            Log.e(TAG, "init failed");
        }
    }

    private ARTUtils() {
    }


    public static boolean init(Context context) {
        return init(context, false);
    }

    public static boolean init(Context context, boolean hookedJavaVM) {
        try {
            System.loadLibrary("dexinterpret");
            nativeInit(hookedJavaVM, context.getApplicationInfo().targetSdkVersion);
            sInit = true;
        } catch (Throwable var5) {
            Log.e("ARTUtils", "Couldn't initialize.", var5);
        }

        return sInit;
    }

    public static boolean isInit() {
        return sInit;
    }

    public static Boolean setIsDex2oatEnabled(boolean enabled) {
        return !sInit ? null : setIsDex2oatEnabledNative(enabled);
    }

    public static Boolean isDex2oatEnabled() {
        return !sInit ? null : isDex2oatEnabledNative();
    }

    public static Boolean setVerificationEnabled(boolean enabled) {
        if (!sInit) {
            return null;
        } else {
            boolean success = setVerificationEnabledNative(enabled);
            if (success && enabled) {
                setSignalCatcherHaltFlag(false);
            } else if (success && !enabled) {
                setSignalCatcherHaltFlag(true);
            }

            return success;
        }
    }

    public static Boolean IsVerificationEnabled() {
        return !sInit ? null : IsVerificationEnabledNative();
    }

    public static Boolean setSignalCatcherHaltFlag(boolean enabled) {
        return !sInit ? null : setSignalCatcherHaltFlagNative(enabled);
    }

    public static Boolean abort() {
        return !sInit ? null : abortNative();
    }

    private static native boolean nativeInit(boolean var0, int var1);

    private static native boolean setIsDex2oatEnabledNative(boolean var0);

    private static native boolean isDex2oatEnabledNative();

    private static native boolean setVerificationEnabledNative(boolean var0);

    private static native boolean IsVerificationEnabledNative();

    private static native boolean setSignalCatcherHaltFlagNative(boolean var0);

    private static native boolean abortNative();
}
