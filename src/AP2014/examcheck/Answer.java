package AP2014.examcheck;

import java.util.Vector;

public class Answer {

    private AnswerSegment[] segments;
    private Vector<StudentExam> copiedFrom;
    private StudentExam author;
    private double score;

    public Answer(StudentExam author, String text) {

        this.copiedFrom = new Vector<StudentExam>();
        this.author=author;

        String[] segmentsStr = text.split("\\b\\b+");
        segments = new AnswerSegment[segmentsStr.length];
        for (int i = 0; i < segments.length; i++)
            segments[i] = new AnswerSegment(segmentsStr[i]);
    }

    public void check(Answer ref) {
        //TODO
        //TODO : check for different lengths!!
        for (int i = 0; i < segments.length; i++)
            segments[i].check(ref.segments[i]);

        //give score
        int total=segments.length;
        int correct=0;
        for(AnswerSegment segment:segments)
            if(segment.isCorrect)
                correct++;
        this.score=(double)correct/total;

    }

    public void checkCopy(Answer ans) {

        if(score==1)
            return;

        for (int i = 0; i < segments.length; i++)
            if (!segments[i].equals(ans.segments[i])) {
                return;//having different sections !
            }

        this.copiedFrom.add(ans.author);
        ans.copiedFrom.add(this.author);
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {

        String r = "";
        for (AnswerSegment segment : segments)
            r += segment + "/";

        boolean copied=(copiedFrom.size()>0);

        r+=(copied?"Y/":"N");

        for (StudentExam studentExam : copiedFrom)
            r += studentExam.getID()+",";
        if(copied)
            r=r.substring(0,r.length()-1);

        return r;
    }
}

class AnswerSegment {
    String text;
    String refText;
    boolean isCorrect;

    AnswerSegment(String text) {
        this.text = text;
    }

    void check(AnswerSegment ref) {
        refText = ref.text;
        isCorrect = (text.equals(refText));
    }

    @Override
    public String toString() {
        if(isCorrect)
            return "T";
        else
            return ("F" + "(" + refText + "-" + text+")");
    }

    @Override
    public boolean equals(Object o) {
        return (o.toString().equals(this.toString()));
    }
}