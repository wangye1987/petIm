package com.weeho.petim.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangkui on 2017/4/27.
 */

public class BaseAddressBean implements Serializable {


    /**
     * result : {"data":[{"id":"410000","name":"河南省","city":[{"id":"410300","name":"洛阳市"},{"id":"410400","name":"平顶山市"}]}]}
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
        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 410000
             * name : 河南省
             * city : [{"id":"410300","name":"洛阳市"},{"id":"410400","name":"平顶山市"}]
             */

            private String id;
            private String name;
            private List<CityBean> city;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<CityBean> getCity() {
                return city;
            }

            public void setCity(List<CityBean> city) {
                this.city = city;
            }

            public static class CityBean {
                /**
                 * id : 410300
                 * name : 洛阳市
                 */

                private String id;
                private String name;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
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
