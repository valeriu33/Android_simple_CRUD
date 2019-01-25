package com.example.vseremet.crud_sqlte.Models;

public class ObjectStudent {
    private int id;
    public String userName;
    public String email;
    public String password;

    public ObjectStudent(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean trySetEmail(String email) {
        if (email.contains("@") && (email.contains(".") && email.length() >= 3)) {
            this.email = email;
            return true;
        }
        return false;
    }

    public boolean trySetUserName(String userName){
        if (userName.length() >= 3){
            this.userName = userName;
            return true;
        }
        return false;
    }

    public boolean trySetPassword(String password){
        if (password.matches("^((?=\\S*?[A-Z])(?=\\S*?[a-z])(?=\\S*?[0-9]).{6,})\\S$")){
            this.password = password;
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
