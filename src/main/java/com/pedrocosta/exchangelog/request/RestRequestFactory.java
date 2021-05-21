//package com.pedrocosta.exchangelog.request;
//
//import com.pedrocosta.exchangelog.request.api.FixerRequester;
//
//@Deprecated
//public class RestRequestFactory {
//
//    public static RestRequest getInstance(String name) {
//        RequesterTypes reqType = RequesterTypes.load(name);
//        RestRequest result = null;
//
//        switch (reqType) {
//            case FIXER:
//                result = new FixerRequester();
//                break;
//            default:
//        }
//
//        return result;
//    }
//}
