package com.weeho.petim.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangkui on 2017/4/27.
 */

public class AcheveBaseBean implements Serializable {

    /**
     * result : {"data":[{"name":"挑食"},{"name":"吃屎"}]}
     * error : {"code":0,"info":""}
     * status : 1
     */

    private ResultBean result;
    private ErrorBean error;
    private String status;

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResultBean getResult() {
        return result;
    }

    public ErrorBean getError() {
        return error;
    }

    public String getStatus() {
        return status;
    }

    public static class ResultBean {
        /**
         * data : [{"name":"挑食"},{"name":"吃屎"}]
         */

        private List<DataBean> data;

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public List<DataBean> getData() {
            return data;
        }

        public static class DataBean {
            /**
             * name : 挑食
             */

            private String name;

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }
    }

    public static class ErrorBean {
        /**
         * code : 0
         * info :
         */

        private int code;
        private String info;

        public void setCode(int code) {
            this.code = code;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public int getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }
}
