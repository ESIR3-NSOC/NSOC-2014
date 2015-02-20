package fr.esir.regulation;

import android.os.Environment;
import com.example.esir.nsoc2014.tsen.lob.objects.ArffGenerated;

import java.io.File;
import java.io.IOException;

public class MachineLearning {
    private DataFromKNX dfk;

    public MachineLearning(DataFromKNX dfk) {
        this.dfk = dfk;
    }

    public long setDataInArff() {
        String externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = externalStorage + File.separator + "DCIM" + File.separator + "arffRegul.arff";

        ArffGenerated arff = new ArffGenerated();

        File file = new File(path);
        if (!file.exists()) {
            arff.generateArffRegul();
            arff.addDataRegulGeneric();
            try {
                arff.saveInstancesInArffFile(arff.getArff(), path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        arff.addInstance(dfk.getI_temp(), dfk.getO_temp(), dfk.getI_hum(), dfk.getO_hum(), dfk.getO_lum(), dfk.getCons());

        try {
            return (long) arff.executeModel();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
