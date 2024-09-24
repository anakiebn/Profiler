package co.za.access.profiler.dataProcessing.model;


import lombok.Data;

@Data
public class WorkOrSchool{

    private String name;
    private String startAndEndDate;

    public WorkOrSchool(String name){
        this.name=name;
    }
}
