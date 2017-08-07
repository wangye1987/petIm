package com.weeho.petim.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangkui on 2017/4/27.
 */

public class HxImBaseBean implements Serializable {

    /**
     * result : {"list":[{"hxusername":"123"}]}
     * error : {"code":0,"info":""}
     * status : 1
     */

    private ResultBean result;
    private ErrorBean error;
    private String status;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class ResultBean {
        private String hxusername;

        public String getHxusername() {
            return hxusername;
        }

        public void setHxusername(String hxusername) {
            this.hxusername = hxusername;
        }
    }

    public static class ErrorBean {
        /**
         * code : 0
         * info :
         */

        private int code;
        private String info;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
