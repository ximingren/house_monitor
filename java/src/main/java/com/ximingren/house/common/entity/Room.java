package com.ximingren.house.common.entity;

// 有_的话，好像匹配不了值
public class Room {
    private int id;
    private String title;
    private String position;
    private String area;
    private String layout;
    private String height;
    private String direction;
    private String district;

    public String getRentMoney() {
        return rentMoney;
    }

    public void setRentMoney(String rentMoney) {
        this.rentMoney = rentMoney;
    }

    private String rentMoney;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", position='" + position + '\'' +
                ", area='" + area + '\'' +
                ", layout='" + layout + '\'' +
                ", height='" + height + '\'' +
                ", direction='" + direction + '\'' +
                ", district='" + district + '\'' +
                ", rentMoney='" + rentMoney + '\'' +
                '}';
    }
}
