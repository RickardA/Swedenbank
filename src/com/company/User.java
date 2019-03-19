package com.company;

import com.company.Annotations.Column;

public class User {

    @Column
    String first_name;
    @Column
    String last_name;
    @Column
    String social_number;
    @Column
    String email;

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getSocial_number() {
        return social_number;
    }

    public String getEmail() {
        return email;
    }
}
