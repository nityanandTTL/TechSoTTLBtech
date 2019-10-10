package com.thyrocare.fragment;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.thyrocare.Controller.ClientEntryController;
import com.thyrocare.R;
import com.thyrocare.application.ApplicationController;
import com.thyrocare.models.api.request.NewClientModel;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.thyrocare.utils.app.AppConstants.BDNAPIKEY;

/**
 * A simple {@link Fragment} subclass.
 */

public class ClientEntryFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    public static final String TAG_FRAGMENT = ClientEntryFragment.class.getSimpleName();
    EditText edtName, edtMobile, edtEmail, edtAddress, edtPincode, edtInchargeName;
    TextView txtLocation,btnUpload;
    Button btnSubmit;
    ImageView btnCamera;


    private int Cmera_REQUEST_CODE = 100;
    private int Storage_Request_Code = 101;
    private  int Gallary  = 1;
    private  int Camera  = 2;
    NewClientModel newClientpostmodel;
    public static String longitude,latitude,img_64;
    ProgressDialog progressDialog;

    static ClientEntryFragment fragment;
    public  static  final String TAG_Fragment = "NEW_CLIENT_ENTRY";
    private GoogleApiClient mgoogleApiClient;
    private  int PLACE_PICKER_REQUEST = 4;

    public ClientEntryFragment() {
        // Required empty public constructor
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


        mgoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), 0, this)
                .addConnectionCallbacks(this)
                .build();
        progressDialog = new ProgressDialog(getContext());






        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowImagepickerDialog();
                if (checkImagepermission()){

                }
                else {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},1);
                }

            }
        });

        txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkpermissionLocation()){

                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try{
                        startActivityForResult(builder.build(getActivity()),PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
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
                if (str_pincode.startsWith("0")||str_pincode.startsWith("9")){
                    if (str_pincode.length()>0){
                        edtPincode.setText(str_pincode.substring(1));
                    }else {
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
                String str_mobileno  =  charSequence.toString();

                if (str_mobileno.startsWith("0")||str_mobileno.startsWith("1")||str_mobileno.startsWith("2")||str_mobileno.startsWith("3")||str_mobileno.startsWith("4")||str_mobileno.startsWith("5")){
                    if (str_mobileno.length()>0){
                        edtMobile.setText(str_mobileno.substring(1));
                    }else {
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
                String str_name  =  charSequence.toString();

                //Toast.makeText(getContext(),str_name.length()+"",Toast.LENGTH_LONG).show();
                if (str_name.startsWith(" ")){
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
                String str_name  =  charSequence.toString();

                //Toast.makeText(getContext(),str_name.length()+"",Toast.LENGTH_LONG).show();
                if (str_name.startsWith(" ")){
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
                String str_name  =  charSequence.toString();
                if (str_name.startsWith(" ")){
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
                String str_name  =  charSequence.toString();

                //Toast.makeText(getContext(),str_name.length()+"",Toast.LENGTH_LONG).show();
                if (str_name.startsWith(" ")){
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
                if(submitEntry()){
                    call_newclient_entry_apicontroller();
                }
            }
        });


        return view;
    }

    public boolean submitEntry() {
        String name = edtName.getText().toString();
        String mobile = edtMobile.getText().toString();
        String email = edtEmail.getText().toString();
        String location = txtLocation.getText().toString();
        String address = edtAddress.getText().toString();
        String pincode = edtPincode.getText().toString();
        String incharge = edtInchargeName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Enter Client Name");
            edtName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(mobile)) {
            edtMobile.setError("Enter Mobile No.");
            edtMobile.requestFocus();
            return false;
        } else if (!TextUtils.isEmpty(email)){
            if (edtEmail.getText().length()>0&&! Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText()).matches()){
                edtEmail.setError("Enter Valid Email Id");
                return false;
            }
        }else if (TextUtils.isEmpty(location)) {
            txtLocation.setError("Select Location");
            txtLocation.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(address)) {
            edtAddress.setError("Enter Address");
            edtAddress.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(pincode)) {
            edtPincode.setError("Enter Pincode");
            edtPincode.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(incharge)) {
            edtInchargeName.setError("Enter Incharge Name");
            edtInchargeName.requestFocus();
            return false;
        }else if (TextUtils.isEmpty(btnUpload.getText().toString())){
            btnUpload.setError("Please upload image to proceed");
            edtInchargeName.requestFocus();
        }
        return true;
    }

    public static ClientEntryFragment newInstance() {
        ClientEntryFragment fragment = new ClientEntryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void postapi_call() {
        newClientpostmodel = new NewClientModel();
        newClientpostmodel.setAddress(edtAddress.getText().toString());
        newClientpostmodel.setBrand("TTL");
        newClientpostmodel.setChannel("MOBILE APPLICATION");
        newClientpostmodel.setCountry( "India");
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
        newClientpostmodel.setOpType( "entry");
        newClientpostmodel.setLatitute(latitude);
        newClientpostmodel.setLongitude(longitude);
        newClientpostmodel.setVisiting_Card(img_64);
    }

    private void call_newclient_entry_apicontroller() {

        if (ApplicationController.clientEntryController != null){
            ApplicationController.clientEntryController = null;
        }
        postapi_call();
        ApplicationController.clientEntryController = new ClientEntryController(getContext(),this);
        ApplicationController.clientEntryController.PostRegisterapicall(newClientpostmodel);
    }

    public  void RefreshFields() {
        txtLocation.setText("");
        edtPincode.setText("");
        edtAddress.setText("");
        edtName.setText("");
        edtName.setText("");
        edtMobile.setText("");
        btnUpload.setText("");
        edtInchargeName.setText("");
        btnCamera.setImageResource(R.drawable.ic_menu_camera);

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setMessage("Clint Registered Successfully")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        getActivity().onBackPressed();

                    }
                });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private boolean checkImagepermission() {

        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            return true;

        }
        else return false;
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
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraintent,Camera);
    }

    private void showGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,Gallary);


    }

    private boolean checkpermissionLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            return false;
        }
        else return true;
    }



    @Override
    public void onStart() {
        super.onStart();
        mgoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mgoogleApiClient.disconnect();
        super.onStop();
    }

/*    @Override
   public void onConnectionFailed(@NonNull ConnectionResult connectionResult){
       Snackbar.make(tv_place_picker_client,connectionResult.getErrorMessage()+"",Snackbar.LENGTH_LONG).show();

   }*/

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!= null){

            if(requestCode == PLACE_PICKER_REQUEST){
                Place place = (Place) PlacePicker.getPlace(getContext(),data);
                txtLocation.setText(place.getName());
                txtLocation.setError(null);
                edtAddress.setText(place.getAddress());
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    longitude = String.valueOf(place.getLatLng().longitude);
                    latitude = String.valueOf(place.getLatLng().latitude);
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude,1);
                    for (Address address:addresses){
                        if (address.getLocality()!=null&&address.getPostalCode()!=null){
                            edtPincode.setText(address.getPostalCode());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
            else if (requestCode == Camera){
                Bundle extras = data.getExtras();
                // Uri selectedimage = data.getData();
                Bitmap imagebitmap = (Bitmap) extras.get("data");
                Uri selectedimage = (Uri) getImageUri(getContext(), imagebitmap);

                btnCamera.setImageBitmap(imagebitmap);
                /*String image_Path = getPath(getContext().getContentResolver(),selectedimage);
                tv_imagename.setText(image_Path);*/
                File finalFile = new File(getPath(getContext().getContentResolver(),selectedimage));
                String imagename = finalFile.getName();
                btnUpload.setText(imagename);
                btnUpload.setError(null);
                img_64 = getFileToByte(finalFile.getPath());

            }
            else if (requestCode == Gallary){
                final Uri imageuri = data.getData();
                try {
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageuri);
                    final  Bitmap   selectImage = BitmapFactory.decodeStream(imageStream);
                    btnCamera.setImageBitmap(selectImage);
                    Uri selectedimage = data.getData();
                    String image_Path = getPath(getContext().getContentResolver(),selectedimage);
                    File f = new File(image_Path);
                    String imageName = f.getName();
                    btnUpload.setText(imageName);
                    btnUpload.setError(null);
                    img_64 = getFileToByte(f.getPath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

    }
    public static String getFileToByte(String filePath){
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try{
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
    }

    private Object getImageUri(Context context, Bitmap imagebitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagebitmap, "leggyT_client_pic", null);
        return Uri.parse(path);
    }

    private String getPath(ContentResolver contentResolver, Uri selectedimage) {

      /*  String result = null;
        String  proj[] = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(selectedimage,proj,null,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                int column_indx =cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_indx);
            }
            cursor.close();
        }
        if (result ==null){
            result = "Not found";

        }
        return result;*/
        if (selectedimage!=null) {


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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPause() {
        super.onPause();

        mgoogleApiClient.stopAutoManage(getActivity());
        mgoogleApiClient.disconnect();
    }
}