package com.spingboot.demo.spingbootdemo.body;

public class RegisterBody {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   private String name;
   private String password;
}
