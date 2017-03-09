package model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vikrant on 2/22/2017.
 */

public class SiteInfo {
    public String uid;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String siteId;
    public String siteName;
    public String location;
    public String totalCost;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }


    public Map<String, Boolean> stars = new HashMap<>();
    public SiteInfo() {
    }

    public SiteInfo(String uid,String siteId,String siteName, String location,String totalCost) {
        this.uid = uid;
        this.siteId = siteId;
        this.siteName = siteName;
        this.location = location;
        this.totalCost = totalCost;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("siteId", siteId);
        result.put("siteName", siteName);
        result.put("location", location);
        result.put("totalCost", totalCost);

        return result;
    }
}
