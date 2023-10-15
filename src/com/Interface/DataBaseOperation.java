package com.Interface;

import java.util.List;

public interface DataBaseOperation {
     String readOneLine(String where,int line);
     List<String> readAll(String where);

    public void write(String where,List<String> allLines);

}
