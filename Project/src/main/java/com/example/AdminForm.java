package com.example;


import lombok.Getter;

//
@Getter
public class AdminForm {
    //lombok사요 ㅇ 해서 간결
    private Long id;
    private String invoice;

    public void setId(Long id) {
        this.id = id;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
}