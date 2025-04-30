package com.example.greenproject.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "second_hand_item")
public class SecondHandItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "item_name")
    private String item_name;
    
    @Column(name = "item_description")
    private String item_description;
    
    @Column(name = "publish_date")
    private Date publish_date;

    @Column(name = "phone_number")
    private String phone_number;
    @Column(name = "image")
    @Lob
    private byte[] image;
    // 添加getter和setter
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getPhone_number() {
        return phone_number;
    }
    
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    
    // 修改getter方法返回类型
    public Integer getId() {  // 从int改为Integer
        return id;
    }
    
    // setter方法参数类型也要相应修改
    public void setId(Integer id) {  // 从int改为Integer
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public Date getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(Date publish_date) {
        this.publish_date = publish_date;
    }
}