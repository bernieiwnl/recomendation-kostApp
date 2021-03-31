package com.example.rekomendasikostapp.CLASS;

public class Users {

    private String idUser;
    private String email;
    private String full_name;
    private String phone;
    private String password;
    private String account;
    private String profile_image_url;
    private Double lattitude;
    private Double longtitude;

    public Users(){

    }


    public Users(String idUser, String email , String full_name , String phone , String password , String account , String profile_image_url, Double lattitude, Double longtitude){
        this.setIdUser(idUser);
        this.setEmail(email);
        this.setFull_name(full_name);
        this.setPhone(phone);
        this.setPassword(password);
        this.setAccount(account);
        this.setProfile_image_url(profile_image_url);
        this.setLattitude(lattitude);
        this.setLongtitude(longtitude);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }


    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }
}
