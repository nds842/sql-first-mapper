package com.github.nds842.sqlfirst.springsample.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class TrackCode {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToOne
    private Postage postage;
    
    @Column(name = "track_code")
    private String trackCode;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Postage getPostage() {
        return postage;
    }
    
    public void setPostage(Postage postage) {
        this.postage = postage;
    }
    
    public String getTrackCode() {
        return trackCode;
    }
    
    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }
}
    
