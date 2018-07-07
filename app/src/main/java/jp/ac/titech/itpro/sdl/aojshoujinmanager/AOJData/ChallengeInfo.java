package jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ChallengeInfo implements Serializable{
    public ProblemInfo problem;
    public ArrayList<SubmitInfo> submits = new ArrayList<>();
}
