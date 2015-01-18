package org.excelsi.caspar.ca;


public interface Rule extends java.io.Serializable {
    Ruleset origin();
    int[][] toPattern();
    int[] colors();
    int background();
    int length();
    int dimensions();
    //String toIncantation();
    //void init(CA c, Initialization i);
    //int getSuggestedInterval(CA c);
    float generate(Plane c, int start, int end, boolean stopOnSame, boolean over, Updater u);

    interface Updater {
        void update(Rule r, int start, int current, int end);
        long interval();
    }
}
