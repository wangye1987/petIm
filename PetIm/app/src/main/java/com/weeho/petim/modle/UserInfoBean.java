package com.weeho.petim.modle;

import java.io.Serializable;

/**
 * Created by wangkui on 2017/4/26.
 */

public class UserInfoBean implements Serializable {
    private String status;//状态码
    private ResultBean result;//返回结果
    private ErrorBean error;//返回失败结果
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public ErrorBean getError() {
        return error;
    }
    public void setError(ErrorBean error) {
        this.error = error;
    }
    public ResultBean getResult() {
        return result;
    }
    public void setResult(ResultBean result) {
        this.result = result;
    }
    public class ResultBean implements Serializable {
        private  DataInfo data;

        public DataInfo getData() {
            return data;
        }

        public void setData(DataInfo data) {
            this.data = data;
        }
    }
    public static class DataInfo implements  Serializable{
        private String nickName;
        private int sex;
        private String weixinNumber;
        private String mobile;
        private String regionId;
        private String chatId;
        private String chatName;
        private String customerId;
        private String chatPassword;
        private String pet;
        private String address;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getWeixinNumber() {
            return weixinNumber;
        }

        public void setWeixinNumber(String weixinNumber) {
            this.weixinNumber = weixinNumber;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getRegionId() {
            return regionId;
        }

        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }

        public String getChatName() {
            return chatName;
        }

        public void setChatName(String chatName) {
            this.chatName = chatName;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getChatPassword() {
            return chatPassword;
        }

        public void setChatPassword(String chatPassword) {
            this.chatPassword = chatPassword;
        }

        public String getPet() {
            return pet;
        }

        public void setPet(String pet) {
            this.pet = pet;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
