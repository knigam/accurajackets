package com.accurajackets.patientidentification.exception;

/**
 * Created by atul on 5/11/14.
 */
public class MyRuntimeException extends RuntimeException {


        public MyRuntimeException(Exception e){
            super();
            e.printStackTrace();
        }

}
