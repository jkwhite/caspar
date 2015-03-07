package org.excelsi.caspar;


public interface Timeline {
    State getState();
    void setState(State s);
    void dispose();
}
