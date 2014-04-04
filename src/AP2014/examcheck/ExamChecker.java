package AP2014.examcheck;

import AP2014.io.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class ExamChecker {
    private Vector<StudentExam> studentExams;
    private String baseDir;
    private Answer[] refAnswers;

    public ExamChecker(String baseDir) {
        this.baseDir=baseDir;
        refAnswers=null;
        studentExams=new Vector<StudentExam>();
    }

    public Vector<StudentExam> getStudentExams() {
        return studentExams;
    }

    public void checkAll() throws IOException{

        studentExams=new Vector<StudentExam>();

        //Read correct answers file

        this.refAnswers = new StudentExam("",new File
                (Utils.joinPath(baseDir,"std.txt"))).getAnswers();

        //Get a list of files and read them

        for (File file : new File(baseDir).listFiles()) {
            if (file.getName().equals("std.txt") ||
                    file.isDirectory() ||
                    !file.getName().endsWith(".txt"))
                continue;

            String ID = Utils.filenameWithoutExtension(file.getName());
            studentExams.add(new StudentExam(ID,file));
        }

        //Pass 1 : check each student answers

        for (StudentExam studentExam : studentExams)
            studentExam.checkAll(refAnswers);

        //Pass 2 : check for copy!

        for (int i = 0; i < studentExams.size(); i++)
            for (int j = i + 1; j < studentExams.size(); j++)
                studentExams.get(i).checkCopy(studentExams.get(j));
    }

}
