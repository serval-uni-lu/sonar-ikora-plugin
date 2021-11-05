package lu.uni.serval.ikora.sonar.checks;

public class IkoraLintCheck extends IkoraCheck {
    @Override
    public void validate() {
        if(ikoraSourceCode == null){
            throw new IllegalStateException("Source code not set");
        }
    }
}
