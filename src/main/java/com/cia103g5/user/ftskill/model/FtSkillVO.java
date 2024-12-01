package com.cia103g5.user.ftskill.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ft_skill")
public class FtSkillVO {

    @Id
    @Column(name = "skill_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer skillNo;

    @Column(name = "skill_name")
    @NotEmpty(message = "專長名稱: 請勿空白")
    @Size(min = 2, max = 20, message = "專長名稱: 長度請在{min}到{max}之間")
    private String skillName;


    public Integer getSkillNo() {
        return skillNo;
    }

    public void setSkillNo(Integer skillNo) {
        this.skillNo = skillNo;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
}
