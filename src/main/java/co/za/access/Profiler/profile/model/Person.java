package co.za.access.Profiler.profile.model;

import java.util.List;
import java.util.Set;

public class Person {

    private String name;
    private String surname;

    private Set<String> potentialEmail;

    private Set<String> potentialPhoneNo;
    private int age;
    private Set<SocialMedia> socialAccounts;

    private Set<Address> potentialAddresses;

    private List<LinkedPerson> friends;

    private boolean  employeed;

    private Carreer carreer;

}
