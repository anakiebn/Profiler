package co.za.access.profiler.dataProcessing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class About {

    private List<WorkOrSchool> jobs;
    private List<WorkOrSchool> schools;
    private List<FamilyMember> familyMembers;
    private List<InRelationshipWith> inRelationshipWith;
    private String hometown;
    private String currentCity;
    private ContactInfo contactInfo;
    private BasicInfo basicInfo;
}
