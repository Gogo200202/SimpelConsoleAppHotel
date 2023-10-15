package com.database;
import com.Interface.DataBaseOperation;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;





public class DataBase implements DataBaseOperation {


    @Override
    public String readOneLine(String where,int line) {
        try {
            return Files.readAllLines(Paths.get("./src/com/database/",where).toAbsolutePath()).get(line);
        }catch (Exception e){

        }

        return null;
    }

    @Override
    public List<String> readAll(String where) {

        Path myPath = Paths.get("./src/com/database/",where).toAbsolutePath();

        try {
            List<String> a=Files.readAllLines(myPath, StandardCharsets.UTF_8);
            return a;
        }catch (Exception e){

        }
        return null;
    }

    @Override
    public void write(String where,List<String> allLines ) {
        Path myPath = Paths.get("./src/com/database/",where).toAbsolutePath();
        try {
            Files.write(myPath, allLines, StandardCharsets.UTF_8);
        }catch (Exception e){

        }

    }

}
