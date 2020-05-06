package com.dmg.lobbyserver.service.impl;

import com.dmg.lobbyserver.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/21 18:03
 * @Version V1.0
 **/
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {


    public void depositNotify(String phoneNumber, String userName ,Long depositNum) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("http://148.66.63.107:8001/api?");
            builder.append("username=");
            builder.append("Monaco-8direct");
            builder.append("&password=");
            builder.append("mfmhmwim");
            builder.append("&ani=");
            builder.append("852678967893");
            builder.append("&dnis=");
            builder.append("86"+phoneNumber);
            builder.append("&message=");
            builder.append(userName);
            builder.append("会员您好，您的存款${"+depositNum+"}已经添加，请您登入查询，谢谢。");
            builder.append("&command=");
            builder.append("submit");

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("47.112.127.70", 8001));
            URL url = new URL(builder.toString());

            HttpURLConnection uc = (HttpURLConnection)url.openConnection(proxy);
            uc.setRequestMethod("GET");
            uc.connect();

            if (uc.getResponseCode()== 200){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void withdrawalNotify(String phoneNumber, String userName ,Long withdrawalNum) {
        try {
            // xxx会员您好，您的提款${amount}已支付，请您查收，谢谢。
            StringBuilder builder = new StringBuilder();
            builder.append("http://148.66.63.107:8001/api?");
            builder.append("username=");
            builder.append("Monaco-8direct");
            builder.append("&password=");
            builder.append("mfmhmwim");
            builder.append("&ani=");
            builder.append("852678967893");
            builder.append("&dnis=");
            builder.append("86"+phoneNumber);
            builder.append("&message=");
            builder.append(userName);
            builder.append("会员您好，您的提款${"+withdrawalNum+"}已支付，请您查收，谢谢。");
            builder.append("&command=");
            builder.append("submit");

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("47.112.127.70", 8001));
            URL url = new URL(builder.toString());

            HttpURLConnection uc = (HttpURLConnection)url.openConnection(proxy);
            uc.setRequestMethod("GET");
            uc.connect();

            if (uc.getResponseCode()== 200){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}