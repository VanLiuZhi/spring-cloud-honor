package com.example.demo.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginVO{

    @JsonProperty("password")
    private String password;

    @JsonProperty("name")
    private String name;
}