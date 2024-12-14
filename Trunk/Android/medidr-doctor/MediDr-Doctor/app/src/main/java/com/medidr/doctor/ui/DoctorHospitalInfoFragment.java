package com.medidr.doctor.ui;


import android.app.Fragment;
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
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medidr.doctor.R;
import com.medidr.doctor.model.AddressDtls;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocHospitalDtls;
import com.medidr.doctor.model.ImageMasterDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;
import com.medidr.doctor.ui.validation.UiValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorHospitalInfoFragment extends Fragment {

    private static final String TAG = "Logmessages";

    ImageButton nxtButton;
    EditText hospitalName;
    EditText hospitalAddress;
    EditText hospitalPincode;
    EditText hospitalReceptionNumber;


    ImageView addImage1;
    ImageView addImage2;
    ImageView addImage3;
    ImageView addImage4;

    Bitmap thumbnailBitmap1 = null;
    Bitmap thumbnailBitmap2 = null;
    Bitmap thumbnailBitmap3 = null;
    Bitmap thumbnailBitmap4 = null;

    Bitmap largeImageBitmap1 = null;
    Bitmap largeImageBitmap2 = null;
    Bitmap largeImageBitmap3 = null;
    Bitmap largeImageBitmap4 = null;

    Bitmap fullImage = null;



    ImageView mainDisplayImage;

    private Integer displayImageNumber = 0;
    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    private final Integer REQUEST_CAMERA = 0;

    private final Integer SELECT_FILE = 1;


    private Integer currentImageNumber = 1;
    private Map<Integer, byte[]> imgsMap = new HashMap<Integer, byte[]>();
    private Map<Integer, String> persistedImgsMap = new HashMap<Integer, String>();

    ImageButton arrowLeftButton,arrowRightButton;
    byte[] mainDisplayImgByteArray = null;

    public DoctorHospitalInfoFragment() {
        // Required empty public constructor
    }

    DocDtls docDtls = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        docDtls = DataManager.getDataManager().getDoctorDetails();

        View v = inflater.inflate(R.layout.fragment_doctor_hospital_info, container, false);

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

        toolbar.setTitle("Edit Hospital Information");
        nxtButton = (ImageButton) v.findViewById(R.id.nxtButton);
        hospitalName = (EditText) v.findViewById(R.id.hospitalName);
        hospitalAddress = (EditText) v.findViewById(R.id.hospitalAddress);
        hospitalPincode = (EditText) v.findViewById(R.id.hospitalPincode);
        hospitalReceptionNumber = (EditText) v.findViewById(R.id.hospitalReceptionNumber);
        addImage1 = (ImageView) v.findViewById(R.id.addImage1);
        addImage2 = (ImageView) v.findViewById(R.id.addImage2);
        addImage3 = (ImageView) v.findViewById(R.id.addImage3);
        addImage4 = (ImageView) v.findViewById(R.id.addImage4);
        arrowLeftButton = (ImageButton) v.findViewById(R.id.arrowLeftButton);
        arrowRightButton = (ImageButton) v.findViewById(R.id.arrowRightButton);
        mainDisplayImage = (ImageView) v.findViewById(R.id.mainDisplayImage);

        setHospitalInformation();

        addImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImageNumber = 1;
                mainDisplayImage.setImageBitmap(largeImageBitmap1);
                addImage1.setBackgroundResource(R.mipmap.add_pressed);
                addImage2.setBackgroundResource(R.mipmap.add_normal);
                addImage3.setBackgroundResource(R.mipmap.add_normal);
                addImage4.setBackgroundResource(R.mipmap.add_normal);
            }
        });
        addImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImageNumber = 2;
                mainDisplayImage.setImageBitmap(largeImageBitmap2);
                addImage2.setBackgroundResource(R.mipmap.add_pressed);
                addImage1.setBackgroundResource(R.mipmap.add_normal);
                addImage3.setBackgroundResource(R.mipmap.add_normal);
                addImage4.setBackgroundResource(R.mipmap.add_normal);
            }
        });
        addImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImageNumber = 3;
                mainDisplayImage.setImageBitmap(largeImageBitmap3);
                addImage3.setBackgroundResource(R.mipmap.add_pressed);
                addImage2.setBackgroundResource(R.mipmap.add_normal);
                addImage1.setBackgroundResource(R.mipmap.add_normal);
                addImage4.setBackgroundResource(R.mipmap.add_normal);
            }
        });
        addImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImageNumber = 4;
                mainDisplayImage.setImageBitmap(largeImageBitmap4);
                addImage4.setBackgroundResource(R.mipmap.add_pressed);
                addImage2.setBackgroundResource(R.mipmap.add_normal);
                addImage3.setBackgroundResource(R.mipmap.add_normal);
                addImage1.setBackgroundResource(R.mipmap.add_normal);
            }
        });

        mainDisplayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        nxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkRequiredValidation()){}
                else {
                    addHospitalDetails();
                }
            }
        });

        arrowLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (displayImageNumber == 5 || displayImageNumber == 0) {
                    displayImageNumber = 1;
                }
                changeImage();
                displayImageNumber++;
            }
        });
        arrowRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (displayImageNumber == -1 || displayImageNumber == 0 ) {
                    displayImageNumber = 5;
                }
                changeImage();
                displayImageNumber--;
            }
        });

        return v;
    }

    public void changeImage() {

        switch (displayImageNumber) {
            case 1:

                   mainDisplayImage.setImageBitmap(largeImageBitmap1);
                    addImage1.setBackgroundResource(R.mipmap.add_pressed);
                    addImage2.setBackgroundResource(R.mipmap.add_normal);
                    addImage3.setBackgroundResource(R.mipmap.add_normal);
                    addImage4.setBackgroundResource(R.mipmap.add_normal);

                break;
            case 2 :
                mainDisplayImage.setImageBitmap(largeImageBitmap2);
                    addImage2.setBackgroundResource(R.mipmap.add_pressed);
                    addImage1.setBackgroundResource(R.mipmap.add_normal);
                    addImage3.setBackgroundResource(R.mipmap.add_normal);
                    addImage4.setBackgroundResource(R.mipmap.add_normal);

                break;
            case 3:
                mainDisplayImage.setImageBitmap(largeImageBitmap3);
                addImage3.setBackgroundResource(R.mipmap.add_pressed);
                    addImage2.setBackgroundResource(R.mipmap.add_normal);
                    addImage1.setBackgroundResource(R.mipmap.add_normal);
                    addImage4.setBackgroundResource(R.mipmap.add_normal);

                break;
            case 4:
                mainDisplayImage.setImageBitmap(largeImageBitmap4);
                    addImage4.setBackgroundResource(R.mipmap.add_pressed);
                    addImage2.setBackgroundResource(R.mipmap.add_normal);
                    addImage3.setBackgroundResource(R.mipmap.add_normal);
                    addImage1.setBackgroundResource(R.mipmap.add_normal);

                break;
        }


}




    private boolean checkReqxValidation() {
        boolean ret = true;

        if (!UiValidation.isValidMobileNumber(hospitalReceptionNumber))
            ret = false;
        if (!UiValidation.isValidPincode(hospitalPincode))
            ret = false;

        return ret;

    }

    public void setHospitalInformation() {


        objNetwork = new NetworkUtilService(getActivity());
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();
        if (isConnectionExist) {

            DoctorService service = new DoctorService();
            Long doctorId = docDtls.getAuthDtls().getUserId();
            String path = "/doctors/getDoctorHospitalDtls/" + doctorId;
            String responseResult = service.getInformationForDoctor(path);

            if (!responseResult.equalsIgnoreCase("") && responseResult != null) {
                Log.d(TAG, "result is :" + responseResult);
                JSONObject jsonObject = null;
                try {
                    JSONObject jsonResponse = new JSONObject(responseResult);
                    String header = jsonResponse.getString("header");
                    Log.d(TAG, "header as :" + header);

                    JSONObject jsonObjectHeader = new JSONObject(header);
                    String statusCode = jsonObjectHeader.getString("statusCode");
                    String statusMessage = jsonObjectHeader.getString("statusMessage");
                    if (statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200")) {
                        try {
                            JSONObject data = jsonResponse.getJSONObject("data");
                            ObjectMapper objectMapper = new ObjectMapper();
                            DocDtls persistedDocDtls = objectMapper.readValue(data.toString(), DocDtls.class);
                            DocHospitalDtls docHospDtls = persistedDocDtls.getDocHospitalDtls();

                            // ImageMasterDtls imageMasterDtls = (ImageMasterDtls) persistedDocDtls.getDocHospitalDtls().getImageMasterDtls();
                            // docDtls.setDocHospitalDtls(persistedDocDtls.getDocHospitalDtls());

                            //docHospDtls.setImageMasterDtls((List<ImageMasterDtls>) imageMasterDtls);
                            //DataManager.getDataManager().setDoctorDetails(doctorDetails);
                            DataManager.getDataManager().getDoctorDetails().setDocHospitalDtls(docHospDtls);

                            docDtls.setDocHospitalDtls(docHospDtls);
                            Log.d(TAG, "Hospital Information as follows :" + docDtls.toString());

                            String strHospitalName = "";
                            String strHospAddress = "";
                            String strHospPincode = "";
                            String strHospContactNo = "";
                            if(docDtls.getDocHospitalDtls() != null) {
                                strHospitalName = docDtls.getDocHospitalDtls().getHospitalName();
                                strHospContactNo = docDtls.getDocHospitalDtls().getHospitalContactNumber();
                                if(docDtls.getDocHospitalDtls().getAddressDtls() != null) {
                                    strHospAddress = docDtls.getDocHospitalDtls().getAddressDtls().getTextAddress();
                                    strHospPincode = String.valueOf(docDtls.getDocHospitalDtls().getAddressDtls().getZipCode());
                                }
                            }

                            hospitalName.setText(strHospitalName);
                            hospitalAddress.setText(strHospAddress);
                            hospitalPincode.setText(strHospPincode);
                            hospitalReceptionNumber.setText(strHospContactNo);

                            List<ImageMasterDtls> hospitalImgsLst = new ArrayList<ImageMasterDtls>();
                            if(docDtls.getDocHospitalDtls() != null) {
                                hospitalImgsLst = docDtls.getDocHospitalDtls().getImageMasterDtls();

                                for (int count = 0; hospitalImgsLst != null && count < hospitalImgsLst.size(); count++) {
                                    String imageIdsStr = null;
                                    switch (count) {
                                        case 0:
                                            if (hospitalImgsLst.get(0).getImageType().equalsIgnoreCase("0")) {
                                                thumbnailBitmap1 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(0).getImageUrl()).openStream());
                                                imageIdsStr = hospitalImgsLst.get(0).getImageId().toString();
                                                if (hospitalImgsLst.get(1).getImageType().equalsIgnoreCase("1")) {
                                                    largeImageBitmap1 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(1).getImageUrl()).openStream());
                                                    imageIdsStr += "_" + hospitalImgsLst.get(1).getImageId().toString();
                                                    fullImage = largeImageBitmap1;
                                                    mainDisplayImage.setImageBitmap(fullImage);
                                                }
                                            } else {
                                                if (hospitalImgsLst.get(1).getImageType().equalsIgnoreCase("0")) {
                                                    thumbnailBitmap1 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(1).getImageUrl()).openStream());
                                                    imageIdsStr = hospitalImgsLst.get(1).getImageId().toString();
                                                }
                                                if (hospitalImgsLst.get(0).getImageType().equalsIgnoreCase("1")) {
                                                    largeImageBitmap1 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(0).getImageUrl()).openStream());
                                                    imageIdsStr += "_" + hospitalImgsLst.get(0).getImageId().toString();
                                                    fullImage = largeImageBitmap1;
                                                    mainDisplayImage.setImageBitmap(fullImage);
                                                }
                                            }
                                            persistedImgsMap.put(1, imageIdsStr);
                                            break;

                                        case 2:
                                            if (hospitalImgsLst.get(2).getImageType().equalsIgnoreCase("0")) {
                                                thumbnailBitmap2 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(2).getImageUrl()).openStream());
                                                imageIdsStr = hospitalImgsLst.get(2).getImageId().toString();
                                                if (hospitalImgsLst.get(3).getImageType().equalsIgnoreCase("1")) {
                                                    largeImageBitmap2 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(3).getImageUrl()).openStream());
                                                    imageIdsStr += "_" + hospitalImgsLst.get(3).getImageId().toString();
                                                }
                                            } else {
                                                if (hospitalImgsLst.get(3).getImageType().equalsIgnoreCase("0")) {
                                                    thumbnailBitmap2 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(3).getImageUrl()).openStream());
                                                    imageIdsStr = hospitalImgsLst.get(3).getImageId().toString();
                                                }
                                                if (hospitalImgsLst.get(2).getImageType().equalsIgnoreCase("1")) {
                                                    largeImageBitmap2 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(2).getImageUrl()).openStream());
                                                    imageIdsStr += "_" + hospitalImgsLst.get(2).getImageId().toString();
                                                }
                                            }
                                            persistedImgsMap.put(2, imageIdsStr);
                                            break;

                                        case 4:
                                            if (hospitalImgsLst.get(4).getImageType().equalsIgnoreCase("0")) {
                                                thumbnailBitmap3 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(4).getImageUrl()).openStream());
                                                imageIdsStr = hospitalImgsLst.get(4).getImageId().toString();
                                                if (hospitalImgsLst.get(5).getImageType().equalsIgnoreCase("1")) {
                                                    largeImageBitmap3 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(5).getImageUrl()).openStream());
                                                    imageIdsStr += "_" + hospitalImgsLst.get(5).getImageId().toString();
                                                }
                                            } else {
                                                if (hospitalImgsLst.get(5).getImageType().equalsIgnoreCase("0")) {
                                                    thumbnailBitmap3 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(5).getImageUrl()).openStream());
                                                    imageIdsStr = hospitalImgsLst.get(5).getImageId().toString();
                                                }
                                                if (hospitalImgsLst.get(4).getImageType().equalsIgnoreCase("1")) {
                                                    largeImageBitmap3 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(4).getImageUrl()).openStream());
                                                    imageIdsStr += "_" + hospitalImgsLst.get(4).getImageId().toString();
                                                }
                                            }
                                            persistedImgsMap.put(3, imageIdsStr);
                                            break;

                                        case 6:
                                            if (hospitalImgsLst.get(6).getImageType().equalsIgnoreCase("0")) {
                                                thumbnailBitmap4 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(6).getImageUrl()).openStream());
                                                imageIdsStr = hospitalImgsLst.get(6).getImageId().toString();
                                                if (hospitalImgsLst.get(7).getImageType().equalsIgnoreCase("1")) {
                                                    largeImageBitmap4 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(7).getImageUrl()).openStream());
                                                    imageIdsStr += "_" + hospitalImgsLst.get(7).getImageId().toString();
                                                }
                                            } else {
                                                if (hospitalImgsLst.get(7).getImageType().equalsIgnoreCase("0")) {
                                                    thumbnailBitmap4 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(7).getImageUrl()).openStream());
                                                    imageIdsStr = hospitalImgsLst.get(7).getImageId().toString();
                                                }
                                                if (hospitalImgsLst.get(6).getImageType().equalsIgnoreCase("1")) {
                                                    largeImageBitmap4 = BitmapFactory.decodeStream(new URL(hospitalImgsLst.get(6).getImageUrl()).openStream());
                                                    imageIdsStr += "_" + hospitalImgsLst.get(6).getImageId().toString();
                                                }
                                            }
                                            persistedImgsMap.put(4, imageIdsStr);
                                            break;

                                        default:
                                            break;
                                    }
                                }

                            }


                            Log.d(TAG, "get all information");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {
            objAlert.showAlertDialog(getActivity(), "No Internet Connection",
                    "check your connection and try again", false);
        }
    }

    private void selectImage() {

        final CharSequence[] items = {"Choose from gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(Html.fromHtml("<b><font color='#58ACFA'>Hospital Photo!!</font></b>"))

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.getData()!=null) {
            System.out.println("Request code ============" + requestCode + data.getData());

            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == getActivity().RESULT_OK) {
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
                }
            }

            if (requestCode == 1) {
                System.out.println("Option 2 selected............................");
                //Log.i(TAG,"Option 2");
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};

                CursorLoader cursorLoader = new CursorLoader(getActivity(), selectedImageUri, projection, null, null, null);
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
            }

        }
    }

    private boolean checkRequiredValidation() {
        boolean ret = true;

        if (!UiValidation.hasText(hospitalName))
            ret = false;

        if (!UiValidation.hasText(hospitalAddress))
            ret = false;

        if (!UiValidation.hasText(hospitalPincode))
            ret = false;
        if (!UiValidation.hasText(hospitalPincode))
            ret = false;

        return ret;
    }
    private void addHospitalDetails() {

/*        List<byte[]> hospitalImgsLst = new ArrayList<byte[]>();
        Iterator<Integer> iter = imgsMap.keySet().iterator();
        while (iter.hasNext()){
            Integer key = iter.next();
            hospitalImgsLst.add(imgsMap.get(key));
        }*/

        objNetwork = new NetworkUtilService(getActivity());
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();
        if (isConnectionExist) {

            //set hospital address details
            DocHospitalDtls docHospitalDtls = DataManager.getDataManager().getDoctorDetails().getDocHospitalDtls();

            if(docHospitalDtls == null) {
                docHospitalDtls = new DocHospitalDtls();
                docHospitalDtls.setDocHospId(0L);
                docHospitalDtls.setDoctorId(docDtls.getAuthDtls().getUserId());
                docHospitalDtls.setModified(false);
            }
            else {
                docHospitalDtls.setModified(true);
            }

            AddressDtls addressDtls = new AddressDtls();
            if(docHospitalDtls.getAddressDtls() != null) {
                addressDtls.setAddressId(docHospitalDtls.getAddressDtls().getAddressId());
                addressDtls.setModified(true);
            }
            else {
                addressDtls.setAddressId(0L);
                addressDtls.setModified(false);
            }
            addressDtls.setTextAddress(hospitalAddress.getText().toString());
            addressDtls.setZipCode(Integer.parseInt(hospitalPincode.getText().toString()));
            docDtls.setAddressDtls(addressDtls);
            docHospitalDtls.setHospitalName(hospitalName.getText().toString());
            docHospitalDtls.setHospitalContactNumber(hospitalReceptionNumber.getText().toString());

            docHospitalDtls.setAddressDtls(addressDtls);
            docDtls.setDocHospitalDtls(docHospitalDtls);

            DoctorService service = new DoctorService();
            service.updateDoctorHospitalDetails(getActivity(), docDtls, "hospital", imgsMap, persistedImgsMap);

            Toast.makeText(getActivity(),
                    "Hospital Information is Saved", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        } else {
            objAlert.showAlertDialog(getActivity(), "No Internet Connection",
                    "check your connection and try again", false);
        }
    }


}
