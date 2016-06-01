package ru.bookpleasure.db.entities;

import ru.bookpleasure.db.Persistable;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by Kirill on 30/03/16.
 */
@Entity
public class ResourceFile implements Persistable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID id;

    public byte[] data;

    @Column(unique = true)
    public String name;
}
