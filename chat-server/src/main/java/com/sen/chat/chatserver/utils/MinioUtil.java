package com.sen.chat.chatserver.utils;

import com.sen.chat.chatserver.config.MinioConfig;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 23:30
 */
@Slf4j
@Component
public class MinioUtil {

    @Resource
    private MinioClient minioClient;

    @Resource(name = "minioConfig", type = MinioConfig.class)
    private MinioConfig configuration;

    /**
     * 判断bucket是否存在，不存在则创建
     */
    public boolean existBucket(String bucketName) {
        boolean exists;
        try {
            exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                exists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            exists = false;
        }
        return exists;
    }

    /**
     * 删除bucket
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 上传文件
     *
     * @param file     文件
     * @param fileName 文件名称
     */
    public void upload(MultipartFile file, String fileName) {
        // 使用putObject上传一个文件到存储桶中。
        try {
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(configuration.getBucketName())
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void composeFile(String fileId, String objectName, Integer chunks) throws Exception {
        List<ComposeSource> sources = buildSources(fileId, chunks);
        log.info("开始合并文件：{}", objectName);
        ObjectWriteResponse response = minioClient.composeObject(ComposeObjectArgs.builder()
                .bucket(configuration.getBucketName())
                .object(objectName)
                .sources(sources)
                .build());
        log.info("合并完成，合并结果：{}", response);
    }

    public void removeChunkFile(String fileId, Integer chunkIndex) {
        List<ComposeSource> sources = buildSources(fileId, chunkIndex);
        //删除分片
        for (ComposeSource source : sources) {
            try {
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(configuration.getTempBucketName()).object(source.object()).build());
            } catch (Exception e) {
                log.error("删除临时文件失败", e);
            }
        }
    }

    private List<ComposeSource> buildSources(String fileId, Integer chunks) {
        List<ComposeSource> sources = new ArrayList<>();
        for (int i = 0; i < chunks; i++) {
            String chunkObjectName = fileId + "_" + i;
            sources.add(ComposeSource.builder().bucket(configuration.getTempBucketName()).object(chunkObjectName).build());
        }
        return sources;
    }

    public ComposeSource uploadChunkToMinio(String objectName, MultipartFile file) throws Exception {
        log.info("上传分片：{},大小：{}", objectName, file.getSize());
        // 创建一个基于buffer的输入流
        InputStream inputStream = file.getInputStream();
        //上传分片
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(configuration.getTempBucketName())
                .object(objectName)
                .stream(inputStream, file.getSize(), -1)
                .build());
        // 将分片信息添加到sourceList中
        return ComposeSource.builder().bucket(configuration.getTempBucketName()).object(objectName).build();
    }


    /**
     * 获取文件访问地址（有过期时间）
     *
     * @param fileName 文件名称
     * @param time     时间
     * @param timeUnit 时间单位
     */
    public String getExpireFileUrl(String fileName, int time, TimeUnit timeUnit) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(configuration.getBucketName())
                    .object(fileName)
                    .expiry(time, timeUnit).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件访问地址
     *
     * @param fileName 文件名称
     */
    public String getFileUrl(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(configuration.getBucketName())
                    .object(fileName)
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名称
     */
    public void download(HttpServletResponse response, String fileName) {
        InputStream in = null;
        try {
            // 获取对象信息
            StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder().bucket(configuration.getBucketName()).object(fileName).build());
            response.setContentType(stat.contentType());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 文件下载
            in = minioClient.getObject(GetObjectArgs.builder().bucket(configuration.getBucketName()).object(fileName).build());
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名称
     */
    public void delete(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(configuration.getBucketName()).object(fileName).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
