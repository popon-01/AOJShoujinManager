package jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProblemInfo implements Serializable{
    public String problemID;
    public String problemName;
    public int timeLimit;
    public int memoryLimit;
}
