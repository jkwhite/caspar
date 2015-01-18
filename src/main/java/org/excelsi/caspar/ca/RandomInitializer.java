package org.excelsi.caspar.ca;


import java.util.Random;


public class RandomInitializer implements Initializer {
    public void init(Plane plane, Rule rule, Random random) {
        int[] colors = rule.colors();
        switch(rule.dimensions()) {
            case 1:
                for(int x=0;x<plane.getWidth();x++) {
                    plane.set(x, 0, colors[random.nextInt(colors.length)]);
                }
                break;
            case 2:
                for(int y=0;y<plane.getHeight();y++) {
                    for(int x=0;x<plane.getWidth();x++) {
                        plane.set(x, y, colors[random.nextInt(colors.length)]);
                    }
                }
                break;
            default:
        }
    }
}
