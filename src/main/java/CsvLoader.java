import com.csvreader.CsvReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvLoader {

    List<StudentInfo> studentInfos;
    String path;

    public CsvLoader(String path) {
        this.path = path;
    }

    public long loadCsv() {
        studentInfos = new ArrayList<>();
        try {
            CsvReader csvReader = null;
            File file =new File(this.path);
            InputStreamReader isr=new InputStreamReader(new FileInputStream(file),"utf-8");
            csvReader = new CsvReader(isr);
            csvReader.readHeaders();
            while (csvReader.readRecord()) {
                String className = csvReader.get("className");
                String id = csvReader.get("id");
                String name = csvReader.get("name");
                studentInfos.add(new StudentInfo(id,name,className));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return studentInfos.size();
    }

    public List<StudentInfo> getStudentInfos() {
        return studentInfos;
    }

    public static void main(String[] args) {
        CsvLoader csLoader = new CsvLoader("src/main/resources/students.csv");
        long successLoadCnt = csLoader.loadCsv();
        System.out.println(successLoadCnt);
}
}
