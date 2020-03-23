package com.example.user.solviolin;

public class termList {
    private String termStart;
    private String termEnd;

    public termList(String termStart, String termEnd) {
        this.termStart = termStart;
        this.termEnd = termEnd;
    }

    public String getTermStart() {
        return termStart;
    }

    public void setTermStart(String termStart) {
        this.termStart = termStart;
    }

    public String getTermEnd() {
        return termEnd;
    }

    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
    }
}
