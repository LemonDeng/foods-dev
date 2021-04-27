package com.dzl.service.impl;


import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

import com.dzl.service.FdfsService;
import org.apache.catalina.webresources.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class FdfsServiceImpl implements FdfsService {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private FileResource fileResource;

    @Override
    public String upload(MultipartFile file, String fileExtName) throws Exception {

        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(),
                                file.getSize(),
                                fileExtName,
                                null);

        String path = storePath.getFullPath();

        return path;
    }


}
