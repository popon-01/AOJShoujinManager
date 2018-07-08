package jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SubmitInfo implements Serializable{
    public int runID;
    public String userID;
    public String problemID;
    public Date submissionDate;
    public String status;
    public String statusShort;
    public String language;
    public int cpuTime;
    public int memory;
    public int codeSize;
}
