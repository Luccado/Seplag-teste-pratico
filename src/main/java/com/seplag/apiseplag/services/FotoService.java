package com.seplag.apiseplag.services;

import io.minio.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Profile("minio")
@Service
public class FotoService {

    @Value("${minio.bucketName}")
    private String bucketName;

    private final MinioClient minioClient;

    @Autowired
    public FotoService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String uploadFoto(MultipartFile file, Integer pessoaId) throws Exception {
        // Verifica se o bucket existe, caso contrário cria
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        String objectName = "fotos/pessoa-" + pessoaId + "/" + file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .contentType(file.getContentType())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .build());

        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .method(io.minio.http.Method.GET)
                        .build());
    }

    public InputStream getFoto(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }
}