
class tsen.Room  {
    @id
    name : String


    @contained
    Measurement : tsen.Sensor[0,*]
    @contained
    lesson : tsen.Activity[0,*]
    @contained
    members : tsen.User[0,*]

}

class tsen.Sensor {

    @id
    groupAddress : String

    sensorId : String
    sensorType : String
    associatedDPT : String
    value : Double
    scale : String
}


class tsen.Activity  {

    @id
    Uid : String

    @contained
    targetedTemperature : tsen.TargetTemperature
}

class tsen.User  {
    @id
    name : String
    targetTemp : Double

}

class tsen.TargetTemperature  {
    value : Double
    Scale : String
}
