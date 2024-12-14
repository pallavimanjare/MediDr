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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.medidr.doctor.R;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocPersonalDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UplaodImageActivity extends AppCompatActivity {

    private static final String TAG = "UplaodImageActivity";



    private final Integer SELECT_FILE = 1;

    private final Integer CANCEL = 2;

    private View btnTakePhoto = null;

    private ImageView profileImage = null;

    private View btnNext = null;

    byte[] proflebyteArray = null;

    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uplaod_image);
        btnTakePhoto = findViewById(R.id.take_photo);
        profileImage = (ImageView)findViewById(R.id.doctor_profile_image);
        btnNext = findViewById(R.id.nxtButton);
        String doctorName = DataManager.getInstance().getPersonalDetails().getFullName();
        Log.i(TAG, "ATTRIBUTES from screen one " + doctorName);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.header_logo);
       /* toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

*/
        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //saveAndProceed();
                objNetwork = new NetworkUtilService(UplaodImageActivity.this);
                objAlert = new ShowAlertDialog();
                isConnectionExist = objNetwork.checkMobileInternetConn();
                Log.d(TAG, "internet is" + isConnectionExist);
                if (isConnectionExist) {
                    DoctorService service = new DoctorService();
                    DocDtls docDtls = DataManager.getDataManager().getDoctorDetails();

                    service.uploadImage(getApplicationContext(), proflebyteArray, docDtls, "profile");
                    docDtls.getDocPersonalDtls().setModified(true);
                    service.addDoctorPersonalDetails(getApplicationContext(), docDtls);

                    Toast.makeText(getApplicationContext(),
                            "Photo is Saved.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UplaodImageActivity.this, HospitalInformationActivity.class);
                    startActivity(intent);
                }
                else {
                    objAlert.showAlertDialog(UplaodImageActivity.this, "No Internet Connection",
                            "check your connection and try again", false);
                }
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch (v.getId()){
                    case R.id.doctor_profile_image:
                        selectImage();
                    default:
                        break;
                }
            }
        });
        btnTakePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch (v.getId()){
                    case R.id.take_photo:
                        selectImage();
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return false;
    }

    private void selectImage() {

        final CharSequence[] items = {"Choose from gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UplaodImageActivity.this);

        builder.setTitle(Html.fromHtml("<b><font color='#58ACFA'>Profile Photo!!</font></b>"))

                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Choose from gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            intent.putExtra("crop","true");
                            intent.putExtra("aspectX",0);
                            intent.putExtra("aspectY",0);
                            intent.putExtra("outputX",256);
                            intent.putExtra("outputY", 256);
                            try{

                                intent.putExtra("return-data",true);
                                startActivityForResult(Intent.createChooser(intent, "SELECT FILE"), SELECT_FILE);
                            }
                            catch(ActivityNotFoundException e){}

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Bundle extras  = data.getExtras();
        if (extras != null ){
            System.out.println("Request code ============" + requestCode + data.getData());

            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {
                if (requestCode == 1) {
                    Bitmap thumbnail = extras.getParcelable("data");//(Bitmap) data.getExtras().get("data");
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
                    profileImage.setImageBitmap(thumbnail);
                }
            }

            if (requestCode == 0) {
                System.out.println("Option 2 selected............................");
                Log.i(TAG, "Option 2");
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};

                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);
                System.out.print("Selected file path ========" + selectedImagePath);

                Bitmap bm;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;

                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                profileImage.setImageBitmap(bm);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                proflebyteArray = stream.toByteArray();

            }

        }
    }




    private void saveAndProceed(){

        DocPersonalDtls docDetails = DataManager.getInstance().getPersonalDetails();
        docDetails.setDoctorProfileThumbnailImage("http://www.img.com/hfdgnjr.png");
        docDetails.setDoctorProfileImage("http://www.img.com/main_img.png");
        Log.i(TAG, "Object after adding img" + docDetails);
        DataManager.getInstance().setPersonalDetails(null);
        DataManager.getInstance().setPersonalDetails(docDetails);
        DocDtls docDtls = new DocDtls();
        docDtls.setAuthDtls(DataManager.getInstance().getAuthDetails());
        docDtls.setDocPersonalDtls(docDetails);

        DoctorService service = new DoctorService();
        service.addDoctorPersonalDetails(this,docDtls);


    }

    private Boolean uploadImageToServer(){
        return true;
    }
}
