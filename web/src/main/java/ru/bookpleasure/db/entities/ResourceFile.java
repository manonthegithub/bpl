package ru.bookpleasure.db.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Kirill on 30/03/16.
 */
@Entity
public class ResourceFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private byte[] data;

    @Column(unique = true)
    private String name;

    @Column(name = Constants.CREATED_AT, nullable = false)
    private Timestamp createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceFile that = (ResourceFile) o;

        if (!id.equals(that.id)) return false;
        if (!Arrays.equals(data, that.data)) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + Arrays.hashCode(data);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
