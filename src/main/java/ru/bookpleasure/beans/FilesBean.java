package ru.bookpleasure.beans;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ru.bookpleasure.db.entities.ResourceFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.*;
import java.nio.file.Paths;
import java.util.List;


/**
 * Created by Kirill on 30/03/16.
 */
@Component
@Lazy
@Scope(
        value = WebApplicationContext.SCOPE_REQUEST,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FilesBean {

    @PersistenceContext
    EntityManager em;

    public static final String IMAGE_FILES_PATH = "/img/products";

    public void loadFilesFromDbAfterDeploy(String path) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ResourceFile> query = cb.createQuery(ResourceFile.class);
            Root<ResourceFile> root = query.from(ResourceFile.class);
            query.select(root);
            TypedQuery<ResourceFile> preparedQuery = em.createQuery(query);
            List<ResourceFile> files = preparedQuery.getResultList();
            for (ResourceFile rf : files) {
                saveToDisk(rf.name, rf.data, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void save(String fileName, byte[] fileData, String path) {
        ResourceFile fileEntity = new ResourceFile();
        fileEntity.name = fileName;
        fileEntity.data = fileData;
        em.merge(fileEntity);
        saveToDisk(fileName, fileData, path);
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
