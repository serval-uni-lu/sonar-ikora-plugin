package lu.uni.serval.ikora.sonar;

import org.sonar.api.batch.fs.TextPointer;

public class IkoraTextPointer implements TextPointer {
    private final int line;
    private final int lineOffset;

    public IkoraTextPointer(int line, int lineOffset){
        this.line = line;
        this.lineOffset = lineOffset;
    }

    @Override
    public int line() {
        return this.line;
    }

    @Override
    public int lineOffset() {
        return this.lineOffset;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.line;
        hash = 31 * hash + this.lineOffset;
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) return false;
        if(other == this) return true;
        if(other.getClass() != this.getClass()) return false;

        final IkoraTextPointer that = (IkoraTextPointer) other;

        return this.line == that.line && this.lineOffset == that.lineOffset;
    }

    @Override
    public int compareTo(TextPointer other) {
        if(this.line == other.line()){
            return Integer.compare(this.line, other.line());
        }

        return Integer.compare(this.lineOffset, other.lineOffset());
    }

    @Override
    public String toString() {
        return this.line + ":" + this.lineOffset;
    }
}
