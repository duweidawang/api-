package com.duwss.common.config;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

import org.springframework.context.annotation.Configuration;


@Configuration
public class Signconfig {

    public static String getSign(String body,String secretKey){
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String content = body + "." + secretKey;
        return md5.digestHex(content);
    }
}
