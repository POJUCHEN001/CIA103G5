package com.cia103g5.user.skill.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ft_skill")
public class SkillVO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer skillNo;
    private String skillName;

    public SkillVO() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_no")
    public Integer getSkillNo() {
        return skillNo;
    }

    public void setSkillNo(Integer skillNo) {
        this.skillNo = skillNo;
    }

    @Column(name = "skill_name")
    @NotEmpty(message = "專長名稱: 請勿空白")
    @Size(min = 2, max = 20, message = "專長名稱: 長度請在{min}到{max}之間")
    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    
}
