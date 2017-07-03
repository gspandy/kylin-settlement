package com.rkylin.settle.esper;

public class OrderEvent {
    private String itemName;//声明变量
     private double price;//声明变量
//重写方法
     public OrderEvent(String itemName, double price) {
       this.itemName = itemName;
       this.price = price;
      }
      public String getItemName() {
        return itemName;
      }
      public double getPrice() {
        return price;
      }
    }
