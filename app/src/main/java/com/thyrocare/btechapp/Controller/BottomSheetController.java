package com.thyrocare.btechapp.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.CheckoutWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.ScanBarcodeWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.Btech_VisitDisplayAdapter;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.activity.OrderPickUpActivity;
import com.thyrocare.btechapp.delegate.OrderRescheduleDialogButtonClickedDelegate;
import com.thyrocare.btechapp.dialog.RescheduleOrderDialog;
import com.thyrocare.btechapp.fragment.ChangePasswordFragment;
import com.thyrocare.btechapp.models.api.response.GetTestListResponseModel;
import com.thyrocare.btechapp.models.api.response.PickupOrderResponseModel;
import com.thyrocare.btechapp.models.data.OrderDetailsModel;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;
import com.thyrocare.btechapp.utils.app.OnPinchListener;

public class BottomSheetController {


    Activity activity;
    int flag = 0;
    ChangePasswordFragment changePasswordFragment;
    ScanBarcodeWoeActivity scanBarcodeWoeActivity;
    CheckoutWoeActivity checkoutWoeActivity;
    OrderPickUpActivity orderPickUpActivity;
    private Global globalclass;
    Uri uri;
    int benId, position;
    String imgUrl, dialogMsg;
    boolean isFromURL;
    ScaleGestureDetector scaleGestureDetector;
    float mScalefloat = 1.0f;
    int FlagDel = 0;
    PickupOrderResponseModel.PickupordersDTO pickupordersDTO;

    public BottomSheetController(Activity activity, ChangePasswordFragment changePasswordFragment) {
        this.activity = activity;
        this.changePasswordFragment = changePasswordFragment;

    }

    public BottomSheetController(Activity activity) {
        this.activity = activity;
    }

    public BottomSheetController(Activity mActivity, ScanBarcodeWoeActivity scanBarcodeWoeActivity, String imgUrl, boolean isFromURL, final int benId, final int position,final int flagDel) {
        this.activity = mActivity;
        this.scanBarcodeWoeActivity = scanBarcodeWoeActivity;
        globalclass = new Global(mActivity);
        this.imgUrl = imgUrl;
        this.isFromURL = isFromURL;
        this.benId = benId;
        this.position = position;
        this.FlagDel = flagDel;
        flag = 2;
    }

    public BottomSheetController(Activity mActivity, CheckoutWoeActivity checkoutWoeActivity, Uri uri) {
        this.activity = mActivity;
        this.checkoutWoeActivity = checkoutWoeActivity;
        this.uri = uri;
        flag = 3;
    }


    public BottomSheetController(Activity mActivity, ScanBarcodeWoeActivity scanBarcodeWoeActivity) {
        this.activity = mActivity;
        this.scanBarcodeWoeActivity = scanBarcodeWoeActivity;
    }

    public BottomSheetController(Activity mActivity, OrderPickUpActivity orderPickUpActivity, String msg, PickupOrderResponseModel.PickupordersDTO pickupordersDTO) {
        this.activity = mActivity;
        this.orderPickUpActivity = orderPickUpActivity;
        this.dialogMsg = msg;
        this.pickupordersDTO = pickupordersDTO;
        flag = 4;
    }

    public void SetOKBottomSheet(String text) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        final View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));
        Button btn_ok = bottomSheet.findViewById(R.id.btn_ok);
        TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
        tv_text.setText(text);
        tv_text.setAllCaps(true);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }


    public void SetLogoutBottomsheet(String string) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);

        View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.logout_bottomsheet, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));

        TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
        tv_text.setText(string);

        Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button btn_no = bottomSheet.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

            }
        });
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }


    public void SetVerifyBottomsheet(String string, String s) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.paymentbottomsheet, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));
        TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
        TextView tv_label = bottomSheet.findViewById(R.id.tv_label);
        tv_text.setText(string);
        tv_label.setText(s);

        Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button btn_no = bottomSheet.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

            }
        });
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }


    public void SetBasicBottomSheet(String text, String descripition) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));
        Button btn_ok = bottomSheet.findViewById(R.id.btn_ok);
        TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
        TextView tv_desc = bottomSheet.findViewById(R.id.tv_description);

        if (descripition.equalsIgnoreCase("")) {
            tv_desc.setVisibility(View.GONE);
        } else {
            tv_desc.setVisibility(View.VISIBLE);
            tv_desc.setText("" + descripition);
        }

        tv_text.setText("" + text);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    bottomSheetDialog.dismiss();
                }
            }
        });

        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }

    public void AddBenBottomSheet() {
        String text = null;

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);

        View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.layout_add_new_ben, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));

        Button btn_ok = bottomSheet.findViewById(R.id.btn_ok);
        TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
        tv_text.setText("" + text);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 1) {
//                    changePasswordFragment.getbottomsheetresponse(bottomSheetDialog);
                }
            }
        });

        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }


    public void SetBottomSheet(Activity mActivity) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        View bottomSheet = null;
        if (flag == 2) {
            bottomSheet = LayoutInflater.from(activity).inflate(R.layout.image_dialog, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));

        } else if (flag == 3) {
            bottomSheet = LayoutInflater.from(activity).inflate(R.layout.image_dialog, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));

        } else if (flag == 4) {
            bottomSheet = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));
        }
        ImageView img_close = bottomSheet.findViewById(R.id.img_close);
        final ImageView imageview = bottomSheet.findViewById(R.id.imageview);
        CardView btn_delete = bottomSheet.findViewById(R.id.btn_delete);
        TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
        CardView btn_yes = bottomSheet.findViewById(R.id.btn_yes);
        CardView btn_no = bottomSheet.findViewById(R.id.btn_no);

        if (flag == 3) {
            btn_delete.setVisibility(View.GONE);
        } else {
            btn_delete.setVisibility(View.VISIBLE);
        }
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 2) {
                    scanBarcodeWoeActivity.getbottomsheetresponse(bottomSheetDialog);
                } else if (flag == 3) {
                    checkoutWoeActivity.getbottomsheetresponse(bottomSheetDialog);
                }


            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 2) {
                    scanBarcodeWoeActivity.vialImageDelete(benId, position, bottomSheetDialog,FlagDel);
                }
            }
        });

        if (flag == 2) {
            if (isFromURL) {
                globalclass.DisplayImagewithDefaultImage(mActivity, imgUrl.replace("\\", "/"), imageview);
            } else {

                globalclass.DisplayDeviceImages(mActivity, imgUrl.replace("\\", "/"), imageview);
            }
        } else if (flag == 3) {
            if (uri != null) {
                imageview.setImageURI(uri);
            }

        }

        if (flag == 4) {
            if (flag == 4) {
                if (!InputUtils.isNull(dialogMsg)) {
                    tv_text.setText(dialogMsg);
                }
            }

            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag == 4) {
                        orderPickUpActivity.getbottomsheetresponse(bottomSheetDialog);
                    }
                }
            });
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag == 4) {
                        if (pickupordersDTO != null) {
                            orderPickUpActivity.callPickup(bottomSheetDialog, pickupordersDTO);
                        }

                    }
                }
            });
        }
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }

    public void setTestListBottomSheet(Activity mActivity, GetTestListResponseModel testListResponseModel) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.item_test_list_display, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));
        LinearLayout ll_tests = bottomSheet.findViewById(R.id.ll_tests);
        if (testListResponseModel != null) {
            scanBarcodeWoeActivity.iflateTestGroupName(ll_tests, testListResponseModel);
        }
        ImageView img_close = (ImageView) bottomSheet.findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcodeWoeActivity.getbottomsheetresponse(bottomSheetDialog);
            }
        });
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }

    

}

