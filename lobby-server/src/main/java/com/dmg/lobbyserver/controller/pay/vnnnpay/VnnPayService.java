package com.dmg.lobbyserver.controller.pay.vnnnpay;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.common.enums.VnnnPayErrorEnum;
import com.dmg.lobbyserver.common.util.AesUtil;
import com.dmg.lobbyserver.model.dto.CommonRespDTO;
import com.dmg.lobbyserver.model.dto.pay.*;
import com.dmg.server.common.util.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:10 2019/12/6
 */
@Slf4j
@Component
public class VnnPayService {

    //appid
    private static final String APP_ID = "2500";

    //token
    private static final String TOKEN = "ui20d*&mc[>ae334";

    //key
    private static final String SECRET_KEY = "54UY2%(!dqk8$@TK";

    @Value("${vnnnpay.domain}")
    private String domain;

    //检查回兑接口
    private static final String CHECK_EXCHANGE_URL = "/kal_pay/v1/check_exchange_kal";

    //回兑接口
    private static final String EXCHANGE_URL = "/kal_pay/v1/exchange_kal";

    // 兑换比例
    private static final BigDecimal EXCHANGE_RATE = new BigDecimal(0.15);

    /**
     * @Author liubo
     * @Description //TODO 检查回兑&回兑
     * @Date 14:57 2020/2/12
     **/
    public CommonRespDTO exchange(String userId, BigDecimal gold, Boolean isExchange, String orderCode) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String rand = String.valueOf(RandomUtil.randomInt(10000, 99999));
        BigDecimal changeLimit = BigDecimal.ZERO;
        try {
            String content = new String(Base64.encodeBase64(AesUtil.encrypt(
                    JSONObject.toJSONString(VnnPayCheckExchangeReqDTO.builder()
                            .app_id(APP_ID)
                            .uid(userId)
                            .vnd(gold.multiply(EXCHANGE_RATE).intValue())
                            .order_id(orderCode).build()).getBytes(Charsets.UTF_8.name()),
                    SECRET_KEY.getBytes(Charsets.UTF_8.name()), SECRET_KEY.getBytes(Charsets.UTF_8.name()))), Charsets.UTF_8.name());
            log.info("加密原始数据解密:{}",new String(AesUtil.decrypt(Base64.decodeBase64(content.getBytes(Charsets.UTF_8.name())), SECRET_KEY.getBytes(Charsets.UTF_8.name()), SECRET_KEY.getBytes(Charsets.UTF_8.name()))));
            VnnPayDataDTO reqData = VnnPayDataDTO.builder()
                    .sig(this.getExchangeSign(timestamp, rand, content))
                    .app_id(APP_ID)
                    .rand(rand)
                    .timestamp(timestamp)
                    .content(content).build();
            log.info("检查回兑&回兑req url:{}", isExchange ? domain.concat(EXCHANGE_URL) : domain.concat(CHECK_EXCHANGE_URL));
            log.info("检查回兑&回兑req data:{}", JSONObject.toJSONString(reqData));
            VnnPayDataDTO result = HttpClient.getInstance().postForJson(isExchange ? domain.concat(EXCHANGE_URL) : domain.concat(CHECK_EXCHANGE_URL), reqData).execute(VnnPayDataDTO.class);
            log.info("检查回兑&回兑resp data:{}", JSONObject.toJSONString(result));
            //签名验证
            if (!result.getSig().equalsIgnoreCase(this.getExchangeSign(result.getTimestamp(), result.getRand(), result.getContent()))) {
                log.error("签名验证失败");
                return CommonRespDTO.builder().status(false).code(VnnnPayErrorEnum.VNNN_PAY_SIGN_ERROR.getCode()).build();
            }
            //解密数据
            String decData = new String(AesUtil.decrypt(Base64.decodeBase64(result.getContent().getBytes(Charsets.UTF_8.name())), SECRET_KEY.getBytes(Charsets.UTF_8.name()), SECRET_KEY.getBytes(Charsets.UTF_8.name())));
            log.info("aes解密data:{}", decData);
            VnnPayCheckExchangeRespDTO vnnPayCheckExchangeRespDTO = JSONObject.parseObject(decData, VnnPayCheckExchangeRespDTO.class);
            if (!"0".equals(String.valueOf(vnnPayCheckExchangeRespDTO.getRet()))) {
                log.error("检查回兑&回兑失败，{}", vnnPayCheckExchangeRespDTO.getMsg());
                return CommonRespDTO.builder().status(false).code(VnnnPayErrorEnum.getCodeByVnnnPayCode(vnnPayCheckExchangeRespDTO.getRet())).build();
            }
            changeLimit = vnnPayCheckExchangeRespDTO.getQuota();
        } catch (Exception e) {
            log.error("请求检查回兑&回兑出现异常：{}", e);
            return CommonRespDTO.builder().status(false).code(VnnnPayErrorEnum.SYS_ERROR.getCode()).build();
        }
        return CommonRespDTO.builder().status(true).code(VnnnPayErrorEnum.SUCCESS.getCode()).data(changeLimit).build();
    }

    /**
     * @Author liubo
     * @Description //TODO 签名验证
     * @Date 11:39 2019/12/6
     **/
    public Boolean signCheck(String sign, String timestamp, String rand) {
        if (sign.equalsIgnoreCase(this.getSign(timestamp, rand))) {
            return true;
        }
        return false;
    }

    /**
     * @Author liubo
     * @Description //TODO 获取签名
     * @Date 11:39 2019/12/6
     **/
    private String getExchangeSign(String timestamp, String rand, String content) {
        String signData = APP_ID.concat("&").concat(content).concat("&").concat(timestamp).concat("&").concat(rand);
        log.info("sign data:{}", signData);
        String sign = DigestUtils.sha1Hex(signData);
        log.info("生成sign:{}", sign);
        return sign;
    }

    /**
     * @Author liubo
     * @Description //TODO 获取签名
     * @Date 11:39 2019/12/6
     **/
    private String getSign(String timestamp, String rand) {
        String signData = APP_ID.concat(":").concat(TOKEN).concat(":").concat(timestamp).concat(":").concat(rand);
        log.info("sign data:{}", signData);
        String sign = DigestUtils.sha1Hex(signData);
        log.info("生成sign:{}", sign);
        return sign;
    }

    /**
     * @Author liubo
     * @Description //TODO 解密
     * @Date 11:39 2019/12/6
     **/
    public <T> T decryptData(String data, Class<T> clazz) {
        try {
            String base64Data = new String(Base64.decodeBase64(data.getBytes(Charsets.UTF_8.name())), Charsets.UTF_8.name());
            log.info("base64解码data:{}", base64Data);
            String decData = new String(AesUtil.decrypt(this.hexString2Bytes(base64Data), SECRET_KEY.getBytes(Charsets.UTF_8.name()), SECRET_KEY.getBytes(Charsets.UTF_8.name())));
            log.info("aes解密data:{}", decData);
            return JSONObject.parseObject(decData, clazz);
        } catch (Exception e) {
            log.info("解密data:{}出现异常:{}", data, e);
        }
        return null;
    }

    public static void main(String[] args) {
        String data = "xZrnFN/qLvoua/mZa73Jpsh3RT7atM23+cIKP/vZX3n6pENCT8cY+KQBKuW5CN4NjCvuDd64yCAvI1ASeeVs1w==";
        try {
            String base64Data = new String(Base64.decodeBase64(data.getBytes(Charsets.UTF_8.name())), Charsets.UTF_8.name());
            log.info("base64解码data:{}", base64Data);
            String decData = new String(AesUtil.decrypt(hexString2Bytes(base64Data), SECRET_KEY.getBytes(Charsets.UTF_8.name()), SECRET_KEY.getBytes(Charsets.UTF_8.name())));
            log.info("aes解密data:{}", decData);
        } catch (Exception e) {
            log.info("解密data:{}出现异常:{}", data, e);
        }
    }
    /**
     * @Author liubo
     * @Description //TODO 加密
     * @Date 11:37 2019/12/6
     **/
    private String encryptData(String data) {
        try {
            log.info("加密data:{}", data);
            String encData = Base64.encodeBase64String(this.bytesToHex(AesUtil.encrypt(
                    data.getBytes(Charsets.UTF_8.name()), SECRET_KEY.getBytes(Charsets.UTF_8.name()), SECRET_KEY.getBytes(Charsets.UTF_8.name()))).getBytes(Charsets.UTF_8.name()));
            log.info("加密后data:{}", encData);
            return encData;
        } catch (Exception e) {
            log.info("加密data:{}出现异常:{}", data, e);
        }
        return null;
    }

    /**
     * @Author liubo
     * @Description //TODO 获取返回值
     * @Date 11:36 2019/12/6
     **/
    public String getRespData(VnnPayRespContentDTO data) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String rand = String.valueOf(RandomUtil.randomInt(10000, 99999));
        return "signature=".concat(this.getSign(timestamp, rand)).concat("&content=").concat(this.encryptData(JSONObject.toJSONString(data)))
                .concat("&timestamp=").concat(timestamp).concat("&rand=").concat(rand);
    }

    /**
     * @Author liubo
     * @Description //TODO 十六进制转字节数组
     * @Date 15:20 2019/12/6
     **/
    private static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer
                    .valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * @Author liubo
     * @Description //TODO 字节数组转16进制
     * @Date 15:20 2019/12/6
     **/
    private String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
