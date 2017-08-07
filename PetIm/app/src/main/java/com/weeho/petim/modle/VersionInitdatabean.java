package com.weeho.petim.modle;

import java.io.Serializable;

/**
 * Created by wangkui on 2017/5/10.
 */

public class VersionInitdatabean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String status;//状态码
    private VersionInfo result;//返回结果
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
    public VersionInfo getResult() {
        return result;
    }
    public void setResult(VersionInfo result) {
        this.result = result;
    }


    public class VersionInfo implements Serializable{

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private String updatetime;
        private String updateinfo;
        private boolean ismust;
        private String url;
        private int version;
        public String getUpdatetime() {
            return updatetime;
        }
        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }
        public String getUpdateinfo() {
            return updateinfo;
        }
        public void setUpdateinfo(String updateinfo) {
            this.updateinfo = updateinfo;
        }
        public boolean getIsmust() {
            return ismust;
        }
        public void setIsmust(boolean ismust) {
            this.ismust = ismust;
        }
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        public int getVersion() {
            return version;
        }
        public void setVersion(int version) {
            this.version = version;
        }


            /**
             * //	{
             //	    "result": {
             //	        "version": {
             //	            "updatetime": 1468944000,
             //	            "updateinfo": "1. 新增商户积分优惠                     \n2. 新增线上客服功能  \n3. 修复上版本系统故障",
             //	            "ismust": false,
             //	            "url": "http://test.heheys.com",
             //	            "version": "1.2.2"
             //	        }
             //	    },
             //	    "error": {},
             //	    "status": 1
             //	}
             */


    }
}

