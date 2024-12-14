package com.medidr.doctor.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.medidr.doctor.config.Constants;
import com.medidr.doctor.model.AppointmentDtls;
import com.medidr.doctor.model.AuthDtls;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocScheduleDtls;
import com.medidr.doctor.model.DocServicesDtls;
import com.medidr.doctor.model.DocVacationSchedules;
import com.medidr.doctor.model.ImageMasterDtls;
import com.medidr.doctor.model.Login;
import com.medidr.doctor.model.Message;
import com.medidr.doctor.model.resposne.DashBoardResponseDetails;
import com.medidr.doctor.ui.Appointment;
import com.medidr.doctor.ui.AppointmentDetailsActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


/**
 * Created by mansi on 3/13/2016.
 */
public class DoctorService {

    private static final String TAG = "Doctor Services";

    private final String URL = Constants.SCHEME + Constants.HOST + ":" + Constants.PORT + Constants.APP;

    private final String CONTENT_TYPE = "application/json";

    ProgressDialog progressDialog = null;

    public String login(final Context context, final Login login) {
        final ObjectMapper mapper = new ObjectMapper();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        String responseResult = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = null;

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String serviceURL = URL + Constants.LOGIN_URI + "/" + login.getMobile() + "/" + login.getPassword();
            HttpGet httpGet = new HttpGet(serviceURL);

            response = httpClient.execute(httpGet);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            if (result != null) {
                responseResult = result.toString();

            }


        } catch (Exception exception) {
            Log.d(TAG, "Exception during login ");
        }
        return responseResult;
    }


    public String authenticateDoctor(String strMobile) {
        final ObjectMapper mapper = new ObjectMapper();

        String responseResult = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = null;

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String serviceURL = URL + Constants.SignUp_URI + "/" + strMobile;
            HttpGet httpGet = new HttpGet(serviceURL);

            response = httpClient.execute(httpGet);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            if (result != null) {
                responseResult = result.toString();

            }


        } catch (Exception exception) {
            Log.d(TAG, "Exception during authenticateDoctor ");
        }
        return responseResult;
    }

    public void addDoctorPersonalDetails(final Context context, DocDtls doctorDetails) {
        Log.i(TAG, "addDoctorPersonalDetails" + doctorDetails);
        final ObjectMapper mapper = new ObjectMapper();
//        progressDialog = new ProgressDialog(context);

        Log.i(TAG, "addDoctorPersonalDetails :: url " + URL + Constants.ADD_DOCTOR);

        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        //AsyncHttpClient client = new AsyncHttpClient();
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Log.i(TAG, "JSON ===" + mapper.writeValueAsString(doctorDetails));
/*            progressDialog.setMessage("Loading");
            progressDialog.show();*/
            String inputRequest = mapper.writeValueAsString(doctorDetails);

            String serviceURL = URL + Constants.ADD_DOCTOR;
            HttpPost httpPost = new HttpPost(serviceURL);

            org.apache.http.entity.StringEntity strEntity = new org.apache.http.entity.StringEntity(inputRequest);
            strEntity.setContentType("application/json");
            httpPost.setEntity(strEntity);
            response = httpClient.execute(httpPost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            if (result != null) {
                String responseResult = result.toString();
                Log.d(TAG, "result is :" + responseResult);
                JSONObject jsonObject = new JSONObject(responseResult);
                ObjectMapper objectMapper = new ObjectMapper();
                DocDtls persistedDocDtls = objectMapper.readValue(jsonObject.getString("data"), DocDtls.class);
                DataManager.getDataManager().setDoctorDetails(persistedDocDtls);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception exception) {
            Log.d(TAG, exception.getMessage());
        }

    }

    public void addDoctorHospitalDetails(final Context context, DocDtls doctorDetails, String featureName, List<byte[]> imgArrLst) {

        final ObjectMapper mapper = new ObjectMapper();
/*        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.show();*/

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpClient = new DefaultHttpClient();

            List<ImageMasterDtls> imageMasterDtlsLst = new ArrayList<ImageMasterDtls>();

            for (byte[] imageByteArray : imgArrLst) {

                String serviceURL = URL + "/doctors/add/" + doctorDetails.getAuthDtls().getUserId().toString() + "/addImages";
                HttpPost httpPost = new HttpPost(serviceURL);
                MultipartEntity multiPartEntity = new MultipartEntity();

                ByteArrayBody byteArr = new ByteArrayBody(imageByteArray, "upload.jpg");

                multiPartEntity.addPart("image", byteArr);
                multiPartEntity.addPart("featureName", new org.apache.http.entity.mime.content.StringBody(featureName));

                httpPost.setEntity(multiPartEntity);
                HttpResponse response = httpClient.execute(httpPost);

                StringBuffer result = new StringBuffer();
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                Log.d(TAG, result.toString());

                if (result != null) {
                    String responseResult = result.toString();
                    Log.d(TAG, "result is :" + responseResult);

                    JSONObject jsonObject = new JSONObject(responseResult);
                    JSONObject dataObject = jsonObject.getJSONObject("data");

                    doctorDetails.getDocPersonalDtls().setDoctorId(doctorDetails.getAuthDtls().getUserId());
                    String largeImgUrl = dataObject.getString("large_img_url_path");
                    String thumbnailImgUrl = dataObject.getString("thumbnail_img_url_path");

                    ImageMasterDtls largeImgDtls = new ImageMasterDtls();
                    largeImgDtls.setImageId(0L);
                    largeImgDtls.setImageUrl(largeImgUrl);
                    largeImgDtls.setImageType("LARGE");

                    imageMasterDtlsLst.add(largeImgDtls);

                    ImageMasterDtls thumbnailImgDtls = new ImageMasterDtls();
                    thumbnailImgDtls.setImageId(0L);
                    thumbnailImgDtls.setImageUrl(thumbnailImgUrl);
                    thumbnailImgDtls.setImageType("THUMBNAIL");

                    imageMasterDtlsLst.add(thumbnailImgDtls);
                }

            }
            doctorDetails.getDocHospitalDtls().setImageMasterDtls(imageMasterDtlsLst);

            Log.i(TAG, "JSON ===" + mapper.writeValueAsString(doctorDetails));
/*            progressDialog.setMessage("Loading");
            progressDialog.show();*/
            String inputRequest = mapper.writeValueAsString(doctorDetails);

            String serviceURL = URL + Constants.ADD_HOSPITAL_DETAILS;
            HttpPost httpPost = new HttpPost(serviceURL);

            org.apache.http.entity.StringEntity strEntity = new org.apache.http.entity.StringEntity(inputRequest);
            strEntity.setContentType("application/json");
            httpPost.setEntity(strEntity);
            HttpResponse response = httpClient.execute(httpPost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            if (result != null) {
                String responseResult = result.toString();
                Log.d(TAG, "result is :" + responseResult);
                JSONObject jsonObject = new JSONObject(responseResult);
                ObjectMapper objectMapper = new ObjectMapper();
                DocDtls persistedDocDtls = objectMapper.readValue(jsonObject.getString("data"), DocDtls.class);
                DataManager.getDataManager().setDoctorDetails(persistedDocDtls);
            }


        } catch (Exception exception) {
            Log.d(TAG, "Exception in Uploading hospital images ");
        }

    }

    public void updateDoctorHospitalDetails(final Context context, DocDtls doctorDetails, String featureName,
                                            Map<Integer, byte[]> imgsMap, Map<Integer, String> persistedImgsMap) {

        final ObjectMapper mapper = new ObjectMapper();
/*        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.show();*/

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpClient = new DefaultHttpClient();


            Iterator<Integer> imgIterator = imgsMap.keySet().iterator();
            List<ImageMasterDtls> imageMasterDtlsLst = new ArrayList<ImageMasterDtls>();

            while (imgIterator.hasNext()) {

                String serviceURL = URL + "/doctors/add/" + doctorDetails.getAuthDtls().getUserId().toString() + "/addImages";
                HttpPost httpPost = new HttpPost(serviceURL);
                MultipartEntity multiPartEntity = new MultipartEntity();
                Integer imgNumber = imgIterator.next();
                byte[] imageByteArray = imgsMap.get(imgNumber);

                ByteArrayBody byteArr = new ByteArrayBody(imageByteArray, "upload.jpg");

                multiPartEntity.addPart("image", byteArr);
                multiPartEntity.addPart("featureName", new org.apache.http.entity.mime.content.StringBody(featureName));

                httpPost.setEntity(multiPartEntity);
                HttpResponse response = httpClient.execute(httpPost);

                StringBuffer result = new StringBuffer();
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                Log.d(TAG, result.toString());

                if (result != null) {
                    String responseResult = result.toString();
                    Log.d(TAG, "result is :" + responseResult);

                    JSONObject jsonObject = new JSONObject(responseResult);
                    JSONObject dataObject = jsonObject.getJSONObject("data");

                    doctorDetails.getDocPersonalDtls().setDoctorId(doctorDetails.getAuthDtls().getUserId());
                    String largeImgUrl = dataObject.getString("large_img_url_path");
                    String thumbnailImgUrl = dataObject.getString("thumbnail_img_url_path");

                    String imgIdsStr = persistedImgsMap.get(imgNumber);
                    if (imgIdsStr == null || "".equalsIgnoreCase(imgIdsStr.trim())) {
                        ImageMasterDtls largeImgDtls = new ImageMasterDtls();
                        largeImgDtls.setImageId(0L);
                        largeImgDtls.setImageUrl(largeImgUrl);
                        largeImgDtls.setImageType("LARGE");
                        largeImgDtls.setModified(false);

                        imageMasterDtlsLst.add(largeImgDtls);

                        ImageMasterDtls thumbnailImgDtls = new ImageMasterDtls();
                        thumbnailImgDtls.setImageId(0L);
                        thumbnailImgDtls.setImageUrl(thumbnailImgUrl);
                        thumbnailImgDtls.setImageType("THUMBNAIL");
                        thumbnailImgDtls.setModified(false);

                        imageMasterDtlsLst.add(thumbnailImgDtls);
                    } else {
                        String[] imgIdsArr = imgIdsStr.split("_");
                        Long largeImgId = new Long(imgIdsArr[1]);
                        Long thumbnailImgId = new Long(imgIdsArr[0]);

                        ImageMasterDtls largeImgDtls = new ImageMasterDtls();
                        largeImgDtls.setImageId(largeImgId);
                        largeImgDtls.setImageUrl(largeImgUrl);
                        largeImgDtls.setImageType("LARGE");
                        largeImgDtls.setModified(true);

                        imageMasterDtlsLst.add(largeImgDtls);

                        ImageMasterDtls thumbnailImgDtls = new ImageMasterDtls();
                        thumbnailImgDtls.setImageId(thumbnailImgId);
                        thumbnailImgDtls.setImageUrl(thumbnailImgUrl);
                        thumbnailImgDtls.setImageType("THUMBNAIL");
                        thumbnailImgDtls.setModified(true);

                        imageMasterDtlsLst.add(thumbnailImgDtls);
                    }

                }

            }

            doctorDetails.getDocHospitalDtls().setImageMasterDtls(imageMasterDtlsLst);
            Log.i(TAG, "JSON ===" + mapper.writeValueAsString(doctorDetails));
/*            progressDialog.setMessage("Loading");
            progressDialog.show();*/
            String inputRequest = mapper.writeValueAsString(doctorDetails);

            String serviceURL = URL + Constants.ADD_HOSPITAL_DETAILS;
            HttpPost httpPost = new HttpPost(serviceURL);

            org.apache.http.entity.StringEntity strEntity = new org.apache.http.entity.StringEntity(inputRequest);
            strEntity.setContentType("application/json");
            httpPost.setEntity(strEntity);
            HttpResponse response = httpClient.execute(httpPost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            if (result != null) {
                String responseResult = result.toString();
                Log.d(TAG, "result is :" + responseResult);
                JSONObject jsonObject = new JSONObject(responseResult);
                ObjectMapper objectMapper = new ObjectMapper();
                DocDtls persistedDocDtls = objectMapper.readValue(jsonObject.getString("data"), DocDtls.class);
                DataManager.getDataManager().setDoctorDetails(persistedDocDtls);
            }


        } catch (Exception exception) {
            Log.d(TAG, "Exception in Uploading hospital images ");
        }

    }


    public void addDoctorWeeklySchedule(final Context context, DocDtls doctorDetails) {

        final ObjectMapper mapper = new ObjectMapper();
      /*  progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.show();*/


        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = null;

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String serviceURL = URL + Constants.ADD_DOCTORS_SCHEDULE_DETAILS;
            HttpPost httpPost = new HttpPost(serviceURL);

            String inputRequest = mapper.writeValueAsString(doctorDetails);
            Log.i(TAG, "JSON ===" + inputRequest);

            org.apache.http.entity.StringEntity strEntity = new org.apache.http.entity.StringEntity(inputRequest);
            strEntity.setContentType("application/json");
            httpPost.setEntity(strEntity);

            response = httpClient.execute(httpPost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            if (result != null) {
                String responseResult = result.toString();
                Log.d(TAG, "result is :" + responseResult);
            }

            if (result != null) {
                String responseResult = result.toString();
                Log.d(TAG, "result is :" + responseResult);
                JSONObject jsonObject = new JSONObject(responseResult);
                ObjectMapper objectMapper = new ObjectMapper();
                List<DocScheduleDtls> docScheduleDtlsList = objectMapper.readValue(jsonObject.getString("data"), new TypeReference<List<DocScheduleDtls>>() {
                });

                doctorDetails.setDocScheduleDtls(docScheduleDtlsList);
                DataManager.getDataManager().setDoctorDetails(doctorDetails);
            }

        } catch (Exception exception) {
            Log.d(TAG, exception.getMessage());
        }

    }

    public void addDoctorServicesDetails(final Context context, DocDtls doctorDetails) {

        final ObjectMapper mapper = new ObjectMapper();
/*        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.show();*/

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = null;

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String serviceURL = URL + Constants.ADD_DOCTORS_SERVICES_DETAILS;
            HttpPost httpPost = new HttpPost(serviceURL);

            String inputRequest = mapper.writeValueAsString(doctorDetails);
            Log.i(TAG, "JSON ===" + inputRequest);

            org.apache.http.entity.StringEntity strEntity = new org.apache.http.entity.StringEntity(inputRequest);
            strEntity.setContentType("application/json");
            httpPost.setEntity(strEntity);

            response = httpClient.execute(httpPost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            if (result != null) {
                String responseResult = result.toString();
                Log.d(TAG, "result is :" + responseResult);
                JSONObject jsonObject = new JSONObject(responseResult);
                ObjectMapper objectMapper = new ObjectMapper();
                List<DocServicesDtls> docServicesDtlsList = objectMapper.readValue(jsonObject.getString("data"), new TypeReference<List<DocServicesDtls>>() {
                });

                doctorDetails.setDocServicesDtls(docServicesDtlsList);
                DataManager.getDataManager().setDoctorDetails(doctorDetails);
            }

        } catch (Exception exception) {
            Log.d(TAG, exception.getMessage());
        }

    }


    public void uploadImage(final Context context, byte[] imageByteArray, DocDtls doctorDetails, String featureName) {
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            String serviceURL = URL + "/doctors/add/" + doctorDetails.getAuthDtls().getUserId().toString() + "/addImages";
            HttpPost httpPost = new HttpPost(serviceURL);
            MultipartEntity multiPartEntity = new MultipartEntity();

            ByteArrayBody byteArr = new ByteArrayBody(imageByteArray, "upload.jpg");

            multiPartEntity.addPart("image", byteArr);
            multiPartEntity.addPart("featureName", new org.apache.http.entity.mime.content.StringBody(featureName));

            httpPost.setEntity(multiPartEntity);
            HttpResponse response = httpClient.execute(httpPost);

            StringBuffer result = new StringBuffer();
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            Log.d(TAG, result.toString());

            if (result != null) {
                String responseResult = result.toString();
                Log.d(TAG, "result is :" + responseResult);
                JSONObject jsonObject = new JSONObject(responseResult);
                JSONObject dataObject = jsonObject.getJSONObject("data");

                doctorDetails.getDocPersonalDtls().setDoctorId(doctorDetails.getAuthDtls().getUserId());
                doctorDetails.getDocPersonalDtls().setDoctorProfileImage(dataObject.getString("large_img_url_path"));
                doctorDetails.getDocPersonalDtls().setDoctorProfileThumbnailImage(dataObject.getString("thumbnail_img_url_path"));

                DataManager.getDataManager().setDoctorDetails(doctorDetails);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Doctor Service", "Error occured while uploading the image");
        }

    }

    public void sendMessage(final Context context, Message message) {

        final ObjectMapper mapper = new ObjectMapper();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(URL + Constants.SEND_MESSAGE, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, byte[] response) {

                progressDialog.hide();
                Toast.makeText(context, "Message Sent", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(int statusCode, Header[] header, byte[] errorResponse, Throwable error) {

                if (statusCode == 500) {
                    Toast.makeText(context, "Error occured while logging-in", Toast.LENGTH_LONG).show();
                }
            }

        });


    }


    public void getDoctorDashboardDetails(final Context context, Long doctorId) {


        final ObjectMapper mapper = new ObjectMapper();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = URL + Constants.DASHBOARD_DETAILS_URI + "/" + doctorId;
        try {

            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] header, byte[] response) {
                    try {
                        progressDialog.hide();
                        String res = new String();
                        DashBoardResponseDetails details = mapper.readValue(res, DashBoardResponseDetails.class);
                        DataManager.getInstance().setDashBoardResponseDetails(details);
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT);
                        onSuccessLoadActivity(context, 1);
                    } catch (Exception e) {
                        Log.i(TAG, "Exception occures" + e.getMessage());
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] header, byte[] errorResponse, Throwable error) {

                    if (statusCode == 500) {
                        Toast.makeText(context, "Error occured while logging-in", Toast.LENGTH_LONG).show();
                    }
                }

            });

        } catch (Exception e) {

        }


    }

    public void addServiceMode(final Context context, DocDtls docDtls, String serviceTypeStr) {

        try {
            final ObjectMapper mapper = new ObjectMapper();
            HttpClient httpClient = new DefaultHttpClient();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String serviceURL = URL + "/doctors/updateDoctorServiceMode/" + docDtls.getAuthDtls().getUserId() + "/" + serviceTypeStr;
            HttpGet httpGet = new HttpGet(serviceURL);

            HttpResponse response = httpClient.execute(httpGet);

            StringBuffer result = new StringBuffer();
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            Log.d(TAG, result.toString());

            if (result != null) {
                String responseResult = result.toString();
                Log.d(TAG, "result is :" + responseResult);
                JSONObject jsonObject = new JSONObject(responseResult);
                JSONObject dataObject = jsonObject.getJSONObject("data");

            }

        } catch (Exception e) {
            Log.e(TAG, "Error occured while adding service", e);
        }


    }


    public void resetPassword(final Context context, AuthDtls auth) {

        final ObjectMapper mapper = new ObjectMapper();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            StringEntity entity = new StringEntity(mapper.writeValueAsString(auth));
            client.post(context, URL + Constants.RESET_PASSWORD, entity, CONTENT_TYPE, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] header, byte[] response) {

                    progressDialog.hide();
                    Toast.makeText(context, "resetPassword Added", Toast.LENGTH_SHORT);
                }

                @Override
                public void onFailure(int statusCode, Header[] header, byte[] errorResponse, Throwable error) {
                    progressDialog.hide();
                    Log.e("TAG", "Error while making api call add service" + new String(errorResponse));
                    if (statusCode == 500) {
                        Toast.makeText(context, "Error occured while logging-in", Toast.LENGTH_LONG).show();
                    }
                }

            });
        } catch (Exception e) {
            Log.e(TAG, "Error occured while adding service", e);
        }


    }

    public String getInformationForDoctor(String urlPath) {

        String responseResult = null;
        try {
            final ObjectMapper mapper = new ObjectMapper();
            HttpClient httpClient = new DefaultHttpClient();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String serviceURL = URL + urlPath;//"/doctors/getAppointmentDetailsForDoctor/"+doctorId; //+ docDtls.getAuthDtls().getUserId() + "/" + serviceTypeStr;
            HttpGet httpGet = new HttpGet(serviceURL);

            HttpResponse response = httpClient.execute(httpGet);

            StringBuffer result = new StringBuffer();
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            Log.d(TAG, result.toString());

            if (result != null) {
                responseResult = result.toString();
                Log.d(TAG, "result is :" + responseResult);
//                JSONObject jsonObject = new JSONObject(responseResult);
//                JSONObject dataObject = jsonObject.getJSONObject("data");

            }

        } catch (Exception e) {
            Log.e(TAG, "Error occured while getting doctors appointments", e);
        }
        return responseResult;

    }

    public void forgotPassword(final Context context, AuthDtls auth) {
        final ObjectMapper mapper = new ObjectMapper();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            StringEntity entity = new StringEntity(mapper.writeValueAsString(auth));
            client.post(context, URL + Constants.FORGOT_PASSWORD, entity, CONTENT_TYPE, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] header, byte[] response) {

                    progressDialog.hide();
                    Toast.makeText(context, "forgotPassword Added", Toast.LENGTH_SHORT);
                }

                @Override
                public void onFailure(int statusCode, Header[] header, byte[] errorResponse, Throwable error) {
                    progressDialog.hide();
                    Log.e("TAG", "Error while making api call forgotPassword" + new String(errorResponse));
                    if (statusCode == 500) {
                        Toast.makeText(context, "Error occured", Toast.LENGTH_LONG).show();
                    }
                }

            });
        } catch (Exception e) {
            Log.e(TAG, "Error occured while forgotPassword", e);
        }


    }

    public String cancelAppointment(AppointmentDtls appointmentDtlsRec) {

        String responseResult = null;

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            InputStream is = null;
            StringBuffer result = new StringBuffer();
            JSONObject userDtls = new JSONObject();
            JSONObject appointmentDtls = new JSONObject();
            try {
                appointmentDtls.put("appointment_id", appointmentDtlsRec.getAppointmentId());
                appointmentDtls.put("appointment_stage", "CANCELLED");
                appointmentDtls.put("appointment_date", appointmentDtlsRec.getAppointmentDate());
                appointmentDtls.put("appointment_from", appointmentDtlsRec.getAppointmentFrom());
                appointmentDtls.put("patient_name", appointmentDtlsRec.getPatientName());
                appointmentDtls.put("fullname", appointmentDtlsRec.getDoctorName());
                appointmentDtls.put("hospital_name", appointmentDtlsRec.getHospitalName());
                appointmentDtls.put("doctor_mobile_number", appointmentDtlsRec.getDoctorMobileNumber());
                appointmentDtls.put("user_mobile_number", appointmentDtlsRec.getUserMobileNumber());
                appointmentDtls.put("user_id", appointmentDtlsRec.getUserId());
                appointmentDtls.put("modified", true);
                appointmentDtls.put("deleted", false);

                Log.d(TAG, "appointmentDtls  has created as :" + userDtls.toString());

                String serviceURL = URL + "/doctors/CancelAppointment";

                HttpResponse response = null;
                HttpClient httpClient = new DefaultHttpClient();

                org.apache.http.entity.StringEntity strEntity = null;
                try {
                    String jsonRequest = appointmentDtls.toString();


                    HttpPost httpPost = new HttpPost(serviceURL);
                    strEntity = new org.apache.http.entity.StringEntity(jsonRequest);
                    strEntity.setContentType("application/json");
                    httpPost.setEntity(strEntity);
                    response = httpClient.execute(httpPost);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                if (result != null) {
                    responseResult = result.toString();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            Log.d(TAG,
                    " Exception in RESTful service response =================>"
                            + exception.getMessage());
        }
        return responseResult;
    }

    public String sendMessageNew(Message message) {

        String responseResult = null;

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            InputStream is = null;
            StringBuffer result = new StringBuffer();

            JSONObject messageDtls = new JSONObject();
            try {

                messageDtls.put("msg_dtl_id", 0);
                messageDtls.put("sender_id", message.getSenderId());
                messageDtls.put("sender_type", "DOCTOR");
                messageDtls.put("msg_txt", message.getMsgText());
                messageDtls.put("msg_time", message.getMsgTime());
                messageDtls.put("msg_type", message.getMsgType());


                Log.d(TAG, "sendMessage  has created as :" + messageDtls.toString());

                String serviceURL = URL + "/doctors/sendMessage";

                HttpResponse response = null;
                HttpClient httpClient = new DefaultHttpClient();

                org.apache.http.entity.StringEntity strEntity = null;
                try {
                    String jsonRequest = messageDtls.toString();


                    HttpPost httpPost = new HttpPost(serviceURL);
                    strEntity = new org.apache.http.entity.StringEntity(jsonRequest);
                    strEntity.setContentType("application/json");
                    httpPost.setEntity(strEntity);
                    response = httpClient.execute(httpPost);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                if (result != null) {
                    responseResult = result.toString();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            Log.d(TAG,
                    " Exception in RESTful service response =================>"
                            + exception.getMessage());
        }
        return responseResult;
    }


    public String updateGCMTokenForDoctor(AuthDtls authDtls) {

        String responseResult = null;

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            InputStream is = null;
            StringBuffer result = new StringBuffer();

            JSONObject authdetails = new JSONObject();
            try {

                authdetails.put("auth_dtl_id", authDtls.getUserId());
                authdetails.put("user_type", "DOCTOR");
                authdetails.put("gcm_token", authDtls.getGcmToken());

                String serviceURL = URL + "/doctors/updateGCMToken";

                HttpResponse response = null;
                HttpClient httpClient = new DefaultHttpClient();

                org.apache.http.entity.StringEntity strEntity = null;
                try {
                    String jsonRequest = authdetails.toString();


                    HttpPost httpPost = new HttpPost(serviceURL);
                    strEntity = new org.apache.http.entity.StringEntity(jsonRequest);
                    strEntity.setContentType("application/json");
                    httpPost.setEntity(strEntity);
                    response = httpClient.execute(httpPost);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                if (result != null) {
                    responseResult = result.toString();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            Log.d(TAG,
                    " Exception in RESTful service response =================>"
                            + exception.getMessage());
        }
        return responseResult;
    }

    public String getOTPForForgotPassword(AuthDtls authDtls) {

        String responseResult = null;
        final ObjectMapper mapper = new ObjectMapper();
//        progressDialog = new ProgressDialog(context);


        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        //AsyncHttpClient client = new AsyncHttpClient();
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Log.i(TAG, "JSON ===" + mapper.writeValueAsString(authDtls));
/*            progressDialog.setMessage("Loading");
            progressDialog.show();*/
            String inputRequest = mapper.writeValueAsString(authDtls);

            String serviceURL = URL + Constants.GENERATE_OTP;
            HttpPost httpPost = new HttpPost(serviceURL);

            org.apache.http.entity.StringEntity strEntity = new org.apache.http.entity.StringEntity(inputRequest);
            strEntity.setContentType("application/json");
            httpPost.setEntity(strEntity);
            response = httpClient.execute(httpPost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            if (result != null) {
                responseResult = result.toString();

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception exception) {
            Log.d(TAG, exception.getMessage());
        }

        return responseResult;
    }

    public String getValidateOTP(AuthDtls authDtls) {

        String responseResult = null;
        final ObjectMapper mapper = new ObjectMapper();


        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        //AsyncHttpClient client = new AsyncHttpClient();
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Log.i(TAG, "JSON ===" + mapper.writeValueAsString(authDtls));
/*            progressDialog.setMessage("Loading");
            progressDialog.show();*/
            String inputRequest = mapper.writeValueAsString(authDtls);

            String serviceURL = URL + Constants.VALIDATE_OTP;
            HttpPost httpPost = new HttpPost(serviceURL);

            org.apache.http.entity.StringEntity strEntity = new org.apache.http.entity.StringEntity(inputRequest);
            strEntity.setContentType("application/json");
            httpPost.setEntity(strEntity);
            response = httpClient.execute(httpPost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            if (result != null) {
                responseResult = result.toString();

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception exception) {
            Log.d(TAG, exception.getMessage());
        }

        return responseResult;
    }


    public String resetPassword(AuthDtls authDtls) {

        String responseResult = null;
        final ObjectMapper mapper = new ObjectMapper();
//        progressDialog = new ProgressDialog(context);


        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        //AsyncHttpClient client = new AsyncHttpClient();
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Log.i(TAG, "JSON ===" + mapper.writeValueAsString(authDtls));
/*            progressDialog.setMessage("Loading");
            progressDialog.show();*/
            String inputRequest = mapper.writeValueAsString(authDtls);

            String serviceURL = URL + Constants.RESET_PASSWORD;
            HttpPost httpPost = new HttpPost(serviceURL);

            org.apache.http.entity.StringEntity strEntity = new org.apache.http.entity.StringEntity(inputRequest);
            strEntity.setContentType("application/json");
            httpPost.setEntity(strEntity);
            response = httpClient.execute(httpPost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            if (result != null) {
                responseResult = result.toString();

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception exception) {
            Log.d(TAG, exception.getMessage());
        }

        return responseResult;
    }


    public String addDoctorVacationSchedule(List<DocVacationSchedules> docVacationSchedules) {

        final ObjectMapper mapper = new ObjectMapper();
        String responseResult = null;
/*        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.show();*/

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = null;

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String serviceURL = URL + Constants.ADD_VACATION_SCHEDULE;
            HttpPost httpPost = new HttpPost(serviceURL);

            String inputRequest = mapper.writeValueAsString(docVacationSchedules);
            Log.i(TAG, "JSON ===" + inputRequest);

            org.apache.http.entity.StringEntity strEntity = new org.apache.http.entity.StringEntity(inputRequest);
            strEntity.setContentType("application/json");
            httpPost.setEntity(strEntity);

            response = httpClient.execute(httpPost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            if (result != null) {
                responseResult = result.toString();
            }

        } catch (Exception exception) {
            Log.d(TAG, exception.getMessage());
        }

        return responseResult;

    }

    public String getAppointmentHistoryExcel(final Context context, Long doctorId, String fromDt, String toDt) {
        final ObjectMapper mapper = new ObjectMapper();
        String filePath=null;

        try {

            HttpClient httpClient = new DefaultHttpClient();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String serviceURL = URL + Constants.GET_APPOINTMENTS_HISTORY_EXCEL + "/" + doctorId + "?fromDate=" + fromDt + "&toDate=" + toDt;
            HttpGet httpGet = new HttpGet(serviceURL);

            HttpResponse response = httpClient.execute(httpGet);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            response.getEntity().writeTo(baos);
            baos.flush();
            baos.close();

            byte[] byteStream = baos.toByteArray();
            Log.d(TAG, "Length of the byte stream = " + byteStream.length);
            String appDir = "/MediDrApp";
            File parentDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    appDir);
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            String parentDirPath = parentDir.getAbsolutePath();

            File file = new File(parentDir + "/AppointmentHistory.xlsx");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteStream);
            fos.flush();
            fos.close();
            filePath = file.getAbsolutePath();
            return  filePath;

        } catch (IOException ioe) {
            Log.d(TAG, ioe.getMessage());
        } catch (Exception exception) {
            Log.d(TAG, exception.getMessage());
        }
        return  filePath;
    }

    private void onSuccessLoadActivity(Context context, Integer value) {

        switch (value) {
            case 1:
                Intent intent = new Intent(context, Appointment.class);
                context.startActivity(intent);

                break;

            case 2:
                Intent intent_two = new Intent(context, AppointmentDetailsActivity.class);
                context.startActivity(intent_two);

                break;


        }

    }


}
