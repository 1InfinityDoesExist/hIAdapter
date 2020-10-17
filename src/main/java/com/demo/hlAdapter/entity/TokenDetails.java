package com.demo.hlAdapter.entity;

import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@lombok.Data
@Entity(name = "TokenDetails")
@Table(name = "token_details")
public class TokenDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String user_name;
    private String parentTenant;
    private String exp;
    private String jti;
    private String client_id;
    @ElementCollection(targetClass = String.class)
    private List<String> authorities;
    @ElementCollection(targetClass = String.class)
    private List<String> scope;
}
