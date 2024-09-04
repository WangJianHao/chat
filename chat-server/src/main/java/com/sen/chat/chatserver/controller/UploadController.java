package com.sen.chat.chatserver.controller;

import com.sen.chat.chatserver.dto.vo.UploadFileResp;
import com.sen.chat.chatserver.service.FileService;
import com.sen.chat.chatserver.utils.MinioUtil;
import com.sen.chat.common.api.SenCommonResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 23:33
 */
@RestController
@RequestMapping("/file")
public class UploadController {

    @Resource
    private MinioUtil minioUtil;

    @Resource
    private FileService fileService;

    /**
     * 上传文件
     */
//    @PostMapping(value = "/upload")
//    public String uploadReport(MultipartFile[] files) {
//        for (MultipartFile file : files) {
//            String fileName = file.getOriginalFilename();
//            String rename = UUID.randomUUID().toString();
//            minioUtil.upload(file, rename);
//        }
//        return "上传成功";
//    }

    /**
     * 上传文件
     */
    @PostMapping(value = "/upload")
    public SenCommonResponse<UploadFileResp> upload(@RequestParam(value = "fileId", required = false) String fileId,
                                                    MultipartFile file,
                                                    @RequestParam(value = "fileName") String fileName,
                                                    @RequestParam(value = "md5") String md5,
                                                    @RequestParam(value = "chunkIndex") Integer chunkIndex,
                                                    @RequestParam(value = "chunks") Integer chunks) {
        return SenCommonResponse.success(fileService.uploadFile(fileId, file, fileName, md5, chunkIndex, chunks));
    }

    /**
     * 预览文件
     */
    @GetMapping("/preview")
    public String preview(String fileName) {
        return minioUtil.getFileUrl(fileName);
    }

    /**
     * 下载文件
     */
    @GetMapping("/download")
    public void download(String fileName, HttpServletResponse response) {
        minioUtil.download(response, fileName);
    }

    /**
     * 删除文件
     */
    @GetMapping("/delete")
    public String delete(String fileName) {
        minioUtil.delete(fileName);
        return "删除成功";
    }

}
