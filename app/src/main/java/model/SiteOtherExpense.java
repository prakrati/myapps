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
