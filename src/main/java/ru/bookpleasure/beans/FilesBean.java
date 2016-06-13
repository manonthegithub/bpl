package ru.bookpleasure.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.bookpleasure.db.entities.ResourceFile;
import ru.bookpleasure.repos.FilesRepo;
import java.io.*;
import java.nio.file.Paths;


/**
 * Created by Kirill on 30/03/16.
 */
@Component
@Lazy
public class FilesBean {

    @Autowired
    FilesRepo filesRepo;

    public static final String IMAGE_FILES_PATH = "/img/products";

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void save(String fileName, byte[] fileData, String path) {
        ResourceFile fileEntity = new ResourceFile();
        fileEntity.name = fileName;
        fileEntity.data = fileData;
        filesRepo.save(fileEntity);
        saveToDisk(fileName, fileData, path);
    }

    public void loadFilesFromDbAfterDeploy(String path) {
        try {
            Iterable<ResourceFile> files = filesRepo.findAll();
            for (ResourceFile rf : files) {
                FilesBean.saveToDisk(rf.name, rf.data, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveToDisk(String fileName, byte[] data, String path) {
        OutputStream file = null;
        File imagePath = Paths.get(path).toFile();
        imagePath.mkdirs();
        File image = new File(imagePath, fileName);
        try {
            file = new BufferedOutputStream(new FileOutputStream(image, false));
            file.write(data);
            file.flush();
        } catch (IOException e) {
            throw new RuntimeException("write failed");
        } finally {
            try {
                if (file != null) file.close();
            } catch (IOException e) {
            }
        }
    }

}
