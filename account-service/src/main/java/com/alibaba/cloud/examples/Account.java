package com.alibaba.cloud.examples;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {

    private Long id;
    private Long userId;
    private BigDecimal total;
    private BigDecimal used;
    private BigDecimal residue;
    private BigDecimal frozen;
}
