package co.za.access.profiler.dataCollection.linkedinSearch.model;

import lombok.Data;

import java.util.List;

@Data
public class Experience {


    private String companyName;
    private String location;
    private List<Position> positions;



}
