package org.excelsi.caspar.ca;


public class SafeMutator implements Mutator {
    private final Mutator _m;


    public SafeMutator(Mutator m) {
        _m = m;
    }

    public String name() {
        return _m.name();
    }

    public String description() {
        return _m.description();
    }

    public Rule mutate(Rule r) {
        if(r instanceof Multirule) {
            return mutate((Multirule)r);
        }
        for(int tries=0;tries<50;tries++) {
            try {
                return _m.mutate(r);
            }
            catch(MutationFailedException | IllegalStateException e) {
                System.err.println(e.toString());
            }
        }
        return r;
    }

    public Multirule mutate(Multirule r) {
        for(int tries=0;tries<50;tries++) {
            try {
                return _m.mutate(r);
            }
            catch(MutationFailedException | IllegalStateException e) {
                System.err.println(e.toString());
            }
        }
        return r;
    }

    public void setRandom(java.util.Random r) {
        _m.setRandom(r);
    }

}
