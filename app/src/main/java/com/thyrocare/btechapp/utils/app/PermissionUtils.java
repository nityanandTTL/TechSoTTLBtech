package com.thyrocare.btechapp.utils.app;

import android.Manifest;
import android.app.Activity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;

import java.util.List;

public class PermissionUtils {

    private static PermissionUtils.OnPermissionSuccessListener onPermissionSuccessListener;

    public static void AsKPermissionatSplashScreen(final Activity activity, PermissionListener permissionListener) {
        TedPermission.with(activity)
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                .setRationaleMessage(ConstantsMessages.PermissionRequiredMsg)
                .setRationaleConfirmText("OK")
                .setDeniedMessage(ConstantsMessages.PermissionRejectedMsg)
                .setPermissionListener(permissionListener)
                .check();
    }

    public static void AsKPermissionForCamera(final Activity activity, final OnPermissionSuccessListener permissionListener) {
        TedPermission.with(activity)
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setRationaleMessage("We need your permission to open Camera in your Device.")
                .setRationaleConfirmText("OK")
                .setDeniedMessage(ConstantsMessages.PermissionRejectedMsg)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (permissionListener != null) {
                            permissionListener.onPermissionGranted();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Global.showCustomStaticToast(activity, ConstantsMessages.PermissionDenied + deniedPermissions.toString());
                    }
                })
                .check();
    }

    public static void AsKPermissionForLocation(final Activity activity, final OnPermissionSuccessListener permissionListener) {
        TedPermission.with(activity)
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .setRationaleMessage("We need your permission to access your location on your Device.")
                .setRationaleConfirmText("OK")
                .setDeniedMessage(ConstantsMessages.PermissionRejectedMsg)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (permissionListener != null) {
                            permissionListener.onPermissionGranted();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Global.showCustomStaticToast(activity, ConstantsMessages.PermissionDenied + deniedPermissions.toString());
                    }
                })
                .check();
    }

    public static void AsKPermissionForCameraAndVideo(final Activity activity, final OnPermissionSuccessListener permissionListener) {
        TedPermission.with(activity)
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setRationaleMessage("We need permission to access and capture images or video from your device for uploading.")
                .setRationaleConfirmText("OK")
                .setDeniedMessage(ConstantsMessages.PermissionRejectedMsg)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (permissionListener != null) {
                            permissionListener.onPermissionGranted();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Global.showCustomStaticToast(activity, ConstantsMessages.PermissionDenied + deniedPermissions.toString());
                    }
                })
                .check();
    }


    public static void AsKPermissionToDownloadReceipt(final Activity activity, final OnPermissionSuccessListener permissionListener) {
        TedPermission.with(activity)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setRationaleMessage("We need permission to download receipt on your device.")
                .setRationaleConfirmText("OK")
                .setDeniedMessage(ConstantsMessages.PermissionRejectedMsg)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (permissionListener != null) {
                            permissionListener.onPermissionGranted();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Global.showCustomStaticToast(activity, ConstantsMessages.PermissionDenied + deniedPermissions.toString());
                    }
                })
                .check();
    }

    public static void AsKPermissionToDownloadReport(final Activity activity, final OnPermissionSuccessListener permissionListener) {
        TedPermission.with(activity)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setRationaleMessage("We need permission to download report on your device.")
                .setRationaleConfirmText("OK")
                .setDeniedMessage(ConstantsMessages.PermissionRejectedMsg)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (permissionListener != null) {
                            permissionListener.onPermissionGranted();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Global.showCustomStaticToast(activity, ConstantsMessages.PermissionDenied + deniedPermissions.toString());
                    }
                })
                .check();
    }

    public static void AsKPermissionToOpenWhatsApp(final Activity activity, final OnPermissionSuccessListener permissionListener) {
        TedPermission.with(activity)
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
                .setRationaleMessage("We need permission to open WhatsApp in your device.")
                .setRationaleConfirmText("OK")
                .setDeniedMessage(ConstantsMessages.PermissionRejectedMsg)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (permissionListener != null) {
                            permissionListener.onPermissionGranted();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Global.showCustomStaticToast(activity, ConstantsMessages.PermissionDenied + deniedPermissions.toString());
                    }
                })
                .check();
    }

    public static void AsKPermissionToCall(final Activity activity, final OnPermissionSuccessListener permissionListener) {
        TedPermission.with(activity)
                .setPermissions(android.Manifest.permission.CALL_PHONE, android.Manifest.permission.ACCESS_NETWORK_STATE)
                .setRationaleMessage("We need permission to call from your device.")
                .setRationaleConfirmText("OK")
                .setDeniedMessage(ConstantsMessages.PermissionRejectedMsg)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (permissionListener != null) {
                            permissionListener.onPermissionGranted();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Global.showCustomStaticToast(activity, ConstantsMessages.PermissionDenied + deniedPermissions.toString());
                    }
                })
                .check();
    }

    public static void AsKPermissionToDownloadAndShareImage(final Activity activity, final OnPermissionSuccessListener permissionListener) {
        TedPermission.with(activity)
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setRationaleMessage("We need permission to download and share the image.")
                .setRationaleConfirmText("OK")
                .setDeniedMessage(ConstantsMessages.PermissionRejectedMsg)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (permissionListener != null) {
                            permissionListener.onPermissionGranted();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Global.showCustomStaticToast(activity, ConstantsMessages.PermissionDenied + deniedPermissions.toString());
                    }
                })
                .check();
    }

    public static void AsKPermissionToDownloadImage(final Activity activity, final OnPermissionSuccessListener permissionListener) {
        TedPermission.with(activity)
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setRationaleMessage("We need permission to download image.")
                .setRationaleConfirmText("OK")
                .setDeniedMessage(ConstantsMessages.PermissionRejectedMsg)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (permissionListener != null) {
                            permissionListener.onPermissionGranted();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Global.showCustomStaticToast(activity, ConstantsMessages.PermissionDenied + deniedPermissions.toString());
                    }
                })
                .check();
    }


    public static void AsKPermissionToRecordAudio(final Activity activity, final OnPermissionSuccessListener permissionListener) {
        TedPermission.with(activity)
                .setPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setRationaleMessage("We need your permission to record audio from your Device.")
                .setRationaleConfirmText("OK")
                .setDeniedMessage(ConstantsMessages.PermissionRejectedMsg)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (permissionListener != null) {
                            permissionListener.onPermissionGranted();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Global.showCustomStaticToast(activity, ConstantsMessages.PermissionDenied + deniedPermissions.toString());
                    }
                })
                .check();
    }


    public void setOnPermissionSuccessListener(OnPermissionSuccessListener onPermissionSuccessListener) {
        this.onPermissionSuccessListener = onPermissionSuccessListener;
    }

    public interface OnPermissionSuccessListener {
        void onPermissionGranted();
    }
}
