package com.github.nds842.sqlfirst.springsample.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;


@Entity
public class Parcel {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private Long width;
    
    private Long weight;
    
    @Column(name = "sender_name")
    private String senderName;
    
    @Column(name = "date_send")
    private Date dateSend;
    
    private Long height;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getWidth() {
        return width;
    }
    
    public void setWidth(Long width) {
        this.width = width;
    }
    
    public Long getWeight() {
        return weight;
    }
    
    public void setWeight(Long weight) {
        this.weight = weight;
    }
    
    public String getSenderName() {
        return senderName;
    }
    
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    
    public Date getDateSend() {
        return dateSend;
    }
    
    public void setDateSend(Date dateSend) {
        this.dateSend = dateSend;
    }
    
    public Long getHeight() {
        return height;
    }
    
    public void setHeight(Long height) {
        this.height = height;
    }
}
    