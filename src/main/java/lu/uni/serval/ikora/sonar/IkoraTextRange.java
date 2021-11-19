package lu.uni.serval.ikora.sonar;

import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.fs.TextRange;

public class IkoraTextRange implements TextRange {
    private final TextPointer start;
    private final TextPointer end;

    public IkoraTextRange(TextPointer start, TextPointer end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public TextPointer start() {
        return this.start;
    }

    @Override
    public TextPointer end() {
        return this.end;
    }

    @Override
    public boolean overlap(TextRange other) {
        if(this.start() == other.start() || this.end() == other.end()){
            return true;
        }

        if(this.start().compareTo(other.start()) < 0){
            return this.end().compareTo(other.start()) <= 0;
        }

        return other.end().compareTo(this.start()) <= 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.start == null ? 0 : this.start.hashCode());
        hash = 31 * hash + (this.end == null ? 0 : this.end.hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) return false;
        if(other == this) return true;
        if(other.getClass() != this.getClass()) return false;

        final IkoraTextRange that = (IkoraTextRange)other;

        return this.start == that.start && this.end == that.end;
    }

    @Override
    public String toString() {
        return "[" + this.start + "," + this.end + "]";
    }
}
