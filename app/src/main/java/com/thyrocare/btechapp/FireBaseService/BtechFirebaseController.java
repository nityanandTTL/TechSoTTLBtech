package com.thyrocare.btechapp.FireBaseService;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.AddEditBenificaryActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.models.api.response.CouponCodeResponseModel;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BtechFirebaseController {

    AddEditBenificaryActivity addEditBenificaryActivity;
    String str_CouponCollection;
    FirebaseFirestore btechFirebaseFireStore;
    Global global;
    Activity activity;

    public BtechFirebaseController(AddEditBenificaryActivity addEditBenificaryActivity, String CouponCollection) {
        this.addEditBenificaryActivity = addEditBenificaryActivity;
        this.str_CouponCollection = CouponCollection;
        btechFirebaseFireStore = FirebaseFirestore.getInstance();
        global = new Global(addEditBenificaryActivity);
        this.activity = addEditBenificaryActivity;
    }

    public void getAllCoupons(String CouponDocument, final boolean pePartner) {
        try {
            global.showProgressDialog(addEditBenificaryActivity, "Please wait...");
            btechFirebaseFireStore.collection(str_CouponCollection).document(CouponDocument).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            global.hideProgressDialog(addEditBenificaryActivity);
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                List<HashMap<String, String>> testlist = (List<HashMap<String, String>>) snapshot.getData().get("Data");
                                CouponCodeResponseModel model = new CouponCodeResponseModel();
                                model.Data = new ArrayList<>();
                                CouponCodeResponseModel.Coupons responseModel;
                                for (int i = 0; i < testlist.size(); i++) {
                                    if (Boolean.parseBoolean(testlist.get(i).get("Status"))) {
                                        responseModel = new CouponCodeResponseModel.Coupons();
                                        responseModel.setCouponCode(testlist.get(i).get("CouponCode"));
                                        responseModel.setCouponHead(testlist.get(i).get("CouponHead"));
                                        responseModel.setCouponSubHead(testlist.get(i).get("CouponSubHead"));
                                        if (!pePartner) {
                                            responseModel.setMinRate(InputUtils.isNull(testlist.get(i).get("MinRate")) ? 0 : Integer.parseInt(testlist.get(i).get("MinRate")));
                                            responseModel.setMaxRate(InputUtils.isNull(testlist.get(i).get("MaxRate")) ? 0 : Integer.parseInt(testlist.get(i).get("MaxRate")));
                                            responseModel.setDiscount(InputUtils.isNull(testlist.get(i).get("Discount")) ? 0 : Integer.parseInt(testlist.get(i).get("Discount")));
                                            responseModel.setStatus(Boolean.parseBoolean(testlist.get(i).get("Status")));
                                        }
                                        model.Data.add(responseModel);
                                    }

                                }
                                addEditBenificaryActivity.getCouponsResponse(model);


                            } else {
                                Log.i("dont exist", "user firebase real time ");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            global.hideProgressDialog(addEditBenificaryActivity);
                            Toast.makeText(addEditBenificaryActivity, ConstantsMessages.SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();

                        }
                    });
        } catch (Exception e) {
            global.hideProgressDialog(addEditBenificaryActivity);
            Toast.makeText(addEditBenificaryActivity, ConstantsMessages.SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void getAllTCCoupons() {
    }
}
