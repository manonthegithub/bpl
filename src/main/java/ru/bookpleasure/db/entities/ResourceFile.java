package ru.bookpleasure.db.entities;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by Kirill on 30/03/16.
 */
@Entity
public class ResourceFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID id;

    public byte[] data;

    @Column(unique = true)
    public String name;
}
