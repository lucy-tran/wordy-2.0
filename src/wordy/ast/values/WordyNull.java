package wordy.ast.values;

public class WordyNull implements WordyValue {
    public WordyNull() {
    }

    @Override
    public WordyType getType() {
        return WordyType.Null;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof WordyNull){
            return true;
        }
        else{
            return false;
        }
    }
}
