package co.za.access.Profiler.dataProcessing.model;

public enum Relationship {
    ROMANTIC("Romantic"),
    RELATIVE("Relative"),
    FAMILY("Family"),
    FRIENDS("Friends"),
    PROFESSIONAL("Professional"),
    NEIGHBOUR("Neighbour"),
    MAY_KNOW("May Know"),
    BROTHER("Brother"),
    SISTER("Sister"),
    MOTHER("Mother"),
    PARENT("Parent"),
    FATHER("Father");

    private final String relationshipType;

    Relationship(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getRelationshipType() {
        return relationshipType;
    }


}

