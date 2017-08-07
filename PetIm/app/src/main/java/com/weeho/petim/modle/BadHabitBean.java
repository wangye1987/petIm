package com.weeho.petim.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangkui on 2017/4/28.
 */

public class BadHabitBean implements Serializable {

    /**
     * result : {"data":{"outdoor":[{"name":"随地大小便"},{"name":"扑人"},{"name":"出门爆冲"}],"indoor":[{"name":"随地大小便"},{"name":"扑人"},{"name":"出门爆冲"}]}}
     * error : {"code":0,"info":""}
     * status : ”1“
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
         * data : {"outdoor":[{"name":"随地大小便"},{"name":"扑人"},{"name":"出门爆冲"}],"indoor":[{"name":"随地大小便"},{"name":"扑人"},{"name":"出门爆冲"}]}
         */

        private DataBean data;

        public void setData(DataBean data) {
            this.data = data;
        }

        public DataBean getData() {
            return data;
        }

        public static class DataBean {
            /**
             * outdoor : [{"name":"随地大小便"},{"name":"扑人"},{"name":"出门爆冲"}]
             * indoor : [{"name":"随地大小便"},{"name":"扑人"},{"name":"出门爆冲"}]
             */

            private List<OutdoorBean> outdoor;
            private List<IndoorBean> indoor;

            public void setOutdoor(List<OutdoorBean> outdoor) {
                this.outdoor = outdoor;
            }

            public void setIndoor(List<IndoorBean> indoor) {
                this.indoor = indoor;
            }

            public List<OutdoorBean> getOutdoor() {
                return outdoor;
            }

            public List<IndoorBean> getIndoor() {
                return indoor;
            }

            public static class OutdoorBean {
                /**
                 * name : 随地大小便
                 */

                private String name;

                public void setName(String name) {
                    this.name = name;
                }

                public String getName() {
                    return name;
                }
            }

            public static class IndoorBean {
                /**
                 * name : 随地大小便
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
