package com.redblack.taksim.ui.logintype.server;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Server {

    public static final String  Main_URL = "http://94.101.81.210:48080/AppService/";
    public static String token = "";

    public static String GetVerifyCode(String obj) {

        String method_Login = "GetVerifyCode.do";

        try {

            URL url = new URL(Main_URL + method_Login); // here is your URL path

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jsonParam", obj);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(jsonObject));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            } else {
                //User Info issue
                //return new String("false : "+responseCode);
                Log.i("Exception: ", "" + responseCode);
                return "false";
            }

        } catch (Exception e) {
            //Connection issue
            //return new String("Exception: " + e.getMessage());
            Log.i("Exception: ", e.getMessage());
            return "false";
        }

    }

    public static String CustomerLogin(String obj) {

        String method_Login = "CustomerLogin.do";


        try {

            URL url = new URL(Main_URL + method_Login); // here is your URL path

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jsonParam", obj);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(jsonObject));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                //get Token
                //String typr = conn.getContentType("_token");
                token = conn.getHeaderField("_token");

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            } else {
                //User Info issue
                //return new String("false : "+responseCode);
                Log.i("Exception: ", "" + responseCode);
                return "false";
            }

        } catch (Exception e) {
            //Connection issue
            //return new String("Exception: " + e.getMessage());
            Log.i("Exception: ", e.getMessage());
            return "false";
        }

    }



    public static String UpdateCustomerInfo(String obj,String token) {

        String method_Login = "UpdateCustomerInfo.do";

        try {

            URL url = new URL(Main_URL + method_Login); // here is your URL path

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jsonParam", obj);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("_token",token);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(jsonObject));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            } else {
                //User Info issue
                //return new String("false : "+responseCode);
                Log.i("Exception: ", "" + responseCode);
                return "false";
            }

        } catch (Exception e) {
            //Connection issue
            //return new String("Exception: " + e.getMessage());
            Log.i("Exception: ", e.getMessage());
            return "false";
        }

    }

    public static String GetUsableCars(String obj,String token) {

        String method_Login = "GetUsableCars.do";

        try {

            URL url = new URL(Main_URL + method_Login); // here is your URL path

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jsonParam", obj);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("_token",token);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(jsonObject));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            } else {
                //User Info issue
                //return new String("false : "+responseCode);
                Log.i("Exception: ", "" + responseCode);
                return "false";
            }

        } catch (Exception e) {
            //Connection issue
            //return new String("Exception: " + e.getMessage());
            Log.i("Exception: ", e.getMessage());
            return "false";
        }

    }

    public static String CreateOrder(String obj,String token) {

        String method_Login = "CreateOrder.do";


        try {

            URL url = new URL(Main_URL + method_Login); // here is your URL path

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jsonParam", obj);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("_token",token);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(jsonObject));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                //get Token
                //String typr = conn.getContentType("_token");
                //token = conn.getHeaderField("_token");

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            } else {
                //User Info issue
                //return new String("false : "+responseCode);
                Log.i("Exception: ", "" + responseCode);
                return "false";
            }

        } catch (Exception e) {
            //Connection issue
            //return new String("Exception: " + e.getMessage());
            Log.i("Exception: ", e.getMessage());
            return "false";
        }

    }

    public static String CancelOrder(String obj,String token) {

        String method_Login = "CancelOrder.do";


        try {

            URL url = new URL(Main_URL + method_Login); // here is your URL path

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jsonParam", obj);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("_token",token);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(jsonObject));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                //get Token
                //String typr = conn.getContentType("_token");
                //token = conn.getHeaderField("_token");

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            } else {
                //User Info issue
                //return new String("false : "+responseCode);
                Log.i("Exception: ", "" + responseCode);
                return "false";
            }

        } catch (Exception e) {
            //Connection issue
            //return new String("Exception: " + e.getMessage());
            Log.i("Exception: ", e.getMessage());
            return "false";
        }

    }

    public static String GetOrderTracking(String obj,String token) {

        String method_Login = "GetOrderTracking.do";


        try {

            URL url = new URL(Main_URL + method_Login); // here is your URL path

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jsonParam", obj);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("_token",token);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(jsonObject));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                //get Token
                //String typr = conn.getContentType("_token");
                //token = conn.getHeaderField("_token");

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            } else {
                //User Info issue
                //return new String("false : "+responseCode);
                Log.i("Exception: ", "" + responseCode);
                return "false";
            }

        } catch (Exception e) {
            //Connection issue
            //return new String("Exception: " + e.getMessage());
            Log.i("Exception: ", e.getMessage());
            return "false";
        }

    }


    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


}
