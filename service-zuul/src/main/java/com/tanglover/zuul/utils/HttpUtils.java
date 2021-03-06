package com.tanglover.zuul.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;

/**
 * @Author TangXu
 * @Description 针对返回json数据格式做处理
 * @Date 2019/7/23 9:52
 */
public class HttpUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    final static String RESULT = "result";
    final static String MSG = "msg";
    /**
     * 状态码：0代表成功，其它代表失败
     */
    final public static int STATUS_SUCCESS = 0;

    /**
     * 读取body内容
     *
     * @param request
     * @return
     */
    public static byte[] readBody(HttpServletRequest request) {
        try {
            return IOUtils.toByteArray(request.getInputStream());
        } catch (IOException e) {
            logger.error("读取body失败,e:", e);
        }
        return new byte[0];
    }

    /**
     * @Author TangXu
     * @Description 解析Request中请求参数转换为JSONObject
     * @Date 2018/9/9 15:16
     * @Param [request]
     */
    public static JSONObject convertMap2JSONObject(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<String> keys = parameterMap.keySet();
        keys.forEach(key -> {
            jsonObject.put(key, parameterMap.get(key)[0]);
        });
        return jsonObject;
    }

    /**
     * @Author TangXu
     * @Description 解析Request，读取流，获取Json对象
     * @Date 2018/9/7 17:42
     * @Param [request]
     */
    public static JSONObject getJSONObject(HttpServletRequest request) {
        String data = null;
        try {
            byte[] body = readBody(request);
            data = new String(body, "UTF-8");
            logger.info("getJSONObject request data is : " + data);
        } catch (IOException e) {
            logger.error("读取body失败,e:", e);
        }
        return JSONObject.parseObject(data);
    }

    /**
     * @author: TangXu
     * @date: 2018/10/26 16:29
     * @description: HttpServletRequest转对象
     * @param: [request, clazz]
     */
    public static Object parseObject(HttpServletRequest request, Class<?> clazz) {
        String data = null;
        try {
            byte[] body = readBody(request);
            data = new String(body, "UTF-8");
            logger.info("getJSONObject request data is : " + data);
            return JSONObject.parseObject(data, clazz);
        } catch (IOException e) {
            logger.error("读取body失败,e:", e);
            return null;
        }
    }

    /**
     * 对结果进行处理
     *
     * @param result
     * @return
     */
    private static byte[] wrapMSG(HttpServletResponse response, String result) {
        byte[] bytes = null;
        try {
            bytes = result.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("处理失败,e:", e);
        }
        // response.setContentType("text/html; charset=UTF-8");
        response.setContentLength(bytes.length);
        logger.debug(result);
        return bytes;
    }

    /**
     * 解密http请求内容,对字节做解密处理
     *
     * @param content
     * @return
     */
    public static JSONObject decrypt(byte[] content) {
        int index = -1;
        for (int i = 0; i < content.length; i++) {
            if (content[i] == (byte) 0) {
                index = i;
                break;
            }
        }
        String shead = new String(ByteUtils.copy(content, 0, index));
        byte[] tail = ByteUtils.copy(content, index + 1, content.length - index - 1);

        try {
            JSONObject json = JSONObject.parseObject(shead);
            // 不加密
            if ("0".equals(json.getString("version"))) {
                return JSONObject.parseObject(new String(tail, "UTF-8"));
            } else {// 加密
                byte[] key = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 0, 0, 0, 0, 0, 0};
                byte[] bJsonMsg = XxteaUtil.decrypt(tail, key);
                String str = new String(bJsonMsg, "UTF-8");
                return JSONObject.parseObject(str);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("e:", e);
        }
        return null;
    }

    /**
     * 返回错误信息
     *
     * @param errorCode
     * @param msg
     * @return
     */
    public static String error(long errorCode, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"").append(RESULT).append("\"").append(":").append(errorCode);
        sb.append(",");
        sb.append("\"").append(MSG).append("\"").append(":").append("\"").append(msg).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**
     * 返回字节类型
     *
     * @param errorCode
     * @param msg
     * @return
     */
    public static byte[] errorBytes(HttpServletResponse response, long errorCode, String msg) {
        String str = error(errorCode, msg);
        return wrapMSG(response, str);
    }

    /**
     * 成功后返回字节类型
     *
     * @return
     */
    public static byte[] successBytes(HttpServletRequest request, HttpServletResponse response, String jsonStr) {
        request.setAttribute("RESPONSE_DATA", JSONObject.parseObject(jsonStr));
        return wrapMSG(response, jsonStr);
    }

    /**
     * 将请求内容转换成json(不对字节做特殊处理)
     *
     * @param content
     * @return
     */
    public static JSONObject convertHttpContent(String content) throws Exception {
        JSONObject json = null;
        try {
            json = JSONObject.parseObject(content);
        } catch (Exception e) {
            if (content.startsWith("val=")) {
                try {
                    content = content.replace("val=", "");
                    content = URLDecoder.decode(content, "utf-8");
                    json = JSONObject.parseObject(content);
                } catch (Exception e1) {
                    throw e1;
                }
            } else {
                throw e;
            }
        }

        return json;
    }

    /**
     * 解密http请求内容,对字节做解密处理
     *
     * @param content_old
     * @return
     */
    public static JSONObject decryptJsBase64(byte[] content_old) {

        String keys = "1234567890";
        try {
            String sss = new String(content_old, "UTF-8");
            String resultData = XxteaUtil.decryptStrBase64(sss.replaceAll("\"", ""), "UTF-8", keys);
            if (!StringUtils.hasLength(resultData)) {
                return null;
            }

            int index = resultData.indexOf("}0");
            if (index < 1) {
                return null;
            }

            String shead = resultData.substring(0, index + 1);
            if (!StringUtils.hasLength(shead)) {
                return null;
            }
            String tail = resultData.substring(index + 2);
            if (!StringUtils.hasLength(tail)) {
                return null;
            }

            JSONObject datajson = null;
            JSONObject json = JSONObject.parseObject(shead);
            if (json == null) {
                return JSONObject.parseObject(tail);
            }
            //这里拿取 app_id version
            String app_id = json.getString("app_id");
            String version = json.getString("version");
            datajson = JSONObject.parseObject(tail);
            datajson.put("version", version);
            return datajson;
        } catch (UnsupportedEncodingException e) {
            logger.error("e:", e);
        }
        return null;
    }

    /**
     * 解密http请求内容,对字节做解密处理
     *
     * @param content
     * @return
     */
    public static JSONObject decrypt2(byte[] content) {
        int index = -1;
        for (int i = 0; i < content.length; i++) {
            if (content[i] == (byte) 0) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            return null;
        }
        String shead = new String(ByteUtils.copy(content, 0, index));
        byte[] tail = ByteUtils.copy(content, index + 1, content.length - index - 1);

        try {
            //数据json
            JSONObject datajson = null;

            JSONObject json = JSONObject.parseObject(shead);
            if (json == null) {
                return JSONObject.parseObject(new String(tail, "UTF-8"));
            }
            //这里拿取 app_id version
            String app_id = json.getString("app_id");
            String version = json.getString("version");

            // 不加密
            if ("0".equals(json.getString("version"))) {
                datajson = JSONObject.parseObject(new String(tail, "UTF-8"));
                datajson.put("version", version);
                return datajson;
            } else {// 加密
                byte[] key = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 0, 0, 0, 0, 0, 0};
                byte[] bjsonmsg = XxteaUtil.decrypt(tail, key);
                String str = new String(bjsonmsg, "UTF-8");
                datajson = JSONObject.parseObject(str);
                datajson.put("version", version);
                return datajson;
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("e:", e);
        }
        return null;
    }

}