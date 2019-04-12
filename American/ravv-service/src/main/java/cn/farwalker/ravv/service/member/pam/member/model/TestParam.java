package cn.farwalker.ravv.service.member.pam.member.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TestParam implements Serializable {
    private static final long serialVersionUID = 1L;
    String password;
    String firstname;
    String salt;
    long memberId;
    long point;

}
