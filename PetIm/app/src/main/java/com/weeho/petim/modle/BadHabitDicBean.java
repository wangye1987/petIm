package com.weeho.petim.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangkui on 2017/4/28.
 */

public class BadHabitDicBean implements Serializable {

    /**
     * result : {"data":{"outdoor":[{"ckeck":0,"name":"随地大小便"},{"ckeck":0,"name":"扑人"},{"ckeck":0,"name":"出门爆冲"},{"ckeck":1,"name":"挑食"},{"ckeck":1,"name":"吃屎"},{"ckeck":1,"name":"不听主人指令"}],"indoor":[{"ckeck":0,"name":"随地大小便"}]}}
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
         * data : {"outdoor":[{"ckeck":0,"name":"随地大小便"},{"ckeck":0,"name":"扑人"},{"ckeck":0,"name":"出门爆冲"},{"ckeck":1,"name":"挑食"},{"ckeck":1,"name":"吃屎"},{"ckeck":1,"name":"不听主人指令"}],"indoor":[{"ckeck":0,"name":"随地大小便"}]}
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
             * outdoor : [{"ckeck":0,"name":"随地大小便"},{"ckeck":0,"name":"扑人"},{"ckeck":0,"name":"出门爆冲"},{"ckeck":1,"name":"挑食"},{"ckeck":1,"name":"吃屎"},{"ckeck":1,"name":"不听主人指令"}]
             * indoor : [{"ckeck":0,"name":"随地大小便"}]
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
                 * ckeck : 0
                 * name : 随地大小便
                 */

                private int check;
                private String viceid;
                private String name;

                public String getViceid() {
                    return viceid;
                }

                public void setViceid(String viceid) {
                    this.viceid = viceid;
                }

                public void setCheck(int ckeck) {
                    this.check = ckeck;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getCheck() {
                    return check;
                }

                public String getName() {
                    return name;
                }
            }

            public static class IndoorBean {
                /**
                 * ckeck : 0
                 * name : 随地大小便
                 */

                private int check;
                private String viceid;
                private String name;

                public String getViceid() {
                    return viceid;
                }

                public void setViceid(String viceid) {
                    this.viceid = viceid;
                }

                public void setCheck(int ckeck) {
                    this.check = ckeck;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getCheck() {
                    return check;
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
