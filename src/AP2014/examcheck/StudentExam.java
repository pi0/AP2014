package AP2014.examcheck;

import AP2014.io.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class StudentExam {

    private String ID;
    private Answer[] answers;
    private String answerString;

    public StudentExam(String ID, File answers) throws IOException{
        this.ID = ID;
        this.answerString = Utils.readAllFile(answers);
        this.answers= parseAnswers(answerString,this);
    }

    public String getID() {
        return ID;
    }

    public Answer[] getAnswers() {
        return answers;
    }

    public void checkAll(Answer[] refAnswers) {
        for (int i = 0; i < answers.length; i++)
            answers[i].check(refAnswers[i]);
    }

    public void checkCopy(StudentExam studentExam) {
        for (int i = 0; i < answers.length; i++)
            answers[i].checkCopy(studentExam.answers[i]);
    }

    private Answer[] parseAnswers(String data, StudentExam studentExam) {
        Vector<Answer> v = new Vector<Answer>();
        for (String ans : data.split("\r\n?===\r\n?")){
            v.add(new Answer(studentExam, ans));
        }

        return v.toArray(new Answer[v.size()]);
    }


    public void saveResults(File out)throws IOException{
        File out2=new File(out,getID());
        out2.mkdir();

        File answersFile=new File(out2,"answers.txt");
        Utils.writeAllFile(answersFile, answerString);

        File resultFile=new File(out2,"results.txt");
        Utils.writeAllFile(resultFile,toString());
    }

    @Override
    public String toString() {
        String s=ID+":\r\n";
        double total=0;
        for(Answer answer:answers) {
            s+=answer+"\r\n";
            total+=answer.getScore();
        }

        s+="Score:"+Math.round((total/answers.length)*100)+"/100\r\n";

        return s;
    }
}
