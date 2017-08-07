package com.weeho.petim.modle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wangkui on 2017/6/12.
 */

public class NotReadList implements Serializable {
    private ArrayList<NotReadBean> list;

    public ArrayList<NotReadBean> getList() {
        return list;
    }

    public void setList(ArrayList<NotReadBean> list) {
        this.list = list;
    }

   public static class NotReadBean implements Serializable{
        private String id;
        private String hxUserid;
        private int num;

       public String getHxUserid() {
           return hxUserid;
       }

       public void setHxUserid(String hxUserid) {
           this.hxUserid = hxUserid;
       }

       public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
