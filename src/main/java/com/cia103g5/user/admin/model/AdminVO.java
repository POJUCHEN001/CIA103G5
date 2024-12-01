package com.cia103g5.user.admin.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**######################
 #                      #
 #      管理員model      #
 #                      #
 ######################*/

@Getter
@Setter
@Entity
@Table(name = "admin_info")
public class AdminVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Integer adminId;
    private String name;
    private String email;

    private String account;
    @JsonIgnore
    private String password;

    @Lob
    @JsonIgnore
    private byte[] photo;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registered_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registeredAt;
    private Integer status;

    @NotNull(message = "dddddd")
    private String phone;

    @Transient
    @JsonProperty //一定要序列化
    private String statusString;
    public void setStatus(Integer status) {
        this.setStatusString(status == 0 ? "在職" : "離職");
        this.status = status;
    }


    //因為JPA是用反射注入，不會使用set方法，故用PostLoad，在反射結束自動來執行
    @PostLoad
    private void initStatusString() {
        this.statusString = this.status == 0 ? "在職" : "離職";
    }



    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", registeredAt=" + registeredAt +
                ", status=" + status +
                '}';
    }
}
