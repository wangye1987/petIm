package com.weeho.petim.modle;

import java.io.Serializable;

/**
 * Created by wangkui on 2017/5/9.
 */

public class UploadImgBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String status;//状态码
    private ResultBean result;//返回结果
    private ErrorBean error;//返回失败结果
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public ErrorBean getError() {
        return error;
    }
    public void setError(ErrorBean error) {
        this.error = error;
    }
    public ResultBean getResult() {
        return result;
    }
    public void setResult(ResultBean result) {
        this.result = result;
    }
    public class ResultBean implements Serializable{
        private String headSculpturePath;

        public String getHeadSculpturePath() {
            return headSculpturePath;
        }

        public void setHeadSculpturePath(String headSculpturePath) {
            this.headSculpturePath = headSculpturePath;
        }
    }

}
