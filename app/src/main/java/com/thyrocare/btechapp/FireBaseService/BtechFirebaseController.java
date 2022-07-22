package com.thyrocare.btechapp.FireBaseService;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.AddEditBenificaryActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.models.api.response.CouponCodeResponseModel;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BtechFirebaseController {

    AddEditBenificaryActivity addEditBenificaryActivity;
    String pe_allCoupons;
    FirebaseFirestore btechFirebaseFireStore;
    Global global;

    public BtechFirebaseController(AddEditBenificaryActivity addEditBenificaryActivity, String pe_allCoupons) {
        this.addEditBenificaryActivity = addEditBenificaryActivity;
        this.pe_allCoupons = pe_allCoupons;
        btechFirebaseFireStore = FirebaseFirestore.getInstance();
        global = new Global(addEditBenificaryActivity);
    }

    public void getAlCoupons() {
        try {
            global.showProgressDialog(addEditBenificaryActivity, "Please wait...");
            btechFirebaseFireStore.collection(pe_allCoupons).document("FhOl4tnTN0tjWCAZpMwv").get()
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
                                    responseModel = new CouponCodeResponseModel.Coupons();
                                    responseModel.setCouponCode(testlist.get(i).get("CouponCode"));
                                    responseModel.setCouponHead(testlist.get(i).get("CouponHead"));
                                    responseModel.setCouponSubHead(testlist.get(i).get("CouponSubHead"));

                                    model.Data.add(responseModel);
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
}
