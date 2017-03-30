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
    public HashMap<String, Object> result = new HashMap<>();
    public String getLabourId() {
        return labourId;
    }

    public void setLabourId(String labourId) {
        this.labourId = labourId;
    }


    public String getLabourPaidBy() {
        return labourPaidBy;
    }

    public void setLabourPaidBy(String labourPaidBy) {
        this.labourPaidBy = labourPaidBy;
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



    public String getLabourName() {
        return labourName;
    }

    public void setLabourName(String labourName) {
        this.labourName = labourName;
    }

    public String getLabourDate() {
        return labourDate;
    }

    public void setLabourDate(String labourDate) {
        this.labourDate = labourDate;
    }

    public String getLabourDays() {
        return labourDays;
    }

    public void setLabourDays(String labourDays) {
        this.labourDays = labourDays;
    }

    public String getLabourWages() {
        return labourWages;
    }

    public void setLabourWages(String labourWages) {
        this.labourWages = labourWages;
    }

    public String getLabourTotalCost() {
        return labourTotalCost;
    }

    public void setLabourTotalCost(String labourTotalCost) {
        this.labourTotalCost = labourTotalCost;
    }


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
