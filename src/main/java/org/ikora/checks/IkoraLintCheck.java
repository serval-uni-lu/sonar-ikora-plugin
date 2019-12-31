package org.ikora.checks;

public class IkoraLintCheck extends IkoraCheck {
    @Override
    public void validate() {
        if(ikoraSourceCode == null){
            throw new IllegalStateException("Source code not set");
        }
    }
}
