package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @KeySql(useGeneratedKeys = true, dialect = IdentityDialect.DEFAULT)
    private Long id;
    private String name;
    private Boolean gender;
    //mysql-connector-java 5.x --> Date
    //mysql-connector-java 8.x --> LocalDateTime
    private LocalDateTime createOn;
    private LocalDateTime updateOn;
}
