package fr.esir.context.dataPackage;

import java.util.HashMap;
import java.util.Map;


public class StudentData {

    private String _studentId;
    private Map<Long,EnvironmentData> _environmentDatas;

    private String currentVote;
    private double TargetTemp;

    public StudentData (String studentId){
        _studentId = studentId;
        _environmentDatas = new HashMap<>();

    }

    public void addEnvironmentData(EnvironmentData envD){
        _environmentDatas.put(envD.getTs(), envD);
    }

    public void set_studentId(String _studentId) {
        this._studentId = _studentId;
    }

    public void set_environmentDatas(Map<Long, EnvironmentData> _environmentDatas) {
        this._environmentDatas = _environmentDatas;
    }

    public Map<Long, EnvironmentData> get_environmentDatas() {
        return _environmentDatas;
    }

    public String get_studentId() {
        return _studentId;
    }

    public void setCurrentVote(String currentVote) {
        this.currentVote = currentVote;
    }

    public void setTargetTemp(double targetTemp) {
        TargetTemp = targetTemp;
    }

    public double getTargetTemp() {
        return TargetTemp;
    }

    public String getCurrentVote() {
        return currentVote;
    }

    public EnvironmentData getEnvironmentData(long ts){
        return _environmentDatas.get(ts);
    }
}
