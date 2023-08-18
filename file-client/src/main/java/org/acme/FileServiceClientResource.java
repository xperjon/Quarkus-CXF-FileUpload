package org.acme;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.quarkiverse.cxf.annotation.CXFClient;
import io.quarkiverse.cxf.it.server.FileService;
import io.quarkiverse.cxf.it.server.Upload;
import io.quarkiverse.cxf.it.server.UploadResponse;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path("/files")
public class FileServiceClientResource {


    @CXFClient("myFileService")
    FileService fileService;

    @POST
    @Path("/upload/{filename}")
    @Produces(MediaType.TEXT_PLAIN)
    public String uploadFile(@PathParam("filename") String filename) {
        Upload file = new Upload();
        //Working file
        // String filename = "file-sample_3kB.pdf";
        //Not working file
        // String filename = "file-sample_150kB.pdf";
        if(filename == null || filename.isEmpty()){
            filename = "file-sample_3kB.pdf";
        }
        file.setFilename(filename);
        byte[] data = null;
        try {
            URL resource = getClass().getClassLoader().getResource(filename);
            data = Files.readAllBytes(Paths.get(resource.toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        file.setData(data);
        UploadResponse response = fileService.uploadFile(file);
        return "Successfully uploaded + " + response.getSize() + " bytes";
    }
}
