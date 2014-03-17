package AP2014.examcheck;

import java.io.File;

public class ExamCheckerApp {

    public static void main(String[] args) {
        try{
        new ExamCheckerApp().Run("C:\\a\\exam");
        }catch (Exception e) {
            System.err.print("Error: "+e.getMessage());
        }
    }

    void Run(String baseDir) throws Exception{

        //Initialize

        File baseDirFile=new File(baseDir);

        if(!baseDirFile.canRead() || !baseDirFile.canWrite())
            throw  new Exception("Permission denied!");
        if(!baseDirFile.isDirectory())
            throw  new Exception("Directory is expected!");
        if(!baseDirFile.exists()){
            baseDirFile.mkdirs();
            if(!baseDirFile.exists())
                throw  new Exception("Unable to create base directory");
        }

        //Read and check all exams

        ExamChecker examChecker = new ExamChecker(baseDir);
        examChecker.checkAll();

        //Print and save results
        File outDir=new File(baseDirFile,"res");
        if(outDir.exists())
            outDir.delete();
        outDir.mkdir();

        for (StudentExam exam : examChecker.getStudentExams()) {
            System.out.println(exam);
            exam.saveResults(outDir);
        }
    }
}
