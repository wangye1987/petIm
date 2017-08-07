package com.weeho.petim.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangkui on 2017/4/27.
 */

public class HxBaseBean implements Serializable {

    /**
     * result : {"list4":[{"hxusername":"weehoPetMindTeacher"}],"list5":[{"hxusername":"weehoPetComplain"}],"list2":[{"hxusername":"weehoPetAction"}],"list3":[{"hxusername":"weehoPetGoodsExpert"}],"list1":[{"hxusername":"weehoPetDogTrainer"}]}
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
        private List<List4Bean> list4;
        private List<List5Bean> list5;
        private List<List2Bean> list2;
        private List<List3Bean> list3;
        private List<List1Bean> list1;

        public List<List4Bean> getList4() {
            return list4;
        }

        public void setList4(List<List4Bean> list4) {
            this.list4 = list4;
        }

        public List<List5Bean> getList5() {
            return list5;
        }

        public void setList5(List<List5Bean> list5) {
            this.list5 = list5;
        }

        public List<List2Bean> getList2() {
            return list2;
        }

        public void setList2(List<List2Bean> list2) {
            this.list2 = list2;
        }

        public List<List3Bean> getList3() {
            return list3;
        }

        public void setList3(List<List3Bean> list3) {
            this.list3 = list3;
        }

        public List<List1Bean> getList1() {
            return list1;
        }

        public void setList1(List<List1Bean> list1) {
            this.list1 = list1;
        }

        public static class List4Bean {
            /**
             * hxusername : weehoPetMindTeacher
             */

            private String hxusername;

            public String getHxusername() {
                return hxusername;
            }

            public void setHxusername(String hxusername) {
                this.hxusername = hxusername;
            }
        }

        public static class List5Bean {
            /**
             * hxusername : weehoPetComplain
             */

            private String hxusername;

            public String getHxusername() {
                return hxusername;
            }

            public void setHxusername(String hxusername) {
                this.hxusername = hxusername;
            }
        }

        public static class List2Bean {
            /**
             * hxusername : weehoPetAction
             */

            private String hxusername;

            public String getHxusername() {
                return hxusername;
            }

            public void setHxusername(String hxusername) {
                this.hxusername = hxusername;
            }
        }

        public static class List3Bean {
            /**
             * hxusername : weehoPetGoodsExpert
             */

            private String hxusername;

            public String getHxusername() {
                return hxusername;
            }

            public void setHxusername(String hxusername) {
                this.hxusername = hxusername;
            }
        }

        public static class List1Bean {
            /**
             * hxusername : weehoPetDogTrainer
             */

            private String hxusername;

            public String getHxusername() {
                return hxusername;
            }

            public void setHxusername(String hxusername) {
                this.hxusername = hxusername;
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
