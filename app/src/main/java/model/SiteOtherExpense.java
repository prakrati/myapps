package model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vikrant on 2/28/2017.
 */

public class SiteOtherExpense {
    public String uid;
    public String siteId;
    public String expId;

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getExpId() {
        return expId;
    }

    public void setExpId(String expId) {
        this.expId = expId;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getExpAmt() {
        return expAmt;
    }

    public void setExpAmt(String expAmt) {
        this.expAmt = expAmt;
    }

    public String expName;
    public String expDate;
    public String expAmt;
    public SiteOtherExpense() {
    }
    public SiteOtherExpense(String uid,String siteId,String expId, String expName,String expDate,String expAmt) {
        this.uid = uid;
        this.siteId = siteId;
        this.expId = expId;
        this.expName = expName;
        this.expDate = expDate;
        this.expAmt = expAmt;


    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("siteId", siteId);
        result.put("expId", expId);
        result.put("expName", expName);
        result.put("expDate", expDate);
        result.put("expAmt", expAmt);

        return result;
    }
}
