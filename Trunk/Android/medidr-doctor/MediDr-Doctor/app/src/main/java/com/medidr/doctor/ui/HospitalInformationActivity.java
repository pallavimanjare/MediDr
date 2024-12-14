package com.medidr.doctor.ui;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.text.Html;



import com.medidr.doctor.R;
import com.medidr.doctor.model.AddressDtls;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocHospitalDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;
import com.medidr.doctor.ui.validation.UiValidation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HospitalInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Hospital Information Activity";

    private View nxtButton = null;

    private ImageButton arrow_right_button, arrow_left_button;

    private EditText rhospitalName, rhospitalAddress, rhospitalPincode, rhospitalReceptionNo;



    private final Integer SELECT_FILE = 1;

    private ImageView image1 = null;

    private ImageView image2 = null;

    private ImageView image3 = null;

    private ImageView image4 = null;

    private ImageView mainDisplayImage = null;

    private Integer displayImageNumber = 0;
    private Integer currentImageNumber = -1;
    private Map<Integer, byte[]> imgsMap = new HashMap<Integer, byte[]>();

    byte[] mainDisplayImgByteArray = null;

    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_information);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.header_logo);
      /*  toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*/
        initUI();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return false;
    }

    private void initUI() {


        rhospitalName = (EditText) findViewById(R.id.hospital_name);

        rhospitalAddress = (EditText) findViewById(R.id.hospital_address);

        rhospitalPincode = (EditText) findViewById(R.id.hospital_pincode);

        rhospitalReceptionNo = (EditText) findViewById(R.id.hospital_reception_number);

        arrow_right_button = (ImageButton) findViewById(R.id.arrow_right_button);
        arrow_left_button = (ImageButton) findViewById(R.id.arrow_left_button);

        arrow_left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (displayImageNumber == 5) {
                    displayImageNumber = 1;
                }
                changeImage();
                displayImageNumber++;
            }
        });
        arrow_right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (displayImageNumber == -1) {
                    displayImageNumber = 1;
                }
                changeImage();
                displayImageNumber--;
            }
        });

        image1 = (ImageView) findViewById(R.id.add_image_1);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_image_1:
                        currentImageNumber = 1;
                        selectImage();
                    default:
                        break;
                }
            }
        });


        image2 = (ImageView) findViewById(R.id.add_image_2);

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_image_2:
                        currentImageNumber = 2;
                        selectImage();
                    default:
                        break;
                }
            }
        });

        image3 = (ImageView) findViewById(R.id.add_image_3);

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_image_3:
                        currentImageNumber = 3;
                        selectImage();
                    default:
                        break;
                }
            }
        });

        image4 = (ImageView) findViewById(R.id.add_image_4);

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_image_4:
                        currentImageNumber = 4;
                        selectImage();
                    default:
                        break;
                }
            }
        });

        mainDisplayImage = (ImageView) findViewById(R.id.mainDisplayImage);

        nxtButton = findViewById(R.id.nxtButton);
        nxtButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nxtButton:
                if (!checkRequiredValidation()) {

                } else if (rhospitalPincode.length() != 6) {
                    rhospitalPincode.setError("Enter 6 digit pincode");

                } else {
                    addHospitalDetails();
                }
                break;

            default:
                break;
        }
    }

    //, , ,

    private boolean checkRegxValidation() {
        boolean ret = true;

        if (!UiValidation.isValidMobileNumber(rhospitalReceptionNo))
            ret = false;
        if (!UiValidation.isValidPincode(rhospitalPincode))
            ret = false;

        return ret;

    }

    public void changeImage() {
        mainDisplayImage = (ImageView) findViewById(R.id.mainDisplayImage);
        byte[] byteArray = null;
        switch (displayImageNumber) {
            case 1:
                if(imgsMap.get(1)!=null) {
                    byteArray = imgsMap.get(1);
                    image1.setBackgroundResource(R.mipmap.add_pressed);
                    image2.setBackgroundResource(R.mipmap.add_normal);
                    image3.setBackgroundResource(R.mipmap.add_normal);
                    image4.setBackgroundResource(R.mipmap.add_normal);
                }
                break;
            case 2 :
                if(imgsMap.get(2)!=null) {
                    byteArray = imgsMap.get(2);
                    image2.setBackgroundResource(R.mipmap.add_pressed);
                    image1.setBackgroundResource(R.mipmap.add_normal);
                    image3.setBackgroundResource(R.mipmap.add_normal);
                    image4.setBackgroundResource(R.mipmap.add_normal);
                }
                break;
            case 3:
                if(imgsMap.get(3)!=null) {
                    byteArray = imgsMap.get(3);
                    image3.setBackgroundResource(R.mipmap.add_pressed);
                    image2.setBackgroundResource(R.mipmap.add_normal);
                    image1.setBackgroundResource(R.mipmap.add_normal);
                    image4.setBackgroundResource(R.mipmap.add_normal);
                }
                break;
            case 4:
                if(imgsMap.get(4)!=null) {
                    byteArray = imgsMap.get(4);
                    image4.setBackgroundResource(R.mipmap.add_pressed);
                    image2.setBackgroundResource(R.mipmap.add_normal);
                    image3.setBackgroundResource(R.mipmap.add_normal);
                    image1.setBackgroundResource(R.mipmap.add_normal);
                }
                break;
        }
        if(byteArray!=null)
        {
            displayImage(byteArray);
        }
    }

    public  void displayImage(byte[] byteArray)
    {

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView mainDisplayImage = (ImageView) findViewById(R.id.mainDisplayImage);
        mainDisplayImage.setImageBitmap(bmp);
    }
    private boolean checkRequiredValidation() {
        boolean ret = true;

        if (!UiValidation.hasText(rhospitalName))
            ret = false;

        if (!UiValidation.hasText(rhospitalAddress))
            ret = false;

        if (!UiValidation.hasText(rhospitalPincode))
            ret = false;
        if (!UiValidation.hasText(rhospitalReceptionNo))
            ret = false;

        return ret;
    }

    private void addHospitalDetails() {

        objNetwork = new NetworkUtilService(HospitalInformationActivity.this);
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();


        if (isConnectionExist) {

            if (!checkRequiredValidation()) {
            }
            /*else if (!checkRegxValidation())
            {
            }*/
            else if (rhospitalPincode.length() != 6) {
                rhospitalPincode.setError("pincode should be 6 digit");
            } else {

                List<byte[]> hospitalImgsLst = new ArrayList<byte[]>();
                Iterator<Integer> iter = imgsMap.keySet().iterator();
                while (iter.hasNext()) {
                    Integer key = iter.next();
                    hospitalImgsLst.add(imgsMap.get(key));
                }

                DocDtls docDtls = DataManager.getDataManager().getDoctorDetails();
                DocHospitalDtls hospitalDtls = DataManager.getDataManager().getHospitalDetails();
                if (hospitalDtls == null) {
                    hospitalDtls = new DocHospitalDtls();
                    hospitalDtls.setDocHospId(0L);
                }
                hospitalDtls.setDoctorId(docDtls.getAuthDtls().getUserId());
                hospitalDtls.setHospitalContactNumber(rhospitalReceptionNo.getText().toString());
                hospitalDtls.setHospitalName(rhospitalName.getText().toString());

                AddressDtls addressDtls = new AddressDtls();
                addressDtls.setAddressId(0L);
                addressDtls.setZipCode(new Integer(rhospitalPincode.getText().toString()));
                addressDtls.setTextAddress(rhospitalAddress.getText().toString());
                hospitalDtls.setAddressDtls(addressDtls);

                //TODO need to rethink on this
                docDtls.setAddressDtls(addressDtls);

                docDtls.setDocHospitalDtls(hospitalDtls);
                DoctorService service = new DoctorService();
                service.addDoctorHospitalDetails(getApplicationContext(), docDtls, "hospital", hospitalImgsLst);

                Toast.makeText(getApplicationContext(),
                        "Your record is Saved", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(HospitalInformationActivity.this, WeeklySchedule.class);
                startActivity(intent);
            }

/*        Intent intent = new Intent(getApplicationContext(), HospitalServicesActivity.class);
        startActivity(intent);*/
        } else {
            objAlert.showAlertDialog(HospitalInformationActivity.this, "No Internet Connection",
                    "check your connection and try again", false);
        }
    }


    private void selectImage() {

        final CharSequence[] items = {"Choose from gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(HospitalInformationActivity.this);

        builder.setTitle(Html.fromHtml("<b><font color='#58ACFA'>Profile Photo!!</font></b>"))

                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Choose from gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "SELECT FILE"), SELECT_FILE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.getData()!=null) {
            System.out.println("Request code ============" + requestCode + data.getData());

            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {
                if (requestCode == 0) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    mainDisplayImgByteArray = bytes.toByteArray();
                    imgsMap.put(currentImageNumber, mainDisplayImgByteArray);

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
                    mainDisplayImage.setImageBitmap(thumbnail);
                    setCssToAddButtonOnClick(currentImageNumber);

                }
            }

            if (requestCode == 1) {
                System.out.println("Option 2 selected............................");
                //Log.i(TAG,"Option 2");
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

                mainDisplayImage.setImageBitmap(bm);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bm.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                mainDisplayImgByteArray = stream.toByteArray();
                imgsMap.put(currentImageNumber, mainDisplayImgByteArray);
                setCssToAddButtonOnClick(currentImageNumber);
            }
        }

    }



    public  void setCssToAddButtonOnClick(int currentImageNumber)
    {
        switch (currentImageNumber)
        {
            case 1:
                image1.setBackgroundResource(R.mipmap.add_pressed);
                image2.setBackgroundResource(R.mipmap.add_normal);
                image3.setBackgroundResource(R.mipmap.add_normal);
                image4.setBackgroundResource(R.mipmap.add_normal);
                break;
            case 2:
                image2.setBackgroundResource(R.mipmap.add_pressed);
                image1.setBackgroundResource(R.mipmap.add_normal);
                image3.setBackgroundResource(R.mipmap.add_normal);
                image4.setBackgroundResource(R.mipmap.add_normal);
                break;
            case 3:
                image3.setBackgroundResource(R.mipmap.add_pressed);
                image2.setBackgroundResource(R.mipmap.add_normal);
                image1.setBackgroundResource(R.mipmap.add_normal);
                image4.setBackgroundResource(R.mipmap.add_normal);
                break;
            case 4:
                image4.setBackgroundResource(R.mipmap.add_pressed);
                image2.setBackgroundResource(R.mipmap.add_normal);
                image3.setBackgroundResource(R.mipmap.add_normal);
                image1.setBackgroundResource(R.mipmap.add_normal);
                break;
        }
    }
    private void saveAndProceed() {

        DocHospitalDtls hospitalDetails = new DocHospitalDtls();
        hospitalDetails.setHospitalName(rhospitalName.getText().toString());
//        hospitalDetails.setAddressDtls(rhospitalAddress.getText().toString());
        hospitalDetails.setHospitalContactNumber(rhospitalReceptionNo.getText().toString());
//        hospitalDetails.setHospitalPincode(Integer.parseInt(rhospitalPincode.getText().toString()));

        DataManager.getDataManager().setHospitalDetails(hospitalDetails);
        Intent intent = new Intent(HospitalInformationActivity.this, WeeklySchedule.class);
        this.startActivity(intent);
        finish();

    }


}