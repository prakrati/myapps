package model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vikrant on 2/28/2017.
 */

public class SiteMaterial {
    public String uid;
    public String siteId;



    public String materialId;
    public String materialName;
    public String materialDate;
    public String materialUnitPrice;
    public String materialQty;
    public String materialPaidBy;
    public String materialTotalCost;
    public HashMap<String, Object> result = new HashMap<>();
    public SiteMaterial() {
    }
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public SiteMaterial(String uid,String siteId,String materialId, String materialName,String materialDate,String materialUnitPrice,String materialQty,String materialPaidBy,String materialTotalCost) {
        this.uid = uid;
        this.siteId = siteId;
        this.materialId = materialId;
        this.materialName = materialName;
        this.materialDate = materialDate;
        this.materialUnitPrice = materialUnitPrice;
        this.materialQty = materialQty;
        this.materialPaidBy = materialPaidBy;
        this.materialTotalCost = materialTotalCost;

}

    public String getMaterialTotalCost() {
        return materialTotalCost;
    }

    public void setMaterialTotalCost(String materialTotalCost) {
        this.materialTotalCost = materialTotalCost;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialDate() {
        return materialDate;
    }

    public void setMaterialDate(String materialDate) {
        this.materialDate = materialDate;
    }

    public String getMaterialUnitPrice() {
        return materialUnitPrice;
    }

    public void setMaterialUnitPrice(String materialUnitPrice) {
        this.materialUnitPrice = materialUnitPrice;
    }

    public String getMaterialQty() {
        return materialQty;
    }

    public void setMaterialQty(String materialQty) {
        this.materialQty = materialQty;
    }

    public String getMaterialPaidBy() {
        return materialPaidBy;
    }

    public void setMaterialPaidBy(String materialPaidBy) {
        this.materialPaidBy = materialPaidBy;
    }

    @Exclude
    public Map<String, Object> toMap() {

        result.put("uid", uid);
        result.put("siteId", siteId);
        result.put("materialId", materialId);
        result.put("materialName", materialName);
        result.put("materialDate", materialDate);
        result.put("materialUnitPrice", materialUnitPrice);
        result.put("materialQty", materialQty);
        result.put("materialPaidBy", materialPaidBy);
        result.put("materialTotalCost", materialTotalCost);
        return result;
    }



}
