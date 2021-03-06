package org.excelsi.caspar.ca;


import java.util.BitSet;
import java.io.*;


public abstract class AbstractRule extends WritableImagePlane implements Rule {
    protected Flags _options = new Flags();


    public AbstractRule(int x, int y) {
        super(x, y);
    }

    public Flags getFlags() {
        return _options;
    }

    public boolean getFlag(Options o) {
        return _options.get(o);
    }

    public void setFlag(Options o) {
        _options.set(o);
    }

    public void setFlag(Options o, boolean state) {
        _options.set(o, state);
    }

    protected static class Flags implements java.io.Serializable {
        private BitSet _s = new BitSet();


        public boolean get(Options o) {
            return _s.get(o.ordinal());
        }

        public void set(Options o) {
            set(o, o.get());
        }

        public void set(Options o, boolean state) {
            _s.set(o.ordinal(), state);
        }

        public void write(DataOutputStream dos) throws IOException {
            dos.writeByte(_s.size());
            for(int i=0;i<_s.size();i++) {
                dos.writeBoolean(_s.get(i));
            }
        }

        public void read(DataInputStream dis) throws IOException {
            int size = dis.readByte();
            for(int i=0;i<size;i++) {
                _s.set(i, dis.readBoolean());
            }
        }
    }
}
