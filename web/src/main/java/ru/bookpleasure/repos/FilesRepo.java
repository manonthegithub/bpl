package ru.bookpleasure.repos;

import org.springframework.data.repository.CrudRepository;
import ru.bookpleasure.db.entities.ResourceFile;

import java.util.List;
import java.util.UUID;

/**
 * Created by Kirill on 13/06/16.
 */
public interface FilesRepo extends CrudRepository<ResourceFile, UUID> {

    List<ResourceFile> findByNameEndingWith(String fileName);

}
