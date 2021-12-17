package com.demo.main.base;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.JSONReader;
import com.oracle.javafx.jmx.json.impl.JSONStreamReaderImpl;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringJoiner;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

@Slf4j
public class ImageTransferUtil {

    private static final String AUTH_PATH = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=OYX48AnqrUG1Qn5C1YoSFsq0&client_secret=XewhRHWaLVAGAZntoHRK89x8YG7wPPOz";
    private static final String TRANSFER_PATH = "https://aip.baidubce.com/file/2.0/mt/pictrans/v1?access_token=";

    private static OkHttpClient okHttpClient = new OkHttpClient();


    public static void main(String[] args) throws Exception {
//        File file = new File("/Users/cjin/source/my_private_workspace/mavenproject/demo_main/src/main/resources/files/截屏2021-12-16 下午3.26.36.png");
//        InputStream is = new FileInputStream(file);
//        ByteArrayOutputStream ops = new ByteArrayOutputStream();
//        byte[] cache = new byte[1024];
//        while (is.read(cache) != -1) {
//            ops.write(cache);
//        }
//
//        String encode = Base64Encoder.encode(ops.toByteArray());
//        String s = "imgBase: data:image/png;base64," + encode;
//
//        Request request = new Request.Builder()
//                .url("https://aidemo.youdao.com/ocrtransapi1")
//                .post(new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("imgBase", s)
//                        .build())
//                .build();
//        Call call = okHttpClient.newCall(request);
//        try {
//            Response response = call.execute();
//            if (response.isSuccessful()) {
//                ResponseBody body = response.body();
//                JSONObject bodyJson = JSONUtil.parseObj(body.string());
//                "renderImage"
//                System.out.println(bodyJson);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        ImageTransferUtil util = new ImageTransferUtil();
        util.scanImageFormTransfer("/Users/cjin/source/my_private_workspace/mavenproject/demo_main/src/main/resources/files");
    }

    /**
     * 扫描文件夹并翻译
     *
     * @param path
     */
    public void scanImageFormTransfer(String path) {
        if (StrUtil.isBlank(path)) {
            log.warn("扫描路径有误path={}", path);
            return;
        }
        File[] files = loadFiles(path);
        for (File file : files) {
            execute(file);
        }
    }

    /**
     * 扫描所有文件（.jpeg .jpg .png）
     *
     * @param path
     * @return
     */
    private File[] loadFiles(String path) {
        File file = new File(path);
        if (file.isFile()) {
            return new File[]{file};
        } else if (file.isDirectory()) {
            return file.listFiles();
        }
        return new File[]{};
    }

    /**
     * 执行AI翻译
     *
     * @param file
     */
    private void execute(File file) {
        String absolutePath = file.getAbsolutePath();
        if (absolutePath.endsWith(".jpeg") || absolutePath.endsWith(".jpg") || absolutePath.endsWith(".png")) {
            String suffix = absolutePath.substring(absolutePath.lastIndexOf("."));
            // 获取授权token
            String authToken = auth();

            // 调用AI翻译图片，并获取base64编码后的图片
            String imageEncodeStr = transferExecute(authToken, file);

            // 保存翻译后的图片
            decodeImage(imageEncodeStr, absolutePath.replaceAll(suffix, "-zh" + suffix));
        }
    }

    /**
     * 获取授权认证
     *
     * @return
     */
    private String auth() {
        Request request = new Request.Builder()
                .url(AUTH_PATH)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                JSONObject bodyJson = JSONUtil.parseObj(body.string());
                return bodyJson.getStr("access_token");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 翻译图片
     *
     * @param token
     * @param file
     * @return
     */
    private String transferExecute(String token, File file) {
        if (StrUtil.isBlank(token)) {
            System.out.println("token is null");
            return null;
        }
        Request request = new Request.Builder()
                .url(TRANSFER_PATH + token)
                .post(new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("from", "en")
                        .addFormDataPart("to", "zh")
                        .addFormDataPart("v", "3")
                        .addFormDataPart("paste", "1")
                        .addFormDataPart("image", file.getName(), RequestBody.create(file, MediaType.parse("application/octet-stream")))
                        .build())
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                JSONObject bodyJson = JSONUtil.parseObj(body.string());
                return bodyJson.getJSONObject("data").getStr("pasteImg");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 翻译图片
     *
     * @param token
     * @param file
     * @return
     */
    private String transferExecute2(String token, File file) {
        InputStream is = null;
        ByteArrayOutputStream ops = null;
        String encode = null;
        try {
            is = new FileInputStream(file);
            ops = new ByteArrayOutputStream();
            byte[] cache = new byte[1024];
            while (is.read(cache) != -1) {
                ops.write(cache);
            }
            encode = Base64Encoder.encode(ops.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ops != null) {
                try {
                    ops.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Request request = new Request.Builder()
                .url("https://aidemo.youdao.com/ocrtransapi1")
                .post(new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("imgBase", "imgBase: data:image/png;base64," + encode)
                        .build())
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                JSONObject bodyJson = JSONUtil.parseObj(body.string());
                return bodyJson.getStr("renderImage");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解码图片并保存到本地
     *
     * @param imgStr
     * @param imgFilePath
     * @return
     */
    private boolean decodeImage(String imgStr, String imgFilePath) {
        OutputStream out = null;
        if ("".equals(imgStr) || imgStr == null)
            return false;
        try {
            //Base64解码
            byte[] b = Base64.decodeBase64(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成jpeg图片

            out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
