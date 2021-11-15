package lu.uni.serval.ikora.sonar.rules;

public class IkoraLintRule extends IkoraRule {
    @Override
    public void validate() {
        if(ikoraSourceCode == null){
            throw new IllegalStateException("Source code not set");
        }
    }
}
