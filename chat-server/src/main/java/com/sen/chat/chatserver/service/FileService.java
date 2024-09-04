package com.sen.chat.chatserver.service;

import com.sen.chat.chatserver.dto.vo.UploadFileResp;
import com.sen.chat.chatserver.utils.MinioUtil;
import com.sen.chat.common.constant.dict.UploadingStatusEnum;
import com.sen.chat.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/27 21:37
 */
@Slf4j
@Service
public class FileService {


    @Resource
    private MinioUtil minioUtil;

    /**
     * 分片上传文件
     *
     * @param fileId     生成的文件ID，第一片不会附带fileId
     * @param file       分片文件
     * @param fileName   文件名称
     * @param md5        MD5值
     * @param chunkIndex 当前文件分片位置
     * @param chunks     分片数
     */
    public UploadFileResp uploadFile(String fileId, MultipartFile file, String fileName,
                                     String md5, Integer chunkIndex, Integer chunks) {
        if (StringUtils.isEmpty(fileId)) {
            fileId = UUID.randomUUID().toString();
        }
        UploadFileResp uploadFileResp = new UploadFileResp();
        uploadFileResp.setFileId(fileId);

        //md5存在实现秒传

        boolean uploadSuccess = true;
        try {
            //分片上传
            String chunkObjectName = fileId + "_" + chunkIndex;
            minioUtil.uploadChunkToMinio(chunkObjectName, file);
            if (chunkIndex < chunks - 1) {
                uploadFileResp.setUploadingStatus(UploadingStatusEnum.UPLOADING.getCode());
                return uploadFileResp;
            }
            //合并
            String objectName = fileId + getFileSuffix(fileName);
            minioUtil.composeFile(fileId, objectName, chunks);
            minioUtil.removeChunkFile(fileId, chunks);
            uploadFileResp.setUploadingStatus(UploadingStatusEnum.UPLOAD_FINISH.getCode());

        } catch (Exception e) {
            log.error("文件上传失败", e);
            uploadSuccess = false;
            throw new BusinessException("文件上传失败");
        } finally {
            if (!uploadSuccess) {
                minioUtil.removeChunkFile(fileId, chunkIndex + 1);
            }
        }
        return uploadFileResp;
    }


    public Boolean createCover4Img(File pic, int width, File targetFile) {
        try {
            BufferedImage src = ImageIO.read(pic);
            int srcWidth = src.getWidth();
            int srcHeight = src.getHeight();
            if (srcWidth <= width) {
                return false;
            }
            compressImage(pic, width, targetFile, false);
            return true;
        } catch (Exception e) {
            log.error("图片生成缩略图失败", e);
            throw new BusinessException("图片生成缩略图失败");
        }
    }

    private void compressImage(File sourceFile, Integer width, File targetFile, Boolean delSource) {
        Process process = null;
        try {
            final String CMD_COMPRESS_IMAGE = "ffmpeg -i %s -vf scale=%d:-1 %s -y";
//            final String CMD_COMPRESS_IMAGE = "ffmpeg -i %s -vf scale=%d:%d %s -y"; //scale=width:height -1表示不压缩
            String compress_cmd = String.format(CMD_COMPRESS_IMAGE, sourceFile.getAbsolutePath(), width, targetFile.getAbsolutePath());
            process = Runtime.getRuntime().exec(compress_cmd);
            recordProcessResult(compress_cmd, process.getInputStream(), process.getErrorStream());
            process.waitFor();
            if (delSource) {
                FileUtils.forceDelete(sourceFile);
            }
        } catch (Exception e) {
            log.error("生成缩略图失败", e);
            throw new BusinessException("生成缩略图失败");
        }
    }

    private void recordProcessResult(final String cmd, final InputStream inputStream, final InputStream errorStream) {
        CountDownLatch countDownLatch = new CountDownLatch(2);//分别是输出流和错误流
        CompletableFuture.runAsync(() -> {
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
                StringBuffer result = new StringBuffer();
                String line = null;
                while ((line = inputReader.readLine()) != null) {
                    result.append(line);
                }
                inputStream.close();
                log.info("命令：{}执行完毕，执行结果：{}", cmd, result);
                countDownLatch.countDown();
            } catch (Exception e) {
                log.error("命令：{}执行失败", cmd, e);
                throw new RuntimeException();
            }
        }).whenComplete((unused, throwable) -> {
            throw new RuntimeException();
        });
        CompletableFuture.runAsync(() -> {
            try {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream, "GBK"));
                StringBuffer err = new StringBuffer();
                String line = null;
                while ((line = errorReader.readLine()) != null) {
                    err.append(line);
                }
                errorStream.close();
                log.info("命令：{}执行完毕，错误信息：{}", cmd, err);
                countDownLatch.countDown();
            } catch (Exception e) {
                log.error("命令：{}执行失败", cmd);
            }
        }).whenComplete((unused, throwable) -> {
            throw new RuntimeException();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("命令：{}执行失败", cmd);
        }
    }

    public String getFileNameNoSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return fileName;
        }
        fileName = fileName.substring(0, index);
        return fileName;
    }

    public String getFileSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        fileName = fileName.substring(index);
        return fileName;
    }
}
