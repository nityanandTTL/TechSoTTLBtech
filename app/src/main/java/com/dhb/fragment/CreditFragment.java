package com.dhb.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.InputUtils;
import com.google.android.gms.vision.text.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;


public class CreditFragment extends Fragment {
    public static final String TAG_FRAGMENT = CreditFragment.class.getSimpleName();
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    private Button btn_submit;
    private TextView tv_no_file_chosen, tv_choose_file, tv_re_enter_cheque_no, tv_re_enter_remark, tv_re_enter_amount, tv_enter_amount, tv_branch_name, tv_select_instrument, tv_cheque_no, tv_date_of_deposit, tv_transaction_number, re_renter_transcation_number;
    private EditText edt_re_enter_cheque_number, edt_deposit, edt_tsp, edt_branch_name, edt_instrument, edt_transaction_number, edt_re_enter_transcation_number, edt_cheque_number, edt_amount, edt_re_enter_amount, edt_remark;
    private int mYear, mMonth, mDay;
    private int PICK_IMAGE_REQUEST = 1;

    public CreditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        activity.toolbarHome.setTitle("Credit");
        appPreferenceManager = new AppPreferenceManager(activity);
        if (getArguments() != null) {

        }

    }

    private void initUI(View view) {
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
        edt_branch_name = (EditText) view.findViewById(R.id.edt_branch_name);
        edt_instrument = (EditText) view.findViewById(R.id.edt_instrument);
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
        return view;
    }

    private void setListeners() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                }
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

    private boolean validate() {
        if (InputUtils.isNull(edt_branch_name.getText().toString())) {
            edt_branch_name.setError("Enter Branch Name");
            edt_branch_name.requestFocus();
        } else if (InputUtils.isNull(edt_instrument.getText().toString())) {
            edt_instrument.setError("Select Instrument");
            edt_instrument.requestFocus();
        } else if (InputUtils.isNull(edt_deposit.getText().toString())) {
            edt_deposit.setError("Select Date of Deposit");
            edt_deposit.requestFocus();
        } else if (InputUtils.isNull(edt_transaction_number.getText().toString())) {
            edt_transaction_number.setError("Enter Transaction Number");
            edt_transaction_number.requestFocus();
        } else if (InputUtils.isNull(edt_re_enter_transcation_number.getText().toString())) {
            edt_re_enter_transcation_number.setError("Re-Enter Transaction Number");
            edt_re_enter_transcation_number.requestFocus();
        } else if (!edt_transaction_number.getText().toString().equals(edt_re_enter_transcation_number.getText().toString())) {
            edt_re_enter_transcation_number.setError("Transaction Number do not match");
            edt_re_enter_transcation_number.requestFocus();
        } else if (InputUtils.isNull(edt_cheque_number.getText().toString())) {
            edt_cheque_number.setError("Enter Cheque Number");
            edt_cheque_number.requestFocus();
        } else if (InputUtils.isNull(edt_re_enter_cheque_number.getText().toString())) {
            edt_re_enter_cheque_number.setError("Re-Enter Cheque Number");
            edt_re_enter_cheque_number.requestFocus();
        } else if (!edt_cheque_number.getText().toString().equals(edt_re_enter_cheque_number.getText().toString())) {
            edt_re_enter_cheque_number.setError("Checque Number do not match");
            edt_re_enter_cheque_number.requestFocus();
        } else if (InputUtils.isNull(edt_amount.getText().toString())) {
            edt_amount.setError("Enter Amount Number");
            edt_amount.requestFocus();
        } else if (InputUtils.isNull(edt_re_enter_amount.getText().toString())) {
            edt_re_enter_amount.setError("Re-Enter Amount");
            edt_re_enter_amount.requestFocus();
        } else if (!edt_amount.getText().toString().equals(edt_re_enter_amount.getText().toString())) {
            edt_re_enter_amount.setError("Amount do not match");
            edt_re_enter_amount.requestFocus();
        } else if (InputUtils.isNull(edt_remark.getText().toString())) {
            edt_remark.setError("Enter Remark");
            edt_remark.requestFocus();
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
            if (data == null) {
                //Display an error
                Logger.error("data: " + data);

                return;
            } else {

                try {
                    InputStream inputStream = activity.getContentResolver().openInputStream(data.getData());
                    Logger.error("inputStream: " + inputStream);
                    Uri uri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};

                    Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String picturePath = cursor.getString(columnIndex); // returns null
                    Logger.error("picturePath  :" + picturePath);
                    tv_no_file_chosen.setText("" + picturePath);
                    cursor.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //https://stackoverflow.com/questions/35028251/android-how-to-select-an-image-from-a-file-manager-after-clicking-on-a-button
                // Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
            }

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
}
