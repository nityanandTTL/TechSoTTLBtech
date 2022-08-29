package com.thyrocare.btechapp.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mindorks.paracamera.Camera;
import com.thyrocare.btechapp.Controller.ClientEntryController;
import com.thyrocare.btechapp.NewScreenDesigns.Controllers.PostEmailValidationController;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ImagePicker;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.activity.HomeScreenActivity;

import application.ApplicationController;

import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.models.api.request.NewClientModel;
import com.thyrocare.btechapp.utils.app.Global;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CheckInternetConnectionMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.UnableToConnectMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.ValidEmailIdMsg;
import static com.thyrocare.btechapp.utils.app.AppConstants.BDNAPIKEY;

/**
 * A simple {@link Fragment} subclass.
 */

public class ClientEntryFragment extends Fragment {
    public static final String TAG_FRAGMENT = ClientEntryFragment.class.getSimpleName();
    public static String longitude, latitude, img_64;
    EditText edtName, edtMobile, edtEmail, edtAddress, edtPincode, edtInchargeName;
    Button btnSubmit;
    ImageView btnCamera;
    HomeScreenActivity mActivity;
    TextView txtLocation, btnUpload;
    NewClientModel newClientpostmodel;
    ProgressDialog progressDialog;
    private int Gallary = 1123;
    private int CameraCode = 2;
    private int PLACE_PICKER_REQUEST = 4;
    private Global globalClass;
    private ConnectionDetector cd;
    private Camera camera;

    public ClientEntryFragment() {
        // Required empty public constructor
    }

    public static String getFileToByte(String filePath) {
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try {
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;
    }

    public static ClientEntryFragment newInstance() {
        ClientEntryFragment fragment = new ClientEntryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Client Entry");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_client_entry, container, false);

        edtName = view.findViewById(R.id.edtName);
        edtMobile = view.findViewById(R.id.edtMobile);
        edtEmail = view.findViewById(R.id.edtEmail);
        txtLocation = view.findViewById(R.id.txtLocation);
        edtAddress = view.findViewById(R.id.edtAddress);
        edtPincode = view.findViewById(R.id.edtPincode);
        btnUpload = view.findViewById(R.id.btnUpload);
        btnCamera = view.findViewById(R.id.btnCamera);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        edtInchargeName = view.findViewById(R.id.edtInchargeName);
        mActivity = (HomeScreenActivity) getActivity();
        if (mActivity.toolbarHome != null) {
            mActivity.toolbarHome.setTitle("Client Entry");
        }

        globalClass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);

        progressDialog = new ProgressDialog(getContext());


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TedPermission.with(mActivity)
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .setRationaleMessage("We need permission to capture Image from your device.")
                        .setRationaleConfirmText("OK")
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > Permission > Camera, Storage")
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                ShowImagepickerDialog();
                            }

                            @Override
                            public void onPermissionDenied(List<String> deniedPermissions) {
                                Toast.makeText(mActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .check();


            }
        });

        txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!cd.isConnectingToInternet())
                    globalClass.showCustomToast(mActivity, CheckInternetConnectionMsg);
                else {

                    if (!Places.isInitialized()) {
                        Places.initialize(mActivity, getResources().getString(R.string.google_maps_key));
                    }
                    // Set the fields to specify which types of place data to return.
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
                    // Start the autocomplete intent.
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                            .build(mActivity);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                }


            }
        });
        edtPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str_pincode = charSequence.toString();
                if (str_pincode.startsWith("0") || str_pincode.startsWith("9")) {
                    if (str_pincode.length() > 0) {
                        edtPincode.setText(str_pincode.substring(1));
                    } else {
                        edtPincode.setText("");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str_mobileno = charSequence.toString();

                if (str_mobileno.startsWith("0") || str_mobileno.startsWith("1") || str_mobileno.startsWith("2") || str_mobileno.startsWith("3") || str_mobileno.startsWith("4") || str_mobileno.startsWith("5")) {
                    if (str_mobileno.length() > 0) {
                        edtMobile.setText(str_mobileno.substring(1));
                    } else {
                        edtMobile.setText("");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str_name = charSequence.toString();

                //Toast.makeText(getContext(),str_name.length()+"",Toast.LENGTH_LONG).show();
                if (str_name.startsWith(" ")) {
                    edtAddress.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str_name = charSequence.toString();

                //Toast.makeText(getContext(),str_name.length()+"",Toast.LENGTH_LONG).show();
                if (str_name.startsWith(" ")) {
                    edtName.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtInchargeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str_name = charSequence.toString();
                if (str_name.startsWith(" ")) {
                    edtInchargeName.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str_name = charSequence.toString();

                //Toast.makeText(getContext(),str_name.length()+"",Toast.LENGTH_LONG).show();
                if (str_name.startsWith(" ")) {
                    edtEmail.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cd.isConnectingToInternet()) {
                    CallEmailValidationApi(edtEmail.getText().toString().trim());
                } else {
                    globalClass.showCustomToast(mActivity, CheckInternetConnectionMsg);
                }
            }
        });


        return view;
    }

    private void CallEmailValidationApi(String EmailID) {
        try {
            if (ApplicationController.PostEmailValidationController != null) {
                ApplicationController.PostEmailValidationController = null;
            }

            ApplicationController.PostEmailValidationController = new PostEmailValidationController(mActivity);
            ApplicationController.PostEmailValidationController.CallEmailValdationAPI(EmailID);
            ApplicationController.PostEmailValidationController.setOnResponseListener(new PostEmailValidationController.OnResponseListener() {
                @Override
                public void onSuccess(boolean isEmailValid) {
                    if (submitEntry(isEmailValid)) {
                        call_newclient_entry_apicontroller();
                    }
                }

                @Override
                public void onfailure(String msg) {
                    globalClass.showcenterCustomToast(mActivity, UnableToConnectMsg, Toast.LENGTH_LONG);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean submitEntry(boolean isEmailValid) {
        String name = edtName.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String location = txtLocation.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String pincode = edtPincode.getText().toString().trim();
        String incharge = edtInchargeName.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            globalClass.showalert_OK("Enter Client Name", mActivity);
            edtName.requestFocus();
            return false;
        }
        if (!TextUtils.isEmpty(name) && name.trim().length() < 2) {
            globalClass.showalert_OK("Client name should be of minimum 2 characters", mActivity);
            edtName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(mobile)) {
            globalClass.showalert_OK("Enter Mobile No.", mActivity);
            edtMobile.requestFocus();
            return false;
        }
        if (mobile.startsWith("0") || mobile.startsWith("1") || mobile.startsWith("2") || mobile.startsWith("3") || mobile.startsWith("4") || mobile.startsWith("5")) {
            globalClass.showalert_OK("Mobile number should start with 6,7,8 or 9", mActivity);
            edtMobile.requestFocus();
            return false;
        }

        if (mobile.trim().length() != 10) {
            globalClass.showalert_OK("Mobile number should be of 10 digit", mActivity);
            edtMobile.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtEmail.getText())) {
            globalClass.showalert_OK("Enter Email Id", mActivity);
            edtEmail.requestFocus();
            return false;
        }
        if (!TextUtils.isEmpty(edtEmail.getText())) {
            if (edtEmail.getText().length() > 0 && !Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
                globalClass.showalert_OK("Enter valid Email Id", mActivity);
                edtEmail.requestFocus();
                return false;
            }

            if (!isEmailValid) {
                edtEmail.requestFocus();
                globalClass.showalert_OK(ValidEmailIdMsg, mActivity);
                return false;
            }

        }
        if (TextUtils.isEmpty(location)) {
            globalClass.showalert_OK("Select Location", mActivity);
            txtLocation.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            globalClass.showalert_OK("Enter Address", mActivity);
            edtAddress.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(pincode)) {
            globalClass.showalert_OK("Enter Pincode", mActivity);
            edtPincode.requestFocus();
            return false;
        }
        if (pincode.length() != 6) {
            globalClass.showalert_OK("Pincode should be of 6 digits", mActivity);
            edtPincode.requestFocus();
            return false;
        }
        if (pincode.startsWith("0") || pincode.startsWith("9")) {
            globalClass.showalert_OK("Pincode should not start with 0 or 9", mActivity);
            edtPincode.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(incharge)) {
            globalClass.showalert_OK("Enter Incharge Name", mActivity);
            edtInchargeName.requestFocus();
            return false;
        }
        if (!TextUtils.isEmpty(incharge) && incharge.trim().length() < 2) {
            globalClass.showalert_OK("Incharge name should be of minimum 2 characters", mActivity);
            edtInchargeName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(btnUpload.getText().toString())) {
            globalClass.showalert_OK("Please upload image to proceed", mActivity);
            edtInchargeName.requestFocus();
            return false;
        }


        return true;
    }

    private void postapi_call() {
        newClientpostmodel = new NewClientModel();
        newClientpostmodel.setAddress(edtAddress.getText().toString().replace("+", ""));
        newClientpostmodel.setBrand("TTL");
        newClientpostmodel.setChannel("MOBILE APPLICATION");
        newClientpostmodel.setCountry("India");
        newClientpostmodel.setEmail(edtEmail.getText().toString());
        newClientpostmodel.setInchargeName(edtInchargeName.getText().toString());
        newClientpostmodel.setName(edtName.getText().toString());
        newClientpostmodel.setTspOLc("MKG00");
        newClientpostmodel.setVerificationSource("");
        newClientpostmodel.setApikey(BDNAPIKEY);
        newClientpostmodel.setMobile(edtMobile.getText().toString());
        newClientpostmodel.setPhone_no(edtMobile.getText().toString());
        newClientpostmodel.setEntryType("SGC");
        newClientpostmodel.setPincode(edtPincode.getText().toString());
        newClientpostmodel.setWebsite("");
        newClientpostmodel.setAPP("BDN-LeggyT");
        newClientpostmodel.setFile(btnUpload.getText().toString());
        newClientpostmodel.setOpType("entry");
        newClientpostmodel.setLatitute(latitude);
        newClientpostmodel.setLongitude(longitude);
        newClientpostmodel.setVisiting_Card(img_64);
    }

    private void call_newclient_entry_apicontroller() {

        if (ApplicationController.clientEntryController != null) {
            ApplicationController.clientEntryController = null;
        }
        postapi_call();
        ApplicationController.clientEntryController = new ClientEntryController(getActivity(), this);
        ApplicationController.clientEntryController.PostRegisterapicall(newClientpostmodel);
    }

    public void RefreshFields() {
        txtLocation.setText("");
        edtPincode.setText("");
        edtAddress.setText("");
        edtName.setText("");
        edtName.setText("");
        edtMobile.setText("");
        edtEmail.setText("");
        btnUpload.setText("");
        edtInchargeName.setText("");
        btnCamera.setImageResource(R.drawable.ic_menu_camera);

        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setMessage("Client Registered Successfully")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        getActivity().onBackPressed();

                    }
                });
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void ShowImagepickerDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Select")
                .setMessage("Choose any one ")
                .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showGallery();
                    }
                })
                .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showCamera();
                    }
                })
                .show();

    }


    private void showCamera() {

        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("BtechApp/ClientEntry")
                .setName("Client_" + System.currentTimeMillis())
                .setImageFormat(com.mindorks.paracamera.Camera.IMAGE_JPEG)
                .setCompression(60)
                .setImageHeight(480)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showGallery() {
       /* Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, Gallary);*/

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallary);


    }


    private boolean checkpermissionLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == PLACE_PICKER_REQUEST) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                txtLocation.setText(place.getName());
                txtLocation.setError(null);
                edtAddress.setText(place.getAddress());
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    longitude = String.valueOf(place.getLatLng().longitude);
                    latitude = String.valueOf(place.getLatLng().latitude);
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    for (Address address : addresses) {
                        if (address.getLocality() != null && address.getPostalCode() != null) {
                            edtPincode.setText(address.getPostalCode());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
                String imageurl = "";
                try {
                    imageurl = camera.getCameraBitmapPath();
                    File ClientPicimagefile = new File(imageurl);
                    if (ClientPicimagefile.exists()) {
                        btnCamera.setImageBitmap(camera.getCameraBitmap());
                        String imagename = ClientPicimagefile.getName();
                        btnUpload.setText(imagename);
                        btnUpload.setError(null);
                        img_64 = getFileToByte(ClientPicimagefile.getPath());
                    } else {
                        Toast.makeText(mActivity, "Failed to capture photo", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == Gallary) {
                final Uri imageuri = data.getData();
                try {
                    /*final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageuri);
                    final Bitmap selectImage = BitmapFactory.decodeStream(imageStream);*/
                    final Bitmap selectImage = ImagePicker.getImageFromResult(mActivity, resultCode, data);
                    ;
                    btnCamera.setImageBitmap(selectImage);
                    Uri selectedimage = data.getData();
                    String image_Path = getPath(getContext().getContentResolver(), selectedimage);
                    File f = new File(image_Path);
                    String imageName = f.getName();
                    btnUpload.setText(imageName);
                    btnUpload.setError(null);
                    img_64 = getFileToByte(f.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private Object getImageUri(Context context, Bitmap imagebitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagebitmap, "client_pic", null);
        return Uri.parse(path);
    }

    private String getPath(ContentResolver contentResolver, Uri selectedimage) {

        if (selectedimage != null) {
            Cursor cursor = contentResolver.query(selectedimage, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                return selectedimage.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(idx);
            }
        }
        return "No Data found";
    }


}