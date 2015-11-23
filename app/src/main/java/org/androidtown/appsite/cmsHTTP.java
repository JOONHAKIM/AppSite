package org.androidtown.appsite;

import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by student on 2015-11-19.
 */
public class cmsHTTP {

    public WebView webView;
    public String mimeType="text/html";
    public String encoding="UTF-8";


    //너무 오랫동안 응답이 없을 경우 통신을 자동으로 종료해야함.(10초)
    public int REGISTRATION_TIMEOUT = 10*1000;

    //로그캣에 정보 기록.
    public String TAG = "cmsHTTP";

    public cmsHTTP() {
    }

    public cmsHTTP(WebView webView) {
        this.webView = webView;
    }

    //웹소켓 기능. 웹서버에 요청할 웹주소(url)과 POST 방식으로 전달할 변수들 AL로 처리.

    public String sendPost(String url, ArrayList<NameValuePair> params){
        //결과 데이터를 담을 result.
        String result = null;
        //응답결과를 담을 객체. 아직 온전치는 않음. 이를 해부해서 result에 담아줌.
        HttpResponse resp = null;
        /*
        HttpEntity : 웹 전달변수
        웹통신에 대한 송숫니 메시지를 담을 수 있는 집합체라고 함.
        아래 3가지 유형으로 나뉨
        1. 스트림형 : 스트림으로 수신하는 콘텐트
        2. 독립형 : 통신연결이나 다른 웹통신 정보와 독립적으로 존재하는 콘텐트
        3. 집합형 : 다른 웹통신 정보에서 가져온 콘텐트
        */
        HttpEntity entity = null;
        /* 전달하려고 하는 변수들을 담고, 인코딩을 지정해줌.
        * */
        try{
            entity = new UrlEncodedFormEntity(params,encoding);

        }catch (UnsupportedEncodingException e){
            throw new AssertionError(e);
        }

        /*HttpPost : 웬 전달변수(배송물)을 setter로 지정해주고,
                     url(배송주소)를 부여해서 완성시켜줌.
        * 웹주소와 함께 정의하고 웹통신 정보를 받아들여 웹통신 정보를 완성하는 형식
        * addHeader 전달변수의 유형(Type)을 정의해준다.
        */

        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(entity.getContentType());
        httpPost.setEntity(entity);

        Log.v(TAG, entity.getContentType().toString());


        /* HttpClient
        *  DefaultHttpClient()를 통해 선언.
        * 화면에 출력하는 기능이 없는 웹통신 객체라고 보면 됨.
        * 쿠키/인증/연결관리자 등의 정보를 받아 실행하는 인터페이스
        * */
        HttpClient httpClient = new DefaultHttpClient();

        /* HttpParams
        * Client를 통해 통신설정 가져옴.
        * */
        HttpParams tmpparms = httpClient.getParams();
        /* HttpConnectionParams
        *  HttpParams에 있는 속성을 가져오거나 설정할 수 있는 Adapter 역할.
        * */
        HttpConnectionParams.setConnectionTimeout(tmpparms, REGISTRATION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(tmpparms, REGISTRATION_TIMEOUT);
        ConnManagerParams.setTimeout(tmpparms,REGISTRATION_TIMEOUT);


        /* send에 대한 응답결과(response 및 result) 처리.*/
        try{
            //응답결과 객체 resp는 httpPost를 웹통신 실행결과로 받아와 할당.
            resp = httpClient.execute(httpPost);
            //resp의 상태 정보 중(getStatusLine), 통신 상태 코드가 SC_OK 일때
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                //isLoggable은 boolean타입으로,TAG가 로그에 찍혔다면,TAG라는 VERBOSE에 Success 띄워라.
                if(Log.isLoggable(TAG,Log.VERBOSE)) Log.v(TAG,"Successful authentication");

                //응답받은 것의 변수 집합체를 설정해준다.
                HttpEntity respEntity = resp.getEntity();

                //전달변수가 있다면
                if(respEntity != null){
                    //집합체에서 콘텐츠를 뽑아서 InputStream에 저장.
                    InputStream instream = respEntity.getContent();
                    //스트림을 String으로 변환해서 저장한다.
                    result = convertStreamToString(instream);
                    //사용한 스트림은 닫아준다.
                    instream.close();
                }else{
                    if(Log.isLoggable(TAG,Log.VERBOSE)) Log.v(TAG,"Error Process" + resp.getStatusLine());
                }
            }
        }catch (final IOException e){
            if (Log.isLoggable(TAG,Log.VERBOSE)){
                Log.v(TAG,"IOException when getting authtoken",e);
            }
        }finally {
            if (Log.isLoggable(TAG,Log.VERBOSE)){
                Log.v(TAG,"Completing");
            }
        }

        return result;
    }

    public void getPost(String url, ArrayList<NameValuePair> params){
        final String furl = url;
        final ArrayList<NameValuePair> fparams= params;

       //스레드에 webView 설정해준다.
        final Thread t = new Thread(){

            @Override
            public void run() {
                try{
                    String result = sendPost(furl,fparams);
                    //javascript 출력 여부.
                    webView.getSettings().setJavaScriptEnabled(true);
                    //javscript가 윈도우창 열 수 있게함.
                    webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    //플러그인  실행할 것
                    webView.getSettings().setPluginState((WebSettings.PluginState.ON_DEMAND));
                    //확대축소 기능
                    webView.getSettings().setSupportZoom(true);
                    webView.getSettings().setSupportMultipleWindows(true);
                    //줌툴 사용시 서프토 줌과..
                    webView.getSettings().setBuiltInZoomControls(false);
                    //false로 해야 이미지 출력 가능.
                    webView.getSettings().setBlockNetworkImage(false);
                    //자동 이미지 로드.
                    webView.getSettings().setLoadsImagesAutomatically(true);
                    //웹뷰포트 이용.<Meta/>태그에서 정의하는 wide viewport를 사용.
                    webView.getSettings().setUseWideViewPort(true);
                    webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    webView.clearCache(true);

                    String baseUrl = "http://www.owllab.com/android/";
                    webView.loadDataWithBaseURL(baseUrl,result,mimeType,encoding,baseUrl);
                    //webView.loadData(result,mimeType,encoding);


                }catch (Exception e){
                    Log.d("::Exception::", e.toString());
                }
            }
        };
        t.start();
    }

    public String convertStreamToString(InputStream is){
        StringBuilder sb= new StringBuilder();
        String line = null;

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,encoding),8);
            while((line= reader.readLine())!=null){
                sb.append(line+"\n");

            }
        }catch(IOException e){
            e.printStackTrace();

        }finally {
            try{is.close();}catch (IOException e){e.printStackTrace();}
        }
        return sb.toString();
    }

    public void postFile(String url,String filename) throws ClientProtocolException, IOException{
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(
                CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

        HttpPost httppost = new HttpPost(url);
        File file = new File(filename);
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile =new FileBody(file,"image/.jpeg");
        mpEntity.addPart("userfile", cbFile);

        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpClient.execute(httppost);
        HttpEntity resEntity = response.getEntity();

        if(resEntity!=null){
            System.out.println(EntityUtils.toString(resEntity));

        }
        if(resEntity!=null){
            resEntity.consumeContent();
        }


        httpClient.getConnectionManager().shutdown();

    }

}
