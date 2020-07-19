package com.coinsthai.model;

import com.coinsthai.pojo.AttachmentPojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author
 */
@Entity
@Table(name = "t_attachment")
public class Attachment extends AttachmentPojo {

    private User user;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    @Override
    public String getId() {
        return super.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column
    @Override
    public String getContentKey() {
        return super.getContentKey();
    }

    @Column
    @Override
    public String getBucket() {
        return super.getBucket();
    }

    @Column
    @Override
    public int getContentLength() {
        return super.getContentLength();
    }

    @Column
    @Override
    public String getContentType() {
        return super.getContentType();
    }

    @Column
    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt();
    }

    @Column
    @Override
    public Date getModifiedAt() {
        return super.getModifiedAt();
    }

    @PrePersist
    protected void prePersist() {
        ModelUtils.prePersist(this);
    }

    @PreUpdate
    protected void preUpdate() {
        ModelUtils.preUpdate(this);
    }

}
