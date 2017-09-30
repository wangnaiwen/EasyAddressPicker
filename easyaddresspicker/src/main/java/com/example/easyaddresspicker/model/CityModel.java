package com.example.easyaddresspicker.model;

import java.util.List;

/**
 * Created by yue on 17/8/4.
 */

public class CityModel {
    private String id;
    private String name;
    private String pid;
    private List<DistrictModel> child;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<DistrictModel> getChild() {
        return child;
    }

    public void setChild(List<DistrictModel> child) {
        this.child = child;
    }
}
