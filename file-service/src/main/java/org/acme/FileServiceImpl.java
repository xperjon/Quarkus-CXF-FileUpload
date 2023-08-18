package org.acme;


import io.quarkiverse.cxf.it.server.FileService;
import io.quarkiverse.cxf.it.server.Upload;
import io.quarkiverse.cxf.it.server.UploadResponse;
import jakarta.jws.WebService;

@WebService(serviceName = "FileService")
public class FileServiceImpl implements FileService{

    @Override
    public UploadResponse uploadFile(Upload parameters) {
        System.out.println("Uploaded file name: " + parameters.getFilename());
        System.out.println("Uploaded file size: " + parameters.getData().length);
        UploadResponse dummy = new UploadResponse();
        dummy.setSize(parameters.getData().length);
        return dummy;
    }

}
