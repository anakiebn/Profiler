package co.za.access.Profiler;

public class InvalidURIParameter extends Exception{
    public InvalidURIParameter(String msg){
        super(msg);
    }
    public InvalidURIParameter(){
        this("invalid URI Parameter");
    }
}
