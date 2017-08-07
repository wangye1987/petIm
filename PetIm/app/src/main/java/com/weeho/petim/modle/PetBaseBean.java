package com.weeho.petim.modle;

import java.io.Serializable;

/**
 * Created by wangkui on 2017/4/27.
 */

public class PetBaseBean implements Serializable {

    /**
     * result : {"data":{"petId":3,"headSculpturePath":" http://localhost:8080/pet/1.jpg","petSex":0,"breed":"品种名称","firstVaccine":"2016年5月12日","cageCulture":0,"petName":"妞妞"}}
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
        /**
         * data : {"petId":3,"headSculpturePath":" http://localhost:8080/pet/1.jpg","petSex":0,"breed":"品种名称","firstVaccine":"2016年5月12日","cageCulture":0,"petName":"妞妞"}
         */

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * petId : 3
             * headSculpturePath :  http://localhost:8080/pet/1.jpg
             * petSex : 0
             * breed : 品种名称
             * firstVaccine : 2016年5月12日
             * cageCulture : 0
             * petName : 妞妞
             */

            private int petId;
            private String headSculpturePath;
            private int petSex;
            private String breedName;
            private String time;
            private int cageCulture;
            private String petName;

            public int getPetId() {
                return petId;
            }

            public void setPetId(int petId) {
                this.petId = petId;
            }

            public String getHeadSculpturePath() {
                return headSculpturePath;
            }

            public void setHeadSculpturePath(String headSculpturePath) {
                this.headSculpturePath = headSculpturePath;
            }

            public int getPetSex() {
                return petSex;
            }

            public void setPetSex(int petSex) {
                this.petSex = petSex;
            }

            public String getBreedName() {
                return breedName;
            }

            public void setBreedName(String breedName) {
                this.breedName = breedName;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getCageCulture() {
                return cageCulture;
            }

            public void setCageCulture(int cageCulture) {
                this.cageCulture = cageCulture;
            }

            public String getPetName() {
                return petName;
            }

            public void setPetName(String petName) {
                this.petName = petName;
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
