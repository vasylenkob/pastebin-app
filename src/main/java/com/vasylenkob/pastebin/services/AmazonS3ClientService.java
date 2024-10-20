package com.vasylenkob.pastebin.services;

import com.vasylenkob.pastebin.models.Post;
import com.vasylenkob.pastebin.models.SavedPost;
import com.vasylenkob.pastebin.models.entities.MetaData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class AmazonS3ClientService {
    @Value("${application.bucket.name}")
    private String bucketName;
    private S3Client s3;

    public AmazonS3ClientService(S3Client s3){
        this.s3 = s3;
    }

    public void savePost(String postName, String content) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(postName)
                .build();
        s3.putObject(putObjectRequest, RequestBody.fromBytes(content.getBytes()));
    }

    public SavedPost getSavedPost(MetaData metaData){
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(metaData.getPostKey())
                .build();
        var response = s3.getObject(getObjectRequest);
        try {
            return new SavedPost(
                    metaData.getTitle(),
                    new String (response.readAllBytes()),
                    metaData.getExpirationDate());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePost(MetaData metaData) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(metaData.getPostKey())
                .build();
        s3.deleteObject(deleteObjectRequest);
    }
}
