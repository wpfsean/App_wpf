package com.zhketech.client.app.sip;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Xml;

import com.zhketech.client.app.sip.beans.DeviceInfor;
import com.zhketech.client.app.sip.utils.Logutils;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by wpf on 2018/6/19.
 */

public class ResolveRtsp implements Runnable {
    public static final String GETCAPABILITIES= "<?xml version=\"1.0\" encoding=\"utf-8\"?><s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\"><s:Header><wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><wsse:UsernameToken><wsse:Username>%s</wsse:Username><wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">%s</wsse:Password><wsse:Nonce>%s</wsse:Nonce><wsu:Created>%s</wsu:Created></wsse:UsernameToken></wsse:Security></s:Header><s:Body xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><GetServices xmlns=\"http://www.onvif.org/ver10/device/wsdl\"><IncludeCapability>true</IncludeCapability></GetServices></s:Body></s:Envelope>";
    public static final String GET_CAPABILITIES = "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\"><s:Header><Security s:mustUnderstand=\"1\" xmlns=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"><UsernameToken><Username>%s</Username><Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">%s</Password><Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">%s</Nonce><Created xmlns=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">%s</Created></UsernameToken></Security></s:Header><s:Body xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><GetCapabilities xmlns=\"http://www.onvif.org/ver10/device/wsdl\"><Category>All</Category></GetCapabilities></s:Body></s:Envelope>";
    public static final String GET_PROFILES = "<?xml version=\"1.0\" encoding=\"utf-8\"?><s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\"><s:Header><wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><wsse:UsernameToken><wsse:Username>%s</wsse:Username><wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">%s</wsse:Password><wsse:Nonce>%s</wsse:Nonce><wsu:Created>%s</wsu:Created></wsse:UsernameToken></wsse:Security></s:Header><s:Body xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><GetProfiles xmlns=\"http://www.onvif.org/ver10/media/wsdl\"></GetProfiles></s:Body></s:Envelope>";
    public static final String GET_PROFILE = "<?xml version=\"1.0\" encoding=\"utf-8\"?><s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\"><s:Header><wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><wsse:UsernameToken><wsse:Username>%s</wsse:Username><wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">%s</wsse:Password><wsse:Nonce>%s</wsse:Nonce><wsu:Created>%s</wsu:Created></wsse:UsernameToken></wsse:Security></s:Header><s:Body xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><GetProfile xmlns=\"http://www.onvif.org/ver10/media/wsdl\"><ProfileToken>%s</ProfileToken></GetProfile></s:Body></s:Envelope>";
    public static final String GET_URI_BODY = "<?xml version=\"1.0\" encoding=\"utf-8\"?><s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\"><s:Header><wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><wsse:UsernameToken><wsse:Username>%s</wsse:Username><wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">%s</wsse:Password><wsse:Nonce>%s</wsse:Nonce><wsu:Created>%s</wsu:Created></wsse:UsernameToken></wsse:Security></s:Header><s:Body xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><GetStreamUri xmlns=\"http://www.onvif.org/ver10/media/wsdl\"><StreamSetup><Stream xmlns=\"http://www.onvif.org/ver10/schema\">RTP-Unicast</Stream><Transport xmlns=\"http://www.onvif.org/ver10/schema\"><Protocol>RTSP</Protocol></Transport></StreamSetup><ProfileToken>%s</ProfileToken></GetStreamUri></s:Body></s:Envelope>";

    private HttpURLConnection mUrlConn;
    private OnHttpSoapListener mListener;
    private DeviceInfor mCamera;
    private String mCreated, mNonce, mAuthPwd;


    public ResolveRtsp(DeviceInfor device) {
        mCamera = device;
        createAuthString();
    }


    private void createAuthString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.CHINA);
        mCreated = df.format(new Date());
        mNonce = getNonce();
        mAuthPwd = getPasswordEncode(mNonce, mCamera.password, mCreated);
    }

    public String getPasswordEncode(String nonce, String password, String date) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            // 从官方文档可以知道我们nonce还需要用Base64解码一次
            byte[] b1 = Base64.decode(nonce.getBytes(), Base64.DEFAULT);
            // 生成字符字节流
            byte[] b2 = date.getBytes(); // "2013-09-17T09:13:35Z";
            byte[] b3 = password.getBytes();
            // 根据我们传得值的长度生成流的长度
            byte[] b4 = new byte[b1.length + b2.length + b3.length];
            // 利用sha-1加密字符
            md.update(b1, 0, b1.length);
            md.update(b2, 0, b2.length);
            md.update(b3, 0, b3.length);
            // 生成sha-1加密后的流
            b4 = md.digest();
            // 生成最终的加密字符串
            String result = new String(Base64.encode(b4, Base64.DEFAULT));
            return result.replace("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getNonce() {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 24; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    public void setOnHttpSoapListener(OnHttpSoapListener listener) {
        mListener = listener;
    }
    @Override
    public void run() {
        synchronized (this) {
            try {
                initConn(mCamera.serviceURL);
                mUrlConn.connect();
                String content = String.format(GETCAPABILITIES,
                        mCamera.username, mAuthPwd, mNonce, mCreated);
                mUrlConn.getOutputStream().write(content.getBytes());
                int code = mUrlConn.getResponseCode();
                if (code == 200) {
                    InputStream inStream = mUrlConn.getInputStream();
                    String res = inputStream2String(inStream);
                    if (!TextUtils.isEmpty(res)){
                        String mediaUrl = findMediaUrlEntryption(res);
                        if ( TextUtils.isEmpty(mediaUrl)) {
                            initConn(mCamera.serviceURL);
                            mUrlConn.connect();
                            content = String.format(GET_CAPABILITIES,
                                    mCamera.username, mAuthPwd, mNonce, mCreated);
                            mUrlConn.getOutputStream().write(content.getBytes());
                            inStream = mUrlConn.getInputStream();
                            res = inputStream2String(inStream);
                            mediaUrl = findMediaUrlEntryption(res);
                        }

                        initConn(mediaUrl);
                        mUrlConn.connect();
                        content = String.format(GET_PROFILES, mCamera.username,
                                mAuthPwd, mNonce, mCreated);
                        mUrlConn.getOutputStream().write(content.getBytes());
                        inStream = mUrlConn.getInputStream();
                        res = inputStream2String(inStream);
                        String profile = getOldProfileToken(res);
                        if (!TextUtils.isEmpty(profile)){
                            initConn(mediaUrl);
                            mUrlConn.connect();
                            content = String.format(GET_PROFILE, mCamera.username,
                                    mAuthPwd, mNonce, mCreated, profile);
                            mUrlConn.getOutputStream().write(content.getBytes());
                            inStream = mUrlConn.getInputStream();
                            res = inputStream2String(inStream);

                            initConn(mediaUrl);
                            mUrlConn.connect();
                            content =   String.format(GET_URI_BODY, mCamera.username,
                                    mAuthPwd, mNonce, mCreated, profile);
                            mUrlConn.getOutputStream().write(content.getBytes());
                            inStream = mUrlConn.getInputStream();
                            res = inputStream2String(inStream);
//                            String uri = getStreamURI(res);

                            Logutils.i("res"+res);

                        }



                    }
                }else {
                    if (mListener != null){
                        mListener.OnHttpSoapDone(mCamera,"no",false);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public void start() {
        new Thread(this).start();
    }

    public interface OnHttpSoapListener {
        public void OnHttpSoapDone(DeviceInfor camera, String uri, boolean success);
    }

    private void initConn(String url) {
        try {
            URL url1 = new URL(url);
            mUrlConn = (HttpURLConnection) url1.openConnection();
            mUrlConn.setDoInput(true);
            mUrlConn.setDoOutput(true);
            mUrlConn.setRequestMethod("POST");
            mUrlConn.setUseCaches(false);
            mUrlConn.setInstanceFollowRedirects(true);
            mUrlConn.setRequestProperty("Content-Type",
                    "application/soap+xml; charset=utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }
    private String findMediaUrlEntryption(String xml) {
        List<String> mediaList = new ArrayList<>();
        List<String> ptzList = new ArrayList<>();
        XmlPullParser parser = Xml.newPullParser();
        InputStream input = new ByteArrayInputStream(xml.getBytes());
        try {
            parser.setInput(input, "UTF-8");
            int eventType = parser.getEventType();
            boolean done = false;
            while (eventType != XmlPullParser.END_DOCUMENT || done) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("Service") ) {
                            eventType = parser.next();
                            String nameSpace = parser.nextText();
                            if (nameSpace.equals("http://www.onvif.org/ver10/media/wsdl")) {
                                eventType = parser.next();
                                String  media_service = parser.nextText();
                                if(!TextUtils.isEmpty(media_service)){
                                    mediaList.add(media_service);
                                }
                            }

                            if (nameSpace.equals("http://www.onvif.org/ver20/ptz/wsdl")) {
                                eventType = parser.next();
                                String ptz_service = parser.nextText();
                                if(!TextUtils.isEmpty(ptz_service)){
                                    Logutils.i("ptz_service:"+ptz_service);
                                    ptzList.add(ptz_service);
                                    mCamera.setSuporrtPtz(true);
                                    mCamera.setPtz_url(ptz_service);
                                }
                            }
                        }
                        if (parser.getName().equals("Media")) {
                            eventType = parser.next();
                            if (parser.getName().equals("XAddr")) {
                                eventType = parser.next();
                                if (!parser.getText().isEmpty()) {
                                    return parser.getText();
                                }
                            }
                        }


                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
            if (mediaList != null && mediaList.size() > 0){
                return mediaList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    private String getOldProfileToken(String xml) {
        List<String> tokenList = new ArrayList<>();
        XmlPullParser parser = Xml.newPullParser();
        InputStream input = new ByteArrayInputStream(xml.getBytes());
        try {
            parser.setInput(input, "UTF-8");
            int eventType = parser.getEventType();
            boolean done = false;
            while (eventType != XmlPullParser.END_DOCUMENT || done) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("Profiles")) {
                            String token = parser.getAttributeValue(null, "token");
                            tokenList.add(token);
                            parser.next();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tokenList.size()>1){
            return tokenList.get(1);
        }else {
            return  tokenList.get(0);
        }

    }
}