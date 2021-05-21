//package com.pedrocosta.exchangelog.request;
//
//@Deprecated
//public enum RequesterTypes {
//
//    FIXER(1, "Fixer");
//
//    int id;
//    String name;
//
//    RequesterTypes(int id, String name) {
//        this.id = id;
//        this.name = name;
//    }
//
//    public static RequesterTypes load(String name) {
//        RequesterTypes result = null;
//
//        if (name != null) {
//            for(RequesterTypes rt : values()) {
//                if (rt.getName().equals(name)) {
//                    result = rt;
//                }
//            }
//        }
//
//        return result;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//}
