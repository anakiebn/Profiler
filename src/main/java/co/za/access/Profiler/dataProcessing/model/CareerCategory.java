package co.za.access.Profiler.dataProcessing.model;

public enum CareerCategory {
    HEALTHCARE("Healthcare"),
    ENGINEERING_AND_TECHNOLOGY("Engineering and Technology"),
    BUSINESS_AND_FINANCE("Business and Finance"),
    EDUCATION_AND_TRAINING("Education and Training"),
    ARTS_DESIGN_AND_MEDIA("Arts, Design, and Media"),
    LAW_AND_PUBLIC_POLICY("Law and Public Policy"),
    SCIENCE_AND_RESEARCH("Science and Research"),
    SKILLED_TRADES_AND_TECHNICAL_SERVICES("Skilled Trades and Technical Services"),
    INFORMATION_TECHNOLOGY("Information Technology"),
    HOSPITALITY_AND_TOURISM("Hospitality and Tourism"),
    SOCIAL_SERVICES("Social Services"),
    SALES_AND_MARKETING("Sales and Marketing"),
    AGRICULTURE_AND_NATURAL_RESOURCES("Agriculture and Natural Resources"),
    TRANSPORTATION_AND_LOGISTICS("Transportation and Logistics"),
    GOVERNMENT_AND_PUBLIC_ADMINISTRATION("Government and Public Administration");

    private final String description;
    CareerCategory(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
