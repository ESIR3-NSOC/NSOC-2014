
class tsen.Room  {
    @id
    name : String

    @contained
    indoorHumiditySensor : tsen.IndoorHumidity[0,*]
    @contained
    outdoorHumiditySensor : tsen.OutdoorHumidity[0,*]
    @contained
    indoorTemperatureSensor : tsen.IndoorTemperature[0,*]
    @contained
    outDoorTemperatureSensor : tsen.OutDoorTemperature[0,*]
    @contained
    airQualitySensor : tsen.CO2[0,*]
    @contained
    valves : tsen.Heater[0,*]
    @contained
    lesson : tsen.Activity[0,*]
    @contained
    members : tsen.User[0,*]
    @contained
    luxSensors : tsen.OutdoorBrightness[0,*]
}

class tsen.IndoorHumidity  {


    name : String
    DPT : String
    value : Double
    scale : String
    @id
    group : String
}

class tsen.OutdoorHumidity  {

    name : String
    DPT : String
    value : Double
    scale : String
    @id
    group : String
}

class tsen.CO2 {

    name: String
    DPT : String
    value :Double
    scale : String
    @id
    group : String
}

class tsen.IndoorTemperature  {


    name: String
    DPT : String
    value : Double
    scale : String
    @id
    group : String
}

class tsen.Heater  {

    name : String
    DPT : String
    value : Double
    @id
    group : String
    scale : String
}

class tsen.Activity  {

    @id
    Uid : String

    start : Long
    end : Long
    @contained
    targetedTemperature : tsen.TargetTemperature
}

class tsen.User  {
    @id
    name : String
    targetTemp : Double

}

class tsen.OutdoorBrightness{
    @id
    group : String
    name: String
    DPT : String
    Scale : String
    value : Double
}

class tsen.OutDoorTemperature{
    @id
    group : String
    name: String
    DPT : String
    Scale : String
    value : Double
}

class tsen.TargetTemperature  {
    value : Double
    Scale : String
}
