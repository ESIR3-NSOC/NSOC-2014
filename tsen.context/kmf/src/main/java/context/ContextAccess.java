package context;

import reference.Reference;
import tsen.factory.TsenTransactionManager;


/**
 * Created by mathi_000 on 02/12/2014.
 */
public class ContextAccess {

    private String _name = Reference.CTX_ACCESS;
    private TsenTransactionManager manager;

    public ContextAccess(){



    }



    public String getName(){
        return _name;
    }
}
