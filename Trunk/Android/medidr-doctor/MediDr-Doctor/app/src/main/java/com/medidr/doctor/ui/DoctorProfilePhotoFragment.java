package com.medidr.doctor.ui;


import android.content.ActivityNotFoundException;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.medidr.doctor.R;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorProfilePhotoFragment extends Fragment {

    private static final String TAG = "Logmessages";

    private final Integer REQUEST_CAMERA = 0;

    private final Integer SELECT_FILE = 1;

    private final Integer CANCEL = 2;

    private ImageButton btntakePhoto = null;

    private ImageView doctorProfileImage = null;

    private ImageButton nxtButton = null;

    byte[] proflebyteArray = null;

    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;


    public DoctorProfilePhotoFragment() {
        // Required empty public constructor
    }

    DocDtls docDtls = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        docDtls = DataManager.getDataManager().getDoctorDetails();
        View v = inflater.inflate(R.layout.fragment_doctor_profile_photo, container, false);


        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setIcon(R.mipmap.header_logo);
        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        // ((HomeActivity) getActivity()).toolbar.setNavigationIcon(R.mipmap.close_button_copy);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().onBackPressed();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        toolbar.setTitle("Edit Profile Photo");

        btntakePhoto = (ImageButton) v.findViewById(R.id.btntakePhoto);

        doctorProfileImage = (ImageView) v.findViewById(R.id.doctorProfileImage);

        nxtButton = (ImageButton) v.findViewById(R.id.nxtButton);

        String imagePath = docDtls.getDocPersonalDtls().getDoctorProfileImage();
        // doctorProfileImage.setImageURI(Uri.parse(imagePath));
        loadImageFromURL(imagePath);

        nxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveAndProceed();
                objNetwork = new NetworkUtilService(getActivity());
                objAlert = new ShowAlertDialog();
                isConnectionExist = objNetwork.checkMobileInternetConn();
                if (isConnectionExist) {
                    DoctorService service = new DoctorService();

                    service.uploadImage(getActivity(), proflebyteArray, docDtls, "profile");
                    docDtls.getDocPersonalDtls().setModified(true);
                    service.addDoctorPersonalDetails(getActivity(), docDtls);

                    Toast.makeText(getActivity(),
                            "Photo is Saved", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);

                } else {
                    objAlert.showAlertDialog(getActivity(), "No Internet Connection",
                            "check your connection and try again", false);
                }
            }
        });
        doctorProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btntakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        return v;
    }

    private void loadImageFromURL(String imageUrl) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
            doctorProfileImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectImage() {

        final CharSequence[] items = {"Choose from gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(Html.fromHtml("<b><font color='#58ACFA'>Profile Photo!!</font></b>"))

                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Choose from gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            //crop image code

                           intent.putExtra("crop","true");
                            intent.putExtra("aspectX",0);
                            intent.putExtra("aspectY",0);
                            intent.putExtra("outputX",256);
                            intent.putExtra("outputY",256);
                            try{

                                intent.putExtra("return-data",true);
                                startActivityForResult(Intent.createChooser(intent, "SELECT FILE"), SELECT_FILE);
                            }
                            catch(ActivityNotFoundException e){}
                            //done with crop image

                        }
                    }
                })
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle extras  = data.getExtras();
        if (extras != null){
            System.out.println("Request code ============" + requestCode + data.getData());

            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == getActivity().RESULT_OK) {
                if (requestCode == 1) {

                    Bitmap thumbnail =extras.getParcelable("data");// (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    proflebyteArray = bytes.toByteArray();
                    File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                    FileOutputStream fo;

                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();

                    } catch (FileNotFoundException e) {
                        Log.e("UPLOAD IMAGE", "Error occured while reading capturing image" + e);
                    } catch (IOException e) {
                        Log.e("UPLOAD IMAGE", "Error occured while reading capturing image" + e);
                    }
                    doctorProfileImage.setImageBitmap(thumbnail);
                }
            }

        }
    }



}
