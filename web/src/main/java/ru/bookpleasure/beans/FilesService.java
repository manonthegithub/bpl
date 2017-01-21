package ru.bookpleasure.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.bookpleasure.db.entities.ResourceFile;
import ru.bookpleasure.repos.FilesRepo;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * Created by Kirill on 30/03/16.
 */
@Component
@Lazy
public class FilesService {

    @Autowired
    FilesRepo filesRepo;

    @Autowired
    ServletContext context;

    private static final String IMAGE_FILES_PATH = "/img/products";

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void save(String fileName, byte[] fileData) {
        ResourceFile fileEntity = new ResourceFile();
        fileEntity.setName(fileName);
        fileEntity.setData(fileData);
        filesRepo.save(fileEntity);
        saveToDisk(fileName, fileData);
    }

    public List<ResourceFile> findByFilenamePrefix(String filename) {
        return filesRepo.findByNameEndingWith(filename);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(ResourceFile... files) {
        Collection<ResourceFile> filesCollection = Arrays.asList(files);
        delete(filesCollection);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Collection<ResourceFile> files) {
        filesRepo.delete(files);
    }

    public void loadFilesFromDbAfterDeploy() {
        filesRepo
                .findAll()
                .forEach(
                        rf -> this.saveToDisk(rf.getName(), rf.getData())
                );
    }

    private void saveToDisk(String fileName, byte[] data) {
        Path imagePath = Paths.get(context.getRealPath(IMAGE_FILES_PATH), fileName);
        imagePath.toFile().mkdirs();
        try {
            Files.write(imagePath, data);
        } catch (IOException e) {
            throw new RuntimeException("write failed");
        }
    }

}
