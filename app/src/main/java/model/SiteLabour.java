package model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vikrant on 2/28/2017.
 */

public class SiteLabour {
    public String uid;
    public String siteId;
    public String labourId;
    public String labourName;
    public String labourDate;
    public String labourDays;
    public String labourWages;
    public String labourPaidBy;
    public String labourTotalCost;
    public SiteLabour() {
    }
    public SiteLabour(String uid,String siteId,String labourId, String labourName,String labourDate,String labourDays,
                      String labourWages,String labourPaidBy,String labourTotalCost) {
        this.uid = uid;
        this.siteId = siteId;
        this.labourId = labourId;
        this.labourName = labourName;
        this.labourDate = labourDate;
        this.labourDays = labourDays;
        this.labourWages = labourWages;
        this.labourPaidBy = labourPaidBy;
        this.labourTotalCost = labourTotalCost;

    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("siteId", siteId);
        result.put("labourId", labourId);
        result.put("labourName", labourName);
        result.put("labourDate", labourDate);
        result.put("labourDays", labourDays);
        result.put("labourWages", labourWages);
        result.put("labourPaidBy", labourPaidBy);
        result.put("labourTotalCost", labourTotalCost);
        return result;
    }
}
