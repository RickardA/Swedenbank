package com.company.Helpers;

public abstract class Extractor {

    public static String extractAccountNumber(Object s) {
        String extractedNumber;
        if (s != null) {
            extractedNumber = s.toString().replaceFirst(".*\\s", "");
        }else{
            extractedNumber = null;
        }
        return extractedNumber;
    }

    public static String checkIfEmpty(String s){
        if (s.isEmpty()){
            return null;
        }else{
            return s;
        }
    }
}
