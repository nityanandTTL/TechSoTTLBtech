package com.thyrocare.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.models.api.request.CashDepositEntryRequestModel;
import com.thyrocare.models.api.response.BankMasterResponseModel;
import com.thyrocare.models.api.response.PaymentModeMasterResponseModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static com.thyrocare.utils.api.NetworkUtils.isNetworkAvailable;

/**
 * to get payment mode<br/>
 * http://bts.dxscloud.com/btsapi/api/Masters/PaymentModeMaster<br/>
 * to get bank names<br/>
 * http://bts.dxscloud.com/btsapi/api/Masters/BankMaster/884543107<br/>
 * on submit<br/>
 * /CashDeposit/CashDepositEntry<br/>
 */
public class CreditFragment extends Fragment {
    public static final String TAG_FRAGMENT = CreditFragment.class.getSimpleName();
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    ArrayList<String> paymentMode = new ArrayList<>();
    ArrayList<String> bankName = new ArrayList<>();
    private Button btn_submit;
    private TextView edt_branch_name, edt_instrument, tv_no_file_chosen, tv_choose_file, tv_re_enter_cheque_no, tv_re_enter_remark, tv_re_enter_amount, tv_enter_amount, tv_branch_name, tv_select_instrument, tv_cheque_no, tv_date_of_deposit, tv_transaction_number, re_renter_transcation_number;
    private EditText edt_re_enter_cheque_number, edt_deposit, edt_tsp, edt_transaction_number, edt_re_enter_transcation_number, edt_cheque_number, edt_amount, edt_re_enter_amount, edt_remark;
    private int mYear, mMonth, mDay;
    private int PICK_IMAGE_REQUEST = 1;
    private String[] test_code_arr;
    private ArrayList<PaymentModeMasterResponseModel> slotsArr;
    private int paymentModePosition = 0;
    private ArrayList<PaymentModeMasterResponseModel> selectedSlotsArr;
    private ArrayList<BankMasterResponseModel> bankArr;
    private String image = "";
    private ArrayList<BankMasterResponseModel> selectedBankArr;
    private String picturePath;
    private TextView tv_file_selected;

    public CreditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();

        if (activity.toolbarHome != null) {
            activity.toolbarHome.setTitle("Credit");
        }
        appPreferenceManager = new AppPreferenceManager(activity);
        if (getArguments() != null) {

        }

    }

    private void initUI(View view) {
        tv_file_selected=(TextView)view.findViewById(R.id.tv_file_selected);
        tv_no_file_chosen = (TextView) view.findViewById(R.id.tv_no_file_chosen);
        tv_branch_name = (TextView) view.findViewById(R.id.tv_branch_name);
        tv_select_instrument = (TextView) view.findViewById(R.id.tv_select_instrument);
        tv_date_of_deposit = (TextView) view.findViewById(R.id.tv_date_of_deposit);
        tv_transaction_number = (TextView) view.findViewById(R.id.tv_transaction_number);
        re_renter_transcation_number = (TextView) view.findViewById(R.id.re_renter_transcation_number);
        tv_cheque_no = (TextView) view.findViewById(R.id.tv_cheque_no);
        tv_enter_amount = (TextView) view.findViewById(R.id.tv_enter_amount);
        tv_re_enter_amount = (TextView) view.findViewById(R.id.tv_re_enter_amount);
        tv_re_enter_remark = (TextView) view.findViewById(R.id.tv_re_enter_remark);
        tv_re_enter_cheque_no = (TextView) view.findViewById(R.id.tv_re_enter_cheque_no);
        tv_choose_file = (TextView) view.findViewById(R.id.tv_choose_file);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        setMandetory(tv_branch_name);
        setMandetory(tv_select_instrument);
        setMandetory(tv_date_of_deposit);
        setMandetory(tv_transaction_number);
        setMandetory(re_renter_transcation_number);
        setMandetory(tv_cheque_no);
        setMandetory(tv_enter_amount);
        setMandetory(tv_re_enter_remark);
        setMandetory(tv_re_enter_cheque_no);
        setMandetory(tv_re_enter_amount);
        edt_tsp = (EditText) view.findViewById(R.id.edt_tsp);
        edt_branch_name = (TextView) view.findViewById(R.id.edt_branch_name);
        edt_instrument = (TextView) view.findViewById(R.id.edt_instrument);
        edt_transaction_number = (EditText) view.findViewById(R.id.edt_transaction_number);
        edt_re_enter_transcation_number = (EditText) view.findViewById(R.id.edt_re_enter_transcation_number);
        edt_cheque_number = (EditText) view.findViewById(R.id.edt_cheque_number);
        edt_amount = (EditText) view.findViewById(R.id.edt_amount);
        edt_re_enter_amount = (EditText) view.findViewById(R.id.edt_re_enter_amount);
        edt_remark = (EditText) view.findViewById(R.id.edt_remark);
        edt_deposit = (EditText) view.findViewById(R.id.edt_deposit);
        edt_re_enter_cheque_number = (EditText) view.findViewById(R.id.edt_re_enter_cheque_number);
    }

    public static CreditFragment newInstance() {
        CreditFragment fragment = new CreditFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credit, container, false);
        initUI(view);
        setListeners();

        callPaymentModeMaster();
        callBankmaster();
        return view;
    }

    private void callBankmaster() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchOrderDetailApiAsyncTask = asyncTaskForRequest.getBankMasterRequestAsyncTask();
        fetchOrderDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new BankMasterApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchOrderDetailApiAsyncTask.execute(fetchOrderDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void callPaymentModeMaster() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchOrderDetailApiAsyncTask = asyncTaskForRequest.getPaymentModeMasterRequestAsyncTask();
        fetchOrderDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new PaymentModeMasterApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchOrderDetailApiAsyncTask.execute(fetchOrderDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void setListeners() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    CashDepositEntryRequestModel cashDepositEntryRequestModel = generateCashDepositeEntryRequestModel();
                    ApiCallAsyncTask orderBookingAPIAsyncTask = new AsyncTaskForRequest(activity).getCashDepositEntryRequestAsyncTask(cashDepositEntryRequestModel);
                    orderBookingAPIAsyncTask.setApiCallAsyncTaskDelegate(new CashDepositEntryAPIAsyncTaskDelegateResult());
                    if (isNetworkAvailable(activity)) {
                        orderBookingAPIAsyncTask.execute(orderBookingAPIAsyncTask);
                    } else {
                        Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        edt_branch_name.setText("--SELECT--");
        edt_branch_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Branch Name")
                        .setItems(bankName.toArray(new String[]{}), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                edt_branch_name.setText(bankName.get(which));

                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        edt_instrument.setText("--SELECT--");

        edt_instrument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Payment Mode")
                        .setItems(paymentMode.toArray(new String[]{}), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(activity, "" + paymentMode.get(which), Toast.LENGTH_SHORT).show();
                                paymentModePosition = which;
                                Logger.error(which + " which");
                                dialog.dismiss();
                                edt_instrument.setText("" + paymentMode.get(which));
                                if (paymentMode.get(which).equals("Cash By ATM")) {
                                    tv_transaction_number.setVisibility(View.VISIBLE);
                                    edt_transaction_number.setVisibility(View.VISIBLE);
                                    re_renter_transcation_number.setVisibility(View.VISIBLE);
                                    edt_re_enter_transcation_number.setVisibility(View.VISIBLE);
                                    tv_cheque_no.setVisibility(View.GONE);
                                    edt_cheque_number.setVisibility(View.GONE);
                                    tv_re_enter_cheque_no.setVisibility(View.GONE);
                                    edt_re_enter_cheque_number.setVisibility(View.GONE);

                                } else if (paymentMode.get(which).equals("Cash")) {
                                    tv_transaction_number.setVisibility(View.GONE);
                                    edt_transaction_number.setVisibility(View.GONE);
                                    re_renter_transcation_number.setVisibility(View.GONE);
                                    edt_re_enter_transcation_number.setVisibility(View.GONE);
                                    tv_cheque_no.setVisibility(View.GONE);
                                    edt_cheque_number.setVisibility(View.GONE);
                                    tv_re_enter_cheque_no.setVisibility(View.GONE);
                                    edt_re_enter_cheque_number.setVisibility(View.GONE);
                                } else if (paymentMode.get(which).equals("Cheque")) {
                                    tv_transaction_number.setVisibility(View.GONE);
                                    edt_transaction_number.setVisibility(View.GONE);
                                    re_renter_transcation_number.setVisibility(View.GONE);
                                    edt_re_enter_transcation_number.setVisibility(View.GONE);
                                    tv_cheque_no.setVisibility(View.VISIBLE);
                                    edt_cheque_number.setVisibility(View.VISIBLE);
                                    tv_re_enter_cheque_no.setVisibility(View.VISIBLE);
                                    edt_re_enter_cheque_number.setVisibility(View.VISIBLE);
                                } else if (paymentMode.get(which).equals("NEFT")) {
                                    tv_transaction_number.setVisibility(View.VISIBLE);
                                    edt_transaction_number.setVisibility(View.VISIBLE);
                                    re_renter_transcation_number.setVisibility(View.VISIBLE);
                                    edt_re_enter_transcation_number.setVisibility(View.VISIBLE);
                                    tv_cheque_no.setVisibility(View.GONE);
                                    edt_cheque_number.setVisibility(View.GONE);
                                    tv_re_enter_cheque_no.setVisibility(View.GONE);
                                    edt_re_enter_cheque_number.setVisibility(View.GONE);
                                }
                            }
                        }).show();

            }
        });
        edt_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edt_deposit.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                String todaysDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        tv_choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

    }

    private CashDepositEntryRequestModel generateCashDepositeEntryRequestModel() {
        CashDepositEntryRequestModel cashDepositEntryRequestModel = new CashDepositEntryRequestModel();
        cashDepositEntryRequestModel.setBTechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        cashDepositEntryRequestModel.setModeId(slotsArr.get(paymentModePosition).getModeId());
        cashDepositEntryRequestModel.setBankId(bankArr.get(0).getBankId());
        cashDepositEntryRequestModel.setTransactionId(edt_transaction_number.getText().toString());
        cashDepositEntryRequestModel.setChequeNo(edt_cheque_number.getText().toString());
        cashDepositEntryRequestModel.setAmount(Integer.parseInt(edt_amount.getText().toString()));
        cashDepositEntryRequestModel.setRemarks(edt_remark.getText().toString());
        cashDepositEntryRequestModel.setImage(image);
        return cashDepositEntryRequestModel;
    }

    private boolean validate() {
        if (edt_branch_name.getText().toString().equals("--SELECT--")) {
            edt_branch_name.setError("Select Branch Name");
            edt_branch_name.requestFocus();
            return false;
        } else if (edt_instrument.getText().toString().equals("--SELECT--")) {
            edt_instrument.setError("Select Instrument");
            edt_instrument.requestFocus();
            return false;
        } else if (InputUtils.isNull(edt_deposit.getText().toString())) {
            edt_deposit.setError("Select Date of Deposit");
            edt_deposit.requestFocus();
            return false;
        } else if (edt_transaction_number.isShown() && InputUtils.isNull(edt_transaction_number.getText().toString())) {
            edt_transaction_number.setError("Enter Transaction Number");
            edt_transaction_number.requestFocus();
            return false;
        } else if (edt_re_enter_transcation_number.isShown() && InputUtils.isNull(edt_re_enter_transcation_number.getText().toString())) {
            edt_re_enter_transcation_number.setError("Re-Enter Transaction Number");
            edt_re_enter_transcation_number.requestFocus();
            return false;
        } else if (!edt_transaction_number.getText().toString().equals(edt_re_enter_transcation_number.getText().toString())) {
            edt_re_enter_transcation_number.setError("Transaction Number do not match");
            edt_re_enter_transcation_number.requestFocus();
            return false;
        } else if (edt_cheque_number.isShown() && InputUtils.isNull(edt_cheque_number.getText().toString())) {
            edt_cheque_number.setError("Enter Cheque Number");
            edt_cheque_number.requestFocus();
            return false;
        } else if (edt_re_enter_cheque_number.isShown() && InputUtils.isNull(edt_re_enter_cheque_number.getText().toString())) {
            edt_re_enter_cheque_number.setError("Re-Enter Cheque Number");
            edt_re_enter_cheque_number.requestFocus();
            return false;
        } else if (edt_cheque_number.isShown() && !edt_cheque_number.getText().toString().equals(edt_re_enter_cheque_number.getText().toString())) {
            edt_re_enter_cheque_number.setError("Checque Number do not match");
            edt_re_enter_cheque_number.requestFocus();
            return false;
        } else if (InputUtils.isNull(edt_amount.getText().toString())) {
            edt_amount.setError("Enter Amount Number");
            edt_amount.requestFocus();
            return false;
        } else if (InputUtils.isNull(edt_re_enter_amount.getText().toString())) {
            edt_re_enter_amount.setError("Re-Enter Amount");
            edt_re_enter_amount.requestFocus();
            return false;
        } else if (!edt_amount.getText().toString().equals(edt_re_enter_amount.getText().toString())) {
            edt_re_enter_amount.setError("Amount do not match");
            edt_re_enter_amount.requestFocus();
            return false;
        } else if (image.equals("")) {
            Toast.makeText(activity, "Choose File", Toast.LENGTH_SHORT).show();
            return false;
        } else if (InputUtils.isNull(edt_remark.getText().toString())) {
            edt_remark.setError("Enter Remark");
            edt_remark.requestFocus();
            return false;
        }

        return true;
    }

    private void setMandetory(TextView mandetory) {
        String text = "<font color=#cc0029>*</font>" + mandetory.getText().toString();
        mandetory.setText(Html.fromHtml(text));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {

            Bitmap bm = null;
            if (data != null) {
                tv_file_selected.setText("Paper Proof[File Type: .jpg,.png]");
                tv_file_selected.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tv_no_file_chosen.setText("File attached");
                try {
                    bm = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            image = encodeTobase64(bm);
            Logger.error("image: " + image);
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private class PaymentModeMasterApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                slotsArr = new ResponseParser(activity).getPaymentModeMasterResponseModel(json, statusCode);
                selectedSlotsArr = new ArrayList<>();
                for (PaymentModeMasterResponseModel slotModel :
                        slotsArr) {
                    // if (slotModel.isMandatorySlot()) {
                    selectedSlotsArr.add(slotModel);
                    // }
                }
               /* if (paymentModeMasterResponseModel != null) {

                }*/
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    paymentMode.add("" + jsonObject.get("ModeName"));
                }


            } else {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private class BankMasterApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                bankArr = new ResponseParser(activity).getBankMasterResponseModel(json, statusCode);
                selectedBankArr = new ArrayList<>();
                for (BankMasterResponseModel slotModel :
                        bankArr) {
                    selectedBankArr.add(slotModel);
                }
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    bankName.add("" + jsonObject.get("BankName"));
                }
            } else {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private class CashDepositEntryAPIAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            activity.toolbarHome.setVisibility(View.VISIBLE);
            activity.pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
        }

        @Override
        public void onApiCancelled() {

        }
    }
}
