package com.weeho.petim.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangkui on 2017/6/6.
 */

public class PetDanBaseBean implements Serializable {

    /**
     * result : {"list":[{"isReach":true,"name":"青铜","nameId":"bronze","activity":[{"activityName":"喂⾷训练坚持5天","isReach":false},{"activityName":"狗记住⾃⼰的名字","isReach":false}]}]}
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * isReach : true
             * name : 青铜
             * nameId : bronze
             * activity : [{"activityName":"喂⾷训练坚持5天","isReach":false},{"activityName":"狗记住⾃⼰的名字","isReach":false}]
             */

            private boolean isReach;
            private String name;
            private String nameId;
            private List<ActivityBean> activity;

            public boolean isIsReach() {
                return isReach;
            }

            public void setIsReach(boolean isReach) {
                this.isReach = isReach;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNameId() {
                return nameId;
            }

            public void setNameId(String nameId) {
                this.nameId = nameId;
            }

            public List<ActivityBean> getActivity() {
                return activity;
            }

            public void setActivity(List<ActivityBean> activity) {
                this.activity = activity;
            }

            public static class ActivityBean {
                /**
                 * activityName : 喂⾷训练坚持5天
                 * isReach : false
                 */

                private String activityName;
                private boolean isReach;

                public String getActivityName() {
                    return activityName;
                }

                public void setActivityName(String activityName) {
                    this.activityName = activityName;
                }

                public boolean isIsReach() {
                    return isReach;
                }

                public void setIsReach(boolean isReach) {
                    this.isReach = isReach;
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
