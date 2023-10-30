package com.example.phpreplica;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Configuration
public class SslConfiguration {

    @Bean
    public CloseableHttpClient httpClient() throws Exception {
        // Load Certificate
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream certStream = new FileInputStream("/home/ornob/Downloads/createorder.crt");
        Certificate cert = cf.generateCertificate(certStream);

        // Load PrivateKey
        String key = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC6zbl2fwSLTy6/\n" +
                "3vltEP6NfQAYrLQfWwwpq4n4u0z169mEBzNrPPvr3QuY3dEyePTqciJa4swVc2B/\n" +
                "lMckPJCqUuDFSCH44dUzh0uuyhXF7AD2gJpTctTZBS5uzNa8G2vkghkO8HA73od0\n" +
                "/QK2SFgcsiZinxdCGX4OQMuIVbCocHu8SJdGM28mR//GGelnjIZrsZBm3nNZgguj\n" +
                "B3BgGXu3GbfeNBqiRLmBRp0Dop5ClAxEpu+WnJTD/GyLvSZJsWi3rmiQU050W2b1\n" +
                "s+Z2l6ufOLavCmBIhXEvugqzCU2cKfquVqb9cLjnQCX0Itmo/tNPJnDj07HbABKt\n" +
                "3g81Tk4hAgMBAAECggEBAIeYGv5jdpX0dRe7L0w24L8tM+u1OM3l1e9NX8oXkuaT\n" +
                "OwF2BKHb03VCr09X+6jqWcTv8fYM2FzdsfcJtH+eItgvbo5lLJuuuYY8t/f5IlLX\n" +
                "W1+Su/LE0LFy7GBq+ZfzQ57LvIq2hpE+lw6ZNOXBn4u82NYPat+FB5Yq/FJK+P9Z\n" +
                "34yHBOuFVP0DfFzVA8EQxlJ1EayQ73rJFa+PW8v1CcNc+x69M4YG/bNF7AFEzvMh\n" +
                "ehGiJ5TM+hOn6uxHf5Gx1iDjorFugby4OaPKB6SYnRG0AqiBlIDM/PRQQK3a/qv7\n" +
                "p7efo6Bh/MJs36AaMNqYGvTdEnzpFvL9criS3Ng8PhECgYEA8u8gaLHhgRHgGsxG\n" +
                "LRkBeVE+fx2fZvJF5rZsniUVA0BaJonfkeaD6unupPNWYJ+HhDgUoajXumFubZIx\n" +
                "fAJBgml84gIV4Kl6VpTt8+PnZazDsbrlMYfK/keSf9bj/zvEjMyNC13ne1U6ktfW\n" +
                "hZSoobOfrSUOsahyF2/yx6sDQ8UCgYEAxNnB2KVS8cDcqf/EIxS3hA7+5dwlMMA9\n" +
                "oeIJEDItOcM9x/lVeXWtxNrRamWAxIn2vE69iEODfwrlhvfEdioMBA+Tj7WF+DqD\n" +
                "AWKhsMa5wdIOGlpU+Z1Tnc5VT7/uwdRx6m8Fb/KH1G5h7jO+/EQ0aV1cKG/tsMXE\n" +
                "UbtqjwxRmq0CgYEA0UD+9o96QdYm5o5cSE5IwbokjhPzERbQ9TjfeJ1SazmMLbQG\n" +
                "+sLHkJquMDFWjuX7Mf2ob9Ni9y0jlIgfzcSXrwC0TG0lcR4qvdeM287Mq/zTmAz3\n" +
                "jOF+DeeSnkF4pIpT9D/hog/h649vY7s9ElUJqDdz7A2Giashk2SfiDObHU0CgYEA\n" +
                "mLDcRwL+Ep3pzEejSR7htqK3H28ivezwIaimJRT2ta53Nq11mpAoaODevavcNRda\n" +
                "9vgQ1iigUCQQpB9+GbZFucSnTjqcn04rniB2kRukrO4VmGVnMnPRjz+kNwcG2PzE\n" +
                "G/df/VTf/WxdtBoZU65IBU6wlIE1mv/cZxV781e32MUCgYBYZzS49anuWOzTX93L\n" +
                "aYNkSttQr2axIBZOsZJN85Vl5elbumbtjQgX4+z2MUGdHe0GJqUGt5gKVfxHPOpX\n" +
                "6z5rx1oGdbhM2rmdwvh4lFPy8s5X2EWuePKCfils2SQG16+FSen7yrANXowSLwXW\n" +
                "B1jwcLCJmyRAdprctkvD3kWM8g==" +
                "-----END PRIVATE KEY-----";
        key = key.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\n", "");
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key)));

        // Create KeyStore
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("certificate", cert);
        keyStore.setKeyEntry("private-key", privateKey, "password".toCharArray(), new Certificate[]{cert});

        // Configure custom SSL context
        SSLContext sslContext = SSLContext.getInstance("TLS");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, "password".toCharArray());
        sslContext.init(kmf.getKeyManagers(), null, new java.security.SecureRandom());

        // Build HttpClient with custom SSL context
        SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslContext)
                .build();

        return HttpClients.custom()
                .setConnectionManager(
                        PoolingHttpClientConnectionManagerBuilder.create()
                                .setSSLSocketFactory(sslSocketFactory)
                                .build()
                )
                .build();
    }
}

