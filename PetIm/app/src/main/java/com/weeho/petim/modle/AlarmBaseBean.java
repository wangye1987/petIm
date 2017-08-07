package com.weeho.petim.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangkui on 2017/5/25.
 */

public class AlarmBaseBean implements Serializable {

    /**
     * result : {"Alarm":[{"alarmId":0,"restTimeTitle":"起床时间","alarmTime":2224434343}]}
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
         * Alarm : [{"alarmId":0,"restTimeTitle":"起床时间","alarmTime":2224434343}]
         */

        private List<AlarmBean> Alarm;

        public void setAlarm(List<AlarmBean> Alarm) {
            this.Alarm = Alarm;
        }

        public List<AlarmBean> getAlarm() {
            return Alarm;
        }

        public static class AlarmBean {
            /**
             * alarmId : 0
             * restTimeTitle : 起床时间
             * alarmTime : 2224434343
             */

            private int alarmId;
            private String restTimeTitle;
            private String alarmTime;
            private boolean isPush;

            public boolean isPush() {
                return isPush;
            }

            public void setPush(boolean push) {
                isPush = push;
            }

            public void setAlarmId(int alarmId) {
                this.alarmId = alarmId;
            }

            public void setRestTimeTitle(String restTimeTitle) {
                this.restTimeTitle = restTimeTitle;
            }

            public void setAlarmTime(String alarmTime) {
                this.alarmTime = alarmTime;
            }

            public int getAlarmId() {
                return alarmId;
            }

            public String getRestTimeTitle() {
                return restTimeTitle;
            }

            public String getAlarmTime() {
                return alarmTime;
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
