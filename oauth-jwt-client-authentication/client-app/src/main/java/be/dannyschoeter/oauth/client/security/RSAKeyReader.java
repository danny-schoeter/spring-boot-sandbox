package be.dannyschoeter.oauth.client.security;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


public class RSAKeyReader {

    private final String privateKeyLocation;
    private final String publicKeyLocation;
    private final ResourceLoader resourceLoader;

    public RSAKeyReader(String privateKeyLocation, String publicKeyLocation, ResourceLoader resourceLoader) {
        this.privateKeyLocation = privateKeyLocation;
        this.publicKeyLocation = publicKeyLocation;
        this.resourceLoader = resourceLoader;
    }


    public RSAPublicKey getPublicKey() throws IOException {
        Resource resource = resourceLoader.getResource(publicKeyLocation);
        try (PEMParser pemParser = new PEMParser(new InputStreamReader(resource.getInputStream()))) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
            return (RSAPublicKey) converter.getPublicKey(publicKeyInfo);
        }
    }

    public RSAPrivateKey getPrivateKey() throws IOException {
        Resource resource = resourceLoader.getResource(privateKeyLocation);
        try (PEMParser pemParser = new PEMParser(new InputStreamReader(resource.getInputStream()))) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemParser.readObject());
            return (RSAPrivateKey) converter.getPrivateKey(privateKeyInfo);
        }
    }
}
