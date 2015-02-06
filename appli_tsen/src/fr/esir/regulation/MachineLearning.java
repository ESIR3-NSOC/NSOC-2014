package fr.esir.regulation;

import android.content.SharedPreferences;
import fr.esir.maintasks.MyActivity;

public class MachineLearning{

    public void setInSharedPref(){
        SharedPreferences sharedpreferences = MyActivity.mRegulation_service.getSharedpreferences();
    }

}
