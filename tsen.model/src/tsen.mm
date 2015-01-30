
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
    value : String
    scale : String
}


class tsen.Activity  {

    @id
    hour : String

    targetTemperature : Double
}

class tsen.User  {
    @id
    id : String
    name : String
    surname : String
    targetTemp : Double

}


