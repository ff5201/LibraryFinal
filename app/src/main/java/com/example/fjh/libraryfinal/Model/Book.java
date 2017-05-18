package com.example.fjh.libraryfinal.Model;

import java.io.Serializable;

public class Book implements Serializable {
    private String ID;
    private String name;
    private String author;
    private String address;

    public Book(String ID, String name, String author,String address) {
        this.ID = ID;
        this.name = name;
        this.author = author;
        this.address=address;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return this.ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAuthor(String price) {
        this.author = author;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAddress(String address){
        this.address=address;
    }

    public String getAddress(){
        return this.address;
    }

    public String toString() {
        return this.ID + " " + this.name + " " + this.author+" "+this.address;
    }
}
