package jp.ac.titech.itpro.sdl.aojshoujinmanager;

import java.io.Serializable;

import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.SubmitInfo;

@SuppressWarnings("serial")
public class SubmitFilter implements Serializable{
    public String language;
    public String status;
    public Long dateFrom;
    public Long dateTo;

    public boolean isOK(SubmitInfo submit){
        if(!language.equals("ALL") && !submit.language.equals(language))
            return false;
        if(!status.equals("ALL")){
            if(status.equals("not AC") && submit.statusShort.equals("AC"))
                return false;
            else if(!submit.statusShort.equals(status))
                return false;
        }
        if(dateFrom != null && submit.submissionDate.compareTo(dateFrom) < 0)
            return false;
        if(dateTo != null && submit.submissionDate.compareTo(dateTo) > 0)
            return false;
        return true;
    }
}
