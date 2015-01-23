package context;

import tsen.TsenDimension;
import tsen.TsenUniverse;
import tsen.TsenView;


public class Context {

    private TsenUniverse _universe;
    private TsenDimension _dim0;


    public Context (TsenUniverse universe){
        _universe = universe ;
    }

    public TsenView getCurrentView(){
        return _dim0.time(System.currentTimeMillis());
    }


}
