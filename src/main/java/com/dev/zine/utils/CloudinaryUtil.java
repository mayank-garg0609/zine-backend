package com.dev.zine.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dev.zine.api.model.images.ImagesUploadRes;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryUtil {
    @Autowired
    private Cloudinary cloudinary;

    public File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }

    /**
     * Uploads a file to Cloudinary.
     *
     * @param file   the MultipartFile to be uploaded
     * @param folder the folder in Cloudinary where the file will be stored
     * @return the URL of the uploaded file
     * @throws IOException if there's an issue with file upload
     */
    @SuppressWarnings("unchecked")
    public ImagesUploadRes uploadFile(MultipartFile file, String folder) throws IOException {
        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                "folder", folder,                       // Target folder in Cloudinary
                "resource_type", "auto",                // Detect the resource type automatically
                "overwrite", true,                     // Allow overwriting files
                "use_filename", true,                  // Use the original file name in Cloudinary
                "unique_filename", true                // Generate a unique name to avoid clashes
            );
            File tempFile = convertMultipartFileToFile(file);
            Map<String, Object> result = cloudinary.uploader().upload(
                tempFile, uploadParams
            );
            if(tempFile.exists()) tempFile.delete();
            ImagesUploadRes res = new ImagesUploadRes();
            res.setUrl(result.get("url").toString());
            res.setPublicId(result.get("public_id").toString());
            res.setMessage("DP successfully updated");
            return res;
        } catch (IOException e) {
            System.err.println("Error during Cloudinary upload: " + e.getMessage());
            throw new IOException("Failed to upload the file to Cloudinary", e);
        }
    }

    @SuppressWarnings("unchecked")
    public String deleteImage(String publicId) {
        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            
            if ("ok".equals(result.get("result"))) {
                return "Image deleted successfully";
            } else {
                return "Failed to delete image";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while deleting image";
        }
    }
}

